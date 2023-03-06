package us.hall.jfxdesktop;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.net.URL;

import javafx.scene.Node;
import javafx.stage.FileChooser;

public class DesktopActions {

	private final static Desktop desktop = Desktop.getDesktop();
	
	public static void browse() {
		if (desktop.isSupported(Desktop.Action.BROWSE)) {
			String urlString = "https://openjfx.io";
			try {
				URI uri = new URL(urlString).toURI();
				desktop.browse(uri);
			}
			catch (Exception ex) { ex.printStackTrace(); }
		}
	}
	
	public static void edit(Node node) {
		if (desktop.isSupported(Desktop.Action.EDIT)) {
			FileChooser fc = new FileChooser();
			File edited = fc.showOpenDialog(node.getScene().getWindow());
			try {
				desktop.edit(edited);
			}
			catch (Exception ex) { ex.printStackTrace(); }
		}
		
	}
	
	public static void open(Node node) {
		if (desktop.isSupported(Desktop.Action.OPEN)) {
			FileChooser fc = new FileChooser();
			File opened = fc.showOpenDialog(node.getScene().getWindow());
			try {
				desktop.open(opened);
			}
			catch (Exception ex) { ex.printStackTrace(); }
		}
	}
	
	public static void mail() {
		if (desktop.isSupported(Desktop.Action.MAIL)) {
			try {
				desktop.mail();
			}
			catch (Exception ex) { ex.printStackTrace(); }
		}
	}

}
