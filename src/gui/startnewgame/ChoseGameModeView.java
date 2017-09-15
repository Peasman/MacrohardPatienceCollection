package gui.startnewgame;

import java.io.File;

import application.MpcApplication;
import controller.IOController;
import controller.MPCController;
import exceptions.InconsistentMoveException;
import gui.battlefield.BattlefieldView;
import gui.mainview.MainMenuViewController;
import gui.sprites.Textures;
import gui.transitions.TransitionFactory;
import gui.transitions.TransitionFactory.fromToParameter;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.GameType;

public class ChoseGameModeView extends BorderPane {
	private Button idiotPatience, freeCell, aggroPatience, back;
	private MPCController mpcController;
	private ImageView view;
	@SuppressWarnings("unused")
	private GameType currentGameType;
	private VBox vbox;
	BorderPane pane;

	public ChoseGameModeView(MPCController mpcController) {
		this.mpcController = mpcController;
		pane = this;
		initButtons();
	}

	private void initButtons() {
		view = new ImageView();
		view.fitWidthProperty().bind(MpcApplication.mainStage.widthProperty());
		view.fitHeightProperty().bind(MpcApplication.mainStage.heightProperty());
		view.setOpacity(0.5);
		this.getChildren().add(view);
		this.setBackground(new Background(new BackgroundImage(Textures.TABLE_TEXTURE, BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
		this.getStylesheets().add(this.getClass().getResource("startNewGame.css").toExternalForm());
		idiotPatience = new Button();
		idiotPatience.setText("Idiot-Patience");
		idiotPatience.getStyleClass().addAll("chose-button", "game-button");
		idiotPatience.setId("IDIOT");
		freeCell = new Button();
		freeCell.setText("FreeCell");
		freeCell.getStyleClass().addAll("chose-button", "game-button");
		freeCell.setId("FREE");
		aggroPatience = new Button();
		aggroPatience.setText("Aggro-Patience");
		aggroPatience.getStyleClass().addAll("chose-button", "game-button");
		aggroPatience.setId("AGGRO");
		back = new Button();
		back.setText("Zurück");
		back.getStyleClass().add("game-button");
		
		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TransitionFactory.animateLayoutSwitch(new MainMenuViewController(mpcController), null, null, true);
			}
		});
		idiotPatience.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!new File("rsc/gamesaves/IDIOT_PATIENCE.mpcs").exists()) {
					idiotPatience(e);
					currentGameType = GameType.IDIOT_PATIENCE;
				} else {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Idiot Patience");
					alert.setHeaderText(null);
					alert.setContentText("Willst Du das gespeicherte Spiel fortsetzen?");
					ButtonType buttonTypeYes = new ButtonType("Ja");
					ButtonType buttonTypeNo = new ButtonType("Nein");
					alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
					alert.initOwner(MpcApplication.mainStage);
					alert.showAndWait();
					if (alert.getResult() == buttonTypeYes) {
						BattlefieldView battlefieldView = new BattlefieldView(mpcController);
						mpcController.loadBattlefield(IOController.getProperty("IDIOT_PATH"));
						TransitionFactory.animateLayoutSwitch(battlefieldView, (ActionEvent event) -> {
							try {
								mpcController.getGameLogicController().startGame();
							} catch (InconsistentMoveException e1) {
								e1.printStackTrace();
							}
						}, null, false);
					} else {
						idiotPatience(e);
						currentGameType = GameType.IDIOT_PATIENCE;
					}
				}
			}
		});

		freeCell.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!new File("rsc/gamesaves/FREECELL.mpcs").exists()) {
					freeCell(e);
					currentGameType = GameType.FREECELL;
				} else {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Idiot Patience");
					alert.setHeaderText(null);
					alert.setContentText("Willst Du das gespeicherte Spiel fortsetzen?");
					ButtonType buttonTypeYes = new ButtonType("Ja");
					ButtonType buttonTypeNo = new ButtonType("Nein");
					alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
					alert.initOwner(MpcApplication.mainStage);
					alert.showAndWait();
					if (alert.getResult() == buttonTypeYes) {
						BattlefieldView battlefieldView = new BattlefieldView(mpcController);
						mpcController.loadBattlefield(IOController.getProperty("FREECELL_PATH"));
						TransitionFactory.animateLayoutSwitch(battlefieldView, (ActionEvent event) -> {
							try {
								mpcController.getGameLogicController().startGame();
							} catch (InconsistentMoveException e1) {
								e1.printStackTrace();
							}
						}, null, false);
					} else {
						freeCell(e);
						currentGameType = GameType.FREECELL;
					}
				}
			}
		});

		aggroPatience.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				TransitionFactory.animateLayoutSwitch(new ChosePlayModeView(mpcController, false), null, null, false);
			}
		});
		ChoseGameModeView.initButtonHoverAnimation(idiotPatience, view, pane);
		ChoseGameModeView.initButtonHoverAnimation(aggroPatience, view, pane);
		ChoseGameModeView.initButtonHoverAnimation(freeCell, view, pane);
		ChoseGameModeView.initButtonHoverAnimation(back, view, pane);
		vbox = new VBox();
		vbox.setSpacing(10);
		vbox.getChildren().addAll(idiotPatience, aggroPatience, freeCell, back);
		vbox.setAlignment(Pos.CENTER);
		this.setCenter(vbox);
	}

	private void idiotPatience(ActionEvent event) {
		TransitionFactory.animateLayoutSwitch(new RegisterWindow(mpcController, GameType.IDIOT_PATIENCE), null, null,
				false);
	}

	private void freeCell(ActionEvent event) {
		TransitionFactory.animateLayoutSwitch(new RegisterWindow(mpcController, GameType.FREECELL), null, null, false);
	}

	public static void initButtonHoverAnimation(Button button, ImageView view, Pane pane) {
		final ScaleTransition growTransition = TransitionFactory.createScaleTransition(button, Duration.millis(100),
				new fromToParameter(1, 1, 1.1, 1.1), Interpolator.LINEAR);

		final ScaleTransition shrinkTransition = TransitionFactory.createScaleTransition(button, Duration.millis(100),
				new fromToParameter(1.1, 1.1, 1, 1), Interpolator.LINEAR);

		button.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				growTransition.play();
				if (view != null) {
					if (button.getId() != null) {
						if (button.getId().equals("IDIOT")) {
							view.setImage(Textures.IDIOT);
						} else if (button.getId().equals("FREE")) {
							view.setImage(Textures.FREE);
						} else if (button.getId().equals("AGGRO")) {
							view.setImage(Textures.AGGRO);
						} else if (button.getId().equals("EASY")) {
							view.setImage(Textures.WALDORF);
						} else if (button.getId().equals("MID")) {
							view.setImage(Textures.GHOST);
						} else if (button.getId().equals("HARD")) {
							view.setImage(Textures.MASTER);
						}
					}
				}
			}
		});

		button.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				shrinkTransition.play();
				if (view != null) {
					view.setImage(Textures.TABLE_TEXTURE);
					pane.setBackground(
							new Background(new BackgroundImage(Textures.TABLE_TEXTURE, BackgroundRepeat.REPEAT,
									BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
				}
			}

		});
	}
}
