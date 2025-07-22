package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class ManageExtsController implements Initializable, RefreshScene {

	@FXML
    private AnchorPane aPane;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private TextArea taExts;
    
    private DfcGlobal dg = DfcGlobal.getInstance();

    @FXML
    void doBtnCancel(ActionEvent event) {
    	dg.sceneNav.scenePop();
    }

    @FXML
    void doBtnSave(ActionEvent event) {
    	String txt = taExts.getText();
    	 try (BufferedWriter writer = new BufferedWriter(new FileWriter(dg.ignoreExtsFile))) {
             writer.write(txt);
         } catch (IOException e) {
             System.err.println("An error occurred while writing to the file: " + e.getMessage());
         }
    	dg.sceneNav.scenePop();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

	@Override
	public void refreshScene() {
		String txt = null;
		try {
			txt = new String(Files.readAllBytes(Paths.get(dg.ignoreExtsFile.getAbsolutePath())), 
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (txt != null)
			taExts.setText(txt);
	}

	@Override
	public void leaveScene() {
		
	}

	@Override
	public void clickIt(String text) {
		
	}

}

