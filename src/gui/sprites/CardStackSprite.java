package gui.sprites;

import java.util.ArrayList;

import gui.battlefield.BattlefieldView;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import model.GameCard;
import model.CardStack;
import model.CardStackType;

public class CardStackSprite extends Pane {
	public enum CardStackOrientation {
		LEFT, RIGHT, TOP, BOTTOM;
	}

	public enum CardStackStyle {
		STACKED, ROW;
	}

	public enum CardStackBackgroundStyle {
		SPADES, HEARTS, CLUBS, DIAMONDS, NONE, BORDER;
	}

	private CardStack stack;
	private ArrayList<CardSprite> cards;
	private CardStackOrientation orientation;
	private CardStackStyle style;
	private CardStackType role;
	private BattlefieldView parent;

	private final double CARD_OFFSET_X = 25;
	private final double CARD_OFFSET_Y = 25;

	public final double BACKGROUND_SCALE = 0.43;

	private CardStackSpriteBackground background;

	public CardStackSprite(CardStack stack, CardStackType role, CardStackOrientation orientation, CardStackStyle style,
			BattlefieldView parent) {
		this.setPickOnBounds(true);
		this.stack = stack;
		this.role = role;
		this.orientation = orientation;
		this.style = style;
		this.parent = parent;

		cards = new ArrayList<CardSprite>();

		CardStackBackgroundStyle backgroundStyle;

		if (stack.getSuit() != null) {
			switch (stack.getSuit()) {
			case CLUBS:
				backgroundStyle = CardStackBackgroundStyle.CLUBS;
				break;
			case DIAMONDS:
				backgroundStyle = CardStackBackgroundStyle.DIAMONDS;
				break;
			case HEARTS:
				backgroundStyle = CardStackBackgroundStyle.HEARTS;
				break;
			case SPADES:
				backgroundStyle = CardStackBackgroundStyle.SPADES;
				break;
			default:
				backgroundStyle = CardStackBackgroundStyle.BORDER;
			}
			background = new CardStackSpriteBackground(backgroundStyle, this);
		} else
			background = new CardStackSpriteBackground(CardStackBackgroundStyle.BORDER, this);

		fill();
	}

	public ArrayList<CardSprite> getCardSprites() {
		return cards;
	}

	public CardStack getCardStack() {
		return stack;
	}

	public CardStackStyle getCardStackStyle() {
		return style;
	}

	public CardStackOrientation getCardStackOrientation() {
		return orientation;
	}

	public CardStackSpriteBackground getCardStackSpriteBackground() {
		return background;
	}
	
	public CardStackType getRole() {
		return role;
	}

	public BattlefieldView getParentBattlefieldView() {
		return parent;
	}

	public void fill() {
		cards.clear();
		getChildren().clear();
		if (background != null)
			getChildren().add(background);

		switch (style) {
		case STACKED:
			double i = 0;
			for (GameCard c : stack.toList()) {
				CardSprite cardSprite = new CardSprite(c, this);
				cards.add(cardSprite);
				cardSprite.setTranslateX(-i);
				cardSprite.setTranslateY(-i / 4);

				i += 0.5;
			}
			break;
		case ROW:
			for (int j = 0; j < stack.toList().size(); j++) {

				GameCard c = stack.toList().get(j);
				CardSprite cardSprite = new CardSprite(c, this);
				cards.add(cardSprite);
				cardSprite.setTranslateX(getCardOffsetX() * j);
				cardSprite.setTranslateY(getCardOffsetY() * j);
			}
			break;
		}

		getChildren().addAll(cards);
	}

	public ArrayList<CardSprite> getFromIncluding(CardSprite sprite) {
		ArrayList<CardSprite> ret = new ArrayList<CardSprite>();
		for (int i = cards.indexOf(sprite); i < cards.size(); i++) {
			ret.add(cards.get(i));
		}
		return ret;
	}

	public CardSprite getTopCardSprite() {
		if (cards.size() > 0)
			return cards.get(cards.size() - 1);
		else
			return null;
	}

	public CardSprite getSpriteForCard(GameCard card) {
		for (CardSprite s : cards) {
			if (s.getCard().equals(card))
				return s;
		}
		return null;
	}

	public ArrayList<CardSprite> getSpritesForCards(ArrayList<GameCard> cards) {
		ArrayList<CardSprite> sprites = new ArrayList<CardSprite>();

		for (GameCard card : cards) {
			CardSprite sprite = getSpriteForCard(card);
			if (sprite != null)
				sprites.add(sprite);
		}
		return sprites;
	}

	public double getCardOffsetX() {
		switch (orientation) {
		case LEFT:
			return -CARD_OFFSET_X;
		case RIGHT:
			return CARD_OFFSET_X;
		default:
			return 0;
		}
	}

	public double getCardOffsetY() {
		switch (orientation) {
		case TOP:
			return -CARD_OFFSET_Y;
		case BOTTOM:
			return CARD_OFFSET_Y;
		default:
			return 0;
		}
	}

	public CardStackOrientation getOrientation() {
		return this.orientation;
	}

	public void updateStack(CardStack stack) {

	}

	public class CardStackSpriteBackground extends StackPane {

		private CardStackSprite parent;
		private Rectangle background;
		private ImageView image;
		private Label label;

		public CardStackSprite getParentCardStackSprite() {
			return parent;
		}

		public CardStackSpriteBackground(CardStackBackgroundStyle style, CardStackSprite parent) {
			this.parent = parent;

			background = new Rectangle(Textures.CARD_IMAGE_WIDTH * Textures.CARD_SCALE,
					Textures.CARD_IMAGE_HEIGHT * Textures.CARD_SCALE);
			background.setArcHeight(Textures.CARD_IMAGE_CORNER_RADIUS * Textures.CARD_SCALE);
			background.setArcWidth(Textures.CARD_IMAGE_CORNER_RADIUS * Textures.CARD_SCALE);
			background.setFill(Color.TRANSPARENT);

			background.setStroke(Color.rgb(255, 255, 255));
			background.setStrokeWidth(3);
			background.setMouseTransparent(true);

			image = new ImageView();
			image.setSmooth(true);
			image.setFitHeight(Textures.CARD_IMAGE_WIDTH * Textures.CARD_SCALE * BACKGROUND_SCALE);
			image.setFitWidth(Textures.CARD_IMAGE_WIDTH * Textures.CARD_SCALE * BACKGROUND_SCALE);
			image.setOpacity(0.7);
			image.setMouseTransparent(true);
			
			label = new Label("");
			label.setMouseTransparent(true);
			label.setTextAlignment(TextAlignment.CENTER);
			label.setAlignment(Pos.CENTER);
	
			
			switch (style) {
			case NONE:
				background.setStroke(Color.rgb(0, 0, 0, 0));
				break;
			case SPADES:
				image.setImage(Textures.SPADES_ICON_WHITE);
				break;
			case CLUBS:
				image.setImage(Textures.CLUBS_ICON_WHITE);
				break;
			case HEARTS:
				image.setImage(Textures.HEARTS_ICON_WHITE);
				break;
			case DIAMONDS:
				image.setImage(Textures.DIAMONDS_ICON_WHITE);
				break;
			case BORDER:
				break;
			}
			label.setPrefWidth(Textures.CARD_IMAGE_WIDTH * Textures.CARD_SCALE * 0.75);
			label.setPrefHeight(Textures.CARD_IMAGE_HEIGHT * Textures.CARD_SCALE);
			label.setWrapText(true);
			getChildren().addAll(background, image, label);
			setOpacity(0.4);
		}
		
		public void setText(String text) {
			label.setText(text);

		}
		public String getText() {
			return label.getText();
		}
	}
}
