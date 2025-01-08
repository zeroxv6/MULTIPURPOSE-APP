package com.example.mpa;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class loginController implements Initializable {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private TextField email;

    @FXML
    private Button btn;

    @FXML
    private Label emailL;

    @FXML
    private Label pswdL;
    private Consumer<Boolean> loginCallback;

    String filePath = "src/main/resources/data/user.json";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailL.setVisible(false);
        pswdL.setVisible(false);
        password.setVisible(false);
        email.setVisible(false);
        btn.setOnAction(e -> {
            checkUser();
        });


    }
    public void setLoginCallback(Consumer<Boolean> callback) {

        this.loginCallback = callback;

    }
    public void addUser(){
        loginManager loginManager = new loginManager();
        String user = username.getText();
        String pass = password.getText();
        String mail = email.getText();
        Argon2 argon2 = Argon2Factory.create();
        String hashedPassword = argon2.hash(2, 65536, 1, pass.toCharArray());
        loginManager.addUser(user,hashedPassword,mail);

        writeToJSON(user,filePath);
    }

    public void checkUser(){
        loginManager loginManager = new loginManager();
        String user = username.getText();

        boolean res = loginManager.isUserRegistered(user);

        if(res){
            password.setVisible(true);
            pswdL.setVisible(true);
            password.requestFocus();
            btn.setOnAction(event -> {
                String pass = password.getText();
                System.out.println(pass);
                boolean res2 = loginManager.verifyLogin(user,pass);
                System.out.println(res2);
                if(res2){
                    System.out.println("Login Successful");
                    if (loginCallback != null) {

                        loginCallback.accept(true);

                    }
                }
                else{
                    System.out.println("Login Failed");
                    if (loginCallback != null) {

                        loginCallback.accept(false);

                    }
                }
            });
        }
        else{
            password.setVisible(true);
            email.setVisible(true);
            pswdL.setVisible(true);
            emailL.setVisible(true);
            password.requestFocus();
            String pass = password.getText();
            String mail = email.getText();
            btn.setOnAction(event -> {
                addUser();
                if (loginCallback != null) {

                    loginCallback.accept(true);
                }
            });
        }
    }
    public void writeToJSON(String username, String filePath) {
        loginManager loginManager = new loginManager();
        String confirmedUsername = loginManager.getUsername(username);

        if (confirmedUsername != null) {
            JSONObject jsonObject = new JSONObject();
            JSONArray userDataArray = new JSONArray();
            JSONObject userObject = new JSONObject();


            userObject.put("user", confirmedUsername);

            userDataArray.put(userObject);
            jsonObject.put("userData", userDataArray);

            try (FileWriter file = new FileWriter(filePath)) {
                file.write(jsonObject.toString(4));
                System.out.println("JSON file has been created successfully");
            } catch (IOException e) {
                System.out.println("Error writing to JSON file: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Username not found in database");
        }
    }
}
