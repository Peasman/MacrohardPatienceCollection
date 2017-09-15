package gui.battlefield;

import java.util.EnumMap;

import controller.MPCController;
import gui.sprites.CardStackSprite;
import gui.sprites.CardStackSprite.CardStackOrientation;
import gui.sprites.CardStackSprite.CardStackStyle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.CardStackType;
import model.GameType;

/**
 * Layout für ein Idioten-Patience Spiel
 * 
 * @author Friedemann Runte, Moritz Ludolph
 */
public class IdiotLayout extends AnchorPane implements BattlefieldLayout {
	private EnumMap<CardStackType, CardStackSprite> cardStackSprites;

	private Label talonCountLabel;
	private Label stackerCountLabel;
	
	private BattlefieldView parent;
	private MPCController mpcController;

	private HBox rows;
	private CardStackSprite stacker;
	private CardStackSprite talon;
	private BorderPane topRow;

	public final double SPACING = 20.0;

	/**
	 * Erzeugt ein Layout für ein Idioten-Patience mit dem Battlefield der
	 * übergebenen BattlefieldView
	 * 
	 * @param parent
	 *            Die Eltern-GUI
	 */
	public IdiotLayout(BattlefieldView parent) {
		this.parent = parent;
		this.mpcController = parent.getMPCController();

		this.cardStackSprites = new EnumMap<>(CardStackType.class);

		initCardStackLabels();
		initLayout();
		initCardStackSprites();

		VBox wrapper = new VBox(talon, talonCountLabel);
		wrapper.setAlignment(Pos.BOTTOM_CENTER);
		topRow.setLeft(wrapper);
		
		topRow.setCenter(rows);
		
		wrapper = new VBox(stacker, stackerCountLabel);
		wrapper.setAlignment(Pos.BOTTOM_CENTER);
		topRow.setRight(wrapper);
		rows.setAlignment(Pos.CENTER);
		rows.setPadding(new Insets(50));

		rows.setSpacing(SPACING);
		getChildren().addAll(topRow);
		AnchorPane.setRightAnchor(topRow, 40.0);
		AnchorPane.setLeftAnchor(topRow, 40.0);
		
		refreshCounters();
	}
	
	private void initCardStackLabels() {
		talonCountLabel = new Label();
		talonCountLabel.setStyle("-fx-font-family: \"Segoe UI Semibold\";\n" + "	-fx-font-size: 20.0;\n"
				+ "	-fx-text-fill: rgba(255.0,255.0,255.0,0.8);");
		
		stackerCountLabel = new Label();
		stackerCountLabel.setStyle("-fx-font-family: \"Segoe UI Semibold\";\n" + "	-fx-font-size: 20.0;\n"
				+ "	-fx-text-fill: rgba(255.0,255.0,255.0,0.8);");
	}


	private void initCardStackSprites() {
		for (CardStackType type : mpcController.getGameController().getCardStackTypes(GameType.IDIOT_PATIENCE)) {
			CardStackSprite cardStackSprite = null;
			switch (type) {
			case TALON:
				cardStackSprite = new CardStackSprite(parent.getBattlefield().getStack(type), type,
						CardStackOrientation.TOP, CardStackStyle.STACKED, parent);
				talon = cardStackSprite;
				break;
			case STACKER_1:
				cardStackSprite = new CardStackSprite(parent.getBattlefield().getStack(type), type,
						CardStackOrientation.TOP, CardStackStyle.STACKED, parent);
				stacker = cardStackSprite;
				break;
			case ROW_1:
			case ROW_2:
			case ROW_3:
			case ROW_4:
				cardStackSprite = new CardStackSprite(parent.getBattlefield().getStack(type), type,
						CardStackOrientation.BOTTOM, CardStackStyle.ROW,  parent);
				rows.getChildren().add(cardStackSprite);
				break;
			default:
			}
			if (cardStackSprite != null)
				cardStackSprites.put(type, cardStackSprite);

		}
	}

	private void initLayout() {
		rows = new HBox();
		topRow = new BorderPane();
	}

	@Override
	public EnumMap<CardStackType, CardStackSprite> getCardStackSprites() {
		return cardStackSprites;
	}

	@Override
	public BattlefieldView getParentBattlefieldView() {
		return parent;
	}

	public void afterMove() {
		refreshCounters();
	}
	
	
	private void refreshCounters() {
		talonCountLabel.setText("" + mpcController.getBattlefield().getStack(CardStackType.TALON).size());
		stackerCountLabel.setText("" + mpcController.getBattlefield().getStack(CardStackType.STACKER_1).size());
	}

}
