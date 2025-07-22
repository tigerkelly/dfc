module dfc {
	requires javafx.controls;
	requires javafx.fxml;
	requires IniFile;
	requires javafx.base;
	requires java.desktop;
	requires javafx.graphics;
	
	opens application to javafx.graphics, javafx.fxml, javafx.base;
}
