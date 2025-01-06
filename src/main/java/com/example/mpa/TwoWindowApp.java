package com.example.mpa;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class TwoWindowApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create the first window
        StackPane firstLayout = new StackPane();
        firstLayout.getChildren().add(new Label("This is the first window"));
        Scene firstScene = new Scene(firstLayout, 300, 200);

        primaryStage.setTitle("First Window");
        primaryStage.setScene(firstScene);
        primaryStage.show();

        // Pause for 5 seconds, then switch to the second window
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> {
            primaryStage.close(); // Close the first window
            openSecondWindow(); // Open the second window
        });
        pause.play();
    }

    private void openSecondWindow() {
        Stage secondStage = new Stage();
        StackPane secondLayout = new StackPane();
        secondLayout.getChildren().add(new Label("This is the second window"));
        Scene secondScene = new Scene(secondLayout, 300, 200);

        secondStage.setTitle("Second Window");
        secondStage.setScene(secondScene);
        secondStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}