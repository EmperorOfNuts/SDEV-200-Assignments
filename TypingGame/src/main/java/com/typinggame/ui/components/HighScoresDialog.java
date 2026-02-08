package com.typinggame.ui.components;

import com.typinggame.filemanagement.HighScoresManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class HighScoresDialog {

    public static void show(HighScoresManager highScoresManager, Scene parentScene) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("High Scores");

        DialogPane dialogPane = dialog.getDialogPane();
        if (parentScene != null) dialogPane.getStylesheets().addAll(parentScene.getRoot().getStylesheets());

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
}