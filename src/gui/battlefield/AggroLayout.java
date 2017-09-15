package gui.battlefield;

import java.util.EnumMap;

import controller.MPCController;
import exceptions.InconsistentMoveException;
import gui.sprites.CardStackSprite;
import gui.sprites.CardStackSprite.CardStackOrientation;
import gui.sprites.CardStackSprite.CardStackSpriteBackground;
import gui.sprites.CardStackSprite.CardStackStyle;
import gui.sprites.Textures;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import model.CardStackType;
import model.GameType;

/**
 * Die Klasse AggroLayout initialisiert das Spielfeld-Layout für die
 * Zank-Patience
 * 
 * @author Friedemann Runte
 * @see Battlefield, BorderPane
 */
public class AggroLayout extends BorderPane implements BattlefieldLayout {
	private EnumMap<CardStackType, CardStackSprite> cardStackSprites;

	private EnumMap<CardStackType, Label> cardsOnStackLabels;

	private final CardStackType[] LABELED_CARD_STACKS = new CardStackType[] { CardStackType.ZANK_LEFT_1,
			CardStackType.ZANK_LEFT_2, CardStackType.ZANK_MIDDLE_1, CardStackType.ZANK_MIDDLE_2,
			CardStackType.ZANK_RIGHT_1, CardStackType.ZANK_RIGHT_2 };

	private AnchorPane zankMiddleWrapper1, zankMiddleWrapper2;

	private final DropShadow player1Glow = new DropShadow();
	private final DropShadow player2Glow = new DropShadow();
	private final Rectangle player1Area = new Rectangle();
	private final Rectangle player2Area = new Rectangle();
	private  Label player1Name;
	private Label player2Name;
	private HBox mainRow, player1Row, player2Row;
	private VBox leftColumn, rightColumn, middleColumnLeft, middleColumnRight;
	private VBox player1Field, player2Field;
	private MPCController mpcController;
	private BattlefieldView parent;

	/**
	 * Der Konstruktor initialisiert alle KartenStapel, ihre Positionen und setzt
	 * das Layout in der BattlefieldView fest.
	 * 
	 * @param parent
	 *            Die BattlefieldView in die das Layout eingesetzt wird.
	 */
	public AggroLayout(BattlefieldView parent) {
		this.parent = parent;
		this.getStylesheets().add(getClass().getResource("battlefieldLayout.css").toExternalForm());
		mpcController = parent.getMPCController();

		cardStackSprites = new EnumMap<>(CardStackType.class);
		cardsOnStackLabels = new EnumMap<>(CardStackType.class);

		initCardStackLabels();
		initLayout();
		initCardStackSprites();

		mainRow.getChildren().addAll(leftColumn, middleColumnLeft, middleColumnRight, rightColumn);

		player1Row.setAlignment(Pos.BOTTOM_LEFT);
		player2Row.setAlignment(Pos.TOP_RIGHT);
		player1Name = new Label(parent.getBattlefield().getPlayerOne().getName());
		player1Name.setAlignment(Pos.CENTER);
		player1Name.setFont(new Font(30));
		player2Name = new Label(parent.getBattlefield().getPlayerTwo().getName());
		player2Name.setAlignment(Pos.CENTER);
		player2Name.setFont(new Font(30));

		AnchorPane player1Wrapper = new AnchorPane(player1Area, player1Row);
		player1Field = new VBox(player1Name, player1Wrapper);
		player1Field.setAlignment(Pos.BOTTOM_CENTER);
		player1Field.setSpacing(15);
//		player1Field
		AnchorPane player2Wrapper = new AnchorPane(player2Area, player2Row);
		player2Field = new VBox(player2Wrapper,player2Name);
		player2Field.setAlignment(Pos.TOP_CENTER);
		player2Field.setSpacing(15);
		
		this.setLeft(player1Field);
		this.setRight(player2Field);
		
		
		
		AnchorPane.setBottomAnchor(player1Row, 0.0);
		AnchorPane.setBottomAnchor(player1Area, 0.0);
		AnchorPane.setTopAnchor(player2Row, 0.0);
		AnchorPane.setTopAnchor(player2Area, 0.0);

		this.setCenter(mainRow);
		
		refreshCounters();
		initPlayerTurnHighlighter();

	}

	private void initPlayerTurnHighlighter() {
		player1Area.setWidth(3 * (Textures.CARD_IMAGE_WIDTH * Textures.CARD_SCALE + 5) + 2);
		player1Area.setHeight(Textures.CARD_IMAGE_HEIGHT * Textures.CARD_SCALE);
		player1Area.setTranslateY(-25);
		player1Area.setTranslateX(2);
		player1Area.setArcHeight(Textures.CARD_IMAGE_CORNER_RADIUS * Textures.CARD_SCALE);
		player1Area.setArcWidth(Textures.CARD_IMAGE_CORNER_RADIUS * Textures.CARD_SCALE);
		player1Area.setFill(Color.TRANSPARENT);

		player1Area.setStroke(Color.rgb(255, 0, 0, 0.3));
		player1Area.setStrokeWidth(3);
		player1Area.setMouseTransparent(true);

		player2Area.setWidth(3 * (Textures.CARD_IMAGE_WIDTH * Textures.CARD_SCALE + 5) + 2);
		player2Area.setHeight(Textures.CARD_IMAGE_HEIGHT * Textures.CARD_SCALE);
		player2Area.setTranslateY(30);
		player2Area.setTranslateX(2);
		player2Area.setArcHeight(Textures.CARD_IMAGE_CORNER_RADIUS * Textures.CARD_SCALE);
		player2Area.setArcWidth(Textures.CARD_IMAGE_CORNER_RADIUS * Textures.CARD_SCALE);
		player2Area.setFill(Color.TRANSPARENT);

		player2Area.setStroke(Color.rgb(0, 0, 255, 0.3));
		player2Area.setStrokeWidth(3);
		player2Area.setMouseTransparent(true);

		player1Glow.setBlurType(BlurType.GAUSSIAN);
		player1Glow.setRadius(30);
		player1Glow.setSpread(0.9);
		player1Glow.setColor(Color.RED);

		player2Glow.setBlurType(BlurType.GAUSSIAN);
		player2Glow.setRadius(30);
		player2Glow.setSpread(0.9);
		player2Glow.setColor(Color.BLUE);
	}

	private void initCardStackLabels() {
		for (CardStackType type : LABELED_CARD_STACKS) {
			Label label = new Label();
			label.setStyle("-fx-font-family: \"Segoe UI Semibold\";\n" + "	-fx-font-size: 20.0;\n"
					+ "	-fx-text-fill: rgba(255.0,255.0,255.0,0.8);");
			cardsOnStackLabels.put(type, label);
		}
	}

	/**
	 * Initialisiert die CardStacksSprites für das Layout, in dem es jedem Stack
	 * einen cardStackSprite gibt.
	 */
	private void initCardStackSprites() {
		for (CardStackType type : mpcController.getGameController().getCardStackTypes(GameType.AGGRO_PATIENCE)) {
			CardStackSprite cardStackSprite = initCardStackSprite(type);
			if (cardStackSprite != null) {
				cardStackSprites.put(type, cardStackSprite);
			}
		}
	}

	/**
	 * Ordnet einem CardStackType einen CardStackSprite hinzu.
	 * 
	 * @param type
	 *            Der Typ des CardStacks
	 * @return <i>cardStackSprite </i> Der CardStackSprite der dem Typen zugewiesen
	 *         wurde.
	 */
	private CardStackSprite initCardStackSprite(CardStackType type) {
		CardStackSprite cardStackSprite = null;
		switch (type.getSuperType()) {
		case ROW:
			cardStackSprite = initRow(type);
			break;
		case STACKER:
			cardStackSprite = initStacker(type);
			break;
		case ZANK_LEFT:
			cardStackSprite = initZankLeft(type);
			break;
		case ZANK_MIDDLE:
			cardStackSprite = initZankMiddle(type);
			break;
		case ZANK_RIGHT:
			cardStackSprite = initZankRight(type);
		default:
			break;
		}
		return cardStackSprite;
	}

	/**
	 * Erstellt einen CardStackSprite vom Typ Row aus dem Type, abhängig von der
	 * Nummer des Stapels.
	 * 
	 * @param type
	 *            Der Typ des Stapels
	 * @return <i> cardStackSprite </i> Der passende CardStackSprite
	 * @see CardStackSprite
	 */
	private CardStackSprite initRow(CardStackType type) {
		CardStackSprite cardStackSprite;
		if (type.getNum() <= CardStackType.ROW_4.getNum()) {
			cardStackSprite = new CardStackSprite(parent.getBattlefield().getStack(type), type,
					CardStackOrientation.LEFT, CardStackStyle.ROW, parent);
			leftColumn.getChildren().add(cardStackSprite);

		} else {
			cardStackSprite = new CardStackSprite(parent.getBattlefield().getStack(type), type,
					CardStackOrientation.RIGHT, CardStackStyle.ROW, parent);
			rightColumn.getChildren().add(cardStackSprite);

		}
		return cardStackSprite;
	}

	/**
	 * Erstellt einen CardStackSprite vom Typ Row aus dem Type, abhängig von der
	 * Nummer des Stapels.
	 * 
	 * @param type
	 *            Der Typ des Stapels
	 * @return <i> cardStackSprite </i> Der passende CardStackSprite
	 * @see CardStackSprite
	 */
	private CardStackSprite initStacker(CardStackType type) {
		CardStackSprite cardStackSprite;
		if (type.getNum() <= CardStackType.STACKER_4.getNum()) {
			cardStackSprite = new CardStackSprite(parent.getBattlefield().getStack(type), type,
					CardStackOrientation.TOP, CardStackStyle.STACKED, parent);
			middleColumnLeft.getChildren().add(cardStackSprite);
		} else {
			cardStackSprite = new CardStackSprite(parent.getBattlefield().getStack(type), type,
					CardStackOrientation.TOP, CardStackStyle.STACKED, parent);
			middleColumnRight.getChildren().add(cardStackSprite);
		}
		return cardStackSprite;

	}

	/**
	 * Erstellt einen CardStackSprite vom Typ Row aus dem Type, abhängig von der
	 * Nummer des Stapels.
	 * 
	 * @param type
	 *            Der Typ des Stapels
	 * @return <i> cardStackSprite </i> Der passende CardStackSprite
	 * @see CardStackSprite
	 */
	private CardStackSprite initZankLeft(CardStackType type) {
		CardStackSprite cardStackSprite;
		if (type == CardStackType.ZANK_LEFT_1) {
			cardStackSprite = new CardStackSprite(parent.getBattlefield().getStack(type), type,
					CardStackOrientation.TOP, CardStackStyle.STACKED, parent);
			VBox wrapper = new VBox(cardStackSprite, cardsOnStackLabels.get(type));
			wrapper.setAlignment(Pos.CENTER);
			player1Row.getChildren().add(wrapper);
		} else {
			cardStackSprite = new CardStackSprite(parent.getBattlefield().getStack(type), type,
					CardStackOrientation.TOP, CardStackStyle.STACKED, parent);
			VBox wrapper = new VBox(cardsOnStackLabels.get(type), cardStackSprite);
			wrapper.setAlignment(Pos.CENTER);
			player2Row.getChildren().add(wrapper);
		}
		return cardStackSprite;

	}

	/**
	 * Erstellt einen CardStackSprite vom Typ Row aus dem Type, abhängig von der
	 * Nummer des Stapels.
	 * 
	 * @param type
	 *            Der Typ des Stapels
	 * @return <i> cardStackSprite </i> Der passende CardStackSprite
	 * @see CardStackSprite
	 */
	private CardStackSprite initZankMiddle(CardStackType type) {
		CardStackSprite cardStackSprite;
		if (type == CardStackType.ZANK_MIDDLE_1) {
			cardStackSprite = new CardStackSprite(parent.getBattlefield().getStack(type), type,
					CardStackOrientation.TOP, CardStackStyle.STACKED, parent);

			zankMiddleWrapper1.getChildren().addAll(cardStackSprite);

			VBox wrapper = new VBox(zankMiddleWrapper1, cardsOnStackLabels.get(type));
			wrapper.setAlignment(Pos.CENTER);
			player1Row.getChildren().add(wrapper);

		} else {
			cardStackSprite = new CardStackSprite(parent.getBattlefield().getStack(type), type,
					CardStackOrientation.TOP, CardStackStyle.STACKED, parent);

			zankMiddleWrapper2.getChildren().addAll(cardStackSprite);

			VBox wrapper = new VBox(cardsOnStackLabels.get(type), zankMiddleWrapper2);
			wrapper.setAlignment(Pos.CENTER);
			player2Row.getChildren().add(wrapper);

		}
		return cardStackSprite;
	}

	/**
	 * Erstellt einen CardStackSprite vom Typ Row aus dem Type, abhängig von der
	 * Nummer des Stapels.
	 * 
	 * @param type
	 *            Der Typ des Stapels
	 * @return <i> cardStackSprite </i> Der passende CardStackSprite
	 * @see CardStackSprite
	 */
	private CardStackSprite initZankRight(CardStackType type) {
		CardStackSprite cardStackSprite;
		if (type == CardStackType.ZANK_RIGHT_1) {
			cardStackSprite = new CardStackSprite(parent.getBattlefield().getStack(type), type,
					CardStackOrientation.TOP, CardStackStyle.STACKED, parent);
			VBox wrapper = new VBox(cardStackSprite, cardsOnStackLabels.get(type));
			wrapper.setAlignment(Pos.CENTER);
			player1Row.getChildren().add(wrapper);
		} else {
			cardStackSprite = new CardStackSprite(parent.getBattlefield().getStack(type), type,
					CardStackOrientation.TOP, CardStackStyle.STACKED, parent);

			VBox wrapper = new VBox(cardsOnStackLabels.get(type), cardStackSprite);
			wrapper.setAlignment(Pos.CENTER);
			player2Row.getChildren().add(wrapper);
		}
		return cardStackSprite;
	}

	private void initLayout() {
		int spacing = 5;
		mainRow = new HBox();
		mainRow.setAlignment(Pos.CENTER);
		mainRow.setSpacing(spacing);
		leftColumn = new VBox();
		leftColumn.setSpacing(spacing);
		middleColumnLeft = new VBox();
		middleColumnLeft.setSpacing(spacing);
		middleColumnRight = new VBox();
		middleColumnRight.setSpacing(spacing);
		rightColumn = new VBox();
		rightColumn.setSpacing(spacing);

		
		zankMiddleWrapper1 = new AnchorPane();
		zankMiddleWrapper2 = new AnchorPane();

		player1Row = new HBox();
		player1Row.setSpacing(spacing);
		player2Row = new HBox();
		player2Row.setSpacing(spacing);
	}

	@Override
	public EnumMap<CardStackType, CardStackSprite> getCardStackSprites() {
		return cardStackSprites;
	}

	@Override
	public BattlefieldView getParentBattlefieldView() {
		return parent;
	}

	@Override
	public void afterMove() {
		showEndTurnMessageIfNecessary();
		refreshCounters();
	}

	private void showEndTurnMessageIfNecessary() {
		cardStackSprites.get(CardStackType.ZANK_MIDDLE_1).setOnMouseClicked(null);
		cardStackSprites.get(CardStackType.ZANK_MIDDLE_1).getCardStackSpriteBackground().setText("");
		cardStackSprites.get(CardStackType.ZANK_MIDDLE_2).setOnMouseClicked(null);
		cardStackSprites.get(CardStackType.ZANK_MIDDLE_2).getCardStackSpriteBackground().setText("");

		if (parent.getBattlefield().getCurrentPlayer().equals(parent.getBattlefield().getPlayerOne())) {
			if (parent.getBattlefield().getStack(CardStackType.ZANK_LEFT_1).size() == 0
					&& parent.getBattlefield().getStack(CardStackType.ZANK_MIDDLE_1).size() == 0) {

				cardStackSprites.get(CardStackType.ZANK_MIDDLE_1).getCardStackSpriteBackground()
						.setText("Rechtsklick um Zug zu beenden");
				cardStackSprites.get(CardStackType.ZANK_MIDDLE_1).setOnMouseClicked((MouseEvent e) -> {
					System.out.println(e.getTarget());
					if (e.getButton() != MouseButton.SECONDARY || !(e.getTarget() instanceof CardStackSpriteBackground))
						return;
					System.out.println("here");
					try {
						mpcController.getGameLogicController().endCurrentTurn();
						System.out.println("ended");
					} catch (InconsistentMoveException e1) {
						e1.printStackTrace();
					}
				});
			}
		} else {
			if (parent.getBattlefield().getStack(CardStackType.ZANK_LEFT_2).size() == 0
					&& parent.getBattlefield().getStack(CardStackType.ZANK_MIDDLE_2).size() == 0) {

				cardStackSprites.get(CardStackType.ZANK_MIDDLE_2).getCardStackSpriteBackground()
						.setText("Rechtsklick um Zug zu beenden");
				cardStackSprites.get(CardStackType.ZANK_MIDDLE_2).setOnMouseClicked((MouseEvent e) -> {
					System.out.println(e.getTarget());
					if (e.getButton() != MouseButton.SECONDARY || !(e.getTarget() instanceof CardStackSpriteBackground))
						return;
					System.out.println("here");
					try {
						mpcController.getGameLogicController().endCurrentTurn();
						System.out.println("e");
					} catch (InconsistentMoveException e1) {
						e1.printStackTrace();
					}
				});
			}
		}
	}

	private void refreshCounters() {
		for (CardStackType type : LABELED_CARD_STACKS) {
			cardsOnStackLabels.get(type).setText("" + mpcController.getBattlefield().getStack(type).size());
		}
	}

	public void onTurnBegin() {
		if (parent.getBattlefield().getCurrentPlayer().equals(parent.getBattlefield().getPlayerOne())) {
			player1Area.setVisible(true);
			player1Area.setEffect(player1Glow);
			player2Area.setVisible(false);
			player2Area.setEffect(null);
		} else {
			player1Area.setVisible(false);
			player1Area.setEffect(null);
			player2Area.setVisible(true);
			player2Area.setEffect(player2Glow);
		}
		showEndTurnMessageIfNecessary();
	}
}
