package gui.startnewgame;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.EnumMap;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

import application.MpcApplication;
import controller.GameController;
import controller.MPCController;
import controller.ServerController;
import exceptions.InconsistentMoveException;
import gui.battlefield.BattlefieldView;
import gui.sprites.Textures;
import gui.transitions.TransitionFactory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
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
import model.Battlefield;
import model.CardStack;
import model.CardStackType;
import model.GameType;
import model.Player;
import model.PlayerType;

public class MultiplayerView extends BorderPane {
	private VBox vbox;
	private Label showntwo;
	private Button local, client, host, back;
	private HBox loadBox;
	private MPCController mpcController;
	private TextField yourname, hisname;
	private String nameOne, nameTwo;
	private RadioButton load, generate;
	private DialogPane pane;
	boolean loadGame;

	public MultiplayerView(MPCController mpcController, Boolean loadGame) {
		final ToggleGroup loadGroup = new ToggleGroup();
		@SuppressWarnings("unused")
		final ToggleGroup playerGroup = new ToggleGroup();
		this.getStylesheets().add(this.getClass().getResource("startNewGame.css").toExternalForm());
		this.loadGame = loadGame;
		this.mpcController = mpcController;
		vbox = new VBox();
		vbox.setSpacing(10);
		showntwo = new Label();
		showntwo.setText("Bitte Auswahl treffen");
		showntwo.getStyleClass().add("chose-eingabe");
		local = new Button();
		host = new Button();
		client = new Button();
		local.getStyleClass().add("game-button1");
		host.getStyleClass().add("game-button1");
		client.getStyleClass().add("game-button1");
		local.setText("Lokales Spiel");
		host.setText("Spiel hosten");
		client.setText("Spiel beitreten");
		generate = new RadioButton();
		generate.setText("Stack generieren");
		generate.getStyleClass().add("chose-radio");
		generate.setToggleGroup(loadGroup);
		load = new RadioButton("Stack laden");
		load.getStyleClass().add("chose-radio");
		load.setToggleGroup(loadGroup);
		back = new Button("Zurück");
		back.getStyleClass().addAll("game-button");
		generate.setToggleGroup(loadGroup);
		load.setToggleGroup(loadGroup);
		yourname = new TextField();
		yourname.setPromptText("Namen eingeben");
		yourname.setMaxWidth(250);
		pane = new DialogPane();
		hisname = new TextField();
		pane.setContent(hisname);

		local.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				generateName(true);
				if (generate.isSelected()) {
					if (!loadGame) {
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setDialogPane(pane);
						alert.setTitle("Name Player 2");
						ButtonType buttonTypeYes = new ButtonType("Okay");
						ButtonType buttonTypeNo = new ButtonType("Zurück");
						alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
						alert.initOwner(MpcApplication.mainStage);
						alert.showAndWait();
						if (alert.getResult() == buttonTypeYes) {
							generateName(false);
							BattlefieldView battlefieldView = new BattlefieldView(mpcController);
							mpcController.setBattlefield(mpcController.getGameController().initBattlefield(
									GameType.AGGRO_PATIENCE, new Player(PlayerType.HUMAN, nameOne),
									new Player(PlayerType.HUMAN, nameTwo)));
							TransitionFactory.animateLayoutSwitch(battlefieldView, (ActionEvent event) -> {
								try {
									mpcController.getGameLogicController().startGame();
								} catch (InconsistentMoveException e1) {
									e1.printStackTrace();
								}
							}, null, false);
						} else {
							TransitionFactory.animateLayoutSwitch(new MultiplayerView(mpcController, false), null, null,
									true);
						}
					} else {
						mpcController.loadBattlefield("rsc/gamesaves/HUMAN_PATIENCE.mpcs");
					}

				} else {
					TransitionFactory.animateLayoutSwitch(
							new LoadStackView(mpcController, GameType.AGGRO_PATIENCE, nameOne), null, null, false);
				}
			}

		});

		host.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				openLobby();
			}
		});

		client.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				clientConnect();
			}
		});

		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				TransitionFactory.animateLayoutSwitch(new ChosePlayModeView(mpcController, false), null, null, true);
			}
		});

		loadGroup.selectToggle(generate);
		loadBox = new HBox();
		loadBox.setSpacing(10);
		loadBox.getChildren().addAll(generate, load);
		loadBox.setAlignment(Pos.CENTER);
		vbox = new VBox();
		vbox.setSpacing(10);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(showntwo, loadBox, yourname, local, host, client, back);
		vbox.setMaxSize(500, 600);
		this.setBackground(new Background(new BackgroundImage(Textures.TABLE_TEXTURE, BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
		this.setCenter(vbox);
		ChoseGameModeView.initButtonHoverAnimation(local, null, this);
		ChoseGameModeView.initButtonHoverAnimation(client, null, this);
		ChoseGameModeView.initButtonHoverAnimation(host, null, this);
		ChoseGameModeView.initButtonHoverAnimation(back, null, this);
	}

	private void generateName(boolean player) {
		if(player){
			if (yourname.getText().trim().equals("")) {
				nameOne = "Player 1";
			} else {
				nameOne = yourname.getText();
			}
		} else {
			if (hisname.getText().trim().equals("")) {
				nameTwo = "Player 2";
			} else {
				nameTwo = hisname.getText();
			}
		}
	}

	private void clientConnect() {
		ServerController client = mpcController.getServerController();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		TextField ip = new TextField();
		DialogPane pane = new DialogPane();
		generateName(true);

		pane.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
		pane.setContent(ip);
		alert.setDialogPane(pane);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			System.out.println("Starte connect");
			BattlefieldView battlefieldView = new BattlefieldView(mpcController);
			client.connect(ip.getText());
			client.sendMessage(nameOne);
			String receive = client.receiveMessage();
			Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<EnumMap<CardStackType, CardStack>>() {
			}.getType(), new EnumMapInstanceCreator<CardStackType, CardStack>(CardStackType.class)).create();
			Battlefield battlefield = gson.fromJson(receive, Battlefield.class);
			battlefield.swapPlayers();
			mpcController.setBattlefield(battlefield);
			TransitionFactory.animateLayoutSwitch(battlefieldView, (ActionEvent event) -> {
				try {
					mpcController.getGameLogicController().startGame();
				} catch (InconsistentMoveException e) {
					e.printStackTrace();
				}
			}, null, false);

			mpcController.getServerController().setReadMovesThread(new Thread(() -> client.readMoves()));
			mpcController.getServerController().getReadMovesThread().start();

		} else {
			System.out.println("ABBRUCH");
		}
	}

	private void openLobby() {
		if (generate.isSelected()) {
			generateName(true);
			ServerController server = mpcController.getServerController();
			server.openServer();
			customAlert = new Alert(AlertType.INFORMATION);
			customAlert.setTitle("Warten auf Spieler");
			customAlert.setHeaderText("Warte darauf, dass sich ein anderer Spieler verbindet.");
			try {
				customAlert.setContentText("Ihre IP ist : " + InetAddress.getLocalHost().toString());
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}
			Thread t = new Thread(() -> waitForConnect());
			t.start();
			customAlert.showAndWait();
			if (mpcController.getServerController().isOnline()) {
				System.out.println("Starte lobby");
				BattlefieldView battlefieldView = new BattlefieldView(mpcController);
				Battlefield battlefield = GameController.getInstance().initBattlefield(GameType.AGGRO_PATIENCE,
						new Player(PlayerType.HUMAN, nameOne),
						new Player(PlayerType.HUMAN_ONLINE, server.receiveMessage()));
				mpcController.setBattlefield(battlefield);
				server.sendMessage(new Gson().toJson(battlefield));
				TransitionFactory.animateLayoutSwitch(battlefieldView, (ActionEvent event) -> {
					try {
						mpcController.getGameLogicController().startGame();
					} catch (InconsistentMoveException e) {
						e.printStackTrace();
					}
				}, null, false);
				mpcController.getServerController().setReadMovesThread(new Thread(() -> server.readMoves()));
				mpcController.getServerController().getReadMovesThread().start();

			} else {

			}
		} else {
			TransitionFactory.animateLayoutSwitch(new LoadStackView(mpcController, GameType.AGGRO_PATIENCE, nameOne),
					null, null, false);
		}
	}

	class EnumMapInstanceCreator<K extends Enum<K>, V> implements InstanceCreator<EnumMap<K, V>> {
		private final Class<K> enumClazz;

		public EnumMapInstanceCreator(final Class<K> enumClazz) {
			super();
			this.enumClazz = enumClazz;
		}

		@Override
		public EnumMap<K, V> createInstance(final Type type) {
			return new EnumMap<K, V>(enumClazz);
		}
	}

	private static Alert customAlert = new Alert(AlertType.INFORMATION);

	public void waitForConnect() {
		while (customAlert.getResult() == null && !mpcController.getServerController().isOnline()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("CANCELLED OR CONNECTED");
		try {
			mpcController.getServerController().getServerSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Platform.runLater(() -> customAlert.close());
	}

}
