package us.hall.jfxdesktop;

import java.awt.Desktop;
import java.awt.desktop.AppForegroundEvent;
import java.awt.desktop.AppForegroundListener;
import java.awt.desktop.AppHiddenEvent;
import java.awt.desktop.AppHiddenListener;
import java.awt.desktop.AppReopenedEvent;
import java.awt.desktop.AppReopenedListener;
import java.awt.desktop.ScreenSleepEvent;
import java.awt.desktop.ScreenSleepListener;
import java.awt.desktop.SystemSleepEvent;
import java.awt.desktop.SystemSleepListener;
import java.net.URI;
import java.net.URL;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.prefs.Preferences;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class JavaFXDesktopController {
	
	private int platformCol = -1;
	//private static final String[] platforms = new String[] { "OS/X", "Windows", "Linux" };

	private static final int OSX = 1, WIN = 2, LINUX = 3;
	private static final List<Desktop.Action> winSupported = Arrays.asList(new Desktop.Action[] {
		Desktop.Action.OPEN, Desktop.Action.EDIT, Desktop.Action.PRINT,
		Desktop.Action.MAIL, Desktop.Action.BROWSE, Desktop.Action.APP_EVENT_SYSTEM_SLEEP,
		Desktop.Action.APP_EVENT_USER_SESSION, Desktop.Action.APP_SUDDEN_TERMINATION,
		Desktop.Action.MOVE_TO_TRASH
	});
	private static final List<Desktop.Action> linuxSupported = Arrays.asList(new Desktop.Action[] {
		Desktop.Action.OPEN, Desktop.Action.MAIL, Desktop.Action.BROWSE	
	});
	private static final List<Desktop.Action> listeners = Arrays.asList(new Desktop.Action[] {
		Desktop.Action.APP_EVENT_FOREGROUND, Desktop.Action.APP_EVENT_SYSTEM_SLEEP,
		Desktop.Action.APP_EVENT_HIDDEN, Desktop.Action.APP_EVENT_REOPENED,
		Desktop.Action.APP_EVENT_SCREEN_SLEEP, Desktop.Action.APP_EVENT_SYSTEM_SLEEP	
	});
	private static final Preferences prefs = Preferences.userRoot().node(JavaFXDesktopController.class.getName());
	private static Logger logger = Logger.getLogger(JavaFXDesktopController.class.getName());
	private FileHandler fh = null;
	
	@FXML   GridPane dt_header;
	
	@FXML
	private Label    status_osx, status_win, status_linux;
	
	@FXML 
	private ScrollPane scroller;
	
	@FXML
	private GridPane dt_actions;
		
	@FXML
	private void initialize() {
		String osname = System.getProperty("os.name");
		if (osname.equals("Mac OS X")) {
			platformCol = 1;
			status_osx.setText("Active");
			status_osx.setTextFill(Color.GREEN);
			
		}
		else if (osname.startsWith("Win")) {
			platformCol = 2;
			status_win.setText("Active");
			status_win.setTextFill(Color.GREEN);
		}
		else {
			platformCol = 3;
			status_linux.setText("Active");
			status_linux.setTextFill(Color.GREEN);
		}
		Desktop desktop = Desktop.getDesktop();
		List<Desktop.Action> actions = Arrays.asList(Desktop.Action.values());
		int row = 0;
		ObservableList<ColumnConstraints> cch = dt_header.getColumnConstraints();
		cch.get(0).setPercentWidth(40);
		for (int i = 1; i < 4; i++) {
			cch.get(i).setPercentWidth(20);
		}
		ObservableList<ColumnConstraints> cc = dt_actions.getColumnConstraints();
		cc.get(0).setPercentWidth(40);
		for (int i = 1; i < 4; i++) {
			cc.get(i).setPercentWidth(20);
		}
		ClassLoader cl = getClass().getClassLoader();
	    Image questionMark = new Image(cl.getResourceAsStream("us/hall/jfxdesktop/question.gif"),25,25,true,true);
	    dt_actions.setAlignment(Pos.CENTER);
		for (Desktop.Action action : actions) {
			Text t = new Text(action.toString());
			t.setFont(Font.font ("System",FontWeight.BOLD, 12));
			t.setFill(Color.BLUE);	
			GridPane.setMargin(t,new Insets(0d,5d,0d,0d));
			GridPane.setHalignment(t, HPos.RIGHT);
			dt_actions.add(t, 0, row);
			Alert.AlertType type = Alert.AlertType.INFORMATION;
			Alert actionAlert = new Alert(type,System.getProperty(action.toString(),action.toString()));
			for (int i=1; i<4; i++) {
				StackPane sp = new StackPane();
				GridPane.setMargin(sp,new Insets(3,3,3,3));
				sp.setPrefWidth(80);
				sp.setMinWidth(80);
				sp.setMaxWidth(80);
				if (i == platformCol) {
					if (desktop.isSupported(action)) {	
						sp.setStyle("-fx-background-color:GREEN;  -fx-border-color: black;");
					    Button button = new Button();
					    //button. setStyle("-fx-background-color: #90EE90; ");
					    ImageView view = new ImageView(questionMark);
					    view.setPreserveRatio(true);
					    button.setGraphic(view);
					    StackPane.setAlignment(button, Pos.CENTER);
					    sp.getChildren().addAll(button);
					    button.setOnAction(e -> {
							actionAlert.setTitle(action.toString());
							DialogPane dp = actionAlert.getDialogPane();
							//System.out.println("dp header before " + dp.getHeader());
							//header.getChildren().add(new Button("Javadoc"));
							//dp.setHeader(new Button("Javadoc"));
							//System.out.println("dp header after " + dp.getHeader());
							//ObservableList<Node> nodes = dp.getChildren();
							HBox header = new HBox();
							//GridPane parts = (GridPane)nodes.get(0);
							//StackPane graphic = (StackPane)parts.getChildren().get(1);
							//System.out.println("graphic " + graphic);
							ImageView graphic = new ImageView(questionMark);
						    graphic.setPreserveRatio(true);
							Button javadoc = new Button("Javadoc");
							javadoc.setOnAction(evt -> {
								if (desktop.isSupported(Desktop.Action.BROWSE)) {
									String urlString = System.getProperty(action.toString()+".doc");
									if (urlString != null) {
										try {
											URI uri = new URL(urlString).toURI();
											desktop.browse(uri);
										}
										catch (Exception ex) { ex.printStackTrace(); }
									}
									else {
										
									}
								}
								else {
									
								}
							});
						    Region filler = new Region(); 
						    HBox.setMargin(javadoc, new Insets(10, 10, 10, 10));
						    HBox.setMargin(graphic, new Insets(10, 10, 10, 10));
						    HBox.setHgrow(filler, Priority.ALWAYS); 
							header.getChildren().addAll(javadoc, filler, graphic);
							dp.setHeader(header);
							System.out.println("methods action " + action.toString());
							InputStream is = cl.getResourceAsStream("methods/"+action.toString());
							BufferedReader reader = new BufferedReader(new InputStreamReader(is));
							boolean loggingOption = false;
							CheckBox logging = new CheckBox("Log listeners");
							logging.setOnAction(evt -> {
								if (logging.isSelected()) {
									if (fh == null) {
										FileChooser fc = new FileChooser();
										
										StringBuilder sb = new StringBuilder();
										if (System.getProperty("os.name").equals("Mac OS X")) {
											sb.append("/Users/");
											sb.append(System.getProperty("user.name"));
											sb.append("/Library/Logs/JavaFXDesktop/");
										}
										else {  // Not currently sure any other platforms support listeners
											sb.append(System.getProperty("user.home"));
											sb.append(System.getProperty("path.separator"));
										}
										String loggingPath = prefs.get("logPath",sb.toString());
										Path logPath = Paths.get(loggingPath);
										try {
											if (!Files.exists(logPath)) {
												Files.createDirectories(logPath);
											}
											fc.setInitialDirectory(new File(sb.toString()));
											fc.setInitialFileName("listener.log");
											File logFile = fc.showSaveDialog(logging.getScene().getWindow());
											prefs.put("logPath",logFile.toString());
											try {
												prefs.flush();
											}
											catch (java.util.prefs.BackingStoreException bsex) {}
											fh = new FileHandler(logFile.toString(), 5000, 1);
											logger.addHandler(fh);
											SimpleFormatter formatter = new SimpleFormatter();  
										    fh.setFormatter(formatter);  
										}
										catch(IOException ioex) {
											ioex.printStackTrace();
										}
									}
								}
								else {
									
								}
							});
							VBox expanded = new VBox();
							if (listeners.contains(action)) {
								loggingOption = true;
							}
							Label sample = new Label("Sample");
							sample.setFont(Font.font ("System",FontWeight.BOLD, 12));
							sample.setTextFill(Color.BLUE);	
							ScrollPane scroller = new ScrollPane();
							TextArea console = new TextArea();
							//console.setFont(Font.font("Source Code Pro",FontWeight.NORMAL, 11));
							console.setStyle("-fx-font: 11pt Arial; -fx-line-spacing: 6px;");
							TextField message = new TextField();
							final AppForegroundListener afl = new AppForeground(message, logger);
							desktop.addAppEventListener(afl);
							final AppHiddenListener ahl = new AppHidden(message, logger);
							desktop.addAppEventListener(ahl);
							final AppReopenedListener arl = new AppReopened(message, logger);
							desktop.addAppEventListener(arl);
							final ScreenSleepListener ssl = new ScreenSleep(message, logger);
							desktop.addAppEventListener(ssl);
							final SystemSleepListener sysl = new SystemSleep(message,logger);
							desktop.addAppEventListener(sysl);
							message.setText("Message");
							message.setDisable(true);
							Button exec = new Button("Execute");
							exec.setOnAction(exec_evt -> {
								try {
									switch(action) {
										case BROWSE: 
											DesktopActions.browse();
											break;
										case EDIT:
											DesktopActions.edit((Node)exec_evt.getSource());
											break;
										case OPEN:
											DesktopActions.open((Node)exec_evt.getSource());
											break;
										case MAIL:
											DesktopActions.mail();
											break;
										case APP_EVENT_FOREGROUND:
											((AppForeground)afl).activate();
											break;
										case APP_EVENT_HIDDEN:
											((AppHidden)ahl).activate();
											break;
										case APP_EVENT_REOPENED:
											((AppReopened)arl).activate();
											break;
										case APP_EVENT_SCREEN_SLEEP:
											desktop.addAppEventListener(ssl);
											break;
										case APP_EVENT_SYSTEM_SLEEP:
											
									}
								}
								catch (Throwable tossed) {
									console.appendText(tossed.toString());
									tossed.printStackTrace();
								}
							});
							StringBuilder sb = new StringBuilder();
							try {
								while(reader.ready()) {
									String line = reader.readLine();
									sb.append(line+"\n");
								}
							}
							catch (IOException ioex) {
								ioex.printStackTrace();
							}
							console.setText(sb.toString());
							scroller.setContent(console);
							scroller.setVvalue(0);
							if (loggingOption) {
								expanded.getChildren().addAll(logging, sample, scroller, message, exec);
							}
							else {
								expanded.getChildren().addAll(sample, scroller, message, exec);
							}
							dp.setExpandableContent(expanded);
							//console.applyCss();
							//Text consoleText = (Text)console.lookup(".text");
							//consoleText.setStyle("-fx-line-spacing: 6px;");
							/*
							for (Node node : nodes) {
								if (node instanceof GridPane) {
									ObservableList<Node> gnodes = ((GridPane)node).getChildren();
									for (Node gnode : gnodes) {
										if (gnode instanceof StackPane) {
											System.out.println("gnode " + gnode + " class " + gnode.getClass());
											ObservableList<Node> snodes = ((StackPane)gnode).getChildren();
											for (Node snode : snodes) {
												System.out.println("snode " + snode + " class " + snode.getClass());
											}
										}
										else System.out.println("gnode " + gnode + " class " + gnode.getClass());
									}
								}
								else System.out.println("node " + node + " class " + node.getClass());
							}
							*/
				            actionAlert.showAndWait();
				        });
					}
					else {
						sp.setStyle("-fx-background-color:RED;  -fx-border-color: black;");
					}
				}
				else {
					if (i == OSX) {
						sp.setStyle("-fx-background-color:GREEN;  -fx-border-color: black;");
					}
					else if (i == WIN) {
						if (winSupported.contains(action)) {
							sp.setStyle("-fx-background-color:GREEN;  -fx-border-color: black;");
						}
						else {
							sp.setStyle("-fx-background-color:RED;  -fx-border-color: black;");
						}
					}
					else if (i == LINUX) {
						if (linuxSupported.contains(action)) {
							sp.setStyle("-fx-background-color:GREEN;  -fx-border-color: black;");
						}
						else {
							sp.setStyle("-fx-background-color:RED;  -fx-border-color: black;");
						}
					}
					else {     // Shouldn't happen
						throw new IllegalStateException("Unknown platform");
					}
				}
				dt_actions.add(sp, i, row);
			}
			row++;
		}
		dt_actions.autosize();
	}
	
	
}

class AppForeground implements AppForegroundListener {
	
	final TextField message;
	boolean active = false;
	Logger logger = null;
	
	public AppForeground(TextField message, Logger logger) {
		this.message = message;
		this.logger = logger;
	}
	
	public void activate() {
		active = true;
	}
	
	public void appMovedToBackground(AppForegroundEvent evt) {
		Platform.runLater(() -> {
			logger.info("AppForeground evt to back");
			if (active) {
				message.setText("App background " + evt.getSource());
				message.setStyle("-fx-text-inner-color: GREEN;");
			}
		});		
	}
	
	public void appRaisedToForeground(AppForegroundEvent evt) {
		Platform.runLater(() -> {
			logger.info("AppForeground evt");
			if (active) {
				message.setText("App foregound " + evt.getSource());
				message.setStyle("-fx-text-inner-color: GREEN;");
				active = false;
			}
		});
	}
}

class AppHidden implements AppHiddenListener {
	
	final TextField message;
	boolean active = false;
	Logger logger = null;
	
	public AppHidden(TextField message, Logger logger) {
		this.message = message;
		this.logger = logger;
	}
	
	public void activate() {
		active = true;
	}
	
	public void appHidden(AppHiddenEvent evt) {
		Platform.runLater(() -> {
			logger.info("AppHidden hidden evt");
			if (active) {
				message.setText("App hidden " + evt.getSource());
				message.setStyle("-fx-text-inner-color: GREEN;");
			}
		});		
	}
	
	public void appUnhidden(AppHiddenEvent evt) {
		Platform.runLater(() -> {
			System.out.println("AppHidden unhidden evt");
			if (active) {
				message.setText("App unhidden " + evt.getSource());
				message.setStyle("-fx-text-inner-color: GREEN;");
				active = false;
			}
		});		
	}
}

class AppReopened implements AppReopenedListener {
	
	final TextField message;
	boolean active = false;
	Logger logger = null;
	
	public AppReopened(TextField message, Logger logger) {
		this.message = message;
		this.logger = logger;
	}
	
	public void activate() {
		active = true;
	}
	
	public void appReopened(AppReopenedEvent evt) {
		Platform.runLater(() -> {
			logger.info("AppReopened evt");
			if (active) {
				message.setText("App reopened " + evt.getSource());
				message.setStyle("-fx-text-inner-color: GREEN;");
				active = false;
			}
		});
	}
}

class ScreenSleep implements ScreenSleepListener {
	
	final TextField message;
	boolean active = false;
	Logger logger = null;
	
	public ScreenSleep(TextField message, Logger logger) {
		this.message = message;
		this.logger = logger;
	}
	
	public void activate() {
		active = true;
	}
	
	public void screenAboutToSleep(ScreenSleepEvent e) {
		Platform.runLater(() -> {
			System.out.println("ScreenSleep evt");
			if (active) {
				message.setText("Screen about to sleep " + e.getSource());
				message.setStyle("-fx-text-inner-color: GREEN;");
			}
		});
	}
	
	public void screenAwoke(ScreenSleepEvent e) {
		Platform.runLater(() -> {
			System.out.println("ScreenSleep evt");
			if (active) {
				message.setText("Screen about to sleep " + e.getSource());
				message.setStyle("-fx-text-inner-color: GREEN;");
				active = false;
			}
		});
	}
}

class SystemSleep implements SystemSleepListener {
	
	final TextField message;
	boolean active = false;
	Logger logger = null;
	
	public SystemSleep(TextField message, Logger logger) {
		this.message = message;
		this.logger = logger;
	}
	
	public void activate() {
		active = true;
	}
	
	public void systemAboutToSleep(SystemSleepEvent e) {
		Platform.runLater(() -> {
			logger.info("SystemSleep evt");
			if (active) {
				message.setText("System about to sleep " + e.getSource());
				message.setStyle("-fx-text-inner-color: GREEN;");
			}
		});		
	}
	
	public void systemAwoke(SystemSleepEvent e) {
		Platform.runLater(() -> {
			logger.info("SystemSleep evt");
			if (active) {
				message.setText("System awoke " + e.getSource());
				message.setStyle("-fx-text-inner-color: GREEN;");
				active = false;
			}
		});		
	}
}