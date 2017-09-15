package application;

import java.io.IOException;
import java.util.ArrayList;

import controller.IOController;
import controller.MPCController;
import gui.mainview.MainMenuViewController;
import gui.sprites.Textures;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.GameStatistics;

/**
 * Klasse zum Starten des Programms
 * 
 * @author Friedemann Runte
 *
 */
public class MpcApplication extends Application {
	public static final boolean DEBUG = true;
	
	public static final int GAME_WIDTH = 1600;
	public static final int GAME_HEIGHT = 920;
	public static final StackPane GAME_WRAPPER = new StackPane();
	public static Stage mainStage;

	
	private void loadStats(MPCController mpcController) {
	//STATSTIKEN WERDEN GELADEN
			try {
				mpcController.getStatisticsController().setGameStatistics(IOController.loadStats(IOController.getProperty("STATS_PATH")));
			} catch (IOException e) {
				System.out.println("Fehlen beim Laden der Statistik");
				mpcController.getStatisticsController().setGameStatistics(new GameStatistics());
			}
	}
	
	private void saveStats(MPCController mpcController) {
		//STATISTIKEN WERDEN GESPEICHERT
		try {
			IOController.saveStats(mpcController.getStatisticsController().getStatistics(), IOController.getProperty("STATS_PATH"));
		} catch (IOException e) {
			System.out.println("Fehlen beim Speichern der Statistik");
			e.printStackTrace();
		}
	}
	
	public static ArrayList<EventHandler<WindowEvent>> closeRequestHandlers = new ArrayList<>();
	
	
	@Override
	public void start(Stage stage) {
		MpcApplication.mainStage = stage;
		stage.setTitle("Macrohard Patience Collection");
		stage.getIcons().add(Textures.ICON);

		MPCController mpcController = new MPCController();

		stage.setMinHeight(GAME_HEIGHT);
		stage.setMinWidth(GAME_WIDTH);
		PerspectiveCamera camera = new PerspectiveCamera();
		
		stage.setHeight(GAME_HEIGHT);
		stage.setWidth(GAME_WIDTH);
		
		closeRequestHandlers.add((WindowEvent event) -> {
			saveStats(mpcController);
		});
		
		closeRequestHandlers.add((WindowEvent) -> {
			IOController.saveProperties();
		});
		
		stage.setOnCloseRequest((WindowEvent event) -> {
			for (EventHandler<WindowEvent> handler : closeRequestHandlers){
				handler.handle(event);
			}
			Platform.exit();
		});
		loadStats(mpcController);
		IOController.loadProperties();
		
		
		GAME_WRAPPER.getChildren().add(new MainMenuViewController(mpcController));
		GAME_WRAPPER.getStyleClass().add("root");
		
		Scene scene = new Scene(GAME_WRAPPER, GAME_WIDTH, GAME_HEIGHT, true, SceneAntialiasing.BALANCED);
		scene.setCamera(camera);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		IOController.playSound("startup_distorted.wav", -25f);
		Textures.initialize();
		launch(args);
	}
}