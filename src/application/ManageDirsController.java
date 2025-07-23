package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ManageDirsController implements Initializable, RefreshScene {
	
	@FXML
    private AnchorPane aPane;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnCancel;
    
    @FXML
    private Button btnSave;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpdate;

    @FXML
    private TextField tfPath;
    
    @FXML
    private CheckBox cbRecursive;
    
    @FXML
    private TableView<ManageDir> tableView;

    @FXML
    private TableColumn<ManageDir, String> tiPath;

    @FXML
    private TableColumn<ManageDir, String> tiRecursive;
    
    @FXML
    private TableColumn<ManageDir, String> tiFound;
    
    private DfcGlobal dg = DfcGlobal.getInstance();

    @FXML
    void doBtnAdd(ActionEvent event) {
    	String path = tfPath.getText();
    	boolean f = cbRecursive.isSelected();
    	
    	File fd = new File(path);
    	
    	ManageDir md = new ManageDir(path, Boolean.toString(f));
    	if (fd.isDirectory() == true) {
    		md.setFound("yes");
    	} else {
    		md.setFound("no");
    	}
    	tableView.getItems().add(md);
    }

    @FXML
    void doBtnCancel(ActionEvent event) {
    	dg.sceneNav.scenePop();
    }
    
    @FXML
    void doBtnSave(ActionEvent event) {
    	ObservableList<ManageDir> lst = tableView.getItems();
    	
    	dg.ini.removeSection("Directories");
    	dg.ini.addSection("Directories");
    	for (ManageDir md : lst) {
    		dg.ini.addValuePair("Directories", md.getPath(), md.getRecursive());
    	}
    	dg.ini.writeFile(true);
    	
    	tfPath.setText("");
    	cbRecursive.setSelected(false);
    	
    	dg.sleepMode = true;
    	dg.createWatch();
    	dg.addDirectories(aPane);
    	dg.sleepMode = false;
    	
    	dg.sceneNav.scenePop();
    }

    @FXML
    void doBtnDelete(ActionEvent event) {
    	String path = tfPath.getText();
    	
    	int idx = tableView.getSelectionModel().getSelectedIndex();
    	
    	boolean yesno = dg.showConfirm("Unwatch directory", path + "\nRemove dictory from watch list?");
    	
    	if (yesno == true) {
    		tableView.getItems().remove(idx);
    		tfPath.setText("");
        	cbRecursive.setSelected(false);
    	}
    }

    @FXML
    void doBtnUpdate(ActionEvent event) {
    	String path = tfPath.getText();
    	String p = tfPath.getText();
    	boolean b = cbRecursive.isSelected();
    	String rec = b + "";
    	
    	System.out.println("Path: " + path + ", b: " + rec);
    	
    	ObservableList<ManageDir> mds = tableView.getItems();
    	for (ManageDir md : mds) {
    		if (md.getPath().equals(p) == true) {
    			md.setPath(path);
    			md.setRecursive(rec);
    			break;
    		}
    	}
    	
    	tfPath.setText("");
    	cbRecursive.setSelected(false);
    }

    @FXML
    void doSelectDir(ActionEvent event) {
    	Stage stage = (Stage)aPane.getScene().getWindow();
    	DirectoryChooser directoryChooser = new DirectoryChooser();
    	
    	File sd = directoryChooser.showDialog(stage);
    	if (sd != null) {
    		tfPath.setText(sd.getAbsolutePath());
    	}
    }
    
    @FXML
    void tblClicked(MouseEvent event) {
    	ManageDir md = (ManageDir)tableView.getSelectionModel().getSelectedItem();
    	
    	if (md != null) {
    		tfPath.setText(md.getPath());
    		if (md.getRecursive().charAt(0) == 'T' || 
    				md.getRecursive().charAt(0) == 't' ||
    				md.getRecursive().charAt(0) == 'Y' ||
    				md.getRecursive().charAt(0) == 'y') {
    			cbRecursive.setSelected(true);
    		} else {
    			cbRecursive.setSelected(false);
    		}
    	}
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tiPath.prefWidthProperty().bind(tableView.widthProperty()
				.subtract(20.0)
				.subtract(tiRecursive.widthProperty())
				.subtract(tiFound.widthProperty()));
		
		tiFound.setCellValueFactory(new PropertyValueFactory<>("found"));
		tiRecursive.setCellValueFactory(new PropertyValueFactory<>("recursive"));
		tiPath.setCellValueFactory(new PropertyValueFactory<>("path"));
		
		tiRecursive.setStyle( "-fx-alignment: CENTER;");
		tiFound.setStyle( "-fx-alignment: CENTER;");
		
		tableView.getItems().clear();
	}

	@Override
	public void refreshScene() {
		tableView.getItems().clear();
		
		Object[] objs = dg.ini.getSectionKeys("Directories");
		for (Object obj : objs) {
			String s = (String)obj;
			File f = new File(s);
			String v = dg.ini.getString("Directories", obj);
			ManageDir md = new ManageDir(s, v);
			if (f.exists() == true) {
				md.setFound("yes");
			} else {
				md.setFound("no");
			}
			
			if (s.equalsIgnoreCase("user.home") == true) {
				md.setFound("yes");
			}
			tableView.getItems().add(md);
		}
	}

	@Override
	public void leaveScene() {
		
	}

	@Override
	public void clickIt(String text) {
		
	}

}
