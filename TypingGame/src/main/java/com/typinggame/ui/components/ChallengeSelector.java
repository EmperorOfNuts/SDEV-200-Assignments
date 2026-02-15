package com.typinggame.ui.components;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ChallengeSelector {
    private final Runnable onWordChallenge;
    private final Runnable onSentenceChallenge;
    private final Runnable onParagraphChallenge;
    private final Runnable onHighScores;

    public ChallengeSelector(Runnable onWordChallenge, Runnable onSentenceChallenge, Runnable onParagraphChallenge, Runnable onHighScores) {
        this.onWordChallenge = onWordChallenge;
        this.onSentenceChallenge = onSentenceChallenge;
        this.onParagraphChallenge = onParagraphChallenge;
        this.onHighScores = onHighScores;
    }

    public VBox createSelectionPanel() {
        VBox selectionPanel = new VBox(10);
        selectionPanel.setPadding(new Insets(20));
        selectionPanel.setPrefWidth(200);

        Label title = new Label("Select Challenge");
        title.getStyleClass().add("selection-title");

        Button wordBtn = new Button("Words");
        wordBtn.setPrefWidth(150);
        wordBtn.setOnAction(e -> onWordChallenge.run());

        Button sentenceBtn = new Button("Sentences");
        sentenceBtn.setPrefWidth(150);
        sentenceBtn.setOnAction(e -> onSentenceChallenge.run());

        Button paragraphBtn = new Button("Paragraph");
        paragraphBtn.setPrefWidth(150);
        paragraphBtn.setOnAction(e -> onParagraphChallenge.run());

        Button highScoresBtn = new Button("High Scores");
        highScoresBtn.setPrefWidth(150);
        highScoresBtn.setOnAction(e -> onHighScores.run());

        Pane spacer = new Pane();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        selectionPanel.getChildren().addAll(title, wordBtn, sentenceBtn, paragraphBtn, spacer, highScoresBtn);
        return selectionPanel;
    }
}