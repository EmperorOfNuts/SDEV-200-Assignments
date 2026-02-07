package com.typinggame.challenges;

import com.typinggame.filemanagement.HighScoresManager;
import com.typinggame.filemanagement.Settings;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public abstract class Challenge {
    protected List<String> dictionary;
    protected double timeLimit;
    protected String challengeText;
    protected double highestWPM;
    protected double averageWPM;
    protected int count;
    protected String challengeType;
    protected SecureRandom random = new SecureRandom();
    
    public Challenge() {
        this.highestWPM = 0.0;
        this.averageWPM = 0.0;
    }

    public List<String> getDictionary() { return dictionary; }
    public void setDictionary(ArrayList<String> dictionary) { this.dictionary = dictionary; }
    
    public double getTimeLimit() { return timeLimit; }
    public void setTimeLimit(double timeLimit) { this.timeLimit = timeLimit; }
    
    public String getChallengeText() { return challengeText; }
    public void setChallengeText(String challengeText) { this.challengeText = challengeText; }
    
    public double getHighestWPM() { return highestWPM; }
    public void setHighestWPM(double highestWPM) { this.highestWPM = highestWPM; }
    
    public double getAverageWPM() { return averageWPM; }
    public void setAverageWPM(double averageWPM) { this.averageWPM = averageWPM; }

    public String getChallengeType() { return challengeType; }
    public void setChallengeType(String challengeType) { this.challengeType = challengeType; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }

    public String generateRandomText(int wordCount) {
        int selectedSize = Math.min(wordCount, dictionary.size());
        List<String> selectedWords = new ArrayList<>();

        for(int i = 0; i < selectedSize; i++) selectedWords.add(dictionary.get(random.nextInt(0, dictionary.size())));

        return String.join(" ", selectedWords);
    }


    public abstract void configureSettings(Settings settings);

    public void saveResults() {
        HighScoresManager.getInstance().saveScore(challengeType, Integer.toString(count), highestWPM, averageWPM);
    };

    public double calculateWPM(int correctChars, double timeInMinutes) { return (correctChars / 5.0) / timeInMinutes; }
}