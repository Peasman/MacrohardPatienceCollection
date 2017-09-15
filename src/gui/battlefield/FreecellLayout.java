package gui.battlefield;

import java.util.EnumMap;

import controller.MPCController;
import gui.sprites.CardStackSprite;
import gui.sprites.CardStackSprite.CardStackOrientation;
import gui.sprites.CardStackSprite.CardStackStyle;
import gui.sprites.Textures;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.CardStackType;
import model.GameType;

/**
 * Layout für ein FreeCell-Patience
 * 
 * @author Moritz Ludolph, Friedemann Runte
 */
public class FreecellLayout extends AnchorPane implements BattlefieldLayout {
	private EnumMap<CardStackType, CardStackSprite> cardStackSprites;

	private BattlefieldView parent;
	private MPCController mpcController;
	private HBox freecells;
	private HBox rows;
	private HBox stackers;
	private VBox mainContainer;
	private HBox topRow;

	public final double SPACING = 5.0;

	/**
	 * Erzeugt ein neues FreeCell-Patience Layout mit dem Battlefield des
	 * übergebenen BattlefieldViews
	 * 
	 * @param parent
	 *            Die Eltern-GUI
	 */
	public FreecellLayout(BattlefieldView parent) {
		this.parent = parent;
		this.mpcController = parent.getMPCController();

		cardStackSprites = new EnumMap<>(CardStackType.class);

		initLayout();
		initCardStackSprites();

		topRow.setAlignment(Pos.CENTER_RIGHT);
		AnchorPane.setRightAnchor(topRow, 0.0);
		AnchorPane.setLeftAnchor(topRow, 0.0);

		topRow.setSpacing(Textures.CARD_IMAGE_WIDTH * Textures.CARD_SCALE + SPACING * 2);
		topRow.getChildren().addAll(freecells, stackers);
		topRow.setAlignment(Pos.CENTER);
		mainContainer.getChildren().addAll(topRow, rows);
		mainContainer.setSpacing(50);
		rows.setSpacing(SPACING);
		rows.setAlignment(Pos.CENTER);
		freecells.setSpacing(SPACING);
		stackers.setSpacing(SPACING);
		mainContainer.setAlignment(Pos.CENTER);
		mainContainer.setFillWidth(true);
		AnchorPane.setTopAnchor(mainContainer, 0.0);
		AnchorPane.setBottomAnchor(mainContainer, 0.0);
		AnchorPane.setLeftAnchor(mainContainer, 0.0);
		AnchorPane.setRightAnchor(mainContainer, 0.0);
		getChildren().addAll(mainContainer);
	}

	private void initCardStackSprites() {
		
		for (CardStackType type : mpcController.getGameController().getCardStackTypes(GameType.FREECELL)) {
			assignTypeToSprite(type);
		}
	}

	private void assignTypeToSprite(CardStackType type) {
		CardStackSprite cardStackSprite = null;
		switch (type.getSuperType()) {
		case FREECELL:
			cardStackSprite = new CardStackSprite(parent.getBattlefield().getStack(type), type,
					CardStackOrientation.TOP, CardStackStyle.STACKED, parent);

			freecells.getChildren().add(cardStackSprite);
			break;
		case STACKER:
			cardStackSprite = new CardStackSprite(parent.getBattlefield().getStack(type), type,
					CardStackOrientation.TOP, CardStackStyle.STACKED, parent);

			stackers.getChildren().add(cardStackSprite);
			break;
		case ROW:
			cardStackSprite = new CardStackSprite(parent.getBattlefield().getStack(type), type,
					CardStackOrientation.BOTTOM, CardStackStyle.ROW, parent);

			rows.getChildren().add(cardStackSprite);
			break;
		default:
			break;
		}
		if (cardStackSprite != null) {
			cardStackSprites.put(type, cardStackSprite);
		}
	}

	private void initLayout() {
		freecells = new HBox();
		rows = new HBox();
		stackers = new HBox();
		topRow = new HBox();
		mainContainer = new VBox();
	}

	@Override
	public EnumMap<CardStackType, CardStackSprite> getCardStackSprites() {
		return cardStackSprites;
	}

	@Override
	public BattlefieldView getParentBattlefieldView() {
		return parent;
	}

}
