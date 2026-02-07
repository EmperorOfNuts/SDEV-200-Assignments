package com.typinggame.filemanagement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class HighScoresManager extends FileManager {
    private static HighScoresManager instance;
    private Map<String, ScoreRecord> highScores;

    private HighScoresManager(String filePath) {
        super(filePath);
        this.highScores = new HashMap<>();
    }

    public static synchronized HighScoresManager getInstance() {
        if (instance == null) {
            instance = new HighScoresManager("data/highscores.json");
        }
        return instance;
    }

    public static synchronized HighScoresManager getInstance(String filePath) {
        if (instance == null) {
            instance = new HighScoresManager(filePath);
        }
        return instance;
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
            gson.toJson(highScores, writer);
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
            Type type = new TypeToken<Map<String, ScoreRecord>>(){}.getType();
            Map<String, ScoreRecord> loaded = gson.fromJson(reader, type);
            if (loaded != null) {
                highScores = loaded;
            }
        }
    }

    @Override
    public void createDefaultIfNotExists() throws IOException {
        if (!Files.exists(Paths.get(filePath))) {
            save(); // Save empty high scores
        }
    }

    public void saveScore(String challengeType, String challengeCount, double highestWPM, double averageWPM) {
        String key = challengeType + " Challenge with " + challengeCount + " " + challengeType.toLowerCase();
        ScoreRecord record = highScores.getOrDefault(key, new ScoreRecord());

        if (highestWPM > record.getHighestWPM()) {
            record.setHighestWPM(highestWPM);
        }

        if (record.getAttempts() == 0) {
            record.setAverageWPM(averageWPM);
        } else {
            double currentTotal = record.getAverageWPM() * record.getAttempts();
            record.setAverageWPM((currentTotal + averageWPM) / (record.getAttempts() + 1));
        }

        record.incrementAttempts();
        highScores.put(key, record);

        try {
            save();
        } catch (IOException e) {
            System.err.println("Failed to save high score: " + e.getMessage());
        }
    }

    public ScoreRecord getScoreRecord(String challengeType) {
        return highScores.getOrDefault(challengeType, new ScoreRecord());
    }

    public Map<String, ScoreRecord> getAllHighScores() {
        return new HashMap<>(highScores);
    }

    public void clearHighScores() throws IOException {
        highScores.clear();
        save();
    }

    public void exportHighScores(String exportPath) throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        Files.createDirectories(Paths.get(exportPath).getParent());

        try (FileWriter writer = new FileWriter(exportPath)) {
            Map<String, Object> exportData = new HashMap<>();
            exportData.put("exportDate", new java.util.Date().toString());
            exportData.put("highScores", highScores);
            gson.toJson(exportData, writer);
        }
    }

    public static class ScoreRecord {
        private double highestWPM;
        private double averageWPM;
        private int attempts;

        public ScoreRecord() {
            this.highestWPM = 0.0;
            this.averageWPM = 0.0;
            this.attempts = 0;
        }

        public double getHighestWPM() { return highestWPM; }
        public void setHighestWPM(double highestWPM) { this.highestWPM = highestWPM; }

        public double getAverageWPM() { return averageWPM; }
        public void setAverageWPM(double averageWPM) { this.averageWPM = averageWPM; }

        public int getAttempts() { return attempts; }
        public void setAttempts(int attempts) { this.attempts = attempts; }
        public void incrementAttempts() { this.attempts++; }
    }
}