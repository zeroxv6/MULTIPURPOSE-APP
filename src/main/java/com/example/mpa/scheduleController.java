package com.example.mpa;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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


    }

    public String getTitle(){
        String schTitle = titleBox.getText();
        return schTitle;
    }

    public String getTimeFrom(){
        int fromH = (int) time1.getValue();
        int fromM = (int) time11.getValue();

        String timeFrom = String.format("%02d : %02d", fromH, fromM);
        return timeFrom;
    }
    public String getTimeTo(){
        int toH = (int) time2.getValue();
        int toM = (int) time21.getValue();

        String timeTo = String.format("%02d : %02d", toH, toM);
        return timeTo;
    }
    public String getDateFrom(){
        LocalDate fromD = date1.getValue();
        return fromD.toString();
    }
    public String getDateTo(){
        LocalDate toD = date2.getValue();
        return toD.toString();
    }
}




