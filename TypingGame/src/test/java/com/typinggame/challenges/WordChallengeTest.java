package com.typinggame.challenges;

import com.typinggame.filemanagement.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WordChallengeTest {

    private WordChallenge wordChallenge;
    private Settings settings;

    @BeforeEach
    void setUp() {
        wordChallenge = new WordChallenge();
        settings = new Settings("config/settings.json");
        settings.setWordCount(25);
        settings.setWordTimeLimit(45.0);
    }

    @Test
    void testConstructor() {
        assertEquals("Words", wordChallenge.getChallengeType());
        assertEquals(50, wordChallenge.getCount()); // Default count
        assertNotNull(wordChallenge.getDictionary());
    }

    @Test
    void testConfigureChallenge() {
        wordChallenge.configureChallenge(settings);

        assertEquals(25, wordChallenge.getCount());
        assertEquals(45.0, wordChallenge.getTimeLimit());
        assertNotNull(wordChallenge.getChallengeText());
        assertFalse(wordChallenge.getChallengeText().isEmpty());
    }

    @Test
    void testLoadDefaultText_FileExists() {
        assertNotNull(wordChallenge.getDictionary());
        assertFalse(wordChallenge.getDictionary().isEmpty());
    }

    @Test
    void testGenerateChallengeText_WordCount() {
        wordChallenge.configureChallenge(settings);
        String text = wordChallenge.getChallengeText();
        String[] words = text.split(" ");
        assertEquals(25, words.length);
    }
}