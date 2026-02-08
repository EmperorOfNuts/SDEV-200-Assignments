package com.typinggame.challenges;

import com.typinggame.filemanagement.HighScoresManager;
import com.typinggame.filemanagement.Settings;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public abstract class Challenge {
    protected double timeLimit;
    protected double highestWPM;
    protected double averageWPM;
    protected int count;
    protected String challengeText;
    protected String challengeType;
    protected List<String> dictionary;
    protected SecureRandom random = new SecureRandom();
    
    public Challenge() {
        this.highestWPM = 0.0;
        this.averageWPM = 0.0;
    }

    protected abstract void loadDefaultText();

    public abstract void configureChallenge(Settings settings);

    public void setTimeLimit(double timeLimit) { this.timeLimit = timeLimit; }
    public double getTimeLimit() { return timeLimit; }

    public void setHighestWPM(double highestWPM) { this.highestWPM = highestWPM; }
    public double getHighestWPM() { return highestWPM; }

    public void setAverageWPM(double averageWPM) { this.averageWPM = averageWPM; }
    public double getAverageWPM() { return averageWPM; }

    public void setCount(int count) { this.count = count; }
    public int getCount() { return count; }

    public void setChallengeText(String challengeText) { this.challengeText = challengeText; }
    public String getChallengeText() { return challengeText; }

    public void setChallengeType(String challengeType) { this.challengeType = challengeType; }
    public String getChallengeType() { return challengeType; }

    public void setDictionary(ArrayList<String> dictionary) { this.dictionary = dictionary; }
    public List<String> getDictionary() { return dictionary; }


    public void saveResults() { HighScoresManager.getInstance().saveScore(challengeType, Integer.toString(count), highestWPM, averageWPM); };

    public String generateChallengeText(int count) {
        int selectedSize = Math.min(count, dictionary.size());
        List<String> selectedWords = new ArrayList<>();

        for(int i = 0; i < selectedSize; i++) selectedWords.add(dictionary.get(random.nextInt(0, dictionary.size())));

        return String.join(" ", selectedWords);
    }

    public double calculateWPM(int correctChars, double timeInMinutes) { return (correctChars / 5.0) / timeInMinutes; }
}