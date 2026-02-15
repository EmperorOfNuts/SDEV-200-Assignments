package com.m5a1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create GridPane
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));

        double imageWidth = 200;
        double imageHeight = 150;

        try {
            Image image1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/flag1.gif")));
            Image image2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/flag2.gif")));
            Image image3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/flag6.gif")));
            Image image4 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/flag7.gif")));

            ImageView imageView1 = new ImageView(image1);
            ImageView imageView2 = new ImageView(image2);
            ImageView imageView3 = new ImageView(image3);
            ImageView imageView4 = new ImageView(image4);

            // Add all Images
            imageView1.setFitWidth(imageWidth);
            imageView1.setFitHeight(imageHeight);
            imageView1.setPreserveRatio(true);

            imageView2.setFitWidth(imageWidth);
            imageView2.setFitHeight(imageHeight);
            imageView2.setPreserveRatio(true);

            imageView3.setFitWidth(imageWidth);
            imageView3.setFitHeight(imageHeight);
            imageView3.setPreserveRatio(true);

            imageView4.setFitWidth(imageWidth);
            imageView4.setFitHeight(imageHeight);
            imageView4.setPreserveRatio(true);

            // Add images to grid (row, column)
            gridPane.add(imageView1, 0, 0); // Top-left
            gridPane.add(imageView2, 1, 0); // Top-right
            gridPane.add(imageView3, 0, 1); // Bottom-left
            gridPane.add(imageView4, 1, 1); // Bottom-right

            // Show stage
            Scene scene = new Scene(gridPane);
            primaryStage.setTitle("4 Image Grid");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.out.println("Error loading images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) { launch(args); }
}