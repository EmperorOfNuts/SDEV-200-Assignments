package com.typinggame.filemanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class SettingsTest {

    private Settings settings;
    private Path testConfigPath;

    @BeforeEach
    void setUp() throws IOException {
        // Create temporary test directory
        testConfigPath = Paths.get("test-config");
        Files.createDirectories(testConfigPath);

        settings = new Settings(testConfigPath.toString() + "/test-settings.json");
    }

    @Test
    void testDefaultValues() {
        assertEquals(50, settings.getWordCount());
        assertEquals(60.0, settings.getWordTimeLimit());
        assertEquals(10, settings.getSentenceCount());
        assertEquals(60.0, settings.getSentenceTimeLimit());
        assertEquals("dark", settings.getTheme());
        assertEquals(26, settings.getFontSize());
        assertFalse(settings.isUseCustomParagraphs());
    }

    @Test
    void testSetAndGetMethods() {
        settings.setWordCount(75);
        assertEquals(75, settings.getWordCount());

        settings.setWordTimeLimit(90.5);
        assertEquals(90.5, settings.getWordTimeLimit());

        settings.setSentenceCount(15);
        assertEquals(15, settings.getSentenceCount());

        settings.setSentenceTimeLimit(120.0);
        assertEquals(120.0, settings.getSentenceTimeLimit());

        settings.setUseCustomParagraphs(true);
        assertTrue(settings.isUseCustomParagraphs());

        settings.setCustomParagraphFile("custom.txt");
        assertEquals("custom.txt", settings.getCustomParagraphFile());

        settings.setParagraphTimeLimit(300.0);
        assertEquals(300.0, settings.getParagraphTimeLimit());

        settings.setTheme("light");
        assertEquals("light", settings.getTheme());

        settings.setFontSize(18);
        assertEquals(18, settings.getFontSize());
    }

    @Test
    void testSaveAndLoad() throws IOException {
        // Modify settings
        settings.setWordCount(100);
        settings.setTheme("light");
        settings.setUseCustomParagraphs(true);
        settings.setCustomParagraphFile("data/custom.txt");

        // Save settings
        settings.save();
        assertTrue(Files.exists(Paths.get(testConfigPath.toString() + "/test-settings.json")));

        // Create new settings instance and load
        Settings loadedSettings = new Settings(testConfigPath.toString() + "/test-settings.json");
        loadedSettings.load();

        // Verify loaded values
        assertEquals(100, loadedSettings.getWordCount());
        assertEquals("light", loadedSettings.getTheme());
        assertTrue(loadedSettings.isUseCustomParagraphs());
        assertEquals("data/custom.txt", loadedSettings.getCustomParagraphFile());
    }

    @Test
    void testCreateDefaultIfNotExists() throws IOException {
        Path settingsFile = Paths.get(testConfigPath.toString() + "/new-settings.json");
        assertFalse(Files.exists(settingsFile));

        Settings newSettings = new Settings(settingsFile.toString());
        newSettings.createDefaultIfNotExists();

        assertTrue(Files.exists(settingsFile));
    }
}