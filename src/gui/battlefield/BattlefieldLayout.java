package gui.battlefield;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Random;

import application.MpcApplication;
import controller.IOController;
import controller.MPCController;
import exceptions.InconsistentMoveException;
import gui.sprites.CardSprite;
import gui.sprites.CardStackSprite;
import gui.sprites.Textures;
import gui.stats.StatsViewController;
import gui.transitions.TransitionFactory;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import model.GameCard;
import model.CardStackType;
import model.GameMove;

/**
 * Interface für Layouts eines Spielfeld
 * 
 * @author moritz
 */
public interface BattlefieldLayout {
	
	
	/**
	 * Gibt eine Map aller Kartenstapeltypen auf die zugehörigen
	 * CardStackSprites zurück
	 * 
	 * @return Die Map der CardStackTypes auf CardStackSprites
	 * @see CardStackType
	 * @see CardStackSprite
	 */
	public abstract EnumMap<CardStackType, CardStackSprite> getCardStackSprites();

	public default void invalidateTextures() {
		for (CardStackSprite stackSprite : getCardStackSprites().values()) {
			stackSprite.fill();
		}
	}

	/**
	 * Wird vom BattlefieldView aufgerufen falls ein Move animiert werden soll,
	 * z.B. falls der Move nicht vom PlayerType.HUMAN ausgeführt wurde. Das
	 * Battlefield ruft dann nicht selber CardStackSprite.fill() auf
	 * 
	 * @param move
	 *            Der zu animierende Move
	 */
	public default void animateMove(GameMove move) {
		BattlefieldView parent = getParentBattlefieldView();
		EnumMap<CardStackType, CardStackSprite> cardStackSprites = getCardStackSprites();

		CardStackSprite to = cardStackSprites.get(move.getTo());
		CardStackSprite from = cardStackSprites.get(move.getFrom());

		if (move.getFrom() == move.getTo()) {
			// TODO flip animation? auto-play von reserve nachdem von reserve
			// gespielt wurde
			to.fill();
			return;
		}

		double toX, toY;
		toX = to.localToScene(to.getBoundsInLocal()).getMinX();
		toY = to.localToScene(to.getBoundsInLocal()).getMinY();
		if (to.getTopCardSprite() != null) {
			toX += to.getTopCardSprite().getTranslateX() + to.getCardOffsetX();
			toY += to.getTopCardSprite().getTranslateY() + to.getCardOffsetY();
		}

		HashMap<CardSprite, Point2D> fromCoordinates = new HashMap<CardSprite, Point2D>();
		ArrayList<CardSprite> cardSpritesToAnimate = new ArrayList<CardSprite>();
		
		for (GameCard card : move.getCards()) {
			if (card == null){
				System.out.println("null card");
				continue;
			}
			CardSprite newSprite = new CardSprite(card, null);
			cardSpritesToAnimate.add(newSprite);

			CardSprite origSprite = from.getSpriteForCard(card);
			double fromX, fromY;
			if (origSprite == null) {
				fromX = from.localToScene(from.getBoundsInLocal()).getMinX();
				fromY = from.localToScene(from.getBoundsInLocal()).getMinY();

			} else {
				fromX = origSprite.localToScene(origSprite.getBoundsInLocal()).getMinX();
				fromY = origSprite.localToScene(origSprite.getBoundsInLocal()).getMinY();
			}
			fromCoordinates.put(newSprite, new Point2D(fromX, fromY));

			parent.getChildren().add(newSprite);
		}

		if (cardSpritesToAnimate.size() == 0) {
			return;
		}

		Timeline timeline = new Timeline();
		timeline.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				to.fill();

				for (CardSprite sprite : cardSpritesToAnimate) {
					sprite.setVisible(false);
					sprite.setManaged(false);
					getChildren().remove(sprite);
				}
				parent.playMoveSound(move.getTurn());
			}
		});

		int i = 0;
		for (CardSprite sprite : cardSpritesToAnimate) {
			double fromX = fromCoordinates.get(sprite).getX();
			double fromY = fromCoordinates.get(sprite).getY();
			sprite.setShadow(true);

			double distance = Math.sqrt(Math.pow(toX + to.getCardOffsetX() * i - (fromX + sprite.getTranslateX()), 2)
					+ Math.pow(toY + to.getCardOffsetY() * i - (fromY + sprite.getTranslateY()), 2));

			timeline.getKeyFrames().addAll(
					new KeyFrame(Duration.ZERO,
							new KeyValue(sprite.translateXProperty(), fromX + sprite.getTranslateX(),
									Interpolator.EASE_BOTH)),
					new KeyFrame(new Duration(Textures.ANIMATION_SPEED * distance), new KeyValue(sprite.translateXProperty(),
							toX + to.getCardOffsetX() * i, Interpolator.EASE_BOTH)),

					new KeyFrame(Duration.ZERO,
							new KeyValue(sprite.translateYProperty(), fromY + sprite.getTranslateY(),
									Interpolator.EASE_BOTH)),
					new KeyFrame(new Duration(Textures.ANIMATION_SPEED * distance), new KeyValue(sprite.translateYProperty(),
							toY + to.getCardOffsetY() * i, Interpolator.EASE_BOTH)));
			i += 1;
		}

		timeline.play();
	}

	BattlefieldView getParentBattlefieldView();

	ObservableList<Node> getChildren();

	public default StackPane performWinAnimation(MPCController mpcController) {
		StackPane pane = new StackPane();
		VBox labelButtonDivider = new VBox();
		Label won = new Label("GEWONNEN");
		Button cont = new Button("Weiter");

		won.setFont(javafx.scene.text.Font.font("sans-serif", FontWeight.BOLD, 80));
		won.setTextFill(Color.GOLD);
		won.setUnderline(true);
		won.setPadding(new Insets(15, 15, 15, 15));
		won.setEffect(new Bloom(0.1));

		labelButtonDivider.getChildren().addAll(won, cont);
		labelButtonDivider.setAlignment(Pos.CENTER);
		labelButtonDivider.setSpacing(10);

		pane.getChildren().add(labelButtonDivider);
		pane.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, .3), null, null)));
		pane.setBorder(new Border(new BorderStroke(Color.rgb(0, 0, 0, 0.4), BorderStrokeStyle.SOLID, null, null)));

		cont.setFont(Font.font("sans-serif", FontWeight.BOLD, 25));
		cont.getStylesheets().add(this.getClass().getResource("battlefieldLayout.css").toExternalForm());
		cont.getStyleClass().add("menu-button");
		cont.setId("win-button");
		cont.setOnAction((ActionEvent e) -> {
			TransitionFactory.animateLayoutSwitch(new StatsViewController(mpcController), null, (ActionEvent event) -> {
				MpcApplication.GAME_WRAPPER.getChildren().remove(pane);
			}, false);
			getParentBattlefieldView().destroy();
		});
		StackPane.setAlignment(labelButtonDivider, Pos.CENTER);

		Random rng = new Random();
		IOController.playSound("victory" + (1 + rng.nextInt(2)) + ".wav", -10f);

		return pane;
	}

	public default StackPane performLoseAnimation(MPCController mpcController) {
		StackPane pane = new StackPane();
		StackPane labelStack = new StackPane();
		VBox labelButtonDivider = new VBox();
		HBox buttonContainer = new HBox();
		Rectangle bgRect;
		Label lost = new Label("VERLOREN");
		Button undo = new Button("Rückgängig");
		Button cont = new Button("Weiter");

		lost.setFont(javafx.scene.text.Font.font("sans-serif", FontWeight.BOLD, 80));
		lost.setUnderline(true);
		lost.setTextFill(Color.BROWN);
		lost.setEffect(new DropShadow());

		undo.setFont(Font.font("sans-serif", FontWeight.BOLD, 25));
		undo.getStylesheets().add(this.getClass().getResource("battlefieldLayout.css").toExternalForm());
		undo.getStyleClass().add("menu-button");
		undo.setId("lost-button");
		undo.setEffect(new DropShadow());
		undo.setOnAction((e) -> {
			pane.setVisible(false);
			MpcApplication.GAME_WRAPPER.getChildren().remove(pane);
			try {
				mpcController.getGameLogicController().undoMove();
			} catch (InconsistentMoveException e1) {
				e1.printStackTrace();
			}
		}); // TODO

		cont.setFont(Font.font("sans-serif", FontWeight.BOLD, 25));
		cont.getStylesheets().add(this.getClass().getResource("battlefieldLayout.css").toExternalForm());
		cont.getStyleClass().add("menu-button");
		cont.setId("lost-button");
		cont.setEffect(new DropShadow());
		cont.setOnAction((ActionEvent e) -> {
			TransitionFactory.animateLayoutSwitch(new StatsViewController(mpcController), null, (ActionEvent event) -> {
				MpcApplication.GAME_WRAPPER.getChildren().remove(pane);
			}, false);
			getParentBattlefieldView().destroy();
		});

		bgRect = new Rectangle(9999, 170);
		bgRect.setTranslateY(10);
		bgRect.setFill(Color.BLACK);
		bgRect.setEffect(new GaussianBlur(25));
		bgRect.setOpacity(0.9);

		labelStack.getChildren().addAll(bgRect, lost);
		buttonContainer.getChildren().addAll(undo, cont);
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setSpacing(10);
		labelButtonDivider.getChildren().addAll(labelStack, buttonContainer);
		labelButtonDivider.setAlignment(Pos.CENTER);
		labelButtonDivider.setSpacing(20);

		pane.getChildren().add(labelButtonDivider);
		pane.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, .3), null, null)));
		pane.setBorder(new Border(new BorderStroke(Color.rgb(0, 0, 0, 0.4), BorderStrokeStyle.SOLID, null, null)));
		StackPane.setAlignment(labelButtonDivider, Pos.CENTER);

		GaussianBlur blur = new GaussianBlur();
		DoubleProperty scaleValue = new SimpleDoubleProperty(1);
		DoubleProperty blurValue = new SimpleDoubleProperty(30);
		DoubleProperty opacityValue = new SimpleDoubleProperty(0);
		lost.setEffect(blur);
		labelStack.setOpacity(opacityValue.doubleValue());

		Timeline timeline = new Timeline();
		blurValue.addListener((observable, oldV, newV) -> {
			blur.setRadius(newV.doubleValue());
		});
		opacityValue.addListener((observable, oldV, newV) -> {
			labelStack.setOpacity(newV.doubleValue());
		});
		scaleValue.addListener((observable, oldV, newV) -> {
			lost.setScaleX(newV.doubleValue());
			lost.setScaleY(newV.doubleValue());
		});
		final KeyValue blurKeyValue = new KeyValue(blurValue, 1);
		final KeyValue scaleKeyValue = new KeyValue(scaleValue, 1.2);
		final KeyValue opacityKeyValue = new KeyValue(opacityValue, 1);
		final KeyFrame blurKeyFrame = new KeyFrame(Duration.millis(1500), blurKeyValue);
		final KeyFrame scaleKeyFrame = new KeyFrame(Duration.millis(2000), scaleKeyValue);
		final KeyFrame opacityKeyFrame = new KeyFrame(Duration.millis(1000), opacityKeyValue);
		timeline.getKeyFrames().add(blurKeyFrame);
		timeline.getKeyFrames().add(scaleKeyFrame);
		timeline.getKeyFrames().add(opacityKeyFrame);
		timeline.play();

		IOController.playSound("lose_sound.wav", -10f);

		return pane;
	}

	public default void onTurnBegin() {

	}

	public default void afterMove() {

	}
}
