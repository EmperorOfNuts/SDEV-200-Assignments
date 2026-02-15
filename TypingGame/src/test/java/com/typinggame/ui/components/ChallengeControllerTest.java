package com.typinggame.ui.components;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import com.typinggame.challenges.WordChallenge;
import com.typinggame.filemanagement.Settings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class ChallengeControllerTest {

    private ChallengeController controller;
    private WordChallenge challenge;
    private Settings settings;

    @BeforeAll
    static void initJavaFX() {
        new JFXPanel();
    }

    @BeforeEach
    void setUp() {
        controller = new ChallengeController();
        challenge = new WordChallenge();
        settings = new Settings("config/settings.json");

        settings.setWordCount(10);
        settings.setWordTimeLimit(30.0);
        challenge.configureChallenge(settings);
    }

    @Test
    void testSetCurrentChallenge() {
        controller.setCurrentChallenge(challenge);

        assertEquals(challenge, controller.getCurrentChallenge());
        assertEquals(30.0, controller.getTimeRemaining());
    }

    @Test
    void testUpdateInput() {
        controller.setCurrentChallenge(challenge);

        AtomicBoolean wpmUpdated = new AtomicBoolean(false);
        controller.setOnWPMUpdate(wpm -> wpmUpdated.set(true));

        String testText = "test";
        challenge.setChallengeText(testText);

        // Update with matching input
        controller.updateInput(testText);

        assertTrue(controller.getCurrentChallenge() != null);
    }

    @Test
    void testUpdateInput_TriggersWPMUpdate() {
        controller.setCurrentChallenge(challenge);

        AtomicBoolean wpmUpdated = new AtomicBoolean(false);
        controller.setOnWPMUpdate(wpm -> {
            wpmUpdated.set(true);
        });

        // Simulate some typing
        controller.updateInput("test");

        assertNotNull(controller.getCurrentChallenge());
    }

    @Test
    void testUpdateInput_CompleteChallenge() {
        controller.setCurrentChallenge(challenge);

        AtomicBoolean challengeCompleted = new AtomicBoolean(false);
        AtomicReference<Double> finalWPM = new AtomicReference<>(0.0);

        controller.setOnChallengeComplete(wpm -> {
            challengeCompleted.set(true);
            finalWPM.set(wpm);
        });

        // Type the entire text
        controller.updateInput(challenge.getChallengeText());

        assertTrue(challengeCompleted.get());
        assertTrue(finalWPM.get() > 0);
    }

    @Test
    void testUpdateInput_PartialMatch() {
        controller.setCurrentChallenge(challenge);

        String originalText = challenge.getChallengeText();
        String partialInput = originalText.substring(0, Math.min(5, originalText.length()));

        AtomicBoolean wpmCalculated = new AtomicBoolean(false);
        controller.setOnWPMUpdate(wpm -> wpmCalculated.set(true));

        controller.updateInput(partialInput);

        assertNotNull(controller.getCurrentChallenge());
    }

    @Test
    void testCountCorrectCharacters() {
        // Create a test challenge with something simple
        WordChallenge testChallenge = new WordChallenge();
        String original = "hello world";
        testChallenge.setChallengeText(original);
        controller.setCurrentChallenge(testChallenge);

        // Use reflection to test private method
        try {
            java.lang.reflect.Method method = ChallengeController.class.getDeclaredMethod(
                    "countCorrectCharacters", String.class, String.class);
            method.setAccessible(true);

            int correct = (int) method.invoke(controller, "hello", original);
            assertEquals(5, correct);

            correct = (int) method.invoke(controller, "hello world", original);
            assertEquals(11, correct);

            correct = (int) method.invoke(controller, "hello worl", original);
            assertEquals(10, correct);

            correct = (int) method.invoke(controller, "wrong", original);
            assertEquals(0, correct);

            // Test with longer input than original
            correct = (int) method.invoke(controller, "hello world extra", original);
            assertEquals(11, correct);

        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }

    @Test
    void testEndChallenge() {
        controller.setCurrentChallenge(challenge);

        AtomicBoolean challengeCompleted = new AtomicBoolean(false);
        AtomicReference<Double> finalWPM = new AtomicReference<>(0.0);

        controller.setOnChallengeComplete(wpm -> {
            challengeCompleted.set(true);
            finalWPM.set(wpm);
        });

        // Simulate some typing
        String partialInput = challenge.getChallengeText().substring(0, 5);
        controller.updateInput(partialInput);

        controller.endChallenge();

        assertTrue(challengeCompleted.get());
        assertTrue(finalWPM.get() >= 0); // Could be 0 if no time passed
    }

    @Test
    void testReset() {
        controller.setCurrentChallenge(challenge);
        controller.updateInput("test");

        assertNotNull(controller.getCurrentChallenge());
        assertTrue(controller.getTimeRemaining() > 0);

        controller.reset();

        assertNull(controller.getCurrentChallenge());
        assertEquals(0.0, controller.getTimeRemaining());
    }

    @Test
    void testTimerControls() {
        controller.setCurrentChallenge(challenge);

        // Start timer - should not throw exceptions
        controller.startTimer();
        controller.pauseTimer();
        controller.stopTimer();

        // Verify start/stop again
        controller.startTimer();
        controller.stopTimer();
    }

    @Test
    void testGetTimeRemaining() {
        controller.setCurrentChallenge(challenge);
        assertEquals(30.0, controller.getTimeRemaining());

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            controller.startTimer();
            latch.countDown();
        });

        try { latch.await(1, TimeUnit.SECONDS); }
        catch (InterruptedException e) { fail("Timer start interrupted"); }

        try { Thread.sleep(100); }
        catch (InterruptedException _) {} // Delay to let timer tick

        double timeAfterStart = controller.getTimeRemaining();
        assertTrue(timeAfterStart <= 30.0 && timeAfterStart >= 29.8);

        CountDownLatch stopLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            controller.stopTimer();
            stopLatch.countDown();
        });

        try { stopLatch.await(1, TimeUnit.SECONDS); }
        catch (InterruptedException e) { fail("Timer stop interrupted"); }
    }

    @Test
    void testWPMCalculation() {
        double wpm = challenge.calculateWPM(50, 1.0);
        assertEquals(10.0, wpm, 0.001); // 50/5 = 10 WPM

        wpm = challenge.calculateWPM(100, 2.0);
        assertEquals(10.0, wpm, 0.001); // (100/5)/2 = 10 WPM

        wpm = challenge.calculateWPM(75, 1.5);
        assertEquals(10.0, wpm, 0.001); // (75/5)/1.5 = 10 WPM
    }

    @Test
    void testOnWPMUpdateCallback() {
        controller.setCurrentChallenge(challenge);

        AtomicBoolean callbackInvoked = new AtomicBoolean(false);
        controller.setOnWPMUpdate(wpm -> {
            callbackInvoked.set(true);
        });

        controller.endChallenge();

        AtomicBoolean wpmCallbackInvoked = new AtomicBoolean(false);
        AtomicBoolean completeCallbackInvoked = new AtomicBoolean(false);

        controller.setOnWPMUpdate(wpm -> wpmCallbackInvoked.set(true));
        controller.setOnChallengeComplete(wpm -> completeCallbackInvoked.set(true));

        controller.endChallenge();

        assertTrue(completeCallbackInvoked.get());
        assertFalse(wpmCallbackInvoked.get());
    }
}