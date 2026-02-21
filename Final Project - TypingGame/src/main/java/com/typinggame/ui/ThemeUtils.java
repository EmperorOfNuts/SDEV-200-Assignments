package com.typinggame.ui;

import com.typinggame.filemanagement.Settings;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.Parent;
import java.net.URL;
import java.io.IOException;

public class ThemeUtils {
    private ThemeUtils() {} // Prevent instantiation

    public static void applyTheme(Settings settings, Scene scene) {
        if (scene == null || scene.getRoot() == null) return;

        String theme = settings.getTheme();

        // Clear ALL stylesheets from ROOT
        scene.getRoot().getStylesheets().clear();

        // Apply the theme CSS
        String cssFile = "/" + theme + "-theme.css";
        URL cssUrl = ThemeUtils.class.getResource(cssFile);

        if (cssUrl != null) {
            String cssExternalForm = cssUrl.toExternalForm();
            scene.getRoot().getStylesheets().add(cssExternalForm);
        } else System.err.println("CSS file not found: " + cssFile);

        applyFontSize(settings, scene);
    }

    public static void applyFontSize(Settings settings, Scene scene) {
        if (scene == null || scene.getRoot() == null) return;

        int fontSize = settings.getFontSize();
        String fontSizeStyle = String.format("-fx-font-size: %dpx;", fontSize);

        scene.getRoot().lookupAll(".text-area").forEach(node -> {
            if (node instanceof TextArea) { node.setStyle(fontSizeStyle); }
        });
    }

    public static void applyThemeToDialog(Settings settings, DialogPane dialogPane) {
        String theme = settings.getTheme();

        // Clear existing stylesheets
        dialogPane.getStylesheets().clear();

        String cssFile = "/" + theme + "-theme.css";
        URL cssUrl = ThemeUtils.class.getResource(cssFile);

        if (cssUrl != null) {
            String cssExternalForm = cssUrl.toExternalForm();
            dialogPane.getStylesheets().add(cssExternalForm);
        }
    }

    public static void changeFontSize(Settings settings, int delta) throws IOException {
        int newSize = settings.getFontSize() + delta;
        if (newSize >= 8 && newSize <= 32) {
            settings.setFontSize(newSize);
            settings.save();
        }
    }
}