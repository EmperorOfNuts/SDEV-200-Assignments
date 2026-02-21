package com.typinggame.ui.components;

import com.typinggame.challenges.Challenge;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.function.Consumer;

public class ChallengeController {
    private double timeRemaining;
    private String currentInput = "";
    private Challenge currentChallenge;
    private Timeline timeline;
    private Consumer<Double> onWPMUpdate;
    private Consumer<Double> onChallengeComplete;
    private boolean isPaused = false;

    public ChallengeController() {}

    public void setCurrentChallenge(Challenge challenge) {
        this.currentChallenge = challenge;
        if (challenge != null) this.timeRemaining = challenge.getTimeLimit();
        this.currentInput = "";
        this.isPaused = false;
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

    private int countCorrectCharacters(String input, String originalText) {
        int correctChars = 0;
        int totalChars = Math.min(input.length(), originalText.length());

        for (int i = 0; i < totalChars; i++) {
            if (input.charAt(i) == originalText.charAt(i)) correctChars++;
        }
        return correctChars;
    }

    public void setOnWPMUpdate(Consumer<Double> onWPMUpdate) { this.onWPMUpdate = onWPMUpdate; }

    public void setOnChallengeComplete(Consumer<Double> onChallengeComplete) { this.onChallengeComplete = onChallengeComplete; }

    public boolean isPaused() { return isPaused; }

    public double getTimeRemaining() { return timeRemaining; }

    public Challenge getCurrentChallenge() { return currentChallenge; }

    public Consumer<Double> getOnWPMUpdate() { return onWPMUpdate; }

    public void startTimer() {
        if (timeline != null) timeline.stop();
        currentInput = "";
        timeRemaining = currentChallenge != null ? currentChallenge.getTimeLimit() : 0;
        isPaused = false;

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    if (!isPaused) {
                        timeRemaining -= 1;
                        calculateAndUpdateWPM();

                        if (timeRemaining <= 0) endChallenge();
                    }
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void pauseTimer() {
        if (timeline != null) isPaused = !isPaused; // Toggle pause state
    }

    public void stopTimer() {
        if (timeline != null) {
            timeline.stop();
            isPaused = false;
        }
    }

    public void resetTimer() {
        if (currentChallenge != null) {
            timeRemaining = currentChallenge.getTimeLimit();
            isPaused = false;
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

    public void reset() {
        stopTimer();
        currentChallenge = null;
        currentInput = "";
        timeRemaining = 0;
        isPaused = false;
    }

    public void updateInput(String input) {
        this.currentInput = input;
        calculateAndUpdateWPM();

        // Check if challenge is complete
        if (currentChallenge != null && input.equals(currentChallenge.getChallengeText())) endChallenge();
    }
}