package com.typinggame.filemanagement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Settings extends FileManager {
    private int wordCount = 50;
    private double wordTimeLimit = 60.0;

    // Sentence Challenge Settings
    private int sentenceCount = 10;
    private double sentenceTimeLimit = 60;

    // Paragraph Challenge Settings
    private boolean useCustomParagraphs = false;
    private String customParagraphFile = "";
    private double paragraphTimeLimit = 60.0;

    // UI Settings
    private String theme = "dark";
    private int fontSize = 26;

    public Settings(String filePath) {
        super(filePath);
    }

    @Override
    public void save() throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        // Create parent directories if they don't exist
        Files.createDirectories(Paths.get(filePath).getParent());

        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(this, writer);
        }
    }

    @Override
    public void load() throws IOException {
        Gson gson = new Gson();

        // Check if file exists
        if (!Files.exists(Paths.get(filePath))) {
            createDefaultIfNotExists();
            return;
        }

        try (FileReader reader = new FileReader(filePath)) {
            Settings loaded = gson.fromJson(reader, Settings.class);
            if (loaded != null) {
                copyProperties(loaded);
            }
        }
    }

    @Override
    public void createDefaultIfNotExists() throws IOException {
        if (!Files.exists(Paths.get(filePath))) { save(); }
    }

    private void copyProperties(Settings source) {
        this.wordCount = source.wordCount;
        this.wordTimeLimit = source.wordTimeLimit;
        this.sentenceCount = source.sentenceCount;
        this.sentenceTimeLimit = source.sentenceTimeLimit;
        this.useCustomParagraphs = source.useCustomParagraphs;
        this.customParagraphFile = source.customParagraphFile;
        this.paragraphTimeLimit = source.paragraphTimeLimit;
        this.theme = source.theme;
        this.fontSize = source.fontSize;
    }

    public int getWordCount() { return wordCount; }
    public void setWordCount(int wordCount) { this.wordCount = wordCount; }

    public double getWordTimeLimit() { return wordTimeLimit; }
    public void setWordTimeLimit(double wordTimeLimit) { this.wordTimeLimit = wordTimeLimit; }

    public int getSentenceCount() { return sentenceCount; }
    public void setSentenceCount(int sentenceCount) { this.sentenceCount = sentenceCount; }

    public double getSentenceTimeLimit() { return sentenceTimeLimit; }
    public void setSentenceTimeLimit(double sentenceTimeLimit) { this.sentenceTimeLimit = sentenceTimeLimit; }

    public boolean isUseCustomParagraphs() { return useCustomParagraphs; }
    public void setUseCustomParagraphs(boolean useCustomParagraphs) { this.useCustomParagraphs = useCustomParagraphs; }

    public String getCustomParagraphFile() { return customParagraphFile; }
    public void setCustomParagraphFile(String customParagraphFile) { this.customParagraphFile = customParagraphFile; }

    public double getParagraphTimeLimit() { return paragraphTimeLimit; }
    public void setParagraphTimeLimit(double paragraphTimeLimit) { this.paragraphTimeLimit = paragraphTimeLimit; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public int getFontSize() { return fontSize; }
    public void setFontSize(int fontSize) { this.fontSize = fontSize; }
}