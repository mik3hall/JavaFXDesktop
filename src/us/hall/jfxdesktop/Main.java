package us.hall.jfxdesktop;
	
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	@Override
	public void init() {
		Properties props = new Properties(System.getProperties());
		try {
			ClassLoader cl = getClass().getClassLoader();
			InputStream is = cl.getResourceAsStream("us/hall/jfxdesktop/actions.properties");
			BufferedInputStream bis = new BufferedInputStream(is);
			props.load(bis);
			System.setProperties(props);
		}
		catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = null;
			root = (BorderPane)FXMLLoader.load(getClass().getResource("JavaFXDesktop.fxml"));
			Scene scene = new Scene(root,450,700);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			MenuBar menuBar = new MenuBar();
			final Menu logMenu = new Menu("Event Log");
	        menuBar.getMenus().addAll(logMenu);
	        menuBar.setUseSystemMenuBar(true);
	        root.getChildren().addAll(menuBar);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Desktop Actions");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
