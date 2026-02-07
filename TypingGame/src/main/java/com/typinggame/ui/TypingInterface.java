package com.typinggame.ui;

import com.typinggame.challenges.*;
import com.typinggame.filemanagement.Settings;
import com.typinggame.filemanagement.HighScoresManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class TypingInterface extends UIManager {
    private Challenge currentChallenge;
    private final HighScoresManager highScoresManager;
    private Timeline timeline;
    private double timeRemaining;

    private TextArea textDisplay;
    private TextArea inputArea;
    private Label timerLabel;
    private Label wpmLabel;
    private VBox challengeSelection;
    private StackPane gameArea;
    private BorderPane mainLayout;

    public TypingInterface(Settings settings, HighScoresManager highScoresManager) {
        super(settings);
        this.highScoresManager = highScoresManager;
    }

    @Override
    public Pane createUI() {
        mainLayout = new BorderPane();

        // Create each area
        MenuBar menuBar = createMenuBar();
        mainLayout.setTop(menuBar);

        challengeSelection = createChallengeSelection();
        mainLayout.setLeft(challengeSelection);

        gameArea = new StackPane();
        gameArea.setPadding(new Insets(20));
        mainLayout.setCenter(gameArea);

        VBox statsPanel = createStatsPanel();
        mainLayout.setRight(statsPanel);

        return mainLayout;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // File menu
        Menu fileMenu = new Menu("File");
        MenuItem settingsItem = new MenuItem("Settings");
        MenuItem exitItem = new MenuItem("Exit");

        settingsItem.setOnAction(e -> showSettingsDialog());
        exitItem.setOnAction(e -> System.exit(0));

        fileMenu.getItems().addAll(settingsItem, new SeparatorMenuItem(), exitItem);

        // View menu
        Menu viewMenu = new Menu("View");
        MenuItem themeItem = new MenuItem("Toggle Theme");
        MenuItem fontSizePlusItem = new MenuItem("Increase Font Size");
        MenuItem fontSizeMinusItem = new MenuItem("Decrease Font Size");

        themeItem.setOnAction(e -> toggleTheme());
        fontSizePlusItem.setOnAction(e -> {
            changeFontSize(2);
            applyFontSize(timerLabel.getScene());
        });
        fontSizeMinusItem.setOnAction(e -> {
            changeFontSize(-2);
            applyFontSize(timerLabel.getScene());
        });

        viewMenu.getItems().addAll(themeItem,
                new SeparatorMenuItem(), fontSizePlusItem, fontSizeMinusItem);

        menuBar.getMenus().addAll(fileMenu, viewMenu);
        return menuBar;
    }

    private VBox createChallengeSelection() {
        VBox selectionPanel = new VBox(10);
        selectionPanel.setPadding(new Insets(20));
        selectionPanel.setPrefWidth(200);

        Label title = new Label("Select Challenge");
        title.getStyleClass().add("selection-title");

        Button wordBtn = new Button("Words");
        wordBtn.setPrefWidth(150);
        wordBtn.setOnAction(e -> startWordChallenge());

        Button sentenceBtn = new Button("Sentences");
        sentenceBtn.setPrefWidth(150);
        sentenceBtn.setOnAction(e -> startSentenceChallenge());

        Button paragraphBtn = new Button("Paragraph");
        paragraphBtn.setPrefWidth(150);
        paragraphBtn.setOnAction(e -> startParagraphChallenge());

        Button highScoresBtn = new Button("High Scores");
        highScoresBtn.setPrefWidth(150);
        highScoresBtn.setOnAction(e -> showHighScores());

        Pane spacer = new Pane();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        selectionPanel.getChildren().addAll(title, wordBtn, sentenceBtn, paragraphBtn, spacer, highScoresBtn);
        return selectionPanel;
    }

    private VBox createStatsPanel() {
        VBox statsPanel = new VBox(10);
        statsPanel.setPadding(new Insets(20));
        statsPanel.setPrefWidth(200);

        Label title = new Label("Statistics");
        title.getStyleClass().add("stats-title");

        timerLabel = new Label("Time: --:--");
        wpmLabel = new Label("WPM: 0");

        timerLabel.getStyleClass().add("stats-label");
        wpmLabel.getStyleClass().add("stats-label");

        statsPanel.getChildren().addAll(title, timerLabel, wpmLabel);
        return statsPanel;
    }

    private void startWordChallenge() {
        currentChallenge = new WordChallenge();
        currentChallenge.configureChallenge(settings);
        startChallenge();
    }

    private void startSentenceChallenge() {
        currentChallenge = new SentenceChallenge();
        currentChallenge.configureChallenge(settings);
        startChallenge();
    }

    private void startParagraphChallenge() {
        currentChallenge = new ParagraphChallenge();
        currentChallenge.configureChallenge(settings);
        startChallenge();
    }

    private void startChallenge() {
        if (currentChallenge == null) return;

        gameArea.getChildren().clear();

        VBox challengeContainer = new VBox(20);
        challengeContainer.setAlignment(Pos.CENTER);
        challengeContainer.setPadding(new Insets(20));

        // Challenge Text Area
        textDisplay = new TextArea(currentChallenge.getChallengeText());
        textDisplay.setEditable(false);
        textDisplay.setWrapText(true);
        textDisplay.setPrefHeight(500);
        textDisplay.getStyleClass().add("challenge-text");
        textDisplay.setStyle(String.format("-fx-font-size: %dpx;", settings.getFontSize()));

        // Input area
        inputArea = new TextArea();
        inputArea.setWrapText(true);
        inputArea.setPrefHeight(200);
        inputArea.getStyleClass().add("input-text");
        inputArea.setStyle(String.format("-fx-font-size: %dpx;", settings.getFontSize()));
        inputArea.textProperty().addListener((obs, oldText, newText) -> {
            checkInput(newText);
        });

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button startBtn = new Button("Start");
        Button pauseBtn = new Button("Pause");
        Button restartBtn = new Button("Restart");
        Button quitBtn = new Button("Quit");

        startBtn.setOnAction(e -> startTimer());
        pauseBtn.setOnAction(e -> pauseTimer());
        restartBtn.setOnAction(e -> restartChallenge());
        quitBtn.setOnAction(e -> quitChallenge());

        buttonBox.getChildren().addAll(startBtn, pauseBtn, restartBtn, quitBtn);

        challengeContainer.getChildren().addAll(
                new Label("Type the text below:"),
                textDisplay,
                new Label("Your input:"),
                inputArea,
                buttonBox
        );

        gameArea.getChildren().add(challengeContainer);

        // Initialize timer
        timeRemaining = currentChallenge.getTimeLimit();
        updateTimerDisplay();
        inputArea.setDisable(true);
    }

    private void startTimer() {
        if (timeline != null) {
            timeline.stop();
        }

        inputArea.setDisable(false);
        inputArea.requestFocus();

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    timeRemaining -= 1;
                    updateTimerDisplay();

                    if (timeRemaining <= 0) endChallenge();
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void pauseTimer() {
        if (timeline != null) {
            timeline.pause();
            inputArea.setDisable(true);
        }
    }

    private void updateTimerDisplay() {
        int minutes = (int) (timeRemaining / 60);
        int seconds = (int) (timeRemaining % 60);
        timerLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
    }

    private void checkInput(String input) {
        String originalText = currentChallenge.getChallengeText();
        int correctChars = 0;
        int totalChars = Math.min(input.length(), originalText.length());

        for (int i = 0; i < totalChars; i++) { if (input.charAt(i) == originalText.charAt(i)) correctChars++; }

        double timeInMinutes = (currentChallenge.getTimeLimit() - timeRemaining) / 60.0;
        if (timeInMinutes > 0) {
            double wpm = currentChallenge.calculateWPM(correctChars, timeInMinutes);
            wpmLabel.setText(String.format("WPM: %.1f", wpm));
        }

        if (input.equals(originalText)) endChallenge();
    }

    private void endChallenge() {
        if (timeline != null) timeline.stop();

        // Calculate final statistics
        String input = inputArea.getText();
        String originalText = currentChallenge.getChallengeText();
        int correctChars = 0;

        for (int i = 0; i < Math.min(input.length(), originalText.length()); i++) {
            if (input.charAt(i) == originalText.charAt(i)) correctChars++;

        }

        double timeInMinutes = (currentChallenge.getTimeLimit() - timeRemaining) / 60.0;
        double finalWPM = currentChallenge.calculateWPM(correctChars, timeInMinutes);

        currentChallenge.setAverageWPM(finalWPM);
        if (finalWPM > currentChallenge.getHighestWPM()) currentChallenge.setHighestWPM(finalWPM);

        // Save and show results
        currentChallenge.saveResults();

        showResultsDialog(finalWPM);
        inputArea.setDisable(true);
    }

    private void restartChallenge() {
        if (currentChallenge != null) {
            currentChallenge.configureChallenge(settings);
            startChallenge();
        }
    }

    private void quitChallenge() {
        if (timeline != null) {
            timeline.stop();
        }
        gameArea.getChildren().clear();
    }

    private void showResultsDialog(double finalWPM) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Challenge Complete");
        alert.setHeaderText("Congratulations!");
        alert.setContentText(String.format("You completed the challenge with %.1f WPM!", finalWPM));

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().addAll(timerLabel.getScene().getRoot().getStylesheets());

        alert.showAndWait();
    }

    private void showSettingsDialog() {
        SettingsInterface settingsInterface = new SettingsInterface(settings);
        settingsInterface.showDialog(timerLabel.getScene());
    }

    private void showHighScores() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("High Scores");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().addAll(timerLabel.getScene().getRoot().getStylesheets());

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        var scores = highScoresManager.getAllHighScores();
        if (scores.isEmpty()) {
            content.getChildren().add(new Label("No high scores yet. Complete a challenge!"));
        } else {
            for (var entry : scores.entrySet()) {
                String challengeType = entry.getKey();
                HighScoresManager.ScoreRecord record = entry.getValue();

                Label scoreLabel = new Label(String.format(
                        "%s: Highest: %.1f WPM, Average: %.1f WPM, Attempts: %d",
                        challengeType, record.getHighestWPM(), record.getAverageWPM(), record.getAttempts()
                ));
                scoreLabel.setWrapText(true);
                content.getChildren().add(scoreLabel);
            }
        }

        dialogPane.setContent(content);
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private void toggleTheme() {
        // Toggle theme
        if ("dark".equals(settings.getTheme())) {
            settings.setTheme("light");
        } else {
            settings.setTheme("dark");
        }

        try {
            settings.save();
            // Reapply theme
            Scene currentScene = timerLabel.getScene();
            if (currentScene != null) {
                applyTheme(currentScene);
            }
        } catch (java.io.IOException e) {
            showErrorDialog("Failed to save theme settings: " + e.getMessage());
        }
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Save Error");
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().addAll(timerLabel.getScene().getRoot().getStylesheets());

        alert.showAndWait();
    }
}