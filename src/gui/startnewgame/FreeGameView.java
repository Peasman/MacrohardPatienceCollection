package gui.startnewgame;

import java.nio.file.Paths;

import ai.zankpatience.ZankPatienceAI;
import controller.MPCController;
import exceptions.InconsistentMoveException;
import gui.battlefield.BattlefieldView;
import gui.sprites.Textures;
import gui.transitions.TransitionFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.Battlefield;
import model.GameType;
import model.Player;
import model.PlayerType;

public class FreeGameView extends BorderPane {
	@SuppressWarnings("unused")
	private MPCController mpcController;
	private VBox playerOne, playerTwo, middle;
	private HBox hbox;
	private Label labelOne, labelTwo, aiLabelOne, aiLabelTwo;
	private Button okay, back;
	private ImageView iconOne, iconTwo, iconMiddle;
	private ComboBox<String> typeOne, typeTwo;
	private TextField nameOne, nameTwo;
	private String yourname, hisname;
	private RadioButton generate, load;

	public FreeGameView(MPCController mpcController) {
		this.getStylesheets().add(this.getClass().getResource("startNewGame.css").toExternalForm());
		this.setBackground(new Background(new BackgroundImage(Textures.TABLE_TEXTURE, BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
		this.getStylesheets().add(this.getClass().getResource("startNewGame.css").toExternalForm());
		this.mpcController = mpcController;
		playerOne = new VBox();
		playerTwo = new VBox();
		middle = new VBox();
		middle.setAlignment(Pos.CENTER);
		middle.setMaxSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
		middle.setSpacing(15);
		hbox = new HBox();
		hbox.setSpacing(10);
		okay = new Button("Okay");
		okay.getStyleClass().add("game-button");
		back = new Button("Zurück");
		back.getStyleClass().add("game-button");
		hbox.getChildren().addAll(okay, back);
		hbox.setAlignment(Pos.CENTER);
		iconMiddle = new ImageView();
		iconMiddle.setImage(Textures.VERSUS);
		ToggleGroup loadGroup = new ToggleGroup();
		load = new RadioButton();
		load.setText("Stack laden");
		load.setToggleGroup(loadGroup);
		generate = new RadioButton();
		generate.setText("Stack generieren");
		generate.setToggleGroup(loadGroup);
		generate.setSelected(true);
		HBox loadBox = new HBox();
		loadBox.getChildren().addAll(load, generate);
		loadBox.setAlignment(Pos.CENTER);
		middle.getChildren().addAll(iconMiddle,loadBox, hbox);
		this.setCenter(middle);
		labelOne = new Label("Player 1");
		labelOne.getStyleClass().add("chose-eingabe");
		labelTwo = new Label("Player 2");
		labelTwo.getStyleClass().add("chose-eingabe");
		iconOne = new ImageView();
		iconOne.setImage(new Image(Paths.get("rsc/LOCAL_PLAYER.png").toUri().toString()));
		iconOne.setFitHeight(250);
		iconOne.setFitWidth(250);
		iconOne.setPreserveRatio(true);
		iconTwo = new ImageView();
		iconTwo.setImage(new Image(Paths.get("rsc/ONLINE_PLAYER.png").toUri().toString()));
		iconTwo.setFitHeight(250);
		iconTwo.setFitWidth(250);
		aiLabelOne = new Label("");
		aiLabelTwo = new Label("");
		aiLabelOne.setVisible(false);
		aiLabelOne.setFont(new Font(25));
		aiLabelOne.setTextFill(Color.WHITE);
		aiLabelTwo.setVisible(false);
		aiLabelOne.setManaged(false);
		aiLabelTwo.setManaged(false);
		aiLabelTwo.setFont(new Font(25));
		aiLabelTwo.setTextFill(Color.WHITE);
		iconTwo.setPreserveRatio(true);
		ObservableList<String> playerTypes = FXCollections.observableArrayList();
		playerTypes.addAll("Mensch", "KI-Einfach", "KI-Medium", "KI-Schwer");
		typeOne = new ComboBox<String>();
		typeOne.getStyleClass().add("game-combo");
		typeOne.setItems(playerTypes);
		typeOne.getSelectionModel().select(0);
		typeOne.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				changeImage(iconOne, newValue, true);
			}
		});
		typeTwo = new ComboBox<String>();
		typeTwo.getStyleClass().add("game-combo");
		typeTwo.setItems(playerTypes);
		typeTwo.getSelectionModel().select(0);
		typeTwo.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
				changeImage(iconTwo, newValue, false);

			}
		});

		nameOne = new TextField();
		nameOne.setPromptText("Name eingeben");
		nameOne.setMaxWidth(150);
		nameTwo = new TextField();
		nameTwo.setPromptText("Name eingeben");
		nameTwo.setMaxWidth(150);
		playerOne.getChildren().addAll(labelOne, iconOne, typeOne, aiLabelOne, nameOne);
		playerOne.setAlignment(Pos.CENTER);
		playerOne.setSpacing(10);
		playerTwo.getChildren().addAll(labelTwo, iconTwo, typeTwo, aiLabelTwo, nameTwo);
		playerTwo.setAlignment(Pos.CENTER);
		playerTwo.setSpacing(10);

		this.setLeft(playerOne);
		this.setRight(playerTwo);
		BorderPane.setMargin(playerOne, new Insets(0, 0, 0, 150));
		BorderPane.setMargin(playerTwo, new Insets(0, 150, 0, 0));

		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				TransitionFactory.animateLayoutSwitch(new ChosePlayModeView(mpcController, false), null, null, false);
			}
		});

		okay.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				if (nameOne.getText().trim().equals("")) {
					yourname = "Player 1";
				} else {
					yourname = nameOne.getText();
				}
				if (nameTwo.getText().trim().equals("")) {
					hisname = "Player 2";
				} else {
					hisname = nameTwo.getText();
				}
				if (load.isSelected()) {
					TransitionFactory.animateLayoutSwitch(
							new LoadStackView(mpcController, GameType.AGGRO_PATIENCE,
									generatePlayer(typeOne.getSelectionModel().getSelectedItem(), true),
									generatePlayer(typeTwo.getSelectionModel().getSelectedItem(), false)),
							null, null, true);
				} else {
					BattlefieldView battlefieldView = new BattlefieldView(mpcController);
					Battlefield battlefield = mpcController.getGameController().initBattlefield(GameType.AGGRO_PATIENCE,
							generatePlayer(typeOne.getSelectionModel().getSelectedItem(), true),
							generatePlayer(typeTwo.getSelectionModel().getSelectedItem(), false));
					mpcController.setBattlefield(battlefield);
					if (battlefield.getPlayerOne().getType() == PlayerType.AI3) {
						ZankPatienceAI zankAi = new ZankPatienceAI(mpcController, battlefield.getPlayerOne());
						mpcController.getGameLogicController().setZankAI(zankAi, zankAi.getDifficulty());
					}
					if (battlefield.getPlayerTwo().getType() == PlayerType.AI3) {
						ZankPatienceAI zankAi = new ZankPatienceAI(mpcController, battlefield.getPlayerTwo());
						mpcController.getGameLogicController().setZankAI(zankAi, zankAi.getDifficulty());
					}

					TransitionFactory.animateLayoutSwitch(battlefieldView, (ActionEvent event) -> {
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

	private void changeImage(ImageView view, String player, boolean playerOne) {
		switch (player) {
		case "Mensch":
			if (playerOne) {
				view.setImage(Textures.HUMAN);
			} else {
				view.setImage(Textures.HUMANONLINE);
			}
			changeTextField(true, playerOne, "");
			break;
		case "KI-Einfach":

			view.setImage(Textures.WALDORF);
			changeTextField(false, playerOne, "Waldorfschüler");
			break;
		case "KI-Medium":
			view.setImage(Textures.GHOST);
			changeTextField(false, playerOne, "Geistes?wissenschaftler");
			break;
		case "KI-Schwer":
			view.setImage(Textures.MASTER);
			changeTextField(false, playerOne, "Master of Science");

			break;
		}
	}

	private void changeTextField(boolean humanPlayer, boolean player, String aiName) {
		if (humanPlayer) {
			if (player) {
				nameOne.setManaged(true);
				nameOne.setVisible(true);
				aiLabelOne.setVisible(false);
				aiLabelOne.setManaged(false);
			} else {
				nameTwo.setManaged(true);
				nameTwo.setVisible(true);
				aiLabelTwo.setVisible(false);
				aiLabelTwo.setManaged(false);
			}
		} else {
			if (player) {
				aiLabelOne.setVisible(true);
				aiLabelOne.setManaged(true);
				nameOne.setManaged(false);
				nameOne.setVisible(false);
				aiLabelOne.setText(aiName);
			} else {
				aiLabelTwo.setVisible(true);
				aiLabelTwo.setManaged(true);
				nameTwo.setManaged(false);
				nameTwo.setVisible(false);
				aiLabelTwo.setText(aiName);

			}
		}
	}

	private Player generatePlayer(String type, boolean player) {
		switch (type) {
		case "Mensch":
			if (player) {
				return new Player(PlayerType.HUMAN, yourname);
			} else {
				return new Player(PlayerType.HUMAN, hisname);
			}

		case "KI-Einfach":
			return new Player(PlayerType.AI1, "Waldorfschüler");
		case "KI-Medium":
			return new Player(PlayerType.AI2, "Geistes?wissenschaftler");
		case "KI-Schwer":
			return new Player(PlayerType.AI3, "Master of Science");
		}
		return null;
	}

}
