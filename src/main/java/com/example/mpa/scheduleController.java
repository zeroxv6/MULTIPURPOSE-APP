package com.example.mpa;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class scheduleController implements Initializable {

    @FXML
    private ChoiceBox time1;

    @FXML
    private ChoiceBox time2;

    @FXML
    private ChoiceBox time11;

    @FXML
    private ChoiceBox time21;

    @FXML
    private RadioButton B1;

    @FXML
    private RadioButton B2;

    @FXML
    private RadioButton B3;

    @FXML
    private RadioButton B4;

    @FXML
    private DatePicker date1;

    @FXML
    private DatePicker date2;

    @FXML
    private TextField titleBox;

    @FXML
    private Button schSave;

    @FXML
    private TextArea schTopics;

    String filePath = "src/main/resources/data/user.json";
    ScreenTimeManager manager = new ScreenTimeManager();
    int userID = manager.getUserId(readUserFromJSON(filePath));


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (int i = 1; i <= 12; i++) {
            String formatted = String.format("%02d", i);
            time1.getItems().add(formatted);
            time2.getItems().add(formatted);
        }
        for (int i = 0; i < 60; i += 5) {
            String formatted = String.format("%02d", i);
            time11.getItems().add(formatted);
            time21.getItems().add(formatted);
        }

        ToggleGroup toggleGroup1 = new ToggleGroup();
        B1.setToggleGroup(toggleGroup1);
        B2.setToggleGroup(toggleGroup1);

        ToggleGroup toggleGroup2 = new ToggleGroup();
        B3.setToggleGroup(toggleGroup2);
        B4.setToggleGroup(toggleGroup2);

        date1.setEditable(false);
        date2.setEditable(false);

        schSave.setOnAction(event -> {
            scheduleManager manager = new scheduleManager();
            manager.addSchedule(userID,getTitle(),getTimeFrom(),getTimeTo(),getDateFrom(),getDateTo(),getTopics());
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

    public String getTitle(){
        String schTitle = titleBox.getText();
        return schTitle;
    }

    public String getTimeFrom() {

        int fromHour;
        String fromM = (String) time11.getValue();

        if(B2.isSelected()){
            fromHour = Integer.parseInt(time1.getValue().toString()) + 12;
        }
        else{
            fromHour = Integer.parseInt(time1.getValue().toString());
        }
        System.out.println(fromHour);

        String timeFrom = String.format("%02d:%02d", fromHour, Integer.parseInt(fromM));
        System.out.println(timeFrom);
        return timeFrom;
    }

    public String getTimeTo() {
        int toHour;
        String toM = (String) time21.getValue();

        if(B2.isSelected()){
            toHour = Integer.parseInt(time2.getValue().toString()) + 12;
        }
        else{
            toHour = Integer.parseInt(time2.getValue().toString());
        }
        System.out.println(toHour);

        String timeFrom = String.format("%02d:%02d", toHour, Integer.parseInt(toM));
        System.out.println(timeFrom);
        return timeFrom;
    }
    public LocalDate getDateFrom(){
        String fromD = date1.getValue().toString();
        return LocalDate.parse(fromD);
    }
    public LocalDate getDateTo(){
        String toD = date2.getValue().toString();
        return LocalDate.parse(toD);
    }
    public String getTopics(){
        String topics = schTopics.getText();
        return topics;
    }
}




