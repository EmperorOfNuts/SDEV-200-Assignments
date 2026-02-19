package M6A1.src.main.java.com.m6a1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;

public class Main extends Application {
    private Connection connection;
    private Statement statement;

    // Text fields and a Label
    private final TextField Id = new TextField();
    private final TextField LastName = new TextField();
    private final TextField FirstName = new TextField();
    private final TextField MiddleInitial = new TextField();
    private final TextField Address = new TextField();
    private final TextField City = new TextField();
    private final TextField State = new TextField();
    private final TextField Telephone = new TextField();
    private final TextField Email = new TextField();
    private final Label labelStatus = new Label(" ");

    @Override
    public void start(Stage primaryStage) {
        initializeDB();

        VBox mainPane = new VBox(10);
        mainPane.setPadding(new Insets(20));
        Label lblTitle = new Label("Staff Management System");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Create grid
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        // Add fields
        gridPane.add(new Label("ID:"), 0, 0);
        gridPane.add(Id, 1, 0);

        gridPane.add(new Label("Last Name:"), 0, 1);
        gridPane.add(LastName, 1, 1);

        gridPane.add(new Label("First Name:"), 0, 2);
        gridPane.add(FirstName, 1, 2);

        gridPane.add(new Label("MI:"), 2, 2);
        MiddleInitial.setPrefWidth(50);
        gridPane.add(MiddleInitial, 3, 2);

        gridPane.add(new Label("Address:"), 0, 3);
        gridPane.add(Address, 1, 3, 3, 1);

        gridPane.add(new Label("City:"), 0, 4);
        gridPane.add(City, 1, 4);

        gridPane.add(new Label("State:"), 2, 4);
        State.setPrefWidth(50);
        gridPane.add(State, 3, 4);

        gridPane.add(new Label("Telephone:"), 0, 5);
        gridPane.add(Telephone, 1, 5);

        gridPane.add(new Label("Email:"), 0, 6);
        Email.setPrefWidth(300);
        gridPane.add(Email, 1, 6, 3, 1);

        // Add buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button btnView = new Button("View");
        Button btnInsert = new Button("Insert");
        Button btnUpdate = new Button("Update");
        Button btnClear = new Button("Clear");

        // Set button actions
        btnView.setOnAction(e -> viewRecord());
        btnInsert.setOnAction(e -> insertRecord());
        btnUpdate.setOnAction(e -> updateRecord());
        btnClear.setOnAction(e -> clearForm());

        buttonBox.getChildren().addAll(btnView, btnInsert, btnUpdate, btnClear);
        labelStatus.setStyle("-fx-text-fill: blue;");

        // Set Stage
        mainPane.getChildren().addAll(lblTitle, gridPane, buttonBox, labelStatus);
        Scene scene = new Scene(mainPane, 600, 400);
        primaryStage.setTitle("M6A1 - Staff Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded");

            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/M6A1?useSSL=false&serverTimezone=UTC",
                    "root",
                    "IGotThisFAHH6769!");
            System.out.println("Database connected");

            statement = connection.createStatement();

        }
        catch (Exception ex) {
            labelStatus.setText("Database connection error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void viewRecord() {
        String id = Id.getText().trim();

        if (id.isEmpty()) {
            labelStatus.setText("Please enter an ID to view");
            return;
        }

        try {
            String queryString = "SELECT * FROM Staff WHERE id = '" + id + "'";
            ResultSet resultSet = statement.executeQuery(queryString);

            if (resultSet.next()) {
                LastName.setText(resultSet.getString("lastName"));
                FirstName.setText(resultSet.getString("firstName"));
                MiddleInitial.setText(resultSet.getString("mi"));
                Address.setText(resultSet.getString("address"));
                City.setText(resultSet.getString("city"));
                State.setText(resultSet.getString("state"));
                Telephone.setText(resultSet.getString("telephone"));
                Email.setText(resultSet.getString("email"));
                labelStatus.setText("Record found for ID: " + id);
            }
            else {
                clearForm();
                labelStatus.setText("Record not found for ID: " + id);
            }
        }
        catch (SQLException ex) {
            labelStatus.setText("Error viewing record: " + ex.getMessage());
        }
    }

    private void insertRecord() {
        String id = Id.getText().trim();

        if (id.isEmpty()) {
            labelStatus.setText("ID is required for insertion");
            return;
        }

        try {
            // Check if ID exists
            String checkQuery = "SELECT id FROM Staff WHERE id = '" + id + "'";
            ResultSet resultSet = statement.executeQuery(checkQuery);

            if (resultSet.next()) {
                labelStatus.setText("ID " + id + " already exists. Use Update instead.");
                return;
            }

            // Format insert call
            String insertString = "INSERT INTO Staff VALUES ('" +
                    id + "', '" +
                    LastName.getText().trim() + "', '" +
                    FirstName.getText().trim() + "', '" +
                    MiddleInitial.getText().trim() + "', '" +
                    Address.getText().trim() + "', '" +
                    City.getText().trim() + "', '" +
                    State.getText().trim() + "', '" +
                    Telephone.getText().trim() + "', '" +
                    Email.getText().trim() + "')";

            int result = statement.executeUpdate(insertString);

            if (result > 0) labelStatus.setText("Record inserted successfully for ID: " + id);
            else labelStatus.setText("Insert failed for ID: " + id);
        }
        catch (SQLException ex) {
            labelStatus.setText("Error inserting record: " + ex.getMessage());
        }
    }

    private void updateRecord() {
        String id = Id.getText().trim();

        if (id.isEmpty()) {
            labelStatus.setText("Please enter an ID to update");
            return;
        }

        try {
            // Check if ID exists
            String checkQuery = "SELECT id FROM Staff WHERE id = '" + id + "'";
            ResultSet resultSet = statement.executeQuery(checkQuery);

            if (!resultSet.next()) {
                labelStatus.setText("ID " + id + " does not exist. Use Insert instead.");
                return;
            }

            // Format update call
            String updateString = "UPDATE Staff SET " +
                    "lastName = '" + LastName.getText().trim() + "', " +
                    "firstName = '" + FirstName.getText().trim() + "', " +
                    "mi = '" + MiddleInitial.getText().trim() + "', " +
                    "address = '" + Address.getText().trim() + "', " +
                    "city = '" + City.getText().trim() + "', " +
                    "state = '" + State.getText().trim() + "', " +
                    "telephone = '" + Telephone.getText().trim() + "', " +
                    "email = '" + Email.getText().trim() + "' " +
                    "WHERE id = '" + id + "'";

            int result = statement.executeUpdate(updateString);

            if (result > 0) labelStatus.setText("Record updated successfully for ID: " + id);
            else labelStatus.setText("Update failed for ID: " + id);
        }
        catch (SQLException ex) {
            labelStatus.setText("Error updating record: " + ex.getMessage());
        }
    }

    private void clearForm() {
        Id.clear();
        LastName.clear();
        FirstName.clear();
        MiddleInitial.clear();
        Address.clear();
        City.clear();
        State.clear();
        Telephone.clear();
        Email.clear();
        labelStatus.setText("Form cleared");
    }

    @Override
    public void stop() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) { launch(args); }
}