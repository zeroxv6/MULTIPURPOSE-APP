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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


    }
    public String readUserFromJSON(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonContent.append(line);
            }

            JSONObject jsonObject = new JSONObject(jsonContent.toString());
            JSONArray userData = jsonObject.getJSONArray("userData");

            if (userData.length() > 0) {
                JSONObject userObject = userData.getJSONObject(0);
                return userObject.getString("user");
            }

        } catch (IOException | JSONException e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    public void confirmPassword(ActionEvent event)throws IOException{
        String filePath = "src/main/resources/data/user.json";
        loginManager pw = new loginManager();
        String username = readUserFromJSON(filePath);
        String password = pw.getPass(username);
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


