package gui.transitions;

import application.MpcApplication;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class TransitionFactory {

	public static class fromToParameter {
		public double fromX;
		public double fromY;
		public double toX;
		public double toY;

		public fromToParameter(double fromX, double fromY, double toX, double toY) {
			this.fromX = fromX;
			this.fromY = fromY;
			this.toX = toX;
			this.toY = toY;
		}
	}

	public static ScaleTransition createScaleTransition(Node node, Duration duration, fromToParameter parameterObject,
			Interpolator interpolator) {
		ScaleTransition scaleTransition = new ScaleTransition(duration, node);
		scaleTransition.setFromX(parameterObject.fromX);
		scaleTransition.setFromY(parameterObject.fromY);
		scaleTransition.setToX(parameterObject.toX);
		scaleTransition.setToY(parameterObject.toY);
		scaleTransition.setAutoReverse(true);

		return scaleTransition;
	}

	public static class CreateRotateByTransitionParameter {
		public double rotateBy;
		public int cycleCount;

		public CreateRotateByTransitionParameter(double rotateBy, int cycleCount) {
			this.rotateBy = rotateBy;
			this.cycleCount = cycleCount;
		}
	}

	public static RotateTransition createRotateByTransition(Node node, Duration duration,
			CreateRotateByTransitionParameter parameterObject, Interpolator interpolator) {
		RotateTransition rotateTransition = new RotateTransition(duration, node);
		rotateTransition.setByAngle(parameterObject.rotateBy);
		rotateTransition.setCycleCount(parameterObject.cycleCount);
		rotateTransition.setInterpolator(interpolator);

		return rotateTransition;
	}

	public static void animateLayoutSwitch(Pane layoutTo, EventHandler<ActionEvent> afterAddedHandler,
			EventHandler<ActionEvent> onFinishedHandler, boolean leftToRight) {
		final int ANIM_DURATION = 250;
		Timeline timeline = new Timeline();

		Pane layoutFrom = (Pane) MpcApplication.GAME_WRAPPER.getChildren().get(0);
		// layoutFrom.setTranslateX(500.0);
		MpcApplication.GAME_WRAPPER.getChildren().add(layoutTo);
		if (afterAddedHandler != null)
			afterAddedHandler.handle(null);

		double width = MpcApplication.GAME_WRAPPER.getWidth();

		if (leftToRight) {
			timeline.getKeyFrames().addAll(
					new KeyFrame(Duration.ZERO, new KeyValue(layoutFrom.translateXProperty(), 0.0)),
					new KeyFrame(new Duration(ANIM_DURATION), new KeyValue(layoutFrom.translateXProperty(), width)),
					new KeyFrame(Duration.ZERO, new KeyValue(layoutTo.translateXProperty(), -width)),
					new KeyFrame(new Duration(ANIM_DURATION), new KeyValue(layoutTo.translateXProperty(), 0)));
		} else {
			timeline.getKeyFrames().addAll(
					new KeyFrame(Duration.ZERO, new KeyValue(layoutFrom.translateXProperty(), 0.0)),
					new KeyFrame(new Duration(ANIM_DURATION), new KeyValue(layoutFrom.translateXProperty(), -width)),
					new KeyFrame(Duration.ZERO, new KeyValue(layoutTo.translateXProperty(), width)),
					new KeyFrame(new Duration(ANIM_DURATION), new KeyValue(layoutTo.translateXProperty(), 0)));

		}
		timeline.setOnFinished(e -> {
			MpcApplication.GAME_WRAPPER.getChildren().remove(layoutFrom);

			MpcApplication.GAME_WRAPPER.getScene().setCamera(new PerspectiveCamera());
			
			if (onFinishedHandler != null)
				onFinishedHandler.handle(e);
		});
		timeline.play();
	}
}
