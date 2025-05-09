package com.example.mpa;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class notesController implements Initializable {

    @FXML
    private DatePicker searchByDate;

    @FXML
    private VBox savedShow;

    @FXML
    private TextField noteTitle;

    @FXML
    private TextArea noteArea;

    @FXML
    private Button noteSaveBtn;

    @FXML
    private Label openedNote;

    @FXML
    private DatePicker searchNotesByDate;

    @FXML
    private ImageView searchBtn;

    @FXML
    private TextField searchBar;

    @FXML
    private Button createNewBtn;

    @FXML
    private Button showAllBtn;

    private notesManager notesManager;


    int userId = 1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showAllBtn.setVisible(false);
        showAllBtn.setDisable(true);
        notesManager = new notesManager();
        searchNotes();
        showNotes();
        noteSaveBtn.setOnAction(e -> saveNotes());
        createNewBtn.setOnAction(e -> {
            noteTitle.clear();
            noteArea.clear();
            openedNote.setText("Creating New Note");
            noteSaveBtn.setOnAction(f -> saveNotes());
        });
        showAllBtn.setOnAction(e -> {
            showAllBtn.setVisible(false);
            showAllBtn.setDisable(true);
            showNotes();
        });
    }

    public void showNotes() {
        List<String> noteTitles = notesManager.title(userId);
        displayNotes(noteTitles);
    }

    private Label currentlyHighlightedLabel = null;
    private void displayNotes(List<String> noteTitles) {
        savedShow.getChildren().clear();

        for (String row : noteTitles) {
            Label label = new Label(row);
            label.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
            savedShow.getChildren().add(label);


            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteOption = new MenuItem("DELETE");
            deleteOption.setOnAction(e -> {
                notesManager.deleteNote(row, userId);
                savedShow.getChildren().remove(label);
                noteTitle.clear();
                noteArea.clear();
                openedNote.setText("Viewing: None");
                showNotes();
            });
            contextMenu.getItems().addAll(deleteOption);
            label.setOnContextMenuRequested(e -> contextMenu.show(label, e.getScreenX(), e.getScreenY()));


            label.setOnMouseClicked(e -> {

                if (currentlyHighlightedLabel != null) {
                    currentlyHighlightedLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
                }

                // Highlight the clicked label
                label.setStyle("-fx-background-color: white; -fx-text-fill: black;");
                currentlyHighlightedLabel = label;

                List<String> noteDesc = notesManager.description(row, userId);
                noteArea.setText(noteDesc.get(0));
                openedNote.setStyle("-fx-font-weight: bold");
                openedNote.setText("Viewing: " + row);

                noteSaveBtn.setOnAction(f -> {
                    String editedNote = noteArea.getText();
                    notesManager.editNote(row, editedNote, userId);
                });
            });
        }
    }

    public void saveNotes() {
        String title = noteTitle.getText();
        String desc = noteArea.getText();
        if (title != null && !title.isEmpty() && desc != null && !desc.isEmpty()) {
            notesManager.saveNote(title, desc, userId);
            showNotes();
            noteTitle.clear();
            noteArea.clear();
            openedNote.setText("Note Saved");
        } else {
            System.out.println("Title or description cannot be empty");
        }
    }

    public void searchNotes() {
        searchBtn.setOnMouseClicked(mouseEvent -> {
            String searchTitle = searchBar.getText();
            LocalDate searchDate = searchNotesByDate.getValue();
            searchBar.clear();
            searchNotesByDate.setValue(null);
            if (searchDate != null && (searchTitle == null || searchTitle.isEmpty())) {
                System.out.println(searchDate);
                showAllBtn.setVisible(true);
                showAllBtn.setDisable(false);
                List<String> results = notesManager.searchNotesByDate(searchDate, userId);
                if (results.isEmpty()) {
                    showNoResultsPopup();
                    return;
                }

                displayNotes(results);
            } else if (searchDate == null && searchTitle != null && !searchTitle.isEmpty()) {
                System.out.println(searchTitle);
                showAllBtn.setVisible(true);
                showAllBtn.setDisable(false);
                List<String> results = notesManager.searchNotess(searchTitle,userId);
                if (results.isEmpty()) {
                    showNoResultsPopup();
                    return;
                }
                displayNotes(results);
            } else if (searchDate != null && searchTitle != null && !searchTitle.isEmpty()) {
                showAllBtn.setVisible(true);
                showAllBtn.setDisable(false);
                List<String> results = notesManager.searchNotesCombined(searchTitle, searchDate, userId);
                if (results.isEmpty()) {
                    showNoResultsPopup();
                    return;
                }

                displayNotes(results);
            }
            else {
                return;
            }

        });
    }
    private void showNoResultsPopup() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Search Results");
        alert.setHeaderText(null);
        alert.setContentText("No notes found matching your search criteria.");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStyleClass().add("alert");
        dialogPane.setStyle("-fx-background-color: #2b2b2b;");

        alert.getDialogPane().lookupButton(ButtonType.OK)
                .setStyle("-fx-background-color: #3c3f41; -fx-text-fill: white;");

        Label contentLabel = (Label) alert.getDialogPane().lookup(".content");
        if (contentLabel != null) {
            contentLabel.setStyle("-fx-text-fill: white;");
        }

        alert.showAndWait();
    }
}
