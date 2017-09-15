package gui.sprites;

import javafx.animation.RotateTransition;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.GameCard;

public class CardSprite extends Rectangle {

	private GameCard card;
	@SuppressWarnings("unused")
	private RotateTransition cardRotator;
	private CardStackSprite parent;

	public CardSprite(GameCard card, CardStackSprite parent) {
		this.card = card;
		this.parent = parent;
		// this.setPickOnBounds(true);
		initEffects();
		fill();
	}

	public void fill() {
		this.setHeight(Textures.CARD_IMAGE_HEIGHT * Textures.CARD_SCALE);
		this.setWidth(Textures.CARD_IMAGE_WIDTH * Textures.CARD_SCALE);

		if (card.isFaceup()) {
			setTexture();
			this.setArcWidth(Textures.CARD_IMAGE_CORNER_RADIUS * Textures.CARD_SCALE);
			this.setArcHeight(Textures.CARD_IMAGE_CORNER_RADIUS * Textures.CARD_SCALE);
		} else {
			this.setArcWidth(Textures.CARD_IMAGE_CORNER_RADIUS * Textures.CARD_SCALE);
			this.setArcHeight(Textures.CARD_IMAGE_CORNER_RADIUS * Textures.CARD_SCALE);
			setTexture();
			//this.setFill(new ImagePattern(Textures.getCardTexture(card)));
		}
	}

		private void setTexture() {
			ImagePattern background = new ImagePattern(Textures.getCardTexture(card));
		this.setFill(background);
	}

	public void rotate(Duration duration) {
		// cardRotator.setDuration(duration);
		// cardRotator.play();
	}

	public GameCard getCard() {
		return card;
	}

	public CardStackSprite getParentSprite() {
		return parent;
	}

	private DropShadow[] effectChain = new DropShadow[3];

	private void applyEffectChain() {
		setEffect(null);
		DropShadow f = null;
		for (int i = 2; i >= 0; i--) {
			if (f == null && effectChain[i] != null) {
				f = effectChain[i];
			} else if (effectChain[i] != null) {
				f.setInput(effectChain[i]);
				f = effectChain[i];
			}
		}
		setEffect(f);
	}

	private DropShadow shadow;
	private DropShadow selectedEffect;
	private DropShadow highlightedEffect;

	private void initEffects() {
		shadow = new DropShadow();
		shadow.setOffsetX(5.0);
		shadow.setOffsetY(5.0);
		shadow.setColor(Color.BLACK);

		selectedEffect = new DropShadow();
		selectedEffect.setBlurType(BlurType.GAUSSIAN);
		selectedEffect.setColor(Color.web("#0093ff"));
		selectedEffect.setRadius(20);

		highlightedEffect = new DropShadow();
		highlightedEffect.setBlurType(BlurType.GAUSSIAN);
		highlightedEffect.setColor(Color.YELLOW);
		highlightedEffect.setRadius(20);

	}

	public void setShadow(boolean visible) {
		if (visible) {
			this.setTranslateZ(-25);
			effectChain[0] = shadow;
			this.setMouseTransparent(true);
			this.toFront();
		} else {
			this.setTranslateZ(0);
			effectChain[0] = null;
			this.setMouseTransparent(false);
		}
		applyEffectChain();
	}

	public void setSelected(boolean selected) {
		if (selected) {
			effectChain[1] = selectedEffect;
		} else {
			effectChain[1] = null;
		}
		applyEffectChain();
	}

	public void setHighlighted(boolean highlighted) {

		if (highlighted) {
			effectChain[2] = highlightedEffect;
		} else {
			effectChain[2] = null;
		}
		applyEffectChain();
	}
}
