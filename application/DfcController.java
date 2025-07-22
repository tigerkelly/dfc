package application;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class DfcController implements Initializable {

    @FXML
    private AnchorPane aPane;
    
    @FXML
    private TableView<ChangedFile> tableView;
    
    @FXML
    private TableColumn<ChangedFile, String> tiFileName;

    @FXML
    private TableColumn<ChangedFile, String> tiPath;
    
    @FXML
    private TableColumn<ChangedFile, String> tiDate;
    
    @FXML
    private TableColumn<ChangedFile, String> tiEvent;
    
    @FXML
    private TableColumn<ChangedFile, String> tiType;

    @FXML
    private Label lblSettings;

    @FXML
    private Label lblVersion;
    
    @FXML
    private Button btnAdd;

    @FXML
    private Button btnClose;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpdate;

    @FXML
    private TextField tfPath;
    
    @FXML
    private Label lblDirs;

    @FXML
    private Label lblExts;
    
    private DfcGlobal dg = DfcGlobal.getInstance();
    
    @FXML
    void doLblDirs(MouseEvent event) {
    	dg.sceneNav.loadScene(SceneNav.MANGEDIRS);
    }

    @FXML
    void doLblExts(MouseEvent event) {
    	dg.sceneNav.loadScene(SceneNav.MANAGEIGNORES);
    }
    
    @FXML
    void doLblSettings(MouseEvent event) {
    	dg.sceneNav.loadScene(SceneNav.SETTINGS);
    }
    
    @FXML
    void doCmReport(ActionEvent event) {
    	LocalDateTime currentDateTime = LocalDateTime.now();
    	String path = dg.ini.getString("System", "reportPath");
    	dg.tv = tableView;
    	dg.generateReport(currentDateTime, path);
    }
    
    @FXML
    void doCmView(ActionEvent event) {
    	dg.sceneNav.loadScene(SceneNav.VIEWREPORT);
    }

    @FXML
    void doSelectDir(ActionEvent event) {

    }
    
    public TableView<ChangedFile> getTable() {
    	return tableView;
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		if (dg.dt == null) {
			dg.dt = new DfcThread("dt");
			dg.dt.setDaemon(true);
			dg.dt.start();
		}
		
		if (dg.rt == null) {
			dg.rt = new ReportThread("rt");
			dg.rt.setDaemon(true);
			dg.rt.start();
		}
		
		tiFileName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
		tiPath.setCellValueFactory(new PropertyValueFactory<>("path"));
		tiDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		tiEvent.setCellValueFactory(new PropertyValueFactory<>("event"));
		tiType.setCellValueFactory(new PropertyValueFactory<>("type"));
		
		tiEvent.setStyle( "-fx-alignment: CENTER;");
		tiType.setStyle( "-fx-alignment: CENTER;");
		
		// Assuming 'tableView' is your TableView and 'tiPath' is the column you want to grow
		tiPath.prefWidthProperty().bind(tableView.widthProperty()
				.subtract(20.0)
				.subtract(tiFileName.widthProperty())
				.subtract(tiDate.widthProperty())
				.subtract(tiType.widthProperty())
				.subtract(tiEvent.widthProperty()));
		
		dg.changes.addChangeListener(new ChangeListener() {

			@Override
			public void changeEventOccurred(ChangeEvent evt) {
				final String data = evt.data;
				
				if (evt.type == Changes.FILE_CREATE) {
					Platform.runLater(new Runnable() {
	                    @Override
						public void run() {
	                    	File f = new File(data);
	                    	
	                    	ObservableList<ChangedFile> ol = tableView.getItems();
	                    	boolean flag = false;
	                    	for (ChangedFile cfs : ol) {
	                    		if (cfs.getPath().equals(data) == true) {
	                    			flag = true;
	                    			DateFormat simple = new SimpleDateFormat(dg.tfmt);
		                			Date d = new Date();
		                			cfs.setDate(simple.format(d));
		                			cfs.setEvent("C");
		                			if (f.isDirectory() == true) {
		                				cfs.setType("D");
		                			} else {
		                				cfs.setType("F");
		                			}
		                			tableView.refresh();
	                    			break;
	                    		}
	                    	}
	                    	
	                    	if (flag == false) {
	                    		ChangedFile cf = new ChangedFile(f.getName(), f.getParent(), "C");
	                    		DateFormat simple = new SimpleDateFormat(dg.tfmt);
	                			Date d = new Date();
	                			cf.setDate(simple.format(d));
	                			if (f.isDirectory() == true) {
	                				cf.setType("D");
	                			} else {
	                				cf.setType("F");
	                			}
	                    		tableView.getItems().add(cf);
	                    		tableView.refresh();
	                    	}
	                    }
	                });
				} else if (evt.type == Changes.FILE_MODIFY) {
					Platform.runLater(new Runnable() {
	                    @Override
						public void run() {
	                    	File f = new File(data);
	                    	
	                    	ObservableList<ChangedFile> ol = tableView.getItems();
	                    	boolean flag = false;
	                    	for (ChangedFile cfs : ol) {
	                    		if (cfs.getPath().equals(data) == true) {
	                    			flag = true;
	                    			DateFormat simple = new SimpleDateFormat(dg.tfmt);
		                			Date d = new Date();
		                			cfs.setDate(simple.format(d));
		                			cfs.setEvent("M");
		                			tableView.refresh();
	                    			break;
	                    		}
	                    	}
	                    	
	                    	if (flag == false) {
	                    		ChangedFile cf = new ChangedFile(f.getName(), f.getParent(), "M");
	                    		DateFormat simple = new SimpleDateFormat(dg.tfmt);
	                			Date d = new Date();
	                			cf.setDate(simple.format(d));
	                			if (f.isDirectory() == true) {
	                				cf.setType("D");
	                			} else {
	                				cf.setType("F");
	                			}
	                    		tableView.getItems().add(cf);
	                    		tableView.refresh();
	                    	}
	                    }
	                });
				} else if (evt.type == Changes.FILE_DELETE) {
					Platform.runLater(new Runnable() {
	                    @Override
						public void run() {
	                    	ObservableList<ChangedFile> ol = tableView.getItems();
	                    	
	                    	boolean flag = false;
	                    	for (ChangedFile cfs : ol) {
	                    		if (cfs.getPath().equals(data) == true) {
	                    			flag = true;
	                    			DateFormat simple = new SimpleDateFormat(dg.tfmt);
		                			Date d = new Date();
		                			cfs.setDate(simple.format(d));
		                			cfs.setEvent("D");
		                			tableView.refresh();
	                    			break;
	                    		}
	                    	}
	                    	
	                    	if (flag == false) {
	                    		File f = new File(data);
	                    		ChangedFile cf = new ChangedFile(f.getName(), f.getParent(), "D");
	                    		DateFormat simple = new SimpleDateFormat(dg.tfmt);
	                			Date d = new Date();
	                			cf.setDate(simple.format(d));
	                    		tableView.getItems().add(cf);
	                    		tableView.refresh();
	                    	}
	                    }
	                });
				} else if (evt.type == Changes.DIR_DELETE) {
					Platform.runLater(new Runnable() {
	                    @Override
						public void run() {
	                    	ObservableList<ChangedFile> ol = tableView.getItems();
	                    	
	                    	boolean flag = false;
	                    	for (ChangedFile cfs : ol) {
	                    		if (cfs.getPath().equals(data) == true) {
	                    			flag = true;
	                    			DateFormat simple = new SimpleDateFormat(dg.tfmt);
		                			Date d = new Date();
		                			cfs.setDate(simple.format(d));
		                			cfs.setEvent("D");
		                			cfs.setType("D");
		                			tableView.refresh();
	                    			break;
	                    		}
	                    	}
	                    	
	                    	if (flag == false) {
	                    		File f = new File(data);
	                    		ChangedFile cf = new ChangedFile(f.getName(), f.getParent(), "D");
	                    		DateFormat simple = new SimpleDateFormat(dg.tfmt);
	                			Date d = new Date();
	                			cf.setDate(simple.format(d));
	                			cf.setType("D");
	                    		tableView.getItems().add(cf);
	                    		tableView.refresh();
	                    	}
	                    }
	                });
				}
					
			}
			
		});
	}
}
