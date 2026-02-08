package com.typinggame;

import com.typinggame.filemanagement.Settings;
import com.typinggame.filemanagement.HighScoresManager;
import com.typinggame.ui.MainInterface;
import com.typinggame.ui.ThemeUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    private Settings settings;
    private HighScoresManager highScoresManager;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize Settings
            settings = new Settings("config/settings.json");
            settings.createDefaultIfNotExists();
            settings.load();

            // Initialize HighScoresManager
            highScoresManager = HighScoresManager.getInstance();
            highScoresManager.createDefaultIfNotExists();
            highScoresManager.load();

            // Initialize MainInterface
            MainInterface typingInterface = new MainInterface(settings, highScoresManager);
            Scene scene = new Scene(typingInterface.createUI(), 1600, 900);

            // Set the scene reference in MainInterface
            typingInterface.setScene(scene); // NEW

            // Apply theme AFTER UI is created
            ThemeUtils.applyTheme(settings, scene);

            primaryStage.setTitle("Typing Game - JavaFX");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            showErrorDialog("Failed to load settings or high scores: " + e.getMessage());
        }
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Initialization Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    void main(String[] args) {
        launch(args);
    }
}