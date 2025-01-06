package com.example.mpa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.Scanner;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("new.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1500, 800);

        timeController controller = fxmlLoader.getController();
        controller.getActive();

        stage.setResizable(false);
        stage.setTitle("MPA");

        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}