package gui.sprites;

import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.HashMap;

import controller.IOController;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import model.GameCard;
import model.GameCard.Rank;
import model.GameCard.Suit;

public class Textures {
	public static final Image ICON = new Image(Paths.get("rsc/ICON.png").toUri().toString());

	public static final double CARD_SCALE = 0.4;
	public static final int CARD_IMAGE_OFFSET = (int) (30 * 0.8);
	public static final int CARD_IMAGE_HEIGHT = (int) (540 * 0.8);
	public static final int CARD_IMAGE_WIDTH = (int) (360 * 0.8);
	public static final int CARD_IMAGE_CORNER_RADIUS = 65;

	public static double ANIMATION_SPEED = 3000.0 / 2000.0;
	
	public static final Image HEARTS_ICON_WHITE = new Image(
			Paths.get("rsc/card_icons/HEARTS_WHITE.png").toUri().toString());
	public static final Image DIAMONDS_ICON_WHITE = new Image(
			Paths.get("rsc/card_icons/DIAMONDS_WHITE.png").toUri().toString());
	public static final Image CLUBS_ICON_WHITE = new Image(
			Paths.get("rsc/card_icons/CLUBS_WHITE.png").toUri().toString());
	public static final Image SPADES_ICON_WHITE = new Image(
			Paths.get("rsc/card_icons/SPADES_WHITE.png").toUri().toString());

	public static final Image TABLE_TEXTURE = new Image(Paths.get("rsc/TABLE_TEXTURE.png").toUri().toString());

	public static final Image WALDORF = new Image(Paths.get("rsc/TABLE_WALDORF.png").toUri().toString());
	public static final Image GHOST = new Image(Paths.get("rsc/TABLE_GHOST.png").toUri().toString());
	public static final Image MASTER = new Image(Paths.get("rsc/TABLE_MASTER.png").toUri().toString());
	public static final Image IDIOT = new Image(Paths.get("rsc/TABLE_IDIOT.png").toUri().toString());
	public static final Image FREE = new Image(Paths.get("rsc/TABLE_FREE.png").toUri().toString());
	public static final Image AGGRO = new Image(Paths.get("rsc/TABLE_AGGRO.png").toUri().toString());
	public static final Image HUMAN = new Image(Paths.get("rsc/LOCAL_PLAYER.png").toUri().toString());
	public static final Image HUMANONLINE = new Image(Paths.get("rsc/ONLINE_PLAYER.png").toUri().toString());
	public static final Image VERSUS = new Image(Paths.get("rsc/versus.png").toUri().toString());

	public static final Image STANDARD_SKIN_PREV = new Image(
			Paths.get("rsc/card_backs/STANDARD_PREVIEW_RES.png").toUri().toString());
	public static final Image PINKGREEN_SKIN_PREV = new Image(
			Paths.get("rsc/card_backs/PINKGREEN_PREVIEW_RES.png").toUri().toString());
	public static final Image HEARTHSTONE_SKIN_PREV = new Image(
			Paths.get("rsc/card_backs/PINKGREEN_PREVIEW_RES.png").toUri().toString());

	private static final CardTextureSet STANDARD_TEXTURES = new CardTextureSet(
			new Image(Paths.get("rsc/CARDS_TEXTURE.png").toUri().toString()),
			new Image(Paths.get("rsc/card_backs/CARD_BACK_RED.png").toUri().toString()),
			new Image(Paths.get("rsc/card_backs/CARD_BACK_BLUE.png").toUri().toString()));

	private static final CardTextureSet PINKGREEN_TEXTURES = new CardTextureSet(
			new Image(Paths.get("rsc/CARDS_TEXTURE.png").toUri().toString()),
			new Image(Paths.get("rsc/card_backs/CARD_BACK_PINK.png").toUri().toString()),
			new Image(Paths.get("rsc/card_backs/CARD_BACK_GREEN.png").toUri().toString()));
	
	private static final CardTextureSet HEARTHSTONE_TEXTURES = new CardTextureSet(
			new Image(Paths.get("rsc/CARDS_TEXTURE.png").toUri().toString()),
			new Image(Paths.get("rsc/card_backs/CARD_BACK_HEARTH_GOLD.gif").toUri().toString(), CARD_IMAGE_WIDTH, CARD_IMAGE_HEIGHT, false, true),
			new Image(Paths.get("rsc/card_backs/CARD_BACK_HEARTH_BLUE.gif").toUri().toString(), CARD_IMAGE_WIDTH, CARD_IMAGE_HEIGHT, false, true));

	public static final EnumMap<CardSkin, CardTextureSet> CARD_TEXTURE_SETS = new EnumMap<>(CardSkin.class);

	public static void initialize() {
		for (CardSkin skin : CardSkin.values()) {
			CARD_TEXTURE_SETS.put(skin, STANDARD_TEXTURES);
		}

		CARD_TEXTURE_SETS.put(CardSkin.STANDARD, STANDARD_TEXTURES);
		CARD_TEXTURE_SETS.put(CardSkin.PINKGREEN, PINKGREEN_TEXTURES);
		CARD_TEXTURE_SETS.put(CardSkin.HEARTH, HEARTHSTONE_TEXTURES);
	}

	public static void setSkin(CardSkin skin) {
		if (!skin.toString().equals(IOController.getProperty("cardSkin"))) {
			IOController.setProperty("cardSkin", skin.toString());
		}
	}

	public static Image getCardTexture(GameCard card) {
		CardSkin currentSkin = CardSkin.valueOf(IOController.getProperty("cardSkin"));
		if(currentSkin == CardSkin.HEARTH) {
			return CARD_TEXTURE_SETS.get(currentSkin).getCardTextureRaw(card);
		} else {
			return CARD_TEXTURE_SETS.get(currentSkin).getCardTexture(card);
		}
	}

	public enum CardSkin {
		STANDARD(0, "Standard"), PINKGREEN(1000, "Pink & Grün"), HEARTH(666, "Hearthstone");
		private int cost;
		private String name;

		private CardSkin(int cost, String name) {
			this.cost = cost;
			this.name = name;
		}

		public int getCost() {
			return cost;
		}

		public String getName() {
			return name;
		}
	}

	private static class CardTextureSet {
		private Image cardsTexture;
		private Image cardBackRed;
		private Image cardBackBlue;

		public CardTextureSet(Image cardsTexture, Image cardBackRed, Image cardBackBlue) {
			this.cardsTexture = cardsTexture;
			this.cardBackRed = cardBackRed;
			this.cardBackBlue = cardBackBlue;
		}

		private HashMap<CardTextureIdentifier, Image> cardTextureBuffer = new HashMap<CardTextureIdentifier, Image>();

		public Image getCardTextureRaw(GameCard card) {
			CardTextureIdentifier ident = new CardTextureIdentifier(card);
			if (!cardTextureBuffer.containsKey(ident)) {
				Image image;
				if (card.isFaceup()) {
					int x = Textures.CARD_IMAGE_OFFSET
							+ ident.rank.ordinal() * (Textures.CARD_IMAGE_WIDTH + Textures.CARD_IMAGE_OFFSET);
					int y = Textures.CARD_IMAGE_OFFSET
							+ ident.suit.ordinal() * (Textures.CARD_IMAGE_HEIGHT + Textures.CARD_IMAGE_OFFSET);

					image = cutoutTexture(x, y);
				} else {
					if (!card.getBlueBack()) {
						image = this.cardBackRed;
					} else {
						image = this.cardBackBlue;
					}
				}
				cardTextureBuffer.put(ident, image);
			}
			return cardTextureBuffer.get(ident);
		}
		
		public Image getCardTexture(GameCard card) {
			CardTextureIdentifier ident = new CardTextureIdentifier(card);
			if (!cardTextureBuffer.containsKey(ident)) {
				Image image;
				if (card.isFaceup()) {
					int x = Textures.CARD_IMAGE_OFFSET
							+ ident.rank.ordinal() * (Textures.CARD_IMAGE_WIDTH + Textures.CARD_IMAGE_OFFSET);
					int y = Textures.CARD_IMAGE_OFFSET
							+ ident.suit.ordinal() * (Textures.CARD_IMAGE_HEIGHT + Textures.CARD_IMAGE_OFFSET);

					image = cutoutTexture(x, y);
				} else {
					if (!card.getBlueBack()) {
						image = scaleTexture(cardBackRed, null, CARD_IMAGE_WIDTH * CARD_SCALE,
								CARD_IMAGE_HEIGHT * CARD_SCALE);
					} else {
						image = scaleTexture(cardBackBlue, null, CARD_IMAGE_WIDTH * CARD_SCALE,
								CARD_IMAGE_HEIGHT * CARD_SCALE);
					}
				}
				cardTextureBuffer.put(ident, image);
			}
			return cardTextureBuffer.get(ident);
		}

		private WritableImage cutoutTexture(double x, double y) {
			// define crop in image coordinates:
			Rectangle2D croppedPortion = new Rectangle2D(x, y, CARD_IMAGE_WIDTH, CARD_IMAGE_HEIGHT);
			// target width and height:
			return scaleTexture(cardsTexture, croppedPortion, CARD_IMAGE_WIDTH * CARD_SCALE,
					CARD_IMAGE_HEIGHT * CARD_SCALE);
		}
	}

	private static WritableImage scaleTexture(Image image, Rectangle2D croppedPortion, double scaledWidth,
			double scaledHeight) {
		ImageView imageView = new ImageView(image);
		if (croppedPortion != null) {
			imageView.setViewport(croppedPortion);
		}
		imageView.setFitWidth(scaledWidth);
		imageView.setFitHeight(scaledHeight);
		imageView.setSmooth(true);
		return imageView.snapshot(null, null);
	}

	private static class CardTextureIdentifier {
		private Suit suit;
		private Rank rank;
		private boolean faceup;
		private boolean blueBack;

		public CardTextureIdentifier(Suit suit, Rank rank, boolean faceup, boolean blueBack) {
			this.suit = suit;
			this.rank = rank;
			this.faceup = faceup;
			this.blueBack = blueBack;
		}

		public CardTextureIdentifier(GameCard card) {
			this(card.getSuit(), card.getRank(), card.isFaceup(), card.getBlueBack());
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (blueBack ? 1231 : 1237);
			result = prime * result + (faceup ? 1231 : 1237);
			result = prime * result + ((rank == null) ? 0 : rank.hashCode());
			result = prime * result + ((suit == null) ? 0 : suit.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CardTextureIdentifier other = (CardTextureIdentifier) obj;
			if (blueBack != other.blueBack)
				return false;
			if (faceup != other.faceup)
				return false;
			if (rank != other.rank)
				return false;
			if (suit != other.suit)
				return false;
			return true;
		}

	}
}
