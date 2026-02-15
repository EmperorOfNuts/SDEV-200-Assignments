package com.m5a2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();

        // Create Circle
        Circle circle = new Circle(150, 150, 100);
        circle.setFill(Color.BLACK);
        circle.setStroke(Color.GRAY);

        // Add mouse pressed handler
        circle.setOnMousePressed(e -> { circle.setFill(Color.WHITE); });

        // Add mouse release handler
        circle.setOnMouseReleased(e -> { circle.setFill(Color.BLACK); });

        pane.getChildren().add(circle);

        // Create Stage
        Scene scene = new Scene(pane, 300, 300);
        primaryStage.setTitle("M5A2");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }
}