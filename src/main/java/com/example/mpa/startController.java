package com.example.mpa;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class startController implements Initializable {
    @FXML
    private Button test;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        test.setVisible(false);
        test.setOnAction(event -> {
            System.out.println("button clicked");
            try {
                displayScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            test.setOnAction(eventt -> closeWindow());
        });
    }

    public void displayScreen() throws IOException {

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
            Parent root = loader.load();

            Scene newScene = new Scene(root, 1500, 800);
            Stage newStage = new Stage();
            newStage.setScene(newScene);
            newStage.setTitle("MAIN");
            newStage.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public void closeWindow(){
        Stage stage = (Stage) test.getScene().getWindow();
        stage.close();
    }

}
