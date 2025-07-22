package application;

import java.io.File;
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
import javafx.stage.FileChooser;

public class ViewReportController  implements Initializable, RefreshScene {

    @FXML
    private AnchorPane aPane;

    @FXML
    private Button btnClose;

    @FXML
    private TextArea taView;
    
    private DfcGlobal dg = DfcGlobal.getInstance();

    @FXML
    void doBtnClose(ActionEvent event) {
    	dg.sceneNav.scenePop();
    }

	@Override
	public void refreshScene() {
		File f2 = new File(dg.logDir.getAbsolutePath());
        if (f2.exists() == false) {
        	f2.mkdirs();
        }
        
		FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
        	     new FileChooser.ExtensionFilter("Log Files", "*.log")
        	    ,new FileChooser.ExtensionFilter("All Files", "*.*")
        	);
        fileChooser.setInitialDirectory(f2);
        File sf = fileChooser.showOpenDialog(null);
        
        if (sf == null) {
        	return;
        }
        
		String txt = null;
		try {
			txt = new String(Files.readAllBytes(Paths.get(sf.getAbsolutePath())), 
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (txt != null)
			taView.setText(txt);
	}

	@Override
	public void leaveScene() {
		
	}

	@Override
	public void clickIt(String text) {
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

}

