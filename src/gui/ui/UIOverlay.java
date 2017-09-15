package gui.ui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.function.Function;

import application.MpcApplication;
import controller.GameLogicController;
import controller.IOController;
import exceptions.InconsistentMoveException;
import gui.battlefield.BattlefieldView;
import gui.mainview.MainMenuViewController;
import gui.sprites.Textures;
import gui.sprites.Textures.CardSkin;
import gui.startnewgame.ChosePlayModeView;
import gui.startnewgame.RegisterWindow;
import gui.transitions.TransitionFactory;
import gui.transitions.TransitionFactory.fromToParameter;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import model.Battlefield;
import model.GameMove;
import model.GameType;
import model.Player;
import model.PlayerType;

public class UIOverlay {
	private BorderPane menuBar;
	private HBox menuBarLeft;
	private HBox menuBarCenter;
	private HBox menuBarRight;

	private Button mainMenuButton, backButton, restartButton, undoButton, fullScreenButton, tipButton;
	private Label gameModeLabel, timeLabel;
	private ScrollPane menu;
	private VBox menuItemList;
	private BattlefieldView battlefieldView;

	// private BorderPane premiumShopLayout;
	private PremiumShop premiumShop;

	public static final double MENU_BAR_HEIGHT = 59;
	public static final double MENU_WIDTH = 200;
	public static final double MENU_ITEM_SPACING = 4;
	public static final double MENU_ITEM_ICON_SIZE = 20;
	private boolean gameRunning = false;
	private boolean blockMenuAnimation = false;
	private TextArea moveField;

	public ScrollPane getMenu() {
		return menu;
	}

	public UIOverlay(BattlefieldView battlefieldView) {
		this.battlefieldView = battlefieldView;

		battlefieldView.getStylesheets().add(this.getClass().getResource("UIOverlay.css").toExternalForm());
		battlefieldView.getStyleClass().add("root");
		initButtons();
		initPremiumShop();
		initMenu();
		initMenuBar();

		MpcApplication.closeRequestHandlers.add((WindowEvent) -> {
			setGameRunning(false);
		});
	}

	private void initButtons() {

		ImageView restartGameButtonIcon = new ImageView(
				new Image(Paths.get("rsc/icons/restart.png").toUri().toString(), 18, 18, true, true));

		restartButton = new Button("Neustarten", restartGameButtonIcon);
		restartButton.setContentDisplay(ContentDisplay.TOP);
		AnchorPane.setBottomAnchor(restartButton, 15.0);
		AnchorPane.setLeftAnchor(restartButton, 15.0);
		restartButton.getStyleClass().addAll("menu-button", "ui-button");
		restartButton.setOnMouseClicked((MouseEvent e) -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.getButtonTypes().clear();
				alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.CANCEL);
				alert.setTitle("Spiel neustarten");
				alert.setHeaderText(
						"Sind Sie sicher, dass Sie das Spiel neustarten wollen? Aller Fortschritt geht dabei verloren.");
				alert.initOwner(MpcApplication.mainStage);
				alert.initStyle(StageStyle.UTILITY);
				alert.setResizable(false);
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.YES) {
					try {
						while (battlefieldView.getMPCController().getGameLogicController().undoMove() > 0) {
						}
					} catch (InconsistentMoveException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		HBox rightButtonsWrapper = new HBox();
		AnchorPane.setBottomAnchor(rightButtonsWrapper, 15.0);
		AnchorPane.setRightAnchor(rightButtonsWrapper, 15.0);

		ImageView undoButtonIcon = new ImageView(
				new Image(Paths.get("rsc/icons/undo.png").toUri().toString(), 24, 24, true, true));

		undoButton = new Button("Rückgängig", undoButtonIcon);
		undoButton.setContentDisplay(ContentDisplay.TOP);
		undoButton.getStyleClass().addAll("menu-button", "ui-button");
		undoButton.setOnMouseClicked((MouseEvent event) -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				try {
					battlefieldView.getMPCController().getGameLogicController().undoMove();
				} catch (InconsistentMoveException e1) {
					e1.printStackTrace();
				}
			}
		});

		ImageView tipButtonIcon = new ImageView(
				new Image(Paths.get("rsc/icons/lightbulb.png").toUri().toString(), 20, 20, true, true));

		tipButton = new Button("Tipp", tipButtonIcon);
		tipButton.setContentDisplay(ContentDisplay.TOP);
		tipButton.getStyleClass().addAll("menu-button", "ui-button");
		tipButton.setOnMouseClicked((MouseEvent e) -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				battlefieldView.showTip();
			}
		});

		rightButtonsWrapper.getChildren().addAll(tipButton, undoButton);
		rightButtonsWrapper.setSpacing(4);

		if (battlefieldView.getMPCController().getServerController().isOnline()) {
			rightButtonsWrapper.setVisible(false);
			rightButtonsWrapper.setManaged(false);
			restartButton.setVisible(false);
			restartButton.setManaged(false);
		}
		moveField = new TextArea();
		moveField.setPromptText("Moves eingeben");
		moveField.setMaxSize(-1, -1);
		battlefieldView.getChildren().add(moveField);
		moveField.setVisible(false);
		moveField.setManaged(false);
		AnchorPane.setLeftAnchor(moveField, 25.0);
		AnchorPane.setRightAnchor(moveField, 25.0);
		AnchorPane.setBottomAnchor(moveField, 100.0);
		moveField.setOnKeyPressed((KeyEvent enter) -> {
			if (enter.getCode() == KeyCode.ENTER && moveField.isVisible()) {
				StringTokenizer tokenizer = new StringTokenizer(moveField.getText(), " ");
				while (tokenizer.hasMoreTokens()) {
					GameMove move = GameMove.fromProtocol(tokenizer.nextToken(),
							battlefieldView.getMPCController().getBattlefield());
					if (move != null) {
						try {
							GameLogicController glc = battlefieldView.getMPCController().getGameLogicController();
							Battlefield battlefield = battlefieldView.getBattlefield();
							glc.executeMove(move, true, false, false);
							int turnId = (battlefield.getMoveHistory().size() == 0) ? -1
									: battlefield.getMoveHistory().get(battlefield.getMoveHistory().size() - 1)
											.getTurn();
							glc.performZankLeftStackFlip(turnId);
							glc.performCardFlips(turnId);
						} catch (InconsistentMoveException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				moveField.clear();
			} else if (enter.getCode() == KeyCode.CIRCUMFLEX) {
				moveField.setVisible(!moveField.isVisible());
				moveField.setManaged(!moveField.isManaged());
				moveField.setMouseTransparent(!moveField.isMouseTransparent());
				moveField.clear();
			}
		});
		battlefieldView.setOnKeyPressed((KeyEvent keyPressed) -> {
			if (keyPressed.getCode() == KeyCode.CIRCUMFLEX) {
				moveField.setVisible(!moveField.isVisible());
				moveField.setManaged(!moveField.isManaged());
				moveField.setMouseTransparent(!moveField.isMouseTransparent());
			}

		});
		battlefieldView.getChildren().addAll(restartButton, rightButtonsWrapper);
	}

	private void initMenuBar() {
		menuBar = new BorderPane();
		menuBarLeft = new HBox();
		menuBarLeft.setAlignment(Pos.CENTER_LEFT);
		menuBarLeft.setSpacing(8);
		menuBarCenter = new HBox();
		menuBarCenter.setAlignment(Pos.CENTER_LEFT);
		menuBarCenter.setSpacing(8);
		menuBarRight = new HBox();
		menuBarRight.setAlignment(Pos.CENTER_RIGHT);
		menuBarRight.setSpacing(8);

		menuBar.setPadding(new Insets(2, 2, 2, 2));
		menuBar.getStyleClass().add("menu-bar");

		AnchorPane.setTopAnchor(menuBar, 0.0);
		AnchorPane.setLeftAnchor(menuBar, 0.0);
		AnchorPane.setRightAnchor(menuBar, 0.0);

		ImageView mainMenuIcon = new ImageView(
				new Image(Paths.get("rsc/icons/menu.png").toUri().toString(), 16, 16, true, true));

		mainMenuButton = new Button("Menü", mainMenuIcon);
		mainMenuButton.getStyleClass().add("menu-button");
		mainMenuButton.setContentDisplay(ContentDisplay.TOP);
		mainMenuButton.setOnMouseClicked((MouseEvent event) -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				if (menu.isVisible())
					hideMenu();
				else
					showMenu();
			}
		});

		ImageView backButtonIcon = new ImageView(
				new Image(Paths.get("rsc/icons/back.png").toUri().toString(), 26, 26, true, true));

		backButton = new Button("Zurück", backButtonIcon);
		backButton.setContentDisplay(ContentDisplay.TOP);
		backButton.getStyleClass().add("menu-button");
		backButton.setOnMouseClicked((MouseEvent event) -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				if (saveGame()) {
					MpcApplication.mainStage.setFullScreen(false);
					MainMenuViewController mvc = new MainMenuViewController(battlefieldView.getMPCController());
					TransitionFactory.animateLayoutSwitch(mvc, null, null, false);
				}
			}
		});

		ImageView fullScreenIcon = new ImageView(
				new Image(Paths.get("rsc/icons/fullscreen.png").toUri().toString(), 16, 16, true, true));
		ImageView fullScreenIconOff = new ImageView(
				new Image(Paths.get("rsc/icons/fullscreen_off.png").toUri().toString(), 16, 16, true, true));
		fullScreenButton = new Button("Vollbild", fullScreenIcon);
		fullScreenButton.getStyleClass().add("menu-button");
		fullScreenButton.setContentDisplay(ContentDisplay.TOP);
		fullScreenButton.setOnMouseClicked((MouseEvent event) -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				if (MpcApplication.mainStage.isFullScreen()) {
					MpcApplication.mainStage.setFullScreen(false);
					fullScreenButton.setGraphic(fullScreenIcon);
				} else {
					MpcApplication.mainStage.setFullScreen(true);
					fullScreenButton.setGraphic(fullScreenIconOff);
				}
			}
		});

		gameModeLabel = new Label(battlefieldView.getBattlefield().getGameType().toLocale());
		gameModeLabel.getStyleClass().add("menu-label");
		gameModeLabel.setPadding(new Insets(0, 0, 0, 50));
		gameModeLabel.setPrefWidth(400);

		timeLabel = new Label("Zeit ");
		timeLabel.getStyleClass().add("menu-label");
		timeLabel.setPadding(new Insets(0, 0, 0, 300));

		menuBarLeft.getChildren().addAll(mainMenuButton, backButton);
		menuBarCenter.getChildren().addAll(gameModeLabel, timeLabel);
		menuBarRight.getChildren().addAll(fullScreenButton);

		menuBar.setLeft(menuBarLeft);
		menuBar.setCenter(menuBarCenter);
		menuBar.setRight(menuBarRight);

		battlefieldView.getChildren().addAll(menuBar);
	}

	public void setGameRunning(boolean running) {
		if (running) {
			Thread t = new Thread(() -> updateTime());
			t.start();
		}
		gameRunning = running;
	}

	private void updateTime() {
		while (gameRunning) {
			long time = battlefieldView.getMPCController().getBattlefield().getGameTime();
			int durationAsInt = (int) time;
			Platform.runLater(() -> timeLabel
					.setText("Zeit: " + durationAsInt / 60 + "m " + (Integer.toString(durationAsInt % 60)) + "s"));
			;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			battlefieldView.getMPCController().getBattlefield().setGameTime(time + 1);

		}
	}

	private void initMenu() {
		menu = new ScrollPane();
		menu.setPrefSize(0, 0);
		menu.setMinSize(0, 0);
		menu.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		menu.setVisible(false);
		menu.setFitToHeight(true);
		menu.setFitToWidth(true);
		menu.setPadding(Insets.EMPTY);
		menu.getStyleClass().addAll("edge-to-edge", "menu");
		AnchorPane.setTopAnchor(menu, MENU_BAR_HEIGHT);

		menuItemList = new VBox(MENU_ITEM_SPACING);
		menuItemList.setMinSize(0, 0);
		menuItemList.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		menuItemList.getStyleClass().add("menu-content");
		menuItemList.setPadding(new Insets(4));
		menuItemList.setStyle("");
		menuItemList.setFillWidth(true);

		MenuItemWrapper tipItem = new MenuItemWrapper(new Image(Paths.get("rsc/icons/lightbulb.png").toUri().toString(),
				MENU_ITEM_ICON_SIZE, MENU_ITEM_ICON_SIZE, true, true), "Tipp");
		tipItem.setOnMouseClicked((MouseEvent e) -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				battlefieldView.showTip();
			}
		});

		MenuItemWrapper aiPlayItem = new MenuItemWrapper(new Image(Paths.get("rsc/icons/play.png").toUri().toString(),
				MENU_ITEM_ICON_SIZE, MENU_ITEM_ICON_SIZE, true, true), "Spiel vorspielen");
		aiPlayItem.setOnMouseClicked((MouseEvent e) -> {
			hideMenu();
			battlefieldView.getMPCController().getBattlefield().setGotHelp();
			battlefieldView.getMPCController().getAIController().startAI(false, true);

		});

		MenuItemWrapper optionItem = new MenuItemWrapper(
				new Image(Paths.get("rsc/icons/options.png").toUri().toString(), MENU_ITEM_ICON_SIZE,
						MENU_ITEM_ICON_SIZE, true, true),
				"Spieloptionen");
		optionItem.setOnMouseClicked((MouseEvent event) -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				// TODO battlefieldView.showOptions();
				hideMenu();
			}
		});

		MenuItemDivider divider1 = new MenuItemDivider();

		MenuItemTitle gamesTitle = new MenuItemTitle("Neues Spiel");

		MenuItemWrapper patienceItem = new MenuItemWrapper(null, "Patience");
		patienceItem.setOnMouseClicked((MouseEvent event) -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				hideMenu();

				if (saveGame()) {
					RegisterWindow mvc = new RegisterWindow(battlefieldView.getMPCController(),
							GameType.IDIOT_PATIENCE);
					TransitionFactory.animateLayoutSwitch(mvc, null, null, false);
				}
			}
		});
		patienceItem.setTextColor(Color.AQUAMARINE);

		MenuItemWrapper freecellItem = new MenuItemWrapper(null, "FreeCell");
		freecellItem.setOnMouseClicked((MouseEvent event) -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				hideMenu();

				if (saveGame()) {
					RegisterWindow mvc = new RegisterWindow(battlefieldView.getMPCController(), GameType.FREECELL);
					TransitionFactory.animateLayoutSwitch(mvc, null, null, false);
				}
			}
		});
		freecellItem.setTextColor(Color.MEDIUMPURPLE);

		MenuItemWrapper zankItem = new MenuItemWrapper(null, "Zank Patience");
		zankItem.setOnMouseClicked((MouseEvent event) -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				hideMenu();

				if (saveGame()) {
					ChosePlayModeView mvc = new ChosePlayModeView(battlefieldView.getMPCController(), false);
					TransitionFactory.animateLayoutSwitch(mvc, null, null, false);
				}
			}
		});
		zankItem.setTextColor(Color.INDIANRED);

		MenuItemDivider divider2 = new MenuItemDivider();

		MenuItemWrapper statsItem = new MenuItemWrapper(new Image(Paths.get("rsc/icons/trophy.png").toUri().toString(),
				MENU_ITEM_ICON_SIZE, MENU_ITEM_ICON_SIZE, true, true), "Statistiken");
		statsItem.setOnMouseClicked((MouseEvent event) -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				// TODO battlefieldView.showStats();
				hideMenu();
			}
		});

		// MenuItemDivider divider3 = new MenuItemDivider();

		MenuItemWrapper premiumItem = new MenuItemWrapper(
				new Image(Paths.get("rsc/icons/premium.png").toUri().toString(), MENU_ITEM_ICON_SIZE,
						MENU_ITEM_ICON_SIZE, true, true),
				"Premiumshop");
		premiumItem.setOnMouseClicked((MouseEvent event) -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				showPremiumShop();
				hideMenu();
			}
		});
		premiumItem.setTextColor(Color.GOLD);

		MenuItemWrapper infoItem = new MenuItemWrapper(new Image(Paths.get("rsc/icons/info.png").toUri().toString(),
				MENU_ITEM_ICON_SIZE, MENU_ITEM_ICON_SIZE, true, true), "Info");
		infoItem.setOnMouseClicked((MouseEvent event) -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				// TODO show information
				if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
					Thread descThread = new Thread(() -> {
						// Neuen Thread starten und Datei öffenen
						try {
							File myFile = new File("rsc/produktbeschreibung.pdf");
							Desktop.getDesktop().open(myFile);
						} catch (IOException ex) {
							// no application registered for PDFs
							ex.printStackTrace();
						}
					});
					descThread.setName("Produktbeschreibung");
					descThread.setDaemon(true);
					descThread.start();
				}
				hideMenu();
			}
		});
		// optionItem, statsItem, divider3,
		if (battlefieldView.getBattlefield().getGameType() == GameType.AGGRO_PATIENCE) {
			menuItemList.getChildren().addAll(tipItem, divider1, gamesTitle, patienceItem, freecellItem, zankItem,
					divider2, premiumItem, infoItem);
		} else {
			menuItemList.getChildren().addAll(tipItem, aiPlayItem, divider1, gamesTitle, patienceItem, freecellItem,
					zankItem, divider2, premiumItem, infoItem);
		}

		if (battlefieldView.getMPCController().getServerController().isOnline()) {
			tipItem.setVisible(false);
			tipItem.setManaged(false);
		}

		AnchorPane wrapper = new AnchorPane();
		wrapper.getChildren().add(menuItemList);
		AnchorPane.setLeftAnchor(menuItemList, 0.0);
		AnchorPane.setRightAnchor(menuItemList, 0.0);

		menu.setContent(wrapper);
		battlefieldView.getChildren().add(menu);
	}

	private boolean saveGame() {
		Battlefield battlefield = battlefieldView.getBattlefield();
		Player playerOne = battlefield.getPlayerOne();
		Player playerTwo = battlefield.getPlayerTwo();
		if (playerTwo != null && playerTwo.getType() == PlayerType.HUMAN_ONLINE)
			return true;

		Alert dialog = new Alert(AlertType.CONFIRMATION);
		ButtonType yes = new ButtonType("Ja");
		ButtonType no = new ButtonType("Nein");
		ButtonType cancel = new ButtonType("Abbrechen");
		dialog.getButtonTypes().setAll(yes, no, cancel);
		dialog.setTitle("Spiel verlassen");
		dialog.setHeaderText("Spiel speichern?");
		dialog.setContentText("Möchten sie das spiel speichern?");
		dialog.initOwner(MpcApplication.mainStage);
		dialog.initStyle(StageStyle.UTILITY);
		dialog.setResizable(false);
		Optional<ButtonType> result = dialog.showAndWait();
		if (result.get() == yes) {
			try {
				switch (battlefield.getGameType()) {
				case AGGRO_PATIENCE:
					if (playerOne.getType() == PlayerType.HUMAN) {
						if (playerTwo.getType() == PlayerType.AI1 || playerTwo.getType() == PlayerType.AI2
								|| playerTwo.getType() == PlayerType.AI3) {
							IOController.saveGame(battlefield, IOController.getProperty("ZANK_VS_AI_PATH"));
						} else if (playerTwo.getType() == PlayerType.HUMAN) {
							IOController.saveGame(battlefield, IOController.getProperty("ZANK_VS_HUMAN_PATH"));
						} else {
							IOController.saveGame(battlefield, IOController.getProperty("ZANK_FREE_PATH"));
						}
					} else {
						IOController.saveGame(battlefield, IOController.getProperty("ZANK_FREE_PATH"));
					}
					break;
				case FREECELL:
					IOController.saveGame(battlefield, IOController.getProperty("FREECELL_PATH"));
					break;
				case IDIOT_PATIENCE:
					IOController.saveGame(battlefield, IOController.getProperty("IDIOT_PATH"));
					break;
				default:
					break;
				}

			} catch (IOException e) {
				Alert saveFailedAlert = new Alert(AlertType.ERROR);
				saveFailedAlert.setTitle("Speichern fehlgeschlagen");
				saveFailedAlert.setHeaderText("Spiel konnte nicht gespeichert werden!");
				saveFailedAlert.initOwner(MpcApplication.mainStage);
				saveFailedAlert.initStyle(StageStyle.UTILITY);
				saveFailedAlert.setResizable(false);
				saveFailedAlert.show();
				e.printStackTrace();
			}
			if (battlefieldView.getMPCController().getServerController().isOnline())
				battlefieldView.getMPCController().getServerController().disconnect();
			battlefieldView.destroy();
			return true;
		} else if (result.get() == no) {
			if (battlefieldView.getMPCController().getServerController().isOnline())
				battlefieldView.getMPCController().getServerController().disconnect();
			battlefieldView.destroy();
			return true;
		} else if (result.get() == cancel) {
			return false;
		}
		return false;
	}

	public void showMenu() {
		if (blockMenuAnimation)
			return;

		blockMenuAnimation = true;

		menu.setVisible(true);
		restartButton.setVisible(false);

		Timeline timeline = new Timeline();

		timeline.setOnFinished((ActionEvent event) -> {
			AnchorPane.setBottomAnchor(menu, 0.0);
			blockMenuAnimation = false;
		});

		timeline.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(menu.prefWidthProperty(), menu.getPrefWidth()),
						new KeyValue(menu.prefHeightProperty(), menu.getPrefHeight())),
				new KeyFrame(new Duration(250), new KeyValue(menu.prefWidthProperty(), MENU_WIDTH), new KeyValue(
						menu.prefHeightProperty(), battlefieldView.getScene().getHeight() - MENU_BAR_HEIGHT)));

		timeline.play();

	}

	public void hideMenu() {
		if (blockMenuAnimation)
			return;

		blockMenuAnimation = true;

		AnchorPane.setBottomAnchor(menu, null);

		Timeline timeline = new Timeline();

		timeline.setOnFinished((ActionEvent event) -> {
			menu.setVisible(false);
			restartButton.setVisible(true);
			blockMenuAnimation = false;
		});
		timeline.getKeyFrames()
				.addAll(new KeyFrame(Duration.ZERO, new KeyValue(menu.prefWidthProperty(), menu.getPrefWidth()),
						new KeyValue(menu.prefHeightProperty(), menu.getPrefHeight())),
						new KeyFrame(new Duration(250), new KeyValue(menu.prefWidthProperty(), 0),
								new KeyValue(menu.prefHeightProperty(), 0)));
		timeline.play();

	}

	private void initPremiumShop() {
		premiumShop = new PremiumShop();
		battlefieldView.getChildren().add(premiumShop);
		AnchorPane.setTopAnchor(premiumShop, MENU_BAR_HEIGHT + PremiumShop.PREMIUMSHOP_INSET);
		AnchorPane.setBottomAnchor(premiumShop, PremiumShop.PREMIUMSHOP_INSET);
	}

	private void showPremiumShop() {
		AnchorPane.setLeftAnchor(premiumShop, null);
		AnchorPane.setRightAnchor(premiumShop, null);

		premiumShop.reset();

		premiumShop.setVisible(true);
		premiumShop.setManaged(true);

		double width = battlefieldView.getLayoutBounds().getWidth() - 2 * PremiumShop.PREMIUMSHOP_INSET;
		premiumShop.setPrefWidth(width);

		Timeline timeline = new Timeline();

		timeline.setOnFinished((ActionEvent event) -> {
			AnchorPane.setLeftAnchor(premiumShop, PremiumShop.PREMIUMSHOP_INSET);
			AnchorPane.setRightAnchor(premiumShop, PremiumShop.PREMIUMSHOP_INSET);
			AnchorPane.setTopAnchor(premiumShop, MENU_BAR_HEIGHT + PremiumShop.PREMIUMSHOP_INSET);
			AnchorPane.setBottomAnchor(premiumShop, PremiumShop.PREMIUMSHOP_INSET);
		});

		timeline.getKeyFrames().addAll(shakeKeyFrames(1000, premiumShop.layoutXProperty(), -width, 20.0, 40));
		timeline.play();
	}

	private void hidePremiumShop() {
		premiumShop.setVisible(false);
		premiumShop.setManaged(false);
	}

	private KeyFrame[] shakeKeyFrames(int duration, DoubleProperty target, Double initialValue, Double targetValue,
			int count) {
		KeyFrame[] frames = new KeyFrame[count];
		double resolution = 30 / (double) count;

		// 0 = initialValue
		// 1 = targetValue

		Function<Double, Double> f = (Double x) -> -1 * Math.sin(Math.sqrt(x) * 0.3 * x) / (x * x) + 1;

		if (initialValue < 0)
			targetValue += -initialValue;

		// f(0) = initialValue
		// f(1) = targetValue
		for (int i = 0; i < count; i++) {

			Double value = initialValue + f.apply(i * resolution) * targetValue;
			frames[i] = new KeyFrame(new Duration(duration / count * i),
					new KeyValue(target, value, Interpolator.EASE_BOTH));

		}
		return frames;
	}

	private class PremiumShop extends BorderPane {
		private Button buyButton;
		private ListView<CardSkin> cardBackListView;
		private static final double PREMIUMSHOP_INSET = 20.0;
		private static final double PREMIUMSHOP_TITLE_BAR_SPACING = 20;

		public PremiumShop() {
			this.getStyleClass().add("premium-shop");
			this.setPrefSize(500, 500);

			AnchorPane titleBar = new AnchorPane();
			VBox content = new VBox();
			HBox buttons = new HBox();
			buttons.setAlignment(Pos.CENTER);

			Label title = new Label("Shop");
			Button closeButton = new Button("");

			closeButton.getStyleClass().add("close-button");
			closeButton.setOnMouseClicked((MouseEvent event) -> {
				if (event.getButton() == MouseButton.PRIMARY) {
					hidePremiumShop();
				}
			});

			Node closeIcon = pathCloseIcon();
			closeIcon.setScaleX(0.5);
			closeIcon.setScaleY(0.5);
			StackPane closeWrapper = new StackPane(closeButton, closeIcon);

			/*
			 * new KeyFrame(Duration.ZERO, new
			 * KeyValue(buyButton.layoutYProperty(), buyButton.getLayoutY(),
			 * Interpolator.EASE_OUT)), new KeyFrame(new Duration(150), new
			 * KeyValue(buyButton.layoutYProperty(), randY,
			 * Interpolator.EASE_OUT))
			 */

			ImageView coinIcon = new ImageView(
					new Image(Paths.get("rsc/icons/money_coin.png").toUri().toString(), 14, 14, true, true));
			Label coinAmount = new Label(IOController.getProperty("coins"));
			closeWrapper.setPadding(new Insets(0, 0, 0, 4));
			buttons.getChildren().addAll(coinAmount, coinIcon, closeWrapper);
			buttons.setSpacing(4);

			titleBar.getChildren().addAll(title, buttons);
			AnchorPane.setLeftAnchor(title, PREMIUMSHOP_TITLE_BAR_SPACING);
			AnchorPane.setTopAnchor(title, PREMIUMSHOP_TITLE_BAR_SPACING / 2);
			AnchorPane.setRightAnchor(buttons, PREMIUMSHOP_TITLE_BAR_SPACING / 2);
			AnchorPane.setTopAnchor(buttons, PREMIUMSHOP_TITLE_BAR_SPACING / 2);

			cardBackListView = new ListView<>();
			cardBackListView.setCellFactory(new Callback<ListView<CardSkin>, ListCell<CardSkin>>() {
				@Override
				public ListCell<CardSkin> call(ListView<CardSkin> studentListView) {
					return new CardBackListCell();
				}
			});

			ObservableList<CardSkin> items = FXCollections.observableArrayList(CardSkin.values());
			cardBackListView.setItems(items);
			cardBackListView.setOrientation(Orientation.HORIZONTAL);
			cardBackListView.setPrefWidth(
					items.size() * CardBackListCell.ITEM_WIDTH + (items.size() * 2) * CardBackListCell.ITEM_PADDING_LR);
			// cardBackListView.setPrefHeight(CardBackListCell.ITEM_HEIGHT + 2 *
			// CardBackListCell.ITEM_PADDING_TB);
			cardBackListView.setPadding(new Insets(0));

			String currentSkin = IOController.getProperty("cardSkin");
			cardBackListView.getSelectionModel().select(CardSkin.valueOf(currentSkin));

			content.setPadding(new Insets(50));
			content.setAlignment(Pos.TOP_CENTER);
			content.getChildren().add(cardBackListView);
			content.setFillWidth(false);

			buyButton = new Button("Kaufen");
			buyButton.getStyleClass().add("buy-button");
			buyButton.setOnMouseEntered((MouseEvent event) -> {
				AnchorPane.setBottomAnchor(buyButton, null);
				AnchorPane.setRightAnchor(buyButton, null);
				Random r = new Random();
				Bounds bound = PremiumShop.this.getLayoutBounds();
				double randX = Math.max(buyButton.getLayoutBounds().getWidth(), Math.max(bound.getMinX(),
						r.nextDouble() * (bound.getWidth() - buyButton.getLayoutBounds().getWidth())));

				// double randY = Math.max(bound.getMinY(),
				// r.nextDouble() * (bound.getHeight() -
				// buyButton.getLayoutBounds().getHeight()));

				Timeline timeline = new Timeline();

				timeline.getKeyFrames().addAll(
						new KeyFrame(Duration.ZERO,
								new KeyValue(buyButton.translateXProperty(), buyButton.getTranslateX(),
										Interpolator.EASE_OUT)),
						new KeyFrame(new Duration(150),
								new KeyValue(buyButton.translateXProperty(), -randX, Interpolator.EASE_OUT)));

				timeline.play();
			});
			buyButton.setOnMouseClicked((MouseEvent event) -> {
				ScaleTransition scaleTransitionOne = TransitionFactory.createScaleTransition(buyButton,
						Duration.millis(50), new fromToParameter(1.0, 1.0, 1.15, 1.15), Interpolator.EASE_BOTH);
				ScaleTransition scaleTransitionTwo = TransitionFactory.createScaleTransition(buyButton,
						Duration.millis(50), new fromToParameter(1.15, 1.15, 1.0, 1.0), Interpolator.LINEAR);

				SequentialTransition clickedAnimation = new SequentialTransition();
				clickedAnimation.getChildren().addAll(scaleTransitionOne, scaleTransitionTwo);
				clickedAnimation.setCycleCount(1);

				clickedAnimation.play();

				CardSkin selectedSkin = cardBackListView.getSelectionModel().getSelectedItem();

				if (!selectedSkin.toString().equals(IOController.getProperty("cardSkin"))) {
					int coins = Integer.parseInt(IOController.getProperty("coins"));
					if (coins >= selectedSkin.getCost()) {
						String newCoins = "" + (coins - selectedSkin.getCost());
						IOController.setProperty("coins", newCoins);
						coinAmount.setText(newCoins);

						Textures.setSkin(selectedSkin);

						battlefieldView.getBattlefieldLayout().invalidateTextures();

						cardBackListView.refresh();
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Kartenskin kaufen");
						alert.setHeaderText("Sie haben nun den Kartenskin gekauft!");
						alert.initOwner(MpcApplication.mainStage);
						alert.initStyle(StageStyle.UTILITY);
						alert.setResizable(false);
						alert.showAndWait();
					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Kartenskin kaufen");
						alert.setHeaderText("Sie besitzen zu wenig Coins um diesen Skin zu kaufen!");
						alert.initOwner(MpcApplication.mainStage);
						alert.initStyle(StageStyle.UTILITY);
						alert.setResizable(false);
						alert.showAndWait();
					}

				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Kartenskin kaufen");
					alert.setHeaderText("Der ausgewählte Skin ist schon aktiv!");
					alert.initOwner(MpcApplication.mainStage);
					alert.initStyle(StageStyle.UTILITY);
					alert.setResizable(false);
					alert.showAndWait();
				}

			});

			this.setTop(titleBar);
			this.setCenter(content);

			HBox bottom = new HBox();
			bottom.setAlignment(Pos.CENTER_RIGHT);
			bottom.getChildren().add(buyButton);
			bottom.setPadding(new Insets(PREMIUMSHOP_TITLE_BAR_SPACING));

			this.setBottom(bottom);

			this.setVisible(false);
			this.setManaged(false);

		}

		public void reset() {
			buyButton.setTranslateX(0);
			CardSkin currentSkin = CardSkin.valueOf(IOController.getProperty("cardSkin"));
			cardBackListView.getSelectionModel().select(currentSkin);
		}

		private Node pathCloseIcon() {
			Path path = new Path();
			path.getElements().addAll(new MoveTo(9, 9), new LineTo(21, 21), new MoveTo(9, 21), new LineTo(21, 9));
			path.getStyleClass().add("close-icon");

			return path;
		}
	}

	private class CardBackListCell extends ListCell<Textures.CardSkin> {
		public static final double ITEM_WIDTH = 200.0;
		public static final double ITEM_HEIGHT = 340.0;
		public static final double ITEM_PADDING_LR = 20.0;
		public static final double ITEM_PADDING_TB = 5.0;

		@Override
		public void updateItem(CardSkin item, boolean empty) {
			super.updateItem(item, empty);

			this.setPadding(new Insets(ITEM_PADDING_TB, ITEM_PADDING_LR, ITEM_PADDING_TB, ITEM_PADDING_LR));

			if (empty)
				return;

			VBox content = new VBox();
			HBox bottom = new HBox();

			Rectangle rect = new Rectangle(ITEM_WIDTH, ITEM_HEIGHT);
			if (item == CardSkin.valueOf(IOController.getProperty("cardSkin"))) {
				this.getStyleClass().add("bought-skin-item");
			} else {
				this.getStyleClass().remove("bought-skin-item");
			}

			switch (item) {
			case STANDARD:
				rect.setFill(new ImagePattern(Textures.STANDARD_SKIN_PREV));
				this.getStyleClass().add("custom-list-cell");
				break;
			case PINKGREEN:
				rect.setFill(new ImagePattern(Textures.PINKGREEN_SKIN_PREV));
				this.getStyleClass().add("custom-list-cell");
				break;
			case HEARTH:
				rect.setFill(new ImagePattern(Textures.HEARTHSTONE_SKIN_PREV));
				this.getStyleClass().add("custom-list-cell");
			default:

				break;
			}

			ImageView coinIcon = new ImageView(
					new Image(Paths.get("rsc/icons/money_coin.png").toUri().toString(), 14, 14, true, true));
			Label coinAmount = new Label("" + item.getCost());

			bottom.getChildren().addAll(coinAmount, coinIcon);
			bottom.setAlignment(Pos.CENTER_RIGHT);
			bottom.setSpacing(4);
			content.getChildren().addAll(rect, bottom);

			setGraphic(content);
		}

	}

	private class MenuItemWrapper extends HBox {
		private ImageView icon;
		private Label labelName;

		public MenuItemWrapper(Image icon, String name) {
			getStyleClass().add("menu-item");
			setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			setAlignment(Pos.CENTER_LEFT);
			setSpacing(16);
			this.setPadding(new Insets(8, 0, 8, 8));

			if (icon != null)
				this.icon = new ImageView(icon);
			else {
				this.icon = new ImageView();
				this.icon.setFitHeight(16);
				this.icon.setFitWidth(16);
			}

			this.labelName = new Label(name);

			getChildren().addAll(this.icon, this.labelName);
		}

		public void setTextColor(Color color) {
			labelName.setStyle("-fx-text-fill: rgba(" + color.getRed() * 255 + ", " + color.getGreen() * 255 + ", "
					+ color.getBlue() * 255 + ", " + color.getOpacity() + ");");
		}
	}

	private class MenuItemDivider extends HBox {

		public MenuItemDivider() {
			getStyleClass().add("menu-item-divider");
			setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

			this.setPrefHeight(1.0);
		}
	}

	private class MenuItemTitle extends HBox {
		private Label labelTitle;

		public MenuItemTitle(String title) {
			getStyleClass().add("menu-item-title");
			labelTitle = new Label(title);
			getChildren().addAll(labelTitle);
			this.setPadding(new Insets(8, 0, 4, 4));
		}

	}

}
