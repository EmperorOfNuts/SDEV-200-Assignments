package com.typinggame.challenges;

import com.typinggame.filemanagement.Settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

public class SentenceChallenge extends Challenge {
    
    public SentenceChallenge() {
        super();
        this.count = 10;
        this.challengeType = "Sentences";
        loadDefaultSentences();
    }
    
    private void loadDefaultSentences() {
        try {
            this.dictionary = Files.readAllLines(Paths.get("data/defaultSentences.txt"));
        } catch (IOException e) {
            System.err.println("Error loading default sentences: " + e.getMessage());
        }
    }

    @Override
    public void configureSettings(Settings settings) {
        this.count = settings.getSentenceCount();
        this.timeLimit = settings.getSentenceTimeLimit();

        this.challengeText = generateSentenceText(count);
    }
    
    private String generateSentenceText(int count) {
        Collections.shuffle(dictionary);
        
        return String.join(" ", dictionary.subList(0, Math.min(count, dictionary.size())));
    }

}