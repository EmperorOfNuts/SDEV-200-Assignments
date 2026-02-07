package com.typinggame.ui.components;

import com.typinggame.filemanagement.Settings;
import com.typinggame.ui.ThemeUtils;
import javafx.scene.control.*;
import javafx.scene.Scene;
import java.io.IOException;

public class MenuBarManager {
    private final Settings settings;
    private final Runnable onSettingsOpen;
    private final Runnable onThemeChange;

    public MenuBarManager(Settings settings, Runnable onSettingsOpen, Runnable onThemeChange) {
        this.settings = settings;
        this.onSettingsOpen = onSettingsOpen;
        this.onThemeChange = onThemeChange; // NEW
    }

    public MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // File Menu
        Menu fileMenu = new Menu("File");
        MenuItem settingsItem = new MenuItem("Settings");
        MenuItem exitItem = new MenuItem("Exit");

        settingsItem.setOnAction(e -> onSettingsOpen.run());
        exitItem.setOnAction(e -> System.exit(0));

        fileMenu.getItems().addAll(settingsItem, new SeparatorMenuItem(), exitItem);

        // View Menu
        Menu viewMenu = new Menu("View");
        MenuItem themeItem = new MenuItem("Toggle Theme");
        MenuItem fontSizePlusItem = new MenuItem("Increase Font Size");
        MenuItem fontSizeMinusItem = new MenuItem("Decrease Font Size");

        themeItem.setOnAction(e -> toggleTheme());
        fontSizePlusItem.setOnAction(e -> changeFontSize(2));
        fontSizeMinusItem.setOnAction(e -> changeFontSize(-2));

        viewMenu.getItems().addAll(themeItem, new SeparatorMenuItem(), fontSizePlusItem, fontSizeMinusItem);

        menuBar.getMenus().addAll(fileMenu, viewMenu);
        return menuBar;
    }

    private void toggleTheme() {
        String newTheme = "dark".equals(settings.getTheme()) ? "light" : "dark";
        settings.setTheme(newTheme);
        try {
            settings.save();
            if (onThemeChange != null) { onThemeChange.run(); }
        } catch (IOException e) {
            System.err.println("Failed to save theme: " + e.getMessage());
        }
    }

    private void changeFontSize(int delta) {
        try {
            ThemeUtils.changeFontSize(settings, delta);
            if (onThemeChange != null) { onThemeChange.run(); }
        } catch (IOException e) {
            System.err.println("Failed to change font size: " + e.getMessage());
        }
    }
}