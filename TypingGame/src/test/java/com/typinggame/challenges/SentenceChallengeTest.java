package com.typinggame.challenges;

import com.typinggame.filemanagement.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SentenceChallengeTest {

    private SentenceChallenge sentenceChallenge;
    private Settings settings;

    @BeforeEach
    void setUp() {
        sentenceChallenge = new SentenceChallenge();
        settings = new Settings("config/settings.json");
        settings.setSentenceCount(5);
        settings.setSentenceTimeLimit(90.0);
    }

    @Test
    void testConstructor() {
        assertEquals("Sentences", sentenceChallenge.getChallengeType());
        assertEquals(10, sentenceChallenge.getCount()); // Default count
        assertNotNull(sentenceChallenge.getDictionary());
    }

    @Test
    void testConfigureChallenge() {
        sentenceChallenge.configureChallenge(settings);

        assertEquals(5, sentenceChallenge.getCount());
        assertEquals(90.0, sentenceChallenge.getTimeLimit());
        assertNotNull(sentenceChallenge.getChallengeText());
    }

    @Test
    void testGenerateChallengeText_SentenceCount() {
        sentenceChallenge.configureChallenge(settings);
        String text = sentenceChallenge.getChallengeText();
        String[] sentences = text.split("\\. ");
        assertEquals(5, sentences.length);
    }
}