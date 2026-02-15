package com.typinggame.filemanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HighScoresManagerTest {

    private HighScoresManager highScoresManager;
    private Path testDataPath;

    @BeforeEach
    void setUp() throws IOException {
        // Create temporary test directory
        testDataPath = Paths.get("test-data");
        Files.createDirectories(testDataPath);

        // Reset singleton for testing
        resetSingleton();

        highScoresManager = HighScoresManager.getInstance();
    }

    private void resetSingleton() {
        try {
            java.lang.reflect.Field instance = HighScoresManager.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSingletonInstance() {
        HighScoresManager instance1 = HighScoresManager.getInstance();
        HighScoresManager instance2 = HighScoresManager.getInstance();

        assertSame(instance1, instance2);
    }

    @Test
    void testSaveScore() {
        highScoresManager.saveScore("Words", "50", 75.5, 68.2);

        Map<String, HighScoresManager.ScoreRecord> scores = highScoresManager.getAllHighScores();
        assertEquals(1, scores.size());

        String expectedKey = "Words Challenge with 50 words";
        assertTrue(scores.containsKey(expectedKey));

        HighScoresManager.ScoreRecord record = scores.get(expectedKey);
        assertEquals(75.5, record.getHighestWPM());
        assertEquals(68.2, record.getAverageWPM());
        assertEquals(1, record.getAttempts());
    }

    @Test
    void testSaveScore_MultipleAttempts() {
        String key = "Words Challenge with 50 words";

        // Set score
        highScoresManager.saveScore("Words", "50", 75.5, 75.5);

        // Set higher score
        highScoresManager.saveScore("Words", "50", 82.3, 82.3);

        Map<String, HighScoresManager.ScoreRecord> scores = highScoresManager.getAllHighScores();
        HighScoresManager.ScoreRecord record = scores.get(key);

        assertEquals(82.3, record.getHighestWPM()); // Highest should update
        assertEquals(78.9, record.getAverageWPM(), 0.1); // (75.5 + 82.3)/2 = 78.9
        assertEquals(2, record.getAttempts());

        // Set lower score
        highScoresManager.saveScore("Words", "50", 70.0, 70.0);

        scores = highScoresManager.getAllHighScores();
        record = scores.get(key);

        assertEquals(82.3, record.getHighestWPM());
        assertEquals(75.93, record.getAverageWPM(), 0.1); // (75.5 +82.3 + 70) / 3 = 75.93
        assertEquals(3, record.getAttempts());
    }

    @Test
    void testGetScoreRecord() {
        highScoresManager.saveScore("Sentences", "10", 65.0, 65.0);

        HighScoresManager.ScoreRecord record = highScoresManager.getScoreRecord("Sentences Challenge with 10 sentences");

        assertEquals(65.0, record.getHighestWPM());
        assertEquals(65.0, record.getAverageWPM());
        assertEquals(1, record.getAttempts());

        // Nonexistent key should return default record
        HighScoresManager.ScoreRecord defaultRecord = highScoresManager.getScoreRecord("NonExistent");
        assertEquals(0.0, defaultRecord.getHighestWPM());
        assertEquals(0.0, defaultRecord.getAverageWPM());
        assertEquals(0, defaultRecord.getAttempts());
    }

    @Test
    void testClearHighScores() throws IOException {
        highScoresManager.saveScore("Words", "50", 75.5, 68.2);
        highScoresManager.saveScore("Sentences", "10", 65.0, 62.5);

        assertEquals(2, highScoresManager.getAllHighScores().size());

        highScoresManager.clearHighScores();

        assertEquals(0, highScoresManager.getAllHighScores().size());
    }

    @Test
    void testExportHighScores() throws IOException {
        highScoresManager.saveScore("Words", "50", 75.5, 68.2);

        Path exportPath = testDataPath.resolve("export.json");
        highScoresManager.exportHighScores(exportPath.toString());

        assertTrue(Files.exists(exportPath));
        String content = Files.readString(exportPath);
        assertTrue(content.contains("exportDate"));
        assertTrue(content.contains("highScores"));
        assertTrue(content.contains("Words Challenge with 50 words"));
    }

    @Test
    void testScoreRecordMethods() {
        HighScoresManager.ScoreRecord record = new HighScoresManager.ScoreRecord();

        assertEquals(0.0, record.getHighestWPM());
        assertEquals(0.0, record.getAverageWPM());
        assertEquals(0, record.getAttempts());

        record.setHighestWPM(95.5);
        assertEquals(95.5, record.getHighestWPM());

        record.setAverageWPM(87.3);
        assertEquals(87.3, record.getAverageWPM());

        record.setAttempts(5);
        assertEquals(5, record.getAttempts());

        record.incrementAttempts();
        assertEquals(6, record.getAttempts());
    }
}