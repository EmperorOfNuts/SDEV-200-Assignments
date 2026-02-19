package M6A2.src.main.java.com.m6a2;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.sql.*;

public class DBConnectionPane extends VBox {
    private final TextField Driver = new TextField("com.mysql.cj.jdbc.Driver");
    private final TextField Url = new TextField("jdbc:mysql://localhost/M6A2");
    private final TextField Username = new TextField("constellation");
    private final PasswordField Password = new PasswordField();
    private final Label labelStatus = new Label("Not connected");
    private final Button Connect = new Button("Connect to DB");
    
    private Connection connection;

    public DBConnectionPane() {
        setSpacing(10);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #999; -fx-border-width: 1;");

        Label lblTitle = new Label("Connect to DB");
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 14));

        labelStatus.setStyle("-fx-text-fill: blue;");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(new Label("JDBC Driver"), 0, 0);
        gridPane.add(Driver, 1, 0);

        gridPane.add(new Label("Database URL"), 0, 1);
        gridPane.add(Url, 1, 1);

        gridPane.add(new Label("Username"), 0, 2);
        gridPane.add(Username, 1, 2);

        gridPane.add(new Label("Password"), 0, 3);
        gridPane.add(Password, 1, 3);

        Button Close = new Button("Close Dialog");
        Close.setOnAction(e -> {
            getScene().getWindow().hide();
        });

        Connect.setOnAction(e -> connectToDB());

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(Connect, Close);

        getChildren().addAll(lblTitle, gridPane, labelStatus, buttonBox);
    }

    private void connectToDB() {
        try {
            Class.forName(Driver.getText().trim());

            connection = DriverManager.getConnection(
                    Url.getText().trim(),
                    Username.getText().trim(),
                    Password.getText()
            );

            labelStatus.setText("Connected to " + Url.getText().trim());
            labelStatus.setStyle("-fx-text-fill: green;");

        } catch (Exception ex) {
            labelStatus.setText("Connection failed: " + ex.getMessage());
            labelStatus.setStyle("-fx-text-fill: red;");
            ex.printStackTrace();
        }
    }

    public Connection getConnection() { return connection; }

    public String getStatus() { return labelStatus.getText(); }
}