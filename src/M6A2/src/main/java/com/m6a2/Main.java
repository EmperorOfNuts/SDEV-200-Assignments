package M6A2.src.main.java.com.m6a2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.sql.*;

public class Main extends Application {
    private final Label labelStatus = new Label("No connection to database");
    private final TextArea textAreaLog = new TextArea();

    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        VBox mainPane = new VBox(10);
        mainPane.setPadding(new Insets(20));
        mainPane.setAlignment(Pos.TOP_CENTER);

        Label lblTitle = new Label("Batch Update Performance Test");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        labelStatus.setWrapText(true);
        labelStatus.setStyle("-fx-text-fill: blue;");

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button btnConnect = new Button("Connect to Database");
        Button btnBatch = new Button("Test Batch Update");
        Button btnNonBatch = new Button("Test Non-Batch Update");
        Button btnClear = new Button("Clear Table");

        btnConnect.setOnAction(e -> showConnectionDialog(primaryStage));
        btnBatch.setOnAction(e -> testBatchUpdate());
        btnNonBatch.setOnAction(e -> testNonBatchUpdate());
        btnClear.setOnAction(e -> clearTable());

        btnConnect.setPrefWidth(150);
        btnBatch.setPrefWidth(150);
        btnNonBatch.setPrefWidth(150);
        btnClear.setPrefWidth(150);

        buttonBox.getChildren().addAll(btnConnect, btnBatch, btnNonBatch, btnClear);

        // Log
        textAreaLog.setPrefHeight(300);
        textAreaLog.setEditable(false);
        textAreaLog.setStyle("-fx-font-family: 'Courier New';");

        mainPane.getChildren().addAll(lblTitle, labelStatus, buttonBox, textAreaLog);

        // Stage
        Scene scene = new Scene(mainPane, 700, 500);
        primaryStage.setTitle("Exercise35_01 - Batch Update Comparison");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showConnectionDialog(Stage owner) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(owner);
        dialog.setTitle("Database Connection");

        DBConnectionPane connectionPane = new DBConnectionPane();

        // Monitor connection status
        connectionPane.setOnMouseClicked(e -> {
            Connection conn = connectionPane.getConnection();
            if (conn != null) {
                this.connection = conn;
                labelStatus.setText("Connected to M6A2 database");
                dialog.close();
            }
        });

        Scene scene = new Scene(connectionPane, 450, 280);
        dialog.setScene(scene);
        dialog.showAndWait();

        // Check connection status
        if (connectionPane.getConnection() != null) {
            this.connection = connectionPane.getConnection();
            labelStatus.setText("Connected to M6A2 database");
        }
    }

    private void testBatchUpdate() {
        if (checkConnection()) return;

        textAreaLog.appendText("\n--- Testing Batch Update ---\n");

        try {
            connection.setAutoCommit(false);

            String sql = "INSERT INTO Temp (num1, num2, num3) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);

            long startTime = System.currentTimeMillis();

            // Insert 1000 records in batches of 100
            int batchSize = 100;
            for (int i = 1; i <= 1000; i++) {
                pstmt.setDouble(1, Math.random() * 1000);
                pstmt.setDouble(2, Math.random() * 1000);
                pstmt.setDouble(3, Math.random() * 1000);
                pstmt.addBatch();

                if (i % batchSize == 0) pstmt.executeBatch();
            }

            // Commit changes
            pstmt.executeBatch();
            connection.commit();

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            textAreaLog.appendText("Batch update completed\n");
            textAreaLog.appendText("The elapsed time is " + elapsedTime + " ms\n");
            System.out.println("Batch update completed");
            System.out.println("The elapsed time is " + elapsedTime + " ms");

            connection.setAutoCommit(true);

        } catch (SQLException ex) {
            textAreaLog.appendText("Error: " + ex.getMessage() + "\n");
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void testNonBatchUpdate() {
        if (checkConnection()) return;

        textAreaLog.appendText("\n--- Testing Non-Batch Update ---\n");

        try {
            String sql = "INSERT INTO Temp (num1, num2, num3) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);

            long startTime = System.currentTimeMillis();

            for (int i = 1; i <= 1000; i++) {
                pstmt.setDouble(1, Math.random() * 1000);
                pstmt.setDouble(2, Math.random() * 1000);
                pstmt.setDouble(3, Math.random() * 1000);
                pstmt.executeUpdate();
            }

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            textAreaLog.appendText("Non-Batch update completed\n");
            textAreaLog.appendText("The elapsed time is " + elapsedTime + " ms\n");
            System.out.println("Non-Batch update completed");
            System.out.println("The elapsed time is " + elapsedTime + " ms");

        } catch (SQLException ex) {
            textAreaLog.appendText("Error: " + ex.getMessage() + "\n");
        }
    }

    private void clearTable() {
        if (checkConnection()) return;

        try {
            Statement stmt = connection.createStatement();
            int count = stmt.executeUpdate("DELETE FROM Temp");
            textAreaLog.appendText("\nCleared " + count + " records from Temp table\n");
        } catch (SQLException ex) {
            textAreaLog.appendText("Error clearing table: " + ex.getMessage() + "\n");
        }
    }

    private boolean checkConnection() {
        if (connection == null) {
            textAreaLog.appendText("Please connect to database first\n");
            return true;
        }
        return false;
    }

    @Override
    public void stop() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) { launch(args); }
}