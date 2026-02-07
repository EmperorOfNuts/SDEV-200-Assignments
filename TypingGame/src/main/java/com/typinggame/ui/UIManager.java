package com.typinggame.ui;

import com.typinggame.filemanagement.Settings;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Pane;
import java.net.URL;

public abstract class UIManager {
    protected Settings settings;

    public UIManager(Settings settings) {
        this.settings = settings;
    }

    public abstract Pane createUI();

    public void applyTheme(Scene scene) {
        String theme = settings.getTheme();

        scene.getRoot().getStylesheets().clear();

        // Load CSS
        String cssFile;
        if ("dark".equals(theme)) {
            cssFile = "/dark-theme.css";
        } else {
            cssFile = "/light-theme.css";
        }

        URL cssUrl = getClass().getResource(cssFile);
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("CSS file not found: " + cssFile);
        }

        applyFontSize(scene);
    }

    public void applyInitialFontSize(Pane rootPane) {
        int fontSize = settings.getFontSize();
        String fontSizeStyle = String.format("-fx-font-size: %dpx;", fontSize);

        rootPane.setStyle(fontSizeStyle);
        rootPane.applyCss();
    }

    public void applyFontSize(Scene scene) {
        int fontSize = settings.getFontSize();
        String fontSizeStyle = String.format("-fx-font-size: %dpx;", fontSize);

        scene.getRoot().lookupAll(".text-area").forEach(node -> {
            if (node instanceof javafx.scene.control.TextArea) {
                ((javafx.scene.control.TextArea) node).setStyle(fontSizeStyle);
            }
        });
    }

    public void applyThemeToDialog(DialogPane dialogPane) {
        String theme = settings.getTheme();
        String cssFile = "/" + theme + "-theme.css";
        URL cssUrl = getClass().getResource(cssFile);
        if (cssUrl != null) {
            dialogPane.getStylesheets().clear();
            dialogPane.getStylesheets().add(cssUrl.toExternalForm());
        }
    }

    public Alert createThemedAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        applyThemeToDialog(alert.getDialogPane());
        return alert;
    }

    public Alert createThemedAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        applyThemeToDialog(alert.getDialogPane());
        return alert;
    }

    public void changeFontSize(int delta) {
        int newSize = settings.getFontSize() + delta;
        if (newSize >= 8 && newSize <= 32) {
            settings.setFontSize(newSize);
            try {
                settings.save();
            } catch (java.io.IOException e) {
                System.err.println("Failed to save font size: " + e.getMessage());
            }
        }
    }
}