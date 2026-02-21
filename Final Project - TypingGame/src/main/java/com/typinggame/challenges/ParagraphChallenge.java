package com.typinggame.challenges;

import com.typinggame.filemanagement.Settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ParagraphChallenge extends Challenge {
    private boolean useCustomParagraphs;
    private String customParagraphFile;
    
    public ParagraphChallenge() {
        super();
        this.useCustomParagraphs = false;
        this.count = 1;
        this.challengeType = "Paragraph";
        loadDefaultText();
    }
    
    protected void loadDefaultText() {
        try { this.dictionary = Files.readAllLines(Paths.get("data/defaultParagraphs.txt")); }
        catch (IOException e) { System.err.println("Error loading default paragraphs: " + e.getMessage()); }
    }

    @Override
    public void configureChallenge(Settings settings) {
        this.useCustomParagraphs = settings.isUseCustomParagraphs();
        this.customParagraphFile = settings.getCustomParagraphFile();
        this.timeLimit = settings.getParagraphTimeLimit();

        if (useCustomParagraphs && customParagraphFile != null && !customParagraphFile.isEmpty()) loadCustomParagraphs(customParagraphFile);
        this.challengeText = generateChallengeText(count);
    }
    
    private void loadCustomParagraphs(String filePath) {
        try { this.dictionary = Files.readAllLines(Paths.get(filePath)); }
        catch (IOException e) { System.err.println("Error loading custom paragraphs: " + e.getMessage()); }
    }

    public void setUseCustomParagraphs(boolean useCustomParagraphs) {
        this.useCustomParagraphs = useCustomParagraphs;
    }
    public boolean isUseCustomParagraphs() { return useCustomParagraphs; }

    public void setCustomParagraphFile(String customParagraphFile) {
        this.customParagraphFile = customParagraphFile;
        if (useCustomParagraphs) loadCustomParagraphs(customParagraphFile);
    }
    public String getCustomParagraphFile() { return customParagraphFile; }
}