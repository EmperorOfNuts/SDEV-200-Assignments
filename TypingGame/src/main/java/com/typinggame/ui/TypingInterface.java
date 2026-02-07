package com.typinggame.ui;

import com.typinggame.challenges.*;
import com.typinggame.filemanagement.*;
import com.typinggame.ui.components.*;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.IOException;

public class TypingInterface implements UIComponent {
    private Settings settings;
    private HighScoresManager highScoresManager;
    private ChallengeController challengeController;
    private ChallengePanel challengePanel;
    private StatsPanel statsPanel;
    private MenuBarManager menuBarManager;
    private ChallengeSelector challengeSelector;

    private BorderPane mainLayout;
    private Scene currentScene;

    public TypingInterface(Settings settings, HighScoresManager highScoresManager) {
        this.settings = settings;
        this.highScoresManager = highScoresManager;
    }

    @Override
    public Pane createUI() {
        mainLayout = new BorderPane();

        challengeController = new ChallengeController(settings);
        challengePanel = new ChallengePanel(settings, challengeController);
        statsPanel = new StatsPanel(challengeController);
        menuBarManager = new MenuBarManager(settings,
                this::showSettingsDialog,
                this::reapplyTheme
        );
        challengeSelector = new ChallengeSelector(
                this::startWordChallenge,
                this::startSentenceChallenge,
                this::startParagraphChallenge,
                this::showHighScores
        );

        mainLayout.setTop(menuBarManager.createMenuBar());
        mainLayout.setLeft(challengeSelector.createSelectionPanel());
        mainLayout.setCenter(challengePanel);
        mainLayout.setRight(statsPanel.createStatsPanel());

        // Setup controller callbacks
        challengeController.setOnWPMUpdate(statsPanel::updateWPM);
        challengeController.setOnChallengeComplete(this::endChallenge);

        return mainLayout;
    }

    private void reapplyTheme() { if (currentScene != null) { ThemeUtils.applyTheme(settings, currentScene); } }

    public void setScene(Scene scene) { this.currentScene = scene; }

    private void startWordChallenge() {
        Challenge challenge = new WordChallenge();
        challenge.configureChallenge(settings);
        startChallenge(challenge);
    }

    private void startSentenceChallenge() {
        Challenge challenge = new SentenceChallenge();
        challenge.configureChallenge(settings);
        startChallenge(challenge);
    }

    private void startParagraphChallenge() {
        Challenge challenge = new ParagraphChallenge();
        challenge.configureChallenge(settings);
        startChallenge(challenge);
    }

    private void startChallenge(Challenge challenge) {
        challengeController.setCurrentChallenge(challenge);
        challengePanel.displayChallenge(challenge);
        statsPanel.reset();
    }

    private void endChallenge(double finalWPM) {

        if (challengeController.getCurrentChallenge() != null) {
            Challenge challenge = challengeController.getCurrentChallenge();

            challenge.setAverageWPM(finalWPM);
            if (finalWPM > challenge.getHighestWPM()) { challenge.setHighestWPM(finalWPM); }

            challenge.saveResults();

            try {
                highScoresManager.save();
            } catch (IOException e) {
                System.err.println("Failed to save high score: " + e.getMessage());
            }
        }

        showResultsDialog(finalWPM);
        challengePanel.getInputArea().setDisable(true);
    }

    private void showSettingsDialog() {
        SettingsInterface settingsInterface = new SettingsInterface(settings);
        Scene currentScene = challengePanel.getScene();
        settingsInterface.showDialog(currentScene);

        // Reapply theme if it changed
        if (currentScene != null) { ThemeUtils.applyTheme(settings, currentScene); }
    }

    private void showHighScores() {
        Scene currentScene = challengePanel.getScene();
        if (currentScene != null) {
            HighScoresDialog.show(highScoresManager, currentScene);
        }
    }

    private void showResultsDialog(double finalWPM) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Challenge Complete");
        alert.setHeaderText("Congratulations!");
        alert.setContentText(String.format("You completed the challenge with %.1f WPM!", finalWPM));

        DialogPane dialogPane = alert.getDialogPane();
        Scene currentScene = challengePanel.getScene();
        if (currentScene != null) {
            dialogPane.getStylesheets().addAll(currentScene.getRoot().getStylesheets());
        }

        alert.showAndWait();
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        Scene currentScene = challengePanel.getScene();
        if (currentScene != null) {
            dialogPane.getStylesheets().addAll(currentScene.getRoot().getStylesheets());
        }

        alert.showAndWait();
    }
}