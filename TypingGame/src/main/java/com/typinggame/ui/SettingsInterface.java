package com.typinggame.ui;

import com.typinggame.filemanagement.Settings;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import java.net.URL;
import java.io.File;
import java.io.IOException;


public class SettingsInterface {
    private final Settings settings;

    // Settings variables
    private Spinner<Integer> wordCountSpinner;
    private Spinner<Double> wordTimeSpinner;
    private Spinner<Integer> sentenceCountSpinner;
    private Spinner<Double> sentenceTimeSpinner;
    private CheckBox useCustomParagraphs;
    private TextField paragraphFileField;
    private Spinner<Double> paragraphTimeSpinner;
    private ComboBox<String> themeCombo;

    public SettingsInterface(Settings settings) {
        this.settings = settings;
    }

    public void showDialog(Scene parentScene) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Settings");
        dialog.setHeaderText("Configure Typing Challenge Settings");

        DialogPane dialogPane = dialog.getDialogPane();

        if (parentScene != null && parentScene.getUserData() instanceof UIManager) {
            UIManager uiManager = (UIManager) parentScene.getUserData();
            uiManager.applyThemeToDialog(dialogPane);
        } else {
            String theme = settings.getTheme();
            String cssFile = "/" + theme + "-theme.css";
            URL cssUrl = getClass().getResource(cssFile);
            if (cssUrl != null) {
                dialogPane.getStylesheets().clear();
                dialogPane.getStylesheets().add(cssUrl.toExternalForm());
            }
        }

        if (parentScene != null) {
            dialogPane.getStylesheets().addAll(parentScene.getRoot().getStylesheets());
        }

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Word Challenge Tab
        Tab wordTab = new Tab("Word Challenge");
        wordTab.setClosable(false);
        wordTab.setContent(createWordTabContent(dialog));

        // Sentence Challenge Tab
        Tab sentenceTab = new Tab("Sentence Challenge");
        sentenceTab.setClosable(false);
        sentenceTab.setContent(createSentenceTabContent());

        // Paragraph Challenge Tab
        Tab paragraphTab = new Tab("Paragraph Challenge");
        paragraphTab.setClosable(false);
        paragraphTab.setContent(createParagraphTabContent(dialog));

        // UI Settings Tab
        Tab uiTab = new Tab("UI Settings");
        uiTab.setClosable(false);
        uiTab.setContent(createUITabContent());

        tabPane.getTabs().addAll(wordTab, sentenceTab, paragraphTab, uiTab);

        dialogPane.setContent(tabPane);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialogPane.setPrefSize(700, 600);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                saveSettings();
                try {
                    settings.save();
                    showInfoDialog(dialogPane);
                } catch (IOException e) {
                    showErrorDialog("Failed to save settings: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private ScrollPane createWordTabContent(Dialog<Void> dialog) {
        GridPane wordGrid = new GridPane();
        wordGrid.setHgap(10);
        wordGrid.setVgap(10);
        wordGrid.setPadding(new Insets(20));

        Label wordSettingsLabel = new Label("Word Challenge Settings:");
        wordSettingsLabel.getStyleClass().add("settings-section");

        Label wordCountLabel = new Label("Word Count:");
        wordCountSpinner = new Spinner<>(10, 200, settings.getWordCount(), 10);
        wordCountSpinner.setEditable(true);
        wordCountSpinner.setTooltip(new Tooltip("Number of words in word challenges (10-200)"));

        Label wordTimeLabel = new Label("Time Limit (seconds):");
        wordTimeSpinner = new Spinner<>(15.0, 300.0, settings.getWordTimeLimit(), 15.0);
        wordTimeSpinner.setEditable(true);
        wordTimeSpinner.setTooltip(new Tooltip("Time limit for word challenges in seconds"));

        // Add Settings to Grid
        wordGrid.add(wordSettingsLabel, 0, 0, 2, 1);
        wordGrid.add(wordCountLabel, 0, 1);
        wordGrid.add(wordCountSpinner, 1, 1);
        wordGrid.add(wordTimeLabel, 0, 2);
        wordGrid.add(wordTimeSpinner, 1, 2);

        return new ScrollPane(wordGrid);
    }

    private ScrollPane createSentenceTabContent() {
        GridPane sentenceGrid = new GridPane();
        sentenceGrid.setHgap(10);
        sentenceGrid.setVgap(10);
        sentenceGrid.setPadding(new Insets(20));

        Label sentenceSettingsLabel = new Label("Sentence Challenge Settings:");
        sentenceSettingsLabel.getStyleClass().add("settings-section");

        Label sentenceCountLabel = new Label("Number of Sentences:");
        sentenceCountSpinner = new Spinner<>(1, 20, settings.getSentenceCount(), 1);
        sentenceCountSpinner.setEditable(true);
        sentenceCountSpinner.setTooltip(new Tooltip("Number of sentences in sentence challenges (1-20)"));

        Label sentenceTimeLabel = new Label("Time Limit (seconds):");
        sentenceTimeSpinner = new Spinner<>(30.0, 600.0, settings.getSentenceTimeLimit(), 30.0);
        sentenceTimeSpinner.setEditable(true);
        sentenceTimeSpinner.setTooltip(new Tooltip("Time limit for sentence challenges in seconds"));

        // Add Settings to Grid
        sentenceGrid.add(sentenceSettingsLabel, 0, 0, 2, 1);
        sentenceGrid.add(sentenceCountLabel, 0, 1);
        sentenceGrid.add(sentenceCountSpinner, 1, 1);
        sentenceGrid.add(sentenceTimeLabel, 0, 2);
        sentenceGrid.add(sentenceTimeSpinner, 1, 2);

        return new ScrollPane(sentenceGrid);
    }

    private ScrollPane createParagraphTabContent(Dialog<Void> dialog) {
        GridPane paragraphGrid = new GridPane();
        paragraphGrid.setHgap(10);
        paragraphGrid.setVgap(10);
        paragraphGrid.setPadding(new Insets(20));

        Label paragraphSettingsLabel = new Label("Paragraph Challenge Settings:");
        paragraphSettingsLabel.getStyleClass().add("settings-section");

        useCustomParagraphs = new CheckBox("Use Custom Paragraphs");
        useCustomParagraphs.setSelected(settings.isUseCustomParagraphs());
        useCustomParagraphs.setTooltip(new Tooltip("Use custom paragraph file instead of built-in paragraphs"));

        Label paragraphFileLabel = new Label("Custom Paragraph File:");
        paragraphFileField = new TextField(settings.getCustomParagraphFile());
        paragraphFileField.setTooltip(new Tooltip("Path to custom paragraph file (txt), make sure each paragraph is on its own line."));

        // Enable/disable custom paragraphs
        useCustomParagraphs.selectedProperty().addListener((obs, oldVal, newVal) -> { paragraphFileField.setDisable(!newVal);});

        Button browseButton = new Button("Browse...");
        browseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Paragraph File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );
            File selectedFile = fileChooser.showOpenDialog(dialog.getOwner());
            if (selectedFile != null) {
                paragraphFileField.setText(selectedFile.getAbsolutePath());
            }
        });

        Label paragraphTimeLabel = new Label("Time Limit (seconds):");
        paragraphTimeSpinner = new Spinner<>(60.0, 900.0, settings.getParagraphTimeLimit(), 30.0);
        paragraphTimeSpinner.setEditable(true);
        paragraphTimeSpinner.setTooltip(new Tooltip("Time limit for paragraph challenges in seconds"));

        // Add Settings to Grid
        paragraphGrid.add(paragraphSettingsLabel, 0, 0, 3, 1);
        paragraphGrid.add(useCustomParagraphs, 0, 1, 3, 1);
        paragraphGrid.add(paragraphFileLabel, 0, 2);
        paragraphGrid.add(paragraphFileField, 1, 2);
        paragraphGrid.add(browseButton, 2, 2);
        paragraphGrid.add(paragraphTimeLabel, 0, 3);
        paragraphGrid.add(paragraphTimeSpinner, 1, 3);

        return new ScrollPane(paragraphGrid);
    }

    private ScrollPane createUITabContent() {
        GridPane uiGrid = new GridPane();
        uiGrid.setHgap(10);
        uiGrid.setVgap(10);
        uiGrid.setPadding(new Insets(20));

        Label uiSettingsLabel = new Label("User Interface Settings:");
        uiSettingsLabel.getStyleClass().add("settings-section");

        Label themeLabel = new Label("Theme (requires restart):");
        themeCombo = new ComboBox<>();
        themeCombo.getItems().addAll("dark", "light");
        themeCombo.setValue(settings.getTheme());
        themeCombo.setTooltip(new Tooltip("Application color theme (Requires restart)"));

        // Add Settings to Grid
        uiGrid.add(uiSettingsLabel, 0, 0, 2, 1);
        uiGrid.add(themeLabel, 0, 1);
        uiGrid.add(themeCombo, 1, 1);

        return new ScrollPane(uiGrid);
    }

    private void saveSettings() {
        settings.setWordCount(wordCountSpinner.getValue());
        settings.setWordTimeLimit(wordTimeSpinner.getValue());

        settings.setSentenceCount(sentenceCountSpinner.getValue());
        settings.setSentenceTimeLimit(sentenceTimeSpinner.getValue());

        settings.setUseCustomParagraphs(useCustomParagraphs.isSelected());
        settings.setCustomParagraphFile(paragraphFileField.getText());
        settings.setParagraphTimeLimit(paragraphTimeSpinner.getValue());

        settings.setTheme(themeCombo.getValue());
    }

    private void showInfoDialog(DialogPane dialogPane) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Settings Saved");
        alert.setHeaderText(null);
        alert.setContentText("All settings have been saved successfully.");

        DialogPane alertDialogPane = alert.getDialogPane();
        alertDialogPane.getStylesheets().addAll(dialogPane.getStylesheets());

        alert.showAndWait();
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Save Error");
        alert.setContentText(message);

        alert.showAndWait();
    }
}