package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Main extends Application {
	private Pane mainPane = null;
	private DfcGlobal dg = DfcGlobal.getInstance();
	
	@Override
	public void start(Stage primaryStage) {
		try {
			System.setProperty("java.net.preferIPv4Stack" , "true");
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/CuteTiger.png")));
			primaryStage.setTitle("DFC by Kelly Wiles");
			Runtime.getRuntime().addShutdownHook(new ShutdownHook());
//			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setScene(createScene(loadMainPane()));
			
			// Use the Run Configuration screens to set InEclipse to true in the Environment tab.
//			String flag = System.getenv("InEclipse");
//			if (flag == null || flag.equalsIgnoreCase("true") == false) {
//				primaryStage.setMaximized(true);
//			}
			
//			primaryStage.setOnCloseRequest((e) -> e.consume());		// disable Stage close button.
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void setWidth(Double width) {
		mainPane.setPrefWidth(width);
	}

	@Override
	public void stop() {
//		System.out.println("*** DFC is Ending. ***");
		
		if (dg.dt != null)
			dg.dt.stopWatching();
		if (dg.rt != null)
			dg.rt.stopReporting();

		SceneInfo si = dg.sceneNav.fxmls.get(dg.scenePeek());
		if (si != null && si.controller instanceof RefreshScene) {
			RefreshScene c = (RefreshScene) si.controller;
			c.leaveScene();
		}
	}

	/**
	 * Loads the main fxml layout.
	 *
	 * @return the loaded pane.
	 * @throws IOException if the pane could not be loaded.
	 */
//    @SuppressWarnings("resource")
	private Pane loadMainPane() throws IOException {
//		System.out.println("*** DFC is Starting. ***");

		FXMLLoader loader = new FXMLLoader();

		mainPane = (Pane) loader.load(getClass().getResourceAsStream(SceneNav.MAIN)); // SceneNav

		SceneNavController mainController = loader.getController();

		dg.sceneNav.setMainController(mainController);
		dg.sceneNav.loadScene(SceneNav.DFC);

		return mainPane;
	}

	/**
	 * Creates the main application scene.
	 *
	 * @param mainPane the main application layout.
	 *
	 * @return the created scene.
	 */
	private Scene createScene(Pane mainPane) {
		Scene scene = new Scene(mainPane);

//		scene.getStylesheets().setAll(getClass().getResource("application.css").toExternalForm());

		return scene;
	}
}
