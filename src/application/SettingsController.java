package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class SettingsController implements Initializable, RefreshScene {

    @FXML
    private AnchorPane aPane;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;
    
    @FXML
    private TextField tfPath;
    
    @FXML
    private CheckBox cbReport;
    
    @FXML
    private PasswordField pfPassword;

    @FXML
    private TextField tfUser;
    
    @FXML
    private ChoiceBox<String> cbMinute;
    
    @FXML
    private ChoiceBox<String> cbHour;
    
    @FXML
    private Spinner<Integer> spinnerPurge;
    
    private DfcGlobal dg = DfcGlobal.getInstance();
    
    @FXML
    void doBtnPath(ActionEvent event) {

    }

    @FXML
    void doBtnCancel(ActionEvent event) {
    	dg.sceneNav.scenePop();
    }

    @FXML
    void doBtnSave(ActionEvent event) {
    	String path = tfPath.getText();
    	int days = spinnerPurge.getValue();
    	String sendTime = new String(cbHour.getValue() + ":" + cbMinute.getValue());
    	boolean f = cbReport.isSelected();
    	
    	if (path != null)
    		dg.ini.addValuePair("System", "reportpath", path);
    	if (sendTime != null)
    		dg.ini.addValuePair("System", "reportTime", sendTime);
    	if (days > 0)
    		dg.ini.addValuePair("System", "reportPurge", days);
    	dg.ini.addValuePair("System",  "reportEnable", Boolean.toString(f));
    	
    	dg.ini.writeFile(true);
    	
    	dg.sceneNav.scenePop();
    }

	@Override
	public void refreshScene() {
		
		String path = dg.ini.getString("System", "reportPath");
		if (path != null) {
			tfPath.setText(path);
		}
		
		String sTime = dg.ini.getString("System", "reportTime");
		if (sTime != null) {
			String[] a = sTime.split(":");
			cbHour.setValue(a[0]);
			cbMinute.setValue(a[1]);
		}
		
		String enable = dg.ini.getString("System", "reportEnable");
		if (enable != null) {
			cbReport.setSelected(Boolean.parseBoolean(enable));
		}
		
		int days = dg.ini.getInt("System", "reportPurge");
		if (days > 0) {
			SpinnerValueFactory<Integer> svf = spinnerPurge.getValueFactory();
			svf.setValue(days);
		}
	}

	@Override
	public void leaveScene() {
		
	}

	@Override
	public void clickIt(String text) {
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		for (int i = 0; i < 24; i++) {
			cbHour.getItems().add("" + i);
		}
		
		for (int i = 0; i < 60; i++) {
			cbMinute.getItems().add("" + i);
		}
		
		SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 120, 30);
		spinnerPurge.setValueFactory(svf);
	}

}

