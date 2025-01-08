package com.example.mpa;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class timeController implements Initializable {

//    @FXML
//    private Label active;

    @FXML
    private ScrollPane scrollActive;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button progressButton;

    @FXML
    private Label totalLabel;

    //    PASSWORD MANAGER
    @FXML
    private Button pswdBtn;

    @FXML
    private Button notesBtn;

    @FXML
    private ScrollPane chatScroll;

    @FXML
    private VBox chatVBox;

    @FXML
    private TextField chatInput;

    @FXML
    private Button chatSend;

    @FXML
    private ImageView closeBtn;

    @FXML
    private AnchorPane activePane;

    @FXML
    private VBox agnVBox;

    @FXML
    private VBox todoVBox;

    @FXML
    private Button todoBtn;

    @FXML
    private TableView<Object> assignmentTable;

    @FXML
    private TableColumn<AssignmentData, String> submissionDateCol;

    @FXML
    private TableColumn<AssignmentData, String> subjectCodeCol;

    @FXML
    private TableColumn<AssignmentData, Void> actionsCol;

    @FXML
    private Button schSetup;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Socket socket;
    private BufferedReader br;
    private PrintWriter pr;
    private Scanner sc;

    String filePath = "src/main/resources/data/user.json";
    ScreenTimeManager manager = new ScreenTimeManager();
    int userID = manager.getUserId(readUserFromJSON(filePath));

    BigDecimal progress = new BigDecimal(String.format("%.2f", 0.0));

    private static final String TODO_FILE_PATH = "src/main/resources/data/todoItems.txt";

    public static class AssignmentData {
        private String submissionDate;
        private String subjectCode;

        public AssignmentData(String submissionDate, String subjectCode) {
            this.submissionDate = submissionDate;
            this.subjectCode = subjectCode;
        }

        public String getSubmissionDate() { return submissionDate; }
        public String getSubjectCode() { return subjectCode; }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String filePath = "src/main/resources/data/data.txt";
        setupTableView();
        populateTableView(readFileLines(filePath));

        schSetup.setOnAction((ActionEvent event) -> {
            try {
                scheduleSetUp();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            Client();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        chatSend.setOnAction((ActionEvent event) -> {
            String message = getInput();
            chatInput.clear();
            try {
                sendMessage(message);
                chatInput.clear();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });



        Font.loadFont(getClass().getResourceAsStream("/fonts/Mauline.otf"), 12);
        scrollActive.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        scrollActive.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

//        progressBar.setStyle("-fx-accent: #bba137; -fx-background-color: #313338");
        progressBar.setProgress(0.0);
        startProgressBarUpdate();
        pswdBtn.setOnAction(event -> {
            try {
                pswdLogin(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        notesBtn.setOnAction(event -> {
            try {
                showNotes(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
//        closeBtn.setOnMouseClicked(event -> {
//            Stage stage = (Stage) closeBtn.getScene().getWindow();
//            stage.close();
//        });
//        progressButton.setOnAction(event -> progressBarControl());

        todoBtn.setOnMouseClicked(event -> {
            todo();
        });
        loadTodoItems();
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

    public void getActive() {

        ScreenTimeManager manager = new ScreenTimeManager();

        new Thread(() -> {
            User32 user32 = User32.INSTANCE;

            char[] buffer = new char[512];

            boolean condition = true;
            long lastUpdateTime = System.currentTimeMillis();
            while (condition) {
                HWND hwnd = user32.GetForegroundWindow();
                user32.GetWindowText(hwnd, buffer, 512);

//                System.out.println("Active Window Title: " + Native.toString(buffer));


                IntByReference intByReference = new IntByReference();
                User32.INSTANCE.GetWindowThreadProcessId(hwnd, intByReference);
//                System.out.println("Active Window Thread ID: " + intByReference.getValue());


                WinNT.HANDLE processHandle = Kernel32.INSTANCE.OpenProcess(Kernel32.PROCESS_QUERY_INFORMATION | Kernel32.PROCESS_VM_READ, false, intByReference.getValue());


                char[] exePath = new char[1024];
                IntByReference size = new IntByReference(exePath.length);
                Kernel32.INSTANCE.QueryFullProcessImageName(processHandle, 0, exePath, size);
//                System.out.println("Active Window Process Name: " + Native.toString(exePath));


                Kernel32.INSTANCE.CloseHandle(processHandle);


                String fullPath = new String(exePath, 0, size.getValue());
                String appName = fullPath.substring(fullPath.lastIndexOf("\\") + 1);
                if (appName.endsWith(".exe")) {

                    appName = appName.substring(0, appName.length() - 4); // Remove the last 4 characters
                    System.out.println(appName);
                }
//                System.out.println("Active Window App Name: " + appName);

                if (appName.equals("ApplicationFrameHost.exe")) {
                    appName = "Unknown App";
                }

//                System.out.println("Active Window App Name: " + appName);


                long currentTimeMillis = System.currentTimeMillis();
                long timeSpentInMillis = currentTimeMillis - lastUpdateTime;
                String windowTitle = Native.toString(buffer);


//                manager.updateAppScreenTime(windowTitle, timeSpentInMillis);
                manager.updateAppScreenTime(appName, timeSpentInMillis, userID);
                String getData = manager.printScreenTime(userID);
                lastUpdateTime = currentTimeMillis;


                Platform.runLater(() -> {
                    Label active = new Label();
                    active.setWrapText(true);
                    active.setStyle("-fx-text-fill: #ffffff;\n" +
                            "    -fx-background-color: linear-gradient(#0A143C, #1A2238);\n" +
                            "    -fx-font-family: \"MS UI Gothic\";\n" +
                            "    -fx-font-size: 18;\n" +
                            "    -fx-font-weight: bold;\n" +
                            "    -fx-arc-height: 30;\n" +
                            "    -fx-arc-width: 30;"

                    );
                    active.setText(getData);
                    active.setPrefWidth(300);
                    activePane.getChildren().add(active);

                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void startProgressBarUpdate() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> progressBarControl()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


    public void progressBarControl() {
        ScreenTimeManager manager = new ScreenTimeManager();
        double finalTime = manager.printSumTime(userID);
        double totalMin = finalTime *24*60;
        double hours = totalMin / 60;
        double minutes = totalMin % 60;
//        System.out.println("%.0f:%.0f", hours, minutes);
        totalLabel.setText(String.format("Total Screen Time: %.0f hours %.0f minutes", hours, minutes));
        System.out.println(finalTime);
        progressBar.setProgress(finalTime);
//        if (progress.doubleValue() < 24) {
//            progress = new BigDecimal(String.format("%.2f", progress.doubleValue() + finalTime));
//            progressBar.setProgress(progress.doubleValue());
//            totalLabel.setText(Double.toString(Math.round(progress.doubleValue()) * 24));
//
//            totalLabel.setText(Integer.toString((int)Math.round(progress.doubleValue())));
//        } else {
//            progressBar.setProgress(24);
//            totalLabel.setText("Your screen time is 24 hours");
//        }
//        Platform.runLater(() -> {
//            progressBar.setProgress(value);
//        });
//        try {
//            Thread.sleep(1000);
//        }
//        catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

    }


    public void pswdLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("passwordManager.fxml"));
        Parent root = loader.load();

        Stage newStage = new Stage();
        Scene newScene = new Scene(root);
        newStage.setScene(newScene);
        newStage.setTitle("Password Manager");
        newStage.show();
    }

    public void showNotes(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("notes.fxml"));
        Parent root = loader.load();

        Stage newStage = new Stage();
        Scene newScene = new Scene(root);
        newStage.setScene(newScene);
        newStage.setTitle("MPA - Notes");
        newStage.show();
    }

    public void Client() throws IOException {
        socket = new Socket("localhost", 9000);

        InputStreamReader in = new InputStreamReader(socket.getInputStream());
        br = new BufferedReader(in);
        pr = new PrintWriter(socket.getOutputStream());

    }


    public void sendMessage(String message) throws IOException {
        if (message == null || message.isEmpty()) {
            System.out.println("Message is empty or null");
            return;
        }


        pr.println(message);
        pr.flush();


        String response = br.readLine();
        if (response != null) {
            System.out.println("server: " + response);


            Platform.runLater(() -> {
                Label serverMessage = new Label("Server: " + response);
                serverMessage.setStyle("-fx-text-fill: white; -fx-wrap-text: true");
                chatVBox.getChildren().add(serverMessage);
            });
        }
    }


    public String getInput(){
        if (chatInput.getText().equals("")) {
            return "message is empty";

        }
        else{
            String input = chatInput.getText();
            return input;
        }
    }
    public static List<String> readFileLines(String filePath) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }

        return lines;
    }
    public void todo() {
        TextInputDialog dialog = new TextInputDialog("Enter your input here");

        dialog.setTitle("Input Dialog");
        dialog.setHeaderText("Please enter your input:");
        dialog.setContentText("Input:");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setGraphic(null);

        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.initStyle(StageStyle.UNDECORATED);

        dialogPane.getStylesheets().add(getClass().getResource("dialog-styles.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        dialog.showAndWait().ifPresent(input -> {
            addTodoItem(input);
        });
    }

    private void addTodoItem(String input) {
        HBox todoHBox = new HBox(15);

        System.out.println("User input: " + input);
        Label todoL = new Label(input);
        todoL.setStyle("-fx-text-fill: white; -fx-font-family: 'OCR A Extended'; -fx-padding: 3; -fx-font-size: 20px;");
        CheckBox todoC = new CheckBox();

        todoC.setOnAction(e -> {
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
                todoVBox.getChildren().remove(todoHBox);
                saveTodoItems();
            }));
            timeline.play();
        });

        todoHBox.getChildren().addAll(todoC, todoL);
        todoVBox.getChildren().add(todoHBox);

        saveTodoItems();
    }

    private void saveTodoItems() {
        List<String> todoItems = new ArrayList<>();
        for (Node node : todoVBox.getChildren()) {
            if (node instanceof HBox) {
                HBox hBox = (HBox) node;
                for (Node child : hBox.getChildren()) {
                    if (child instanceof Label) {
                        Label label = (Label) child;
                        todoItems.add(label.getText());
                    }
                }
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TODO_FILE_PATH))) {
            for (String item : todoItems) {
                writer.write(item);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTodoItems() {
        List<String> todoItems = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TODO_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                todoItems.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String item : todoItems) {
            addTodoItem(item);
        }
    }
    private void setupTableView() {
        submissionDateCol.setCellValueFactory(new PropertyValueFactory<>("submissionDate"));
        subjectCodeCol.setCellValueFactory(new PropertyValueFactory<>("subjectCode"));

        actionsCol.setCellFactory(column -> new TableCell<AssignmentData, Void>() {
            private final Button actionButton = new Button("Action");
            {
                actionButton.setOnAction(event -> {
                    AssignmentData data = getTableView().getItems().get(getIndex());
                    handleActionButton(data);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionButton);
                }
            }
        });
    }

    private void handleActionButton(AssignmentData data) {

        System.out.println("Action button clicked for:");
        System.out.println("Submission Date: " + data.getSubmissionDate());
        System.out.println("Subject Code: " + data.getSubjectCode());

    }

    private void populateTableView(List<String> lines) {
        ObservableList<Object> data = FXCollections.observableArrayList();

        for (String line : lines) {
            try {

                String[] parts = line.split(" ");
                if (parts.length >= 6) {
                    String submissionDate = parts[2];
                    String subjectCode = parts[5];
                    data.add(new AssignmentData(submissionDate, subjectCode));
                }
            } catch (Exception e) {
                System.err.println("Error parsing line: " + line);
                e.printStackTrace();
            }
        }

        assignmentTable.setItems(data);
    }


    public void scheduleSetUp() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("schedule.fxml"));
        Parent root = loader.load();

        Stage newStage = new Stage();
        Scene newScene = new Scene(root);
        newStage.setScene(newScene);
        newStage.setTitle("Schedule Setup");
        newStage.show();
    }

    public void scheduleDisplay(){
        scheduleController schedule = new scheduleController();
        String sTitle = schedule.getTitle();
        String sTimeFrom = schedule.getTimeFrom();
        String sTimeTo = schedule.getTimeTo();
        String sDateFrom = schedule.getDateFrom();
        String sDateTo = schedule.getDateTo();

    }


}





