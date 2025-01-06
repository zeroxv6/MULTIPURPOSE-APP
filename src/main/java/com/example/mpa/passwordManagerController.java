package com.example.mpa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class passwordManagerController implements Initializable {

    @FXML
    private Button confirmClicked;

    @FXML
    private PasswordField pswdBox;

    private Stage stage;
    private Scene scene;
    private Parent root;

//    @FXML
//    private Button homeBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (confirmClicked != null) {
            confirmClicked.setOnAction(event -> {
                try {
                    confirmPassword(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
//        else {
//            System.out.println("confirmClicked is null! Check the FXML file.");
//        }
//        if(homeBtn != null) {
//            homeBtn.setOnAction(event -> {
//                try{
//                    getBack(event);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//        }
//        else{
//            System.out.println("homeBtn is null! Check the FXML file.");
//        }

    }


    public void confirmPassword(ActionEvent event)throws IOException{

        String password = "raman";
        Argon2 argon2 = Argon2Factory.create();
        String hashedPassword = argon2.hash(2, 65536, 1, password.toCharArray());
        String userPassword = pswdBox.getText();

        if (argon2.verify(hashedPassword, userPassword.toCharArray())) {
            root = FXMLLoader.load(getClass().getResource("vault.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }
//        else {
//            System.out.println("Password is incorrect!");
//
//        }
    }
//    public void addNewPassword(ActionEvent event) throws IOException{
//
//    }

//    public void getBack(ActionEvent event) throws IOException{
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
//        Parent root = loader.load();
//
//        // If you need access to the controller
//        timeController controller = loader.getController();
//
//        controller.initialize();
//
//        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }
}


