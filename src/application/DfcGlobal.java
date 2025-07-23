package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.rkw.IniFile;

import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DfcGlobal {

private static DfcGlobal singleton = null;
	
	private DfcGlobal() {
		initGlobals();
	}
	
	private void initGlobals() {
		appVersion = "1.0.0";
		
		changes = new Changes();
		
		homeDir = new File(System.getProperty("user.home") + File.separator + "Dfc");
		if (homeDir.exists() == false)
			homeDir.mkdirs();
		
		File iniFile = new File(homeDir, "dfc.ini");
		if (iniFile.exists() == false) {
			try {
				copyResource("/resources/dfc.ini", iniFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		ini = new IniFile(iniFile.getAbsolutePath());
		
		logDir = new File(homeDir, "Reports");
		if (logDir.exists() == false) {
			logDir.mkdirs();
		}
		
		ignoreDirsFile = new File(homeDir, "ignore_dirs.txt");
//		System.out.println(ignoreFile.getAbsolutePath());
		if (ignoreDirsFile.exists() == false) {
			try {
				copyResource("/resources/ignore_dirs.txt", ignoreDirsFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		ignoreDirs = new ArrayList<String>();
		
		try {
            Scanner myReader = new Scanner(ignoreDirsFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                data = data.trim();
                if (data == null || data.length() <= 0 || data.charAt(0) == '#') {
                	continue;
                }
                ignoreDirs.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred: File not found.");
            e.printStackTrace();
        }
		
		ignoreFilesFile = new File(homeDir, "ignore_files.txt");
//		System.out.println(ignoreFile.getAbsolutePath());
		if (ignoreFilesFile.exists() == false) {
			try {
				copyResource("/resources/ignore_files.txt", ignoreFilesFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		ignoreFiles = new ArrayList<String>();
		
		try {
            Scanner myReader = new Scanner(ignoreFilesFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                data = data.trim();
                if (data == null || data.length() <= 0 || data.charAt(0) == '#') {
                	continue;
                }
                ignoreFiles.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred: File not found.");
            e.printStackTrace();
        }
		
		ignoreExtsFile = new File(homeDir, "ignore_exts.txt");
//		System.out.println(ignoreExtsFile.getAbsolutePath());
		if (ignoreExtsFile.exists() == false) {
			try {
				copyResource("/resources/ignore_exts.txt", ignoreExtsFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		ignoreExts = new ArrayList<String>();
		
		try {
            Scanner myReader = new Scanner(ignoreExtsFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                data = data.trim();
                if (data == null || data.length() <= 0 || data.charAt(0) == '#') {
                	continue;
                }
                ignoreExts.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred: File not found.");
            e.printStackTrace();
        }
		
//		System.out.println(ignoreList);
		
		sceneNav = new SceneNav();
		
		createWatch();
		
		addDirectories(null);
	}
	
	public String appVersion = null;
	public File baseDir = null;
	public File homeDir = null;
	
	public IniFile ini = null;
	
	public WatchService ws = null;
	
	private Alert alert = null;
	
	public SceneNav sceneNav = null;
	
	public int headsPid = 0;
	public int tailsPid = 0;
	public long sleepTime = 30;
	
	public String screenRes = null;
	public double screenWidth = 0.0;
	public double screenHeight = 0.0;
	public double screenScaleX = 0.0;
	public double screenScaleY = 0.0;
	public double screenDpi = 0.0;
	
//	public boolean snapShot1 = false;
//	public boolean snapShot2 = false;
	public long lastAction = 0;
	public boolean sleepMode = false;
	public boolean showBoth = false;
	public boolean sleepFlag = false;
//	public SleepModeThread smt = null;
	public Timeline sleepTask = null;
	
	public String ipAddress = null;
	public String user = null;
	public String password = null;
	public String path = null;
	
	public List<String> ignoreDirs = null;
	public List<String> ignoreFiles = null;
	public List<String> ignoreExts = null;
	public File ignoreExtsFile = null;
	public File ignoreFilesFile = null;
	public File ignoreDirsFile = null;
	public File logDir = null;
	
	public DfcThread dt = null;
	public ReportThread rt = null;
	
	public Changes changes = null;
	public String tfmt = "yy/MM/dd HH:mm:ss";
	public TableView<ChangedFile> tv = null;
	
	public static DfcGlobal getInstance() {
		// return SingletonHolder.singleton;
		if (singleton == null) {
			synchronized (DfcGlobal.class) {
				singleton = new DfcGlobal();
			}
		}
		return singleton;
	}
	
	public String scenePeek() {
		if (sceneNav.sceneQue == null || sceneNav.sceneQue.isEmpty())
			return SceneNav.DFC;
		else
			return sceneNav.sceneQue.peek();
	}
	
	public void guiRestart(String msg) {
		String errMsg = String.format("A GUI error occurred.\r\nError loading %s\r\n\r\nRestarting GUI.", msg);
		showAlert("GUI Error", errMsg, AlertType.CONFIRMATION, false);
		System.exit(1);
	}

	public void loadSceneNav(String fxml) {
		if (sceneNav.loadScene(fxml) == true) {
			guiRestart(fxml);
		}
	}
	
	public void closeAlert() {
		if (alert != null) {
			alert.close();
			alert = null;
		}
	}
	
	public void createWatch() {
		try {
			if (ws != null) {
				ws.close();
			}
			ws = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addDirectories(AnchorPane aPane) {
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		
		if (aPane != null)
        	aPane.getScene().setCursor(Cursor.WAIT);

        // Define the task to be executed
        Runnable oneTimeTask = () -> {
            Object[] objs = ini.getSectionKeys("Directories");
    		for (Object obj : objs) {
    			String k = (String)obj;
    			String s = ini.getString("Directories", obj);
//    			System.out.println("Dir: " + obj + ", Recursive: " + s);
    			
    			try {
    				
    				Path dir = null;
    				
    				if (k.equalsIgnoreCase("user.home") == true) {
    					dir = Paths.get(System.getProperty("user.home"));
    				} else {
    					File f = new File(k);
    					if (f.exists() == false) {
    						continue;
    					}
    					dir = Paths.get((String)obj);
    				}
    	            
    	            if (s.charAt(0) == 'T' || s.charAt(0) == 't' || s.charAt(0) == 'Y' || s.charAt(0) == 'y') {
    	            	addRecursive(dir);
    	            } else {
    	            	dir.register(ws, StandardWatchEventKinds.ENTRY_CREATE,
            			        StandardWatchEventKinds.ENTRY_DELETE,
            			        StandardWatchEventKinds.ENTRY_MODIFY);
    	            }
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
        };

        // Schedule the task to run after a delay of 1 seconds
        scheduler.schedule(oneTimeTask, 1, TimeUnit.SECONDS);

        // It's important to shut down the scheduler when no more tasks are expected
        // Otherwise, the program might not terminate
        scheduler.shutdown(); 
        
        if (aPane != null)
			aPane.getScene().setCursor(null);
	}
	
	public void addRecursive(Path dir) {
			
		try {
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                	
                	Path fn = dir.getFileName();
                	
                	// check if directory is on the ignore list.
                	for (String s : ignoreDirs) {
                		if (s.equals(fn.toString()) == true) {
//                			System.out.println("Skip dir: " + dir);
                			return FileVisitResult.SKIP_SUBTREE;
                		}
                	}
                	
                    dir.register(ws, StandardWatchEventKinds.ENTRY_CREATE,
        			        StandardWatchEventKinds.ENTRY_DELETE,
        			        StandardWatchEventKinds.ENTRY_MODIFY);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void generateReport(LocalDateTime currentDateTime, String path) {
		if (tv == null) {
			return;
		}
		
		String pattern2 = "yyyy_MM_dd_HH_mm";
		String pattern3 = "yyyy/MM/dd HH:mm";
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern(pattern2);
        DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern(pattern3);
        String fd2 = currentDateTime.format(formatter2);
        String fd3 = currentDateTime.format(formatter3);
        
        File f2 = new File(path);
        if (f2.exists() == false) {
        	f2.mkdirs();
        }
        
//        String fileName = path + File.separator + fd2 + ".log";
//        System.out.println("fileName: " + fileName);
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
        	     new FileChooser.ExtensionFilter("Log Files", "*.log")
        	    ,new FileChooser.ExtensionFilter("All Files", "*.*")
        	);
        fileChooser.setInitialDirectory(f2);
        fileChooser.setInitialFileName(fd2 + ".log");
        File sf = fileChooser.showSaveDialog(null);
        
        if (sf == null) {
        	return;
        }
        
    	try {
            FileWriter writer = new FileWriter(sf); // Creates or overwrites the file
            writer.write("# DFC Report " + fd3 + "\n");
            writer.write("# Date, FileName, Path, Event, Type\n\n");
            
            ObservableList<ChangedFile> lst = tv.getItems();
            for (ChangedFile cf : lst) {
            	writer.write(cf.getDate() + ", " + cf.getFileName() + ", " + 
            			cf.getPath() + ", " + cf.getEvent() + ", " + cf.getType() + "\n");
            }
            
            writer.close();
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
	}
	
	public void purgeReports(LocalDateTime currentDateTime, String path) {
		File f = new File(path);
		File[] files = f.listFiles( new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".log");
			}
		});
		
		long purge = ini.getLong("System",  "reportPurge");
		
		List<File> lst = new ArrayList<File>();
		
		for (File file : files) {
			long lm = file.lastModified();
			Instant instant = Instant.ofEpochMilli(lm);
			ZoneId systemZoneId = ZoneId.systemDefault();
			LocalDateTime ldt = instant.atZone(systemZoneId).toLocalDateTime();
			
			long days = currentDateTime.until(ldt, ChronoUnit.DAYS);
			if (days > purge) {
				lst.add(file);
			}
		}
		
		if (lst.size() > 0) {
			for (File file : lst) {
				file.delete();
			}
		}
	}
	
//	public boolean isWindows(String osName) {
//        return (osName.contains("win"));
//    }
//
//    public boolean isMac(String osName) {
//        return (osName.contains("mac"));
//    }
//
//    public boolean isUnix(String osName) {
//        return (osName.contains("nix") || osName.contains("nux") || osName.contains("aix"));
//    }
//
//    public boolean isSolaris(String osName) {
//        return (osName.contains("sunos"));
//    }
	
	public boolean showConfirm(String title, String msg) {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.getDialogPane().setPrefWidth(725.0);
		for ( ButtonType bt : alert.getDialogPane().getButtonTypes() ) {
		    Button button = ( Button ) alert.getDialogPane().lookupButton( bt );
		    button.setStyle("-fx-font-size: 24px;");
		}
		alert.setTitle(title);
		alert.setHeaderText(null);
		
		alert.setContentText(msg);
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(
		   getClass().getResource("myDialogs.css").toExternalForm());
		dialogPane.getStyleClass().add("myDialog");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			return true;
		} else {
			return false;
		}
	}
	
	public ButtonType yesNoAlert(String title, String msg, AlertType alertType) {
		ButtonType yes = new ButtonType("Yes", ButtonData.OK_DONE);
		ButtonType no = new ButtonType("No", ButtonData.CANCEL_CLOSE);
		Alert alert = new Alert(alertType, msg, yes, no);
		alert.getDialogPane().setPrefWidth(500.0);
		alert.setTitle(title);
		alert.setHeaderText(null);
		
		for (ButtonType bt : alert.getDialogPane().getButtonTypes()) {
			Button button = (Button) alert.getDialogPane().lookupButton(bt);
			button.setStyle("-fx-font-size: 16px;");
			button.setPrefWidth(100.0);
		}
		
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("myDialogs.css").toExternalForm());
		dialogPane.getStyleClass().add("myDialog");

		Optional<ButtonType> result = alert.showAndWait();
		
		return result.get();
	}

	public ButtonType showAlert(String title, String msg, AlertType alertType, boolean yesNo) {
		alert = new Alert(alertType);
		alert.getDialogPane().setPrefWidth(500.0);
		for (ButtonType bt : alert.getDialogPane().getButtonTypes()) {
			Button button = (Button) alert.getDialogPane().lookupButton(bt);
			if (yesNo == true) {
				if (button.getText().equals("Cancel"))
					button.setText("No");
				else if (button.getText().equals("OK"))
					button.setText("Yes");
			}
			button.setStyle("-fx-font-size: 16px;");
			button.setPrefWidth(100.0);
		}
		alert.setTitle(title);
		alert.setHeaderText(null);

		alert.setContentText(msg);
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("myDialogs.css").toExternalForm());
		dialogPane.getStyleClass().add("myDialog");

		ButtonType bt = alert.showAndWait().get();

		alert = null;

		return bt;
	}
	
	public void showOutput(String title, String msg, AlertType alertType, boolean yesNo) {
		alert = new Alert(alertType);
		alert.getDialogPane().setPrefWidth(750.0);
		alert.getDialogPane().setPrefHeight(450.0);
		for (ButtonType bt : alert.getDialogPane().getButtonTypes()) {
			Button button = (Button) alert.getDialogPane().lookupButton(bt);
			if (yesNo == true) {
				if (button.getText().equals("Cancel"))
					button.setText("No");
				else if (button.getText().equals("OK"))
					button.setText("Yes");
			}
			button.setStyle("-fx-font-size: 16px;");
			button.setPrefWidth(100.0);
		}
		alert.setTitle(title);
		alert.setHeaderText(null);
	
		TextArea txt = new TextArea(msg);
		txt.setStyle("-fx-font-size: 16px;");
		txt.setWrapText(true);

		alert.getDialogPane().setContent(txt);
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("myDialogs.css").toExternalForm());
		dialogPane.getStyleClass().add("myDialog");

		alert.showAndWait().get();

		alert = null;
	}

	public void Msg(String msg) {
		System.out.println(msg);
	}
	
	public boolean copyFile(File in, File out) {
		
		try {
	        FileInputStream fis  = new FileInputStream(in);
	        FileOutputStream fos = new FileOutputStream(out);
	        byte[] buf = new byte[8192];
	        int i = 0;
	        while((i = fis.read(buf)) != -1) {
	            fos.write(buf, 0, i);
	        }
	        fis.close();
	        fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return true;
		}
		
		return false;
    }
	
	public void deleteDir(File file) {
	    File[] contents = file.listFiles();
	    if (contents != null) {
	        for (File f : contents) {
	            deleteDir(f);
	        }
	    }
	    file.delete();
	}
	
	public boolean isDigit(String s) {
		boolean tf = true;
		
		for (int i = 0; i < s.length(); i++) {
			if (Character.isDigit(s.charAt(i)) == false) {
				tf = false;
				break;
			}
		}
		
		return tf;
	}
	
	public void centerScene(Node node, String fxml, String title, String data) {
		FXMLLoader loader = null;
		try {
			Stage stage = new Stage();
			stage.setTitle(title);

			loader = new FXMLLoader(getClass().getResource(fxml));

			stage.initModality(Modality.APPLICATION_MODAL);

			stage.setScene(new Scene(loader.load()));
			stage.hide();
			
			stage.getScene().setCursor(Cursor.NONE);

			Stage ps = (Stage) node.getScene().getWindow();

			ChangeListener<Number> widthListener = (observable, oldValue, newValue) -> {
				double stageWidth = newValue.doubleValue();
				stage.setX(ps.getX() + ps.getWidth() / 2 - stageWidth / 2);
			};
			ChangeListener<Number> heightListener = (observable, oldValue, newValue) -> {
				double stageHeight = newValue.doubleValue();
				stage.setY(ps.getY() + ps.getHeight() / 2 - stageHeight / 2);
			};

			stage.widthProperty().addListener(widthListener);
			stage.heightProperty().addListener(heightListener);

			// Once the window is visible, remove the listeners
			stage.setOnShown(e2 -> {
				stage.widthProperty().removeListener(widthListener);
				stage.heightProperty().removeListener(heightListener);
			});

			stage.showAndWait();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public FXMLLoader loadScene(Node node, String fxml, String title, String data) {
		FXMLLoader loader = null;
		try {
			Stage stage = new Stage();
			stage.setTitle(title);

			loader = new FXMLLoader(getClass().getResource(fxml));

			stage.initModality(Modality.APPLICATION_MODAL);

			stage.setScene(new Scene(loader.load()));
			stage.hide();
			
			stage.getScene().setCursor(Cursor.NONE);

			Stage ps = (Stage) node.getScene().getWindow();

			ChangeListener<Number> widthListener = (observable, oldValue, newValue) -> {
				double stageWidth = newValue.doubleValue();
				stage.setX(ps.getX() + ps.getWidth() / 2 - stageWidth / 2);
			};
			ChangeListener<Number> heightListener = (observable, oldValue, newValue) -> {
				double stageHeight = newValue.doubleValue();
				stage.setY(ps.getY() + ps.getHeight() / 2 - stageHeight / 2);
			};

			stage.widthProperty().addListener(widthListener);
			stage.heightProperty().addListener(heightListener);

			// Once the window is visible, remove the listeners
			stage.setOnShown(e2 -> {
				stage.widthProperty().removeListener(widthListener);
				stage.heightProperty().removeListener(heightListener);
			});

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return loader;
	}
	
	public ProcessRet runProcess(String[] args, boolean flag) {
    	Process p = null;
    	
    	ProcessBuilder pb = new ProcessBuilder();
		pb.redirectErrorStream(true);
		pb.command(args);

		try {
			p = pb.start();
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}

//		@SuppressWarnings("resource")
		StreamGobbler inGobbler = new StreamGobbler(p.getInputStream(), flag);
		inGobbler.start();

		int ev = 0;
		try {
			ev = p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return new ProcessRet(ev, inGobbler.getOutput());
    }
	
	public String copyResource(String resourceName, File fd) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        try {
            stream = DfcGlobal.class.getResourceAsStream(resourceName);
            if(stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            resStreamOut = new FileOutputStream(fd);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
        	if (stream != null)
        		stream.close();
        	if (resStreamOut != null)
        		resStreamOut.close();
        }

        return fd + resourceName;
    }
}
