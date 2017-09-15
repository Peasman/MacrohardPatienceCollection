package gui.startnewgame;

import controller.MPCController;
import exceptions.InconsistentMoveException;
import gui.battlefield.BattlefieldView;
import gui.sprites.Textures;
import gui.transitions.TransitionFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.GameType;
import model.Player;
import model.PlayerType;

public class RegisterWindow extends BorderPane{
	private VBox vbox;
	private HBox hbox,loadBox;
	private Label shown;
	private Button back,okay;
	private TextField yourname;
	private RadioButton load, generate;
	private MPCController mpcController;
	private GameType currentGameType ;
	
	public RegisterWindow(MPCController mpcController, GameType type) {
		this.currentGameType = type;
		this.mpcController = mpcController;
		register();
	}
	
	private void register() {
		final ToggleGroup loadGroup = new ToggleGroup();
		this.getStylesheets().add(this.getClass().getResource("startNewGame.css").toExternalForm());
		generate = new RadioButton("Stack generieren");
		generate.getStyleClass().add("chose-radio");
		generate.setToggleGroup(loadGroup);
		generate.setSelected(true);
		load = new RadioButton("Stack laden");
		load.getStyleClass().add("chose-radio");
		load.setToggleGroup(loadGroup);
		vbox = new VBox();
		hbox = new HBox();
		hbox.setSpacing(10);
		okay = new Button();
		okay.setText("Okay");
		okay.getStyleClass().addAll("game-button");
		back = new Button();
		back.setText("Zurück");
		back.getStyleClass().addAll("game-button");
		shown = new Label();
		shown.setText("Bitte Namen eingeben");
		shown.getStyleClass().addAll("chose-eingabe");
		yourname = new TextField();
		yourname.setPromptText("Name eingeben");
		yourname.setMaxWidth(250);
		loadBox = new HBox();
		loadBox.setSpacing(10);
		loadBox.setAlignment(Pos.CENTER);
		HBox.setMargin(load, new Insets(10, 0, 10, 0));
		
		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e)
			{
				back();
			}
		});
		
		ChoseGameModeView.initButtonHoverAnimation(okay,null,this);
		ChoseGameModeView.initButtonHoverAnimation(back,null,this);
		
		okay.setOnAction(new EventHandler<ActionEvent> () {

			@Override
			public void handle(ActionEvent event) {
				startGame();
			}
			
		});
		
		loadBox.getChildren().addAll(generate, load);
		hbox.getChildren().addAll(okay, back);
		vbox.getChildren().addAll(shown, yourname, loadBox, hbox);
		hbox.setAlignment(Pos.CENTER);
		vbox.setMaxSize(400, 600);
		vbox.setSpacing(10);
		this.setCenter(vbox);
		vbox.setAlignment(Pos.CENTER);
		this.setBackground(new Background(new BackgroundImage(Textures.TABLE_TEXTURE, BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));		//VBox.setMargin(hbox1, new Insets(0, 0, 10, 0));
	}
	
	private void startGame()
	{
		if (generate.isSelected()) {
			if (currentGameType == GameType.IDIOT_PATIENCE) {
				BattlefieldView battlefieldView = new BattlefieldView(mpcController);
				mpcController.setBattlefield(mpcController.getGameController().initBattlefield(GameType.IDIOT_PATIENCE,
						new Player(PlayerType.HUMAN, yourname.getText()), null));
				TransitionFactory.animateLayoutSwitch(battlefieldView, (ActionEvent event) -> {
					try {
						mpcController.getGameLogicController().startGame();
					} catch (InconsistentMoveException e) {
						e.printStackTrace();
					}
				}, null, true);
			} else if (currentGameType == GameType.FREECELL) {
				BattlefieldView battlefieldView = new BattlefieldView(mpcController);
				mpcController.setBattlefield(mpcController.getGameController().initBattlefield(GameType.FREECELL,
						new Player(PlayerType.HUMAN, yourname.getText()), null));

				TransitionFactory.animateLayoutSwitch(battlefieldView, (ActionEvent event) -> {
					try {
						mpcController.getGameLogicController().startGame();
					} catch (InconsistentMoveException e) {
						e.printStackTrace();
					}
				}, null, true);
			}
		} else {
			loadStack();
		}
	}
	private void loadStack() {
		TransitionFactory.animateLayoutSwitch(new LoadStackView(mpcController, currentGameType, yourname.getText()), null, null, false);
	}
	private void back()
	{
		TransitionFactory.animateLayoutSwitch(new ChoseGameModeView(mpcController), null, null, true);
	}
}
