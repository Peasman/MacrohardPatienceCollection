package gui.startnewgame;

import controller.GameController.InitBattlefieldParameter;
import controller.MPCController;
import exceptions.InconsistentMoveException;
import gui.battlefield.BattlefieldView;
import gui.sprites.Textures;
import gui.transitions.TransitionFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.CardStack;
import model.GameType;
import model.Player;
import model.PlayerType;

public class LoadStackView extends BorderPane {
	MPCController mpcController;
	GameType gameType;
	HBox hbox, scdhbox;
	VBox vbox;
	Label shown;
	TextArea box, scdbox;
	Button okay, back;
	public CardStack stack, scdstack;

	public LoadStackView(MPCController mpcController, GameType type, String name) {
		this.gameType = type;
		this.mpcController = mpcController;
		this.getStylesheets().add(this.getClass().getResource("startNewGame.css").toExternalForm());
		shown = new Label("Bitte Stack als Text eingeben");
		shown.getStyleClass().add("chose-eingabe");
		okay = new Button("Okay");
		okay.getStyleClass().add("game-button");
		back = new Button("Zurück");
		back.getStyleClass().add("game-button");
		vbox = new VBox();
		vbox.setSpacing(10);
		vbox.setAlignment(Pos.CENTER);
		hbox = new HBox();
		hbox.setSpacing(10);
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().addAll(okay, back);
		box = new TextArea();
		box.setMaxSize(200, 100);
		box.setPromptText("Karten Player 1");

		if (GameType.AGGRO_PATIENCE == type) {
			scdhbox = new HBox();
			scdhbox.getChildren().addAll(box, scdbox = new TextArea());
			scdhbox.setAlignment(Pos.CENTER);
			scdbox.setMaxSize(200, 100);
			scdbox.setPromptText("Karten Player 2");
			vbox.getChildren().addAll(shown, scdhbox, hbox);
		} else {
			vbox.getChildren().addAll(shown, box, hbox);
		}

		ChoseGameModeView.initButtonHoverAnimation(okay, null, this);
		ChoseGameModeView.initButtonHoverAnimation(back, null, this);

		okay.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				if (GameType.IDIOT_PATIENCE == type) {
					stack = CardStack.fromProtocol(box.getText(), false);
					BattlefieldView battlefieldView = new BattlefieldView(mpcController);
					mpcController.setBattlefield(mpcController.getGameController().initBattlefield(
							GameType.IDIOT_PATIENCE,
							new InitBattlefieldParameter(new Player(PlayerType.HUMAN, name), null, stack, null)));
					TransitionFactory.animateLayoutSwitch(battlefieldView, (ActionEvent event) -> {
						try {
							mpcController.getGameLogicController().startGame();
						} catch (InconsistentMoveException exept) {
							exept.printStackTrace();
						}
					}, null, true);
				} else if (GameType.AGGRO_PATIENCE == type) {
					stack = CardStack.fromProtocol(box.getText(), false);
					scdstack = CardStack.fromProtocol(scdbox.getText(), false);
					BattlefieldView battlefieldView = new BattlefieldView(mpcController);
					mpcController.setBattlefield(mpcController.getGameController().initBattlefield(
							GameType.AGGRO_PATIENCE, new InitBattlefieldParameter(new Player(PlayerType.HUMAN, name),
									new Player(PlayerType.HUMAN, "Peter"), stack, scdstack)));
					TransitionFactory.animateLayoutSwitch(battlefieldView, (ActionEvent event) -> {
						try {
							mpcController.getGameLogicController().startGame();
						} catch (InconsistentMoveException exept) {
							exept.printStackTrace();
						}
					}, null, true);
				} else {
					stack = CardStack.fromProtocol(box.getText(), true);
					BattlefieldView battlefieldView = new BattlefieldView(mpcController);
					mpcController.setBattlefield(mpcController.getGameController().initBattlefield(GameType.FREECELL,
							new InitBattlefieldParameter(new Player(PlayerType.HUMAN, name), null, stack, null)));
					TransitionFactory.animateLayoutSwitch(battlefieldView, (ActionEvent event) -> {
						try {
							mpcController.getGameLogicController().startGame();
						} catch (InconsistentMoveException exept) {
							exept.printStackTrace();
						}
					}, null, true);
				}

			}
		});

		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				TransitionFactory.animateLayoutSwitch(new ChoseGameModeView(mpcController), null, null, true);
			}
		});
		this.setBackground(new Background(new BackgroundImage(Textures.TABLE_TEXTURE, BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
		this.setCenter(vbox);
	}

	public LoadStackView(MPCController mpcController, GameType type, Player playerOne, Player playerTwo) {
		this.gameType = type;
		this.mpcController = mpcController;
		this.getStylesheets().add(this.getClass().getResource("startNewGame.css").toExternalForm());
		shown = new Label("Bitte Stack als Text eingeben");
		shown.getStyleClass().add("chose-eingabe");
		okay = new Button("Okay");
		okay.getStyleClass().add("game-button");
		back = new Button("Zurück");
		back.getStyleClass().add("game-button");
		vbox = new VBox();
		vbox.setSpacing(10);
		vbox.setAlignment(Pos.CENTER);
		hbox = new HBox();
		hbox.setSpacing(10);
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().addAll(okay, back);
		box = new TextArea();
		box.setMaxSize(200, 100);
		box.setPromptText("Karten Player 1");

		if (GameType.AGGRO_PATIENCE == type) {
			scdhbox = new HBox();
			scdhbox.getChildren().addAll(box, scdbox = new TextArea());
			scdhbox.setAlignment(Pos.CENTER);
			scdbox.setMaxSize(200, 100);
			scdbox.setPromptText("Karten Player 2");
			vbox.getChildren().addAll(shown, scdhbox, hbox);
		} else {
			vbox.getChildren().addAll(shown, box, hbox);
		}

		ChoseGameModeView.initButtonHoverAnimation(okay, null, this);
		ChoseGameModeView.initButtonHoverAnimation(back, null, this);

		okay.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				if (GameType.AGGRO_PATIENCE == type) {
					stack = CardStack.fromProtocol(box.getText().trim(), false);
					scdstack = CardStack.fromProtocol(scdbox.getText().trim(), false);
					System.out.println(stack);
					System.out.println(scdstack);
					BattlefieldView battlefieldView = new BattlefieldView(mpcController);
					mpcController
							.setBattlefield(mpcController.getGameController().initBattlefield(GameType.AGGRO_PATIENCE,
									new InitBattlefieldParameter(playerOne, playerTwo, stack, scdstack)));
					TransitionFactory.animateLayoutSwitch(battlefieldView, (ActionEvent event) -> {
						try {
							mpcController.getGameLogicController().startGame();
						} catch (InconsistentMoveException exept) {
							exept.printStackTrace();
						}
					}, null, true);
				}
			}
		});

		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				TransitionFactory.animateLayoutSwitch(new ChoseGameModeView(mpcController), null, null, true);
			}
		});
		this.setBackground(new Background(new BackgroundImage(Textures.TABLE_TEXTURE, BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
		this.setCenter(vbox);
	}

}
