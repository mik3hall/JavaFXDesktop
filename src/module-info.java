module JavaFXDesktop {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.desktop;
	requires java.logging;
	requires java.prefs;
	
	opens us.hall.jfxdesktop to javafx.graphics, javafx.fxml;
}
