package com.typinggame.challenges;

import com.typinggame.filemanagement.Settings;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WordChallenge extends Challenge {

    public WordChallenge() {
        super();
        this.count = 50;
        this.challengeType = "Words";
    loadDefaultWords();
    }

    private void loadDefaultWords() {
        try {
            this.dictionary = Files.readAllLines(Paths.get("data/defaultWords.txt"));
        } catch (IOException e) {
            System.err.println("Error loading default words: " + e.getMessage());
        }
    }
    
    @Override
    public void configureSettings(Settings settings) {
        this.count = settings.getWordCount();
        this.timeLimit = settings.getWordTimeLimit();

        this.challengeText = generateRandomText(count);
    }

}