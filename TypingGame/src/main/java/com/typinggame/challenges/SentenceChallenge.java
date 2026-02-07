package com.typinggame.challenges;

import com.typinggame.filemanagement.Settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SentenceChallenge extends Challenge {
    
    public SentenceChallenge() {
        super();
        this.count = 10;
        this.challengeType = "Sentences";
        loadDefaultText();
    }
    
    protected void loadDefaultText() {
        try {
            this.dictionary = Files.readAllLines(Paths.get("data/defaultSentences.txt"));
        } catch (IOException e) {
            System.err.println("Error loading default sentences: " + e.getMessage());
        }
    }

    @Override
    public void configureChallenge(Settings settings) {
        this.count = settings.getSentenceCount();
        this.timeLimit = settings.getSentenceTimeLimit();

        this.challengeText = generateChallengeText(count);
    }

}