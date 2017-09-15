package gui.settings;

import java.util.Optional;

import application.MpcApplication;
import controller.IOController;
import controller.MPCController;
import gui.mainview.MainMenuViewController;
import gui.sprites.Textures;
import gui.transitions.TransitionFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SettingsView extends BorderPane {
	private VBox settingsContainer;
	private Button backButton;
	@SuppressWarnings("unused")
	private MPCController mpcController;
	
	public SettingsView(MPCController mpcController) {
		super();
		this.setPadding(new Insets(30.0));
		this.mpcController = mpcController;
		this.setBackground(new Background(new BackgroundImage(Textures.TABLE_TEXTURE, BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

		this.getStylesheets().add(getClass().getResource("SettingsView.css").toExternalForm());
		this.getStyleClass().add("settings-view");

		settingsContainer = new VBox();
		settingsContainer.setAlignment(Pos.CENTER);
		settingsContainer.setPrefWidth(300);

		double volumeSliderWidth = 200;

		Slider volumeSlider = new Slider();
		volumeSlider.setMin(0);
		volumeSlider.setMax(100);
		volumeSlider.setMinWidth(volumeSliderWidth);
		volumeSlider.setMaxWidth(volumeSliderWidth);

		ProgressBar pb = new ProgressBar(0);
		pb.setMinWidth(volumeSliderWidth);
		pb.setMaxWidth(volumeSliderWidth);
		Label pi = new Label();
		pi.setPrefWidth(40);

		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
				pb.setProgress(new_val.doubleValue() / 100);
				pi.setText(new_val.intValue() + "");
			}
		});

		String volume = IOController.getProperty("masterVolume");
		pi.setText(volume);
		volumeSlider.setValue(Double.parseDouble(volume));

		StackPane pane = new StackPane();

		pane.getChildren().addAll(pb, volumeSlider);

		HBox sliderWrapper = new HBox();
		sliderWrapper.getChildren().addAll(pane, pi);
		sliderWrapper.setPrefWidth(250);
		sliderWrapper.setSpacing(10);
		sliderWrapper.setAlignment(Pos.CENTER);

		Label volumeLabel = new Label("Master Volume: ");

		HBox volumeWrapper = new HBox();
		volumeWrapper.getChildren().addAll(volumeLabel, sliderWrapper);
		volumeWrapper.setSpacing(10);
		volumeWrapper.setMaxWidth(600);
		volumeWrapper.setAlignment(Pos.CENTER);

		settingsContainer.getChildren().add(volumeWrapper);

		setCenter(settingsContainer);

		backButton = new Button("Zurück");
		backButton.setOnMouseClicked((MouseEvent event) -> {
			
			if (changedSettings((int)volumeSlider.getValue())) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.getButtonTypes().clear();
				alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
				alert.setTitle("Einstellungen speichern?");
				alert.setHeaderText("Sie haben Einstellungen verändert, möchten Sie diese speichern?");
				alert.initOwner(MpcApplication.mainStage);
				
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.YES) {
					saveSettings((int)volumeSlider.getValue());
				}
			}
			
			TransitionFactory.animateLayoutSwitch(new MainMenuViewController(mpcController), null, null, false);
		});
		backButton.setPrefHeight(50.0);
		backButton.setPrefWidth(100.0);
		
		
		
		HBox buttons = new HBox();
		buttons.setAlignment(Pos.BOTTOM_RIGHT);
		buttons.getChildren().add(backButton);
		setBottom(buttons);
	}
	
	private void saveSettings(int masterVolume) {
		IOController.setProperty("masterVolume", masterVolume + "");
	}
	private boolean changedSettings(int masterVolume) {
		if (!IOController.getProperty("masterVolume").equals(masterVolume+""))
			return true;
		
		
		return false;
	}
	
}
