package com.typinggame.ui.components;

import com.typinggame.challenges.Challenge;
import com.typinggame.filemanagement.Settings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ChallengePanel extends VBox {
    private final Settings settings;
    private final ChallengeController controller;
    private TextArea textDisplay;
    private TextArea inputArea;

    public ChallengePanel(Settings settings, ChallengeController controller) {
        this.settings = settings;
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
        setSpacing(20);

        // Challenge Text Area
        textDisplay = new TextArea();
        textDisplay.setEditable(false);
        textDisplay.setWrapText(true);
        textDisplay.setPrefHeight(500);
        textDisplay.getStyleClass().add("challenge-text");

        // Input Area
        inputArea = new TextArea();
        inputArea.setWrapText(true);
        inputArea.setPrefHeight(200);
        inputArea.getStyleClass().add("input-text");

        // Listen for input changes and send to controller
        inputArea.textProperty().addListener((obs, oldText, newText) -> { controller.updateInput(newText); });

        // Control Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button startBtn = new Button("Start");
        Button pauseBtn = new Button("Pause");
        Button restartBtn = new Button("Restart");
        Button newBtn = new Button("New");

        startBtn.setOnAction(e -> {
            controller.startTimer();
            inputArea.setDisable(false);
            inputArea.requestFocus();
        });

        pauseBtn.setOnAction(e -> {
            controller.pauseTimer();
            inputArea.setDisable(true);
        });

        restartBtn.setOnAction(e -> restartChallenge());
        newBtn.setOnAction(e -> newChallenge());

        buttonBox.getChildren().addAll(startBtn, pauseBtn, restartBtn, newBtn);

        getChildren().addAll(
                new Label("Type the text below:"),
                textDisplay,
                new Label("Your input:"),
                inputArea,
                buttonBox
        );

        updateFontSize();
        clear();
    }

    public void newChallenge() {
        Challenge challenge = controller.getCurrentChallenge();
        inputArea.clear();
        challenge.configureChallenge(settings);
        displayChallenge(challenge);
    }

    public void displayChallenge(Challenge challenge) {
        if (challenge != null) {
            textDisplay.setText(challenge.getChallengeText());
            inputArea.clear();
            inputArea.setDisable(true);
            updateFontSize();
        }
    }

    public void clear() {
        textDisplay.clear();
        inputArea.clear();
        inputArea.setDisable(true);
        controller.reset();
    }

    private void restartChallenge() {
        if (controller.getCurrentChallenge() != null) {
            Challenge challenge = controller.getCurrentChallenge();
            displayChallenge(challenge);
        }
    }

    private void updateFontSize() {
        int fontSize = settings.getFontSize();
        String fontSizeStyle = String.format("-fx-font-size: %dpx;", fontSize);
        textDisplay.setStyle(fontSizeStyle);
        inputArea.setStyle(fontSizeStyle);
    }

    public TextArea getInputArea() { return inputArea; }
}