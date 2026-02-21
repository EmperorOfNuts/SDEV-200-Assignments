package com.typinggame.challenges;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChallengeTest {

    private TestChallenge challenge;

    @BeforeEach
    void setUp() {
        challenge = new TestChallenge();
        List<String> dictionary = Arrays.asList("hello", "world", "java", "testing", "code");
        challenge.setDictionary(new ArrayList<>(dictionary));
    }

    @Test
    void testGenerateChallengeText_WithValidCount() {
        String result = challenge.generateChallengeText(3);
        String[] words = result.split(" ");
        assertEquals(3, words.length);

        // Verify all words are from dictionary
        for (String word : words) assertTrue(challenge.getDictionary().contains(word));
    }

    @Test
    void testGenerateChallengeText_CountGreaterThanDictionarySize() {
        String result = challenge.generateChallengeText(10);
        String[] words = result.split(" ");
        assertEquals(5, words.length); // Should use dictionary.size()
    }

    @Test
    void testCalculateWPM_WithValidInput() {
        double wpm = challenge.calculateWPM(50, 1.0); // 50 chars in 1 minute
        assertEquals(10.0, wpm, 0.001); // 50/5 = 10 WPM
    }

    @Test
    void testCalculateWPM_WithZeroTime() {
        double wpm = challenge.calculateWPM(50, 0);
        assertTrue(Double.isInfinite(wpm) || wpm > 1000);
    }

    @Test
    void testSetAndGetMethods() {
        challenge.setTimeLimit(120.0);
        assertEquals(120.0, challenge.getTimeLimit());

        challenge.setHighestWPM(85.5);
        assertEquals(85.5, challenge.getHighestWPM());

        challenge.setAverageWPM(72.3);
        assertEquals(72.3, challenge.getAverageWPM());

        challenge.setCount(5);
        assertEquals(5, challenge.getCount());

        challenge.setChallengeText("Test text");
        assertEquals("Test text", challenge.getChallengeText());

        challenge.setChallengeType("Test");
        assertEquals("Test", challenge.getChallengeType());
    }

    // Concrete implementation for testing
    private static class TestChallenge extends Challenge {
        @Override
        protected void loadDefaultText() { dictionary = new ArrayList<>(); }

        @Override
        public void configureChallenge(com.typinggame.filemanagement.Settings settings) {}
    }
}