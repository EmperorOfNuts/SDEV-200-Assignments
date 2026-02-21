package com.typinggame.ui.components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class StatsPanel {
    private final ChallengeController controller;
    private Label timerLabel;
    private Label wpmLabel;

    public StatsPanel(ChallengeController controller) {
        this.controller = controller;
    }

    private void updateTimerDisplay() {
        double timeRemaining = controller.getTimeRemaining();
        int minutes = (int) (timeRemaining / 60);
        int seconds = (int) (timeRemaining % 60);
        timerLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
    }

    public void reset() {
        timerLabel.setText("Time: --:--");
        wpmLabel.setText("WPM: 0");
    }

    public void updateWPM(double wpm) {
        wpmLabel.setText(String.format("WPM: %.1f", wpm));
    }

    public VBox createStatsPanel() {
        VBox statsPanel = new VBox(10);
        statsPanel.setPadding(new Insets(20));
        statsPanel.setPrefWidth(200);

        Label title = new Label("Statistics");
        title.getStyleClass().add("stats-title");

        timerLabel = new Label("Time: --:--");
        wpmLabel = new Label("WPM: 0");

        timerLabel.getStyleClass().add("stats-label");
        wpmLabel.getStyleClass().add("stats-label");

        // Update timer periodically
        javafx.animation.AnimationTimer timer = new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                updateTimerDisplay();
            }
        };
        timer.start();

        statsPanel.getChildren().addAll(title, timerLabel, wpmLabel);
        return statsPanel;
    }
}