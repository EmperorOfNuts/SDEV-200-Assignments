package com.typinggame.challenges;

import com.typinggame.filemanagement.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParagraphChallengeTest {

    private ParagraphChallenge paragraphChallenge;
    private Settings settings;

    @BeforeEach
    void setUp() {
        paragraphChallenge = new ParagraphChallenge();
        settings = new Settings("config/settings.json");
        settings.setUseCustomParagraphs(false);
        settings.setParagraphTimeLimit(120.0);
    }

    @Test
    void testConstructor() {
        assertEquals("Paragraph", paragraphChallenge.getChallengeType());
        assertEquals(1, paragraphChallenge.getCount()); // Default count
        assertNotNull(paragraphChallenge.getDictionary());
    }

    @Test
    void testConfigureChallenge_DefaultParagraphs() {
        paragraphChallenge.configureChallenge(settings);

        assertEquals(120.0, paragraphChallenge.getTimeLimit());
        assertNotNull(paragraphChallenge.getChallengeText());
    }

    @Test
    void testSetCustomParagraphFile() throws IOException {
        // Create a temporary custom paragraphs file with some random text
        Path tempFile = Files.createTempFile("custom-paragraphs", ".txt");
        List<String> customParagraphs = List.of(
                "This is the first custom paragraph for testing.",
                "This is the second custom paragraph with more content.",
                "And here is a third paragraph to complete the test."
        );
        Files.write(tempFile, customParagraphs);

        paragraphChallenge.setUseCustomParagraphs(true);
        paragraphChallenge.setCustomParagraphFile(tempFile.toString());

        assertEquals(tempFile.toString(), paragraphChallenge.getCustomParagraphFile());
        assertEquals(customParagraphs, paragraphChallenge.getDictionary());

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testGenerateChallengeText_WithCustomParagraphs() throws IOException {
        Path tempFile = Files.createTempFile("custom-paragraphs", ".txt");
        List<String> customParagraphs = List.of(
                "This is a test paragraph for the typing challenge."
        );
        Files.write(tempFile, customParagraphs);

        settings.setUseCustomParagraphs(true);
        settings.setCustomParagraphFile(tempFile.toString());
        settings.setParagraphTimeLimit(180.0);

        paragraphChallenge.configureChallenge(settings);

        assertEquals(180.0, paragraphChallenge.getTimeLimit());
        assertEquals(customParagraphs.get(0), paragraphChallenge.getChallengeText());

        Files.deleteIfExists(tempFile);
    }
}