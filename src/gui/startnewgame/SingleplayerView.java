package gui.startnewgame;

import ai.zankpatience.ZankPatienceAI;
import controller.GameController;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import model.GameType;
import model.Player;
import model.PlayerType;

public class SingleplayerView extends BorderPane {
	private VBox vbox;
	private HBox loadBox;
	private Button kieasy, kimid, kihard, back;
	private RadioButton generate, load;
	private Label shown;
	private TextField yourname;
	@SuppressWarnings("unused")
	private boolean loadGame;
	private ImageView view;
	private MPCController mpcController;
	private BorderPane pane;
	private Player human;

	public SingleplayerView(MPCController mpcContoroller, Boolean loadGame) {
		this.setBackground(new Background(new BackgroundImage(Textures.TABLE_TEXTURE, BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
		this.getStylesheets().add(this.getClass().getResource("startNewGame.css").toExternalForm());
		this.loadGame = loadGame;
		this.mpcController = mpcContoroller;
		view = new ImageView();
		pane = this;
		pane.getChildren().add(view);
		view.setOpacity(0.5);
		vbox = new VBox();
		vbox.setMaxSize(500, 600);
		vbox.setAlignment(Pos.CENTER);
		this.setCenter(vbox);
		vbox.setSpacing(10);
		loadBox = new HBox();
		loadBox.setSpacing(10);
		ToggleGroup loadGroup = new ToggleGroup();
		generate = new RadioButton("Stack generieren");
		generate.setToggleGroup(loadGroup);
		generate.getStyleClass().add("chose-radio");
		generate.setSelected(true);
		load = new RadioButton("Stack laden");
		load.setToggleGroup(loadGroup);
		load.getStyleClass().add("chose-radio");
		loadBox.getChildren().addAll(generate, load);
		loadBox.setAlignment(Pos.CENTER);
		shown = new Label("Bitte Auswahl treffen");
		shown.getStyleClass().add("chose-eingabe");
		yourname = new TextField();
		yourname.setMaxWidth(300);
		yourname.setPromptText("Name eingeben");
		kieasy = new Button("Waldorfschüler");
		kieasy.getStyleClass().add("chose-button1");
		kieasy.setId("EASY");
		kimid = new Button("Geistes? -" + "\n" + "wissenschaftler");
		kimid.setTextAlignment(TextAlignment.CENTER);
		kimid.getStyleClass().add("chose-button2");
		kimid.setId("MID");
		kihard = new Button("Master of" + "\n" + "Science");
		kihard.setTextAlignment(TextAlignment.CENTER);
		kihard.getStyleClass().add("chose-button3");
		kihard.setId("HARD");
		back = new Button("Zurück");
		back.getStyleClass().addAll("game-button");
		vbox.getChildren().addAll(shown, loadBox, yourname, kieasy, kimid, kihard, back);

		ChoseGameModeView.initButtonHoverAnimation(back, view, pane);
		ChoseGameModeView.initButtonHoverAnimation(kieasy, view, pane);
		ChoseGameModeView.initButtonHoverAnimation(kimid, view, pane);
		ChoseGameModeView.initButtonHoverAnimation(kihard, view, pane);

		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				TransitionFactory.animateLayoutSwitch(new ChosePlayModeView(mpcController, false), null, null, true);
			}
		});

		kieasy.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (yourname.getText().trim().equals("")) {
					human = new Player(PlayerType.HUMAN, "Player 1");
				} else {
					human = new Player(PlayerType.HUMAN, yourname.getText());
				}
				Player aiPlayer = new Player(PlayerType.AI1, "Waldorfschüler");
				if (load.isSelected()) {
					TransitionFactory.animateLayoutSwitch(
							new LoadStackView(mpcContoroller, GameType.AGGRO_PATIENCE, human, aiPlayer), null, null,
							true);
				} else {
					mpcController.setBattlefield(
							GameController.getInstance().initBattlefield(GameType.AGGRO_PATIENCE, human, aiPlayer));
					BattlefieldView fieldView = new BattlefieldView(mpcController);
					ZankPatienceAI zankAi = new ZankPatienceAI(mpcController, aiPlayer);
					mpcController.getGameLogicController().setZankAI(zankAi, zankAi.getDifficulty());
					TransitionFactory.animateLayoutSwitch(fieldView, (event) -> {
						try {
							mpcController.getGameLogicController().startGame();
						} catch (InconsistentMoveException e1) {
							e1.printStackTrace();
						}
					}, null, false);
				}
			}
		});

		kimid.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (yourname.getText().trim().equals("")) {
					human = new Player(PlayerType.HUMAN, "Player 1");
				} else {
					human = new Player(PlayerType.HUMAN, yourname.getText());
				}
				Player aiPlayer = new Player(PlayerType.AI2, "Geistes?wissenschaftler");
				if (load.isSelected()) {
					TransitionFactory.animateLayoutSwitch(
							new LoadStackView(mpcContoroller, GameType.AGGRO_PATIENCE, human, aiPlayer), null, null,
							true);
				} else {
					mpcController.setBattlefield(
							GameController.getInstance().initBattlefield(GameType.AGGRO_PATIENCE, human, aiPlayer));
					BattlefieldView fieldView = new BattlefieldView(mpcController);
					ZankPatienceAI zankAi = new ZankPatienceAI(mpcController, aiPlayer);
					mpcController.getGameLogicController().setZankAI(zankAi, zankAi.getDifficulty());
					TransitionFactory.animateLayoutSwitch(fieldView, (event) -> {
						try {
							mpcController.getGameLogicController().startGame();
						} catch (InconsistentMoveException e1) {
							e1.printStackTrace();
						}
					}, null, false);
				}
			}
		});

		kihard.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (yourname.getText().trim().equals("")) {
					human = new Player(PlayerType.HUMAN, "Player 1");
				} else {
					human = new Player(PlayerType.HUMAN, yourname.getText());
				}
				Player aiPlayer = new Player(PlayerType.AI3, "Master of Science");

				if (load.isSelected()) {
					TransitionFactory.animateLayoutSwitch(
							new LoadStackView(mpcContoroller, GameType.AGGRO_PATIENCE, human, aiPlayer), null, null,
							true);
				} else {
					mpcController.setBattlefield(
							GameController.getInstance().initBattlefield(GameType.AGGRO_PATIENCE, human, aiPlayer));
					BattlefieldView fieldView = new BattlefieldView(mpcController);
					ZankPatienceAI zankAi = new ZankPatienceAI(mpcController, aiPlayer);
					mpcController.getGameLogicController().setZankAI(zankAi, zankAi.getDifficulty());
					TransitionFactory.animateLayoutSwitch(fieldView, (event) -> {
						try {
							mpcController.getGameLogicController().startGame();
						} catch (InconsistentMoveException e1) {
							e1.printStackTrace();
						}
					}, null, false);
				}

			}
		});
	}
}
