package gui.mainview;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import controller.MPCController;
import gui.settings.SettingsView;
import gui.sprites.Textures;
import gui.startnewgame.ChoseGameModeView;
import gui.stats.StatsViewController;
import gui.transitions.TransitionFactory;
import gui.transitions.TransitionFactory.CreateRotateByTransitionParameter;
import gui.transitions.TransitionFactory.fromToParameter;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class MainMenuViewController extends StackPane {

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="btnNewGame"
	private Button btnNewGame; // Value injected by FXMLLoader

	@FXML // fx:id="btnLoadGame"
	private Button btnLoadGame; // Value injected by FXMLLoader

	@FXML // fx:id="btnSettings"
	private Button btnSettings; // Value injected by FXMLLoader

	@FXML // fx:id="btnStatistics"
	private Button btnStatistics; // Value injected by FXMLLoader

	@FXML
	private BorderPane mainContainer;
	@FXML
	private ImageView title;

	private SequentialTransition btnActionAnimation;
	private MPCController mpcController;

	public MainMenuViewController (MPCController mpcController) {
		this.mpcController = mpcController;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		this.setBackground(new Background(new BackgroundImage(Textures.TABLE_TEXTURE, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
				BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

//		mainContainer.setMinHeight(Main.GAME_HEIGHT);
//		mainContainer.setMinWidth(Main.GAME_WIDTH);
//		mainContainer.setMaxHeight(Integer.MAX_VALUE);
//		mainContainer.setMaxWidth(Integer.MAX_VALUE);

	}

	@FXML
	void onActionBtnLoadGame(ActionEvent event) {
		performBtnActionAnimation(btnLoadGame);

	}

	@FXML
	void onActionBtnNewGame(ActionEvent event) {
		performBtnActionAnimation(btnNewGame);
		//StartNewGameView startNewGameView = new StartNewGameView(mpcController);
		ChoseGameModeView choseGameModeView = new ChoseGameModeView(mpcController);
		TransitionFactory.animateLayoutSwitch(choseGameModeView, null, null, false);
	}

	@FXML
	void onActionBtnSettings(ActionEvent event) {
		performBtnActionAnimation(btnSettings);
		SettingsView settingsView = new SettingsView(mpcController);
		TransitionFactory.animateLayoutSwitch(settingsView, null, null, true);
	}

	@FXML
	void onActionBtnStatistics(ActionEvent event) {
		// final ScaleTransition scaleTransition =
		// TransitionFactory.createScaleTransition(btnStatistics,
		// Duration.millis(1000), 1, 1, 1.5, 1.5, Interpolator.LINEAR);
		// scaleTransition.play();
		performBtnActionAnimation(btnStatistics);
		
		
		StatsViewController stats = new StatsViewController(mpcController);
		
		TransitionFactory.animateLayoutSwitch(stats, null, null, true);
		
		//Scene scene = new Scene(stats,Main.GAME_WIDTH,Main.GAME_HEIGHT);
		//Main.stage.setScene(scene);
	}

	@FXML // This method is called by the FXMLLoader when initialization is
	void initialize() {
		assert btnNewGame != null : "fx:id=\"btnNewGame\" was not injected: check your FXML file 'main_menu.fxml'.";
		assert btnLoadGame != null : "fx:id=\"btnLoadGame\" was not injected: check your FXML file 'main_menu.fxml'.";
		assert btnSettings != null : "fx:id=\"btnSettings\" was not injected: check your FXML file 'main_menu.fxml'.";
		assert btnStatistics != null : "fx:id=\"btnStatistics\" was not injected: check your FXML file 'main_menu.fxml'.";

		// Image imgNewGame = new Image("");

		// btnNewGame.setGraphic(new ImageView(imgNewGame));
		// btnSettings.setGraphic(new ImageView(imgSettings));

		ScaleTransition scaleTransitionOne = TransitionFactory.createScaleTransition(btnSettings, Duration.millis(50),
				new fromToParameter(1.2, 1.2, 1.15, 1.15), Interpolator.LINEAR);
		ScaleTransition scaleTransitionTwo = TransitionFactory.createScaleTransition(btnSettings, Duration.millis(50),
				new fromToParameter(1.15, 1.15, 1.2, 1.2), Interpolator.LINEAR);

		btnActionAnimation = new SequentialTransition();
		btnActionAnimation.getChildren().addAll(scaleTransitionOne, scaleTransitionTwo);
		btnActionAnimation.setCycleCount(1);
		btnActionAnimation.setAutoReverse(true);
		
		this.getStylesheets().add(this.getClass().getResource("main_menu.css").toExternalForm());
		this.getStyleClass().add("root");

		initBtnSettings();
		initBtnStatistics();
		initBtnLoadGame();
		initBtnNewGame();
		btnLoadGame.setVisible(false);
		btnLoadGame.setManaged(false);

	}

	private void initBtnSettings() {
		Image imgSettings = new Image(getClass().getResource("settings.png").toExternalForm(), 100, 100, true, true);
		BackgroundImage btnSettingsBgImg = new BackgroundImage(imgSettings, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background btnSettingsBg = new Background(btnSettingsBgImg);
		btnSettings.setBackground(btnSettingsBg);

		btnSettings.setMinWidth(100);
		btnSettings.setMinHeight(100);

		// btnSettings.setMaxWidth(110);
		// btnSettings.setMaxHeight(110);

		final RotateTransition rotateTransition = TransitionFactory.createRotateByTransition(btnSettings,
				Duration.millis(500), new CreateRotateByTransitionParameter(90, 1), Interpolator.EASE_OUT);

		final ScaleTransition growTransition = TransitionFactory.createScaleTransition(btnSettings,
				Duration.millis(100), new fromToParameter(1, 1, 1.2, 1.2), Interpolator.LINEAR);
		// growTransition.setAutoReverse(true);

		final ScaleTransition shrinkTransition = TransitionFactory.createScaleTransition(btnSettings,
				Duration.millis(100), new fromToParameter(1.2, 1.2, 1, 1), Interpolator.LINEAR);
		// shrinkTransition.setAutoReverse(true);

		btnSettings.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				growTransition.play();
				rotateTransition.play();
			}

		});

		btnSettings.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				shrinkTransition.play();
			}

		});
	}

	private void initBtnStatistics() {
		Image imgStatistics = new Image(getClass().getResource("trophy.png").toExternalForm(), 100, 100, true, true);
		BackgroundImage btnStatisticsBgImg = new BackgroundImage(imgStatistics, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background btnStatisticsBg = new Background(btnStatisticsBgImg);
		btnStatistics.setBackground(btnStatisticsBg);
		btnStatistics.setMinWidth(100);
		btnStatistics.setMinHeight(100);

		final ScaleTransition growTransition = TransitionFactory.createScaleTransition(btnStatistics,
				Duration.millis(100), new fromToParameter(1, 1, 1.2, 1.2), Interpolator.LINEAR);
		// growTransition.setAutoReverse(true);

		final ScaleTransition shrinkTransition = TransitionFactory.createScaleTransition(btnStatistics,
				Duration.millis(100), new fromToParameter(1.2, 1.2, 1, 1), Interpolator.LINEAR);
		// shrinkTransition.setAutoReverse(false);

		btnStatistics.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				growTransition.play();
			}

		});

		btnStatistics.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				shrinkTransition.play();
			}

		});
	}

	public void initBtnLoadGame() {
//		Image imgStatistics = new Image(getClass().getResource("load.png").toExternalForm(), 100, 100, true, true);
//		BackgroundImage btnStatisticsBgImg = new BackgroundImage(imgStatistics, BackgroundRepeat.NO_REPEAT,
//				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
//		Background btnStatisticsBg = new Background(btnStatisticsBgImg);
//		btnLoadGame.setBackground(btnStatisticsBg);
//		btnLoadGame.setMinWidth(100);
//		btnLoadGame.setMinHeight(100);
		
		ImageView imageView = new ImageView(new Image(getClass().getResource("load.png").toExternalForm(), 50, 50, true, true));
		
		btnLoadGame.setGraphic(imageView);
		btnLoadGame.setContentDisplay(ContentDisplay.LEFT);
		btnLoadGame.getStyleClass().add("menu-button");
		btnLoadGame.setPadding(new Insets(0, 10, 0, 0));

		final ScaleTransition growTransition = TransitionFactory.createScaleTransition(btnLoadGame,
				Duration.millis(100), new fromToParameter(1, 1, 1.2, 1.2), Interpolator.LINEAR);
		// growTransition.setAutoReverse(true);

		final ScaleTransition shrinkTransition = TransitionFactory.createScaleTransition(btnLoadGame,
				Duration.millis(100), new fromToParameter(1.2, 1.2, 1, 1), Interpolator.LINEAR);
		// shrinkTransition.setAutoReverse(false);

		btnLoadGame.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				growTransition.play();
			}

		});

		btnLoadGame.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				shrinkTransition.play();
			}

		});
	}

	public void initBtnNewGame() {
//		Image imgStatistics = new Image(getClass().getResource("play.png").toExternalForm(), 100, 100, true, true);
//		BackgroundImage btnStatisticsBgImg = new BackgroundImage(imgStatistics, BackgroundRepeat.NO_REPEAT,
//				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
//		Background btnStatisticsBg = new Background(btnStatisticsBgImg);
//		btnNewGame.setBackground(btnStatisticsBg);
//		btnNewGame.setMinWidth(100);
//		btnNewGame.setMinHeight(100);
		
		ImageView imageView = new ImageView(new Image(getClass().getResource("newGameIcon.png").toExternalForm(), 50, 50, true, true));
		btnNewGame.setGraphic(imageView);
		btnNewGame.setContentDisplay(ContentDisplay.LEFT);
		btnNewGame.getStyleClass().add("menu-button");
		btnNewGame.setPadding(new Insets(0, 10, 0, 0));

		final ScaleTransition growTransition = TransitionFactory.createScaleTransition(btnNewGame, Duration.millis(100),
				new fromToParameter(1, 1, 1.2, 1.2), Interpolator.LINEAR);
		// growTransition.setAutoReverse(true);

		final ScaleTransition shrinkTransition = TransitionFactory.createScaleTransition(btnNewGame,
				Duration.millis(100), new fromToParameter(1.2, 1.2, 1, 1), Interpolator.LINEAR);
		// shrinkTransition.setAutoReverse(false);

		btnNewGame.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				growTransition.play();
			}

		});

		btnNewGame.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				shrinkTransition.play();
			}

		});
	}

	private void performBtnActionAnimation(Node node) {
		btnActionAnimation.setNode(node);

		for (Animation transition : btnActionAnimation.getChildren()) {
			((ScaleTransition) transition).setNode(node);
		}

		btnActionAnimation.play();
	}

}
