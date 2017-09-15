package gui.startnewgame;

import java.io.File;

import application.MpcApplication;
import controller.IOController;
import controller.MPCController;
import exceptions.InconsistentMoveException;
import gui.battlefield.BattlefieldView;
import gui.sprites.Textures;
import gui.transitions.TransitionFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.GameType;
@SuppressWarnings("unused")
public class ChosePlayModeView extends BorderPane {
	private MPCController mpcController;
	private GameType currentGameType;
	private boolean loadGame;
	private Button singleplay, multiplay, freegame, back;
	private Label shown;
	private VBox vbox;
	private ImageView view;
	private BorderPane pane;

	public ChosePlayModeView(MPCController mpcController, Boolean loadGame) {
		this.setBackground(new Background(new BackgroundImage(Textures.TABLE_TEXTURE, BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
		this.getStylesheets().add(this.getClass().getResource("startNewGame.css").toExternalForm());
		this.mpcController = mpcController;
		this.loadGame = loadGame;
		view = new ImageView();
		pane = this;
		vbox = new VBox();
		vbox.setSpacing(10);
		this.setCenter(vbox);
		singleplay = new Button("Spiel gegen" + "\n" + "Computer");
		singleplay.getStyleClass().add("game-button1");
		multiplay = new Button("Spiel gegen" + "\n" + "Mensch");
		multiplay.getStyleClass().add("game-button1");
		freegame = new Button("Freies Spiel \n konfigurieren");
		freegame.getStyleClass().add("game-button1");
		freegame.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!new File(IOController.getProperty("ZANK_FREE_PATH")).exists()) {
					TransitionFactory.animateLayoutSwitch(new FreeGameView(mpcController), null, null, false);
					currentGameType = GameType.AGGRO_PATIENCE;
					} else {
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Idiot Patience");
						alert.setHeaderText(null);
						alert.setContentText("Willst Du das gespeicherte Spiel fortsetzen?");
						alert.initOwner(MpcApplication.mainStage);
						ButtonType buttonTypeYes = new ButtonType("Ja");
						ButtonType buttonTypeNo = new ButtonType("Nein");
						alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
						alert.showAndWait();
						if (alert.getResult() == buttonTypeYes) {
							BattlefieldView battlefieldView = new BattlefieldView(mpcController);
							mpcController.loadBattlefield(IOController.getProperty("ZANK_FREE_PATH"));
							TransitionFactory.animateLayoutSwitch(battlefieldView, (ActionEvent event) -> {
								try {
									mpcController.getGameLogicController().startGame();
								} catch (InconsistentMoveException e1) {
									e1.printStackTrace();
								}
							}, null, false);
						} else {
							TransitionFactory.animateLayoutSwitch(new FreeGameView(mpcController), null, null,
									false);
						}
					}
				}
			});
		shown = new Label("Welche Spielart?");
		shown.getStyleClass().add("chose-eingabe");
		back = new Button("Zurück");
		back.getStyleClass().add("game-button");
		vbox.getChildren().addAll(shown, singleplay, multiplay, freegame, back);
		vbox.setAlignment(Pos.CENTER);

		ChoseGameModeView.initButtonHoverAnimation(singleplay, view, pane);
		ChoseGameModeView.initButtonHoverAnimation(multiplay, view, pane);
		ChoseGameModeView.initButtonHoverAnimation(back, view, pane);
		ChoseGameModeView.initButtonHoverAnimation(freegame, view, pane);

		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				TransitionFactory.animateLayoutSwitch(new ChoseGameModeView(mpcController), null, null, true);
			}
		});

		singleplay.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!new File(IOController.getProperty("ZANK_VS_AI_PATH")).exists()) {
					TransitionFactory.animateLayoutSwitch(new SingleplayerView(mpcController, false), null, null,
							false);
					currentGameType = GameType.AGGRO_PATIENCE;
				} else {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Idiot Patience");
					alert.setHeaderText(null);
					alert.setContentText("Willst Du das gespeicherte Spiel fortsetzen?");
					alert.initOwner(MpcApplication.mainStage);
					ButtonType buttonTypeYes = new ButtonType("Ja");
					ButtonType buttonTypeNo = new ButtonType("Nein");
					alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
					alert.showAndWait();
					if (alert.getResult() == buttonTypeYes) {
						BattlefieldView battlefieldView = new BattlefieldView(mpcController);
						mpcController.loadBattlefield(IOController.getProperty("ZANK_VS_AI_PATH"));
						TransitionFactory.animateLayoutSwitch(battlefieldView, (ActionEvent event) -> {
							try {
								mpcController.getGameLogicController().startGame();
							} catch (InconsistentMoveException e1) {
								e1.printStackTrace();
							}
						}, null, false);
					} else {
						TransitionFactory.animateLayoutSwitch(new SingleplayerView(mpcController, false), null, null,
								false);
					}
				}
			}
		});

		multiplay.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!new File(IOController.getProperty("ZANK_VS_HUMAN_PATH")).exists()) {
					TransitionFactory.animateLayoutSwitch(new MultiplayerView(mpcController, false), null, null,
							false);
					currentGameType = GameType.AGGRO_PATIENCE;
				} else {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Idiot Patience");
					alert.setHeaderText(null);
					alert.setContentText("Willst Du das gespeicherte Spiel fortsetzen?");
					alert.initOwner(MpcApplication.mainStage);
					ButtonType buttonTypeYes = new ButtonType("Ja");
					ButtonType buttonTypeNo = new ButtonType("Nein");
					alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
					alert.showAndWait();
					if (alert.getResult() == buttonTypeYes) {
						BattlefieldView battlefieldView = new BattlefieldView(mpcController);
						mpcController.loadBattlefield(IOController.getProperty("ZANK_VS_HUMAN_PATH"));
						TransitionFactory.animateLayoutSwitch(battlefieldView, (ActionEvent event) -> {
							try {
								mpcController.getGameLogicController().startGame();
							} catch (InconsistentMoveException e1) {
								e1.printStackTrace();
							}
						}, null, false);
					} else {
						TransitionFactory.animateLayoutSwitch(new MultiplayerView(mpcController, false), null, null,
								false);
					}
				}
			}
		});
	}
}
