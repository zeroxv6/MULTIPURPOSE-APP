//package com.example.mpa;
//
//import javafx.animation.PauseTransition;
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//
//public class Start extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        try {
//
//            FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("start.fxml"));
//            Parent root = fxmlLoader.load();
//            Scene scene = new Scene(root, 600, 300);
//
//            primaryStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
//            primaryStage.setResizable(false);
//            primaryStage.setTitle("prodIPv4");
//            primaryStage.setScene(scene);
//            primaryStage.show();
//
//
//            PauseTransition pause = new PauseTransition(Duration.seconds(5));
//            pause.setOnFinished(event -> {
//                primaryStage.close(); // Close the first window
//                openSecondWindow(); // Open the second window
//            });
//            pause.play();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void openSecondWindow() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
//            Parent root = loader.load();
//            Stage secondStage = new Stage();
//            Scene secondScene = new Scene(root);
//            timeController controller = loader.getController();
//            controller.getActive();
//            secondStage.setTitle("MAIN");
//            secondStage.setScene(secondScene);
//            secondStage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

package com.example.mpa;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Start extends Application {

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
            pause.setOnFinished(event -> {
                primaryStage.close();
                openLoginWindow();
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

    public static void main(String[] args) {
        launch(args);
    }
}