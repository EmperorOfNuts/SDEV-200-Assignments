package com.m5a3;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private Text displayText;
    private Slider redSlider;
    private Slider greenSlider;
    private Slider blueSlider;
    private Slider opacitySlider;

    @Override
    public void start(Stage primaryStage) {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20));

        // Create Text
        displayText = new Text("Show Color");
        displayText.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        borderPane.setCenter(displayText);
        BorderPane.setAlignment(displayText, Pos.CENTER);

        // Create Slider Panel
        GridPane controlPanel = new GridPane();
        controlPanel.setHgap(20);
        controlPanel.setVgap(15);
        controlPanel.setPadding(new Insets(20));
        controlPanel.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");
        controlPanel.setAlignment(Pos.CENTER);

        // Create Sliders & Labels
        Label redLabel = new Label("Red:");
        redLabel.setAlignment(Pos.CENTER_RIGHT);
        controlPanel.add(redLabel, 0, 0);
        controlPanel.add(createSlider(255, 0), 1, 0);

        Label greenLabel = new Label("Green:");
        greenLabel.setAlignment(Pos.CENTER_RIGHT);
        controlPanel.add(greenLabel, 0, 1);
        controlPanel.add(createSlider(255, 0), 1, 1);

        Label blueLabel = new Label("Blue:");
        blueLabel.setAlignment(Pos.CENTER_RIGHT);
        controlPanel.add(blueLabel, 0, 2);
        controlPanel.add(createSlider(255, 0), 1, 2);

        Label opacityLabel = new Label("Opacity %:");
        opacityLabel.setAlignment(Pos.CENTER_RIGHT);
        controlPanel.add(opacityLabel, 0, 3);
        controlPanel.add(createSlider(100, 100), 1, 3);

        borderPane.setBottom(controlPanel);
        BorderPane.setAlignment(controlPanel, Pos.CENTER);

        updateColor();

        // Add Listeners
        redSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateColor());
        greenSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateColor());
        blueSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateColor());
        opacitySlider.valueProperty().addListener((obs, oldVal, newVal) -> updateColor());

        // Show Stage
        Scene scene = new Scene(borderPane, 500, 350);
        primaryStage.setTitle("M5A3");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Slider createSlider(double max, double initialValue) {
        Slider slider = new Slider(0, max, initialValue);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(50);
        slider.setMinorTickCount(5);
        slider.setBlockIncrement(10);
        slider.setPrefWidth(300);

        if (max == 255 && redSlider == null) redSlider = slider;
        else if (max == 255 && greenSlider == null) greenSlider = slider;
        else if (max == 255 && blueSlider == null) blueSlider = slider;
        else if (max == 100) opacitySlider = slider;

        return slider;
    }

    private void updateColor() {
        double red = redSlider.getValue() / 255.0;
        double green = greenSlider.getValue() / 255.0;
        double blue = blueSlider.getValue() / 255.0;
        double opacity = opacitySlider.getValue() / 100.0;

        Color color = new Color(red, green, blue, opacity);
        displayText.setFill(color);
    }

    public static void main(String[] args) { launch(args); }
}