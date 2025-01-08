package com.example.mpa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class vaultController implements Initializable {

    @FXML
    private TextField vaultUserName;

    @FXML
    private PasswordField vaultUserPassword;

    @FXML
    private TextField vaultMisc;

    @FXML
    private Button vaultSaveButton;

    @FXML
    private VBox vboxUserName;

    @FXML
    private VBox vboxPassword;

    @FXML
    private VBox vboxMisc;

    @FXML
    private VBox vboxCopy;

    @FXML
    private VBox vboxDelete;

    @FXML
    private ScrollPane vaultScroll;

    @FXML
    private ImageView copyImg;

    @FXML
    private ImageView delImg;


    String filePath = "src/main/resources/data/user.json";
    ScreenTimeManager manager = new ScreenTimeManager();
    int userID = manager.getUserId(readUserFromJSON(filePath));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        vaultScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        vaultScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        showPassword();
        vaultSaveButton.setOnAction((ActionEvent event) -> {
            vaultSave(event);
        });
    }
    public static String readUserFromJSON(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            boolean jsonStarted = false;

            while ((line = br.readLine()) != null) {
                if (line.trim().startsWith("{")) {
                    jsonStarted = true;
                }
                if (jsonStarted) {
                    jsonContent.append(line);
                }
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

    public void vaultSave(ActionEvent actionEvent) {
        String username = vaultUserName.getText();
        String password = vaultUserPassword.getText();
        String misc = vaultMisc.getText();

        vaultManager vaultController = new vaultManager();

        vaultController.updatePassword(username, password, misc, userID);
        showPassword();
    }

    public void showPassword(){
        List<String> vaultUserName = vaultManager.getUserNames(userID);
        List<String> vaultPasswords = vaultManager.getUserPasswords(userID);
        List<String> vaultMisc = vaultManager.getMiscData(userID);
        int totalRows = vaultManager.getRows(userID);
        vboxUserName.getChildren().clear();
        vboxPassword.getChildren().clear();
        vboxMisc.getChildren().clear();

//        vboxCopy.getChildren().clear();
//        vboxDelete.getChildren().clear();


//        int count = 0;
        for (String row : vaultUserName) {
            TextField textField = new TextField();
            textField.setText(row);
            vboxUserName.getChildren().add(textField);
            textField.setStyle("-fx-background-color: #383A40; -fx-border-color: white; -fx-text-fill: white");
            textField.setCursor(javafx.scene.Cursor.HAND);
            textField.setOnMouseClicked(event -> copyToClipboard(textField.getText()));
        }
//            ImageView copyImg = new ImageView();
//            Image image1 = new Image(getClass().getResource("copy.png").toExternalForm());
//            copyImg.setImage(image1);
//
//
//            ImageView delImg = new ImageView();
//            Image image2 = new Image(getClass().getResource("delete.png").toExternalForm());
//            delImg.setImage(image2);
//
//            copyImg.setFitWidth(32);
//            copyImg.setFitHeight(32);
//            delImg.setFitWidth(32);
//            delImg.setFitHeight(32);
//
//            copyImg.setPreserveRatio(true);
//            delImg.setPreserveRatio(true);
//
//            vboxCopy.getChildren().add(copyImg);
//            vboxDelete.getChildren().add(delImg);
//            count++;

        for (String row : vaultPasswords) {
            TextField textField = new TextField();
            textField.setText(row);
            vboxPassword.getChildren().add(textField);
            textField.setStyle("-fx-background-color: #383A40; -fx-border-color: white; -fx-text-fill: white");
            textField.setCursor(javafx.scene.Cursor.HAND);
            textField.setOnMouseClicked(event -> copyToClipboard(textField.getText()));
        }
        for (String row : vaultMisc) {
            TextField textField = new TextField();
            textField.setText(row);
            vboxMisc.getChildren().add(textField);
            textField.setStyle("-fx-background-color: #383A40; -fx-border-color: white; -fx-text-fill: white");
            textField.setCursor(javafx.scene.Cursor.HAND);
            textField.setOnMouseClicked(event -> copyToClipboard(textField.getText()));
        }


//        for(int i = 0; i<count; i++){
//            ImageView copyImg = new ImageView();
//            Image image1 = new Image(getClass().getResource("copy.png").toExternalForm());
//            copyImg.setImage(image1);
//            ImageView delImg = new ImageView();
//            Image image2 = new Image(getClass().getResource("delete.png").toExternalForm());
//            delImg.setImage(image2);
//            copyImg.setFitWidth(20);
//            copyImg.setFitHeight(20);
//            delImg.setFitWidth(20);
//            delImg.setFitHeight(20);
//            vboxCopy.getChildren().add(copyImg);
//            vboxDelete.getChildren().add(delImg);
        }
    private void copyToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    }

}

