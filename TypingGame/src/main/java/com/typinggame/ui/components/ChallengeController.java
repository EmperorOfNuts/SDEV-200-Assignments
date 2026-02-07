package com.typinggame.ui.components;

import com.typinggame.challenges.Challenge;
import com.typinggame.filemanagement.Settings;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.function.Consumer;

public class ChallengeController {
    private Challenge currentChallenge;
    private Timeline timeline;
    private double timeRemaining;
    private Consumer<Double> onWPMUpdate;
    private Consumer<Double> onChallengeComplete;
    private final Settings settings;

    // Track current user input
    private String currentInput = "";

    public ChallengeController(Settings settings) {
        this.settings = settings;
    }

    public void setCurrentChallenge(Challenge challenge) {
        this.currentChallenge = challenge;
        if (challenge != null) {
            this.timeRemaining = challenge.getTimeLimit();
        }
        this.currentInput = ""; // Reset input when new challenge starts
    }

    public Challenge getCurrentChallenge() {
        return currentChallenge;
    }

    public void updateInput(String input) {
        this.currentInput = input;
        calculateAndUpdateWPM();

        // Check if challenge is complete
        if (currentChallenge != null && input.equals(currentChallenge.getChallengeText())) {
            endChallenge();
        }
    }

    private void calculateAndUpdateWPM() {
        if (currentChallenge == null || currentInput.isEmpty()) return;

        int correctChars = countCorrectCharacters(currentInput, currentChallenge.getChallengeText());
        double timeInMinutes = (currentChallenge.getTimeLimit() - timeRemaining) / 60.0;

        if (timeInMinutes > 0 && onWPMUpdate != null) {
            double wpm = currentChallenge.calculateWPM(correctChars, timeInMinutes);
            onWPMUpdate.accept(wpm);
        }
    }

    public void startTimer() {
        if (timeline != null) {
            timeline.stop();
        }

        // Reset for new attempt
        currentInput = "";
        timeRemaining = currentChallenge != null ? currentChallenge.getTimeLimit() : 0;

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    timeRemaining -= 1;
                    // Update WPM based on current input
                    calculateAndUpdateWPM();

                    if (timeRemaining <= 0) {
                        endChallenge();
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void pauseTimer() {
        if (timeline != null) {
            timeline.pause();
        }
    }

    public void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    public void endChallenge() {
        stopTimer();

        if (currentChallenge != null && onChallengeComplete != null) {
            int correctChars = countCorrectCharacters(currentInput, currentChallenge.getChallengeText());
            double timeInMinutes = (currentChallenge.getTimeLimit() - timeRemaining) / 60.0;
            double finalWPM = currentChallenge.calculateWPM(correctChars, timeInMinutes);
            onChallengeComplete.accept(finalWPM);
        }
    }

    private int countCorrectCharacters(String input, String originalText) {
        int correctChars = 0;
        int totalChars = Math.min(input.length(), originalText.length());

        for (int i = 0; i < totalChars; i++) {
            if (input.charAt(i) == originalText.charAt(i)) { correctChars++; }
        }
        return correctChars;
    }

    public double getTimeRemaining() {
        return timeRemaining;
    }

    public void setOnWPMUpdate(Consumer<Double> onWPMUpdate) {
        this.onWPMUpdate = onWPMUpdate;
    }

    public void setOnChallengeComplete(Consumer<Double> onChallengeComplete) {
        this.onChallengeComplete = onChallengeComplete;
    }

    public void reset() {
        stopTimer();
        currentChallenge = null;
        currentInput = "";
        timeRemaining = 0;
    }
}