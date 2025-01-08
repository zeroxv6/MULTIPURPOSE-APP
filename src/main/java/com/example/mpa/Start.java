
package com.example.mpa;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Start extends Application {

    String filePath = "src/main/resources/data/user.json";

    @Override
    public void start(Stage primaryStage) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("start.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 600, 300);

            primaryStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
            primaryStage.setResizable(false);
            primaryStage.setTitle("prodIPv4");
            primaryStage.setScene(scene);
            primaryStage.show();


            PauseTransition pause = new PauseTransition(Duration.seconds(1));
//            pause.setOnFinished(event -> {
//                primaryStage.close();
//                checkData();
//                openLoginWindow();
//            });
//            pause.play();

            pause.setOnFinished(event -> {
                primaryStage.close();
                if (checkJSONFormat(filePath)) {
                    openMainWindow();
                } else {
                    openLoginWindow();
                }
            });
            pause.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openLoginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login form.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            Scene loginScene = new Scene(root);
            loginStage.setTitle("Login");
            loginStage.setScene(loginScene);
            loginStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
//            loginStage.setOnHidden(event -> openMainWindow());

            loginController controller = loader.getController();

            controller.setLoginCallback(success -> {
                if (success) {
                    loginStage.close();
                    openMainWindow();
                }
            });
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openMainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("new.fxml"));
            Parent root = loader.load();
            Stage mainStage = new Stage();
            Scene mainScene = new Scene(root);
            timeController controller = loader.getController();
            controller.getActive();
            mainStage.setTitle("MAIN");
            mainStage.setScene(mainScene);
            mainStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
            mainStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void checkData(){

        StringBuilder jsonContent = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                jsonContent.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(jsonContent.toString());
        JSONArray userData = jsonObject.getJSONArray("userData");

        for (int i = 0; i < userData.length(); i++) {
            JSONObject student = userData.getJSONObject(i);
            String username = student.getString("user");
            String password = student.getString("pass");

            System.out.println("Username: " + username + ", Password: " + password);
        }
    }

    public static boolean checkJSONFormat(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonContent.append(line);
            }

            JSONObject jsonObject = new JSONObject(jsonContent.toString());
            JSONArray userData = jsonObject.getJSONArray("userData");

            if (userData.length() > 0) {
                JSONObject firstObject = userData.getJSONObject(0);
                return firstObject.has("user");
            }
            return false;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
