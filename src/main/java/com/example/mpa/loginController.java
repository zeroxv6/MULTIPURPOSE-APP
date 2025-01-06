package com.example.mpa;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;


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
        loginManager.addUser(user,pass,mail);
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

                        loginCallback.accept(true); // Notify success

                    }
                }
                else{
                    System.out.println("Login Failed");
                    if (loginCallback != null) {

                        loginCallback.accept(false); // Notify failure

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

                    loginCallback.accept(true); // Notify success

                }
            });
        }
    }
}