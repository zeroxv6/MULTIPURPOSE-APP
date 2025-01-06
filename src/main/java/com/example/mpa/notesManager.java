package com.example.mpa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class notesManager {

    public static List<String> description(String title) {
        List<String> notesDescription = new ArrayList<>();
        String sql = "SELECT description FROM notes WHERE title = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String description = rs.getString("description");
                    notesDescription.add(description);
                }
            }
            System.out.println(notesDescription);

        } catch (SQLException e) {
            System.err.println("Error: Unable to retrieve data from 'vault' table.");
            e.printStackTrace();
        }

        return notesDescription;
    }
    public static List<String> title() {
        List<String> notesTitle = new ArrayList<>();
        String sql = "SELECT title FROM notes";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String title = rs.getString("title");
                notesTitle.add(title);
            }

        } catch (SQLException e) {
            System.err.println("Error: Unable to retrieve data from 'vault' table.");
            e.printStackTrace();
        }

        return notesTitle;
    }

    public static List<String> lastEdited() {
        List<String> notesEdited = new ArrayList<>();
        String sql = "SELECT lastEdited FROM notes";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("lastEdited");
                notesEdited.add(id);
            }

        } catch (SQLException e) {
            System.err.println("Error: Unable to retrieve data from 'vault' table.");
            e.printStackTrace();
        }

        return notesEdited;
    }

    public void saveNote(String title, String description) {
        String sql = "INSERT INTO notes (title, description) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, description);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error: Unable to update screen time.");
            e.printStackTrace();
        }

    }
    public void editNote(String title, String description) {
        String sql = "UPDATE notes SET description = ? WHERE title = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, description);
            pstmt.setString(2, title);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error: Unable to update screen time.");
            e.printStackTrace();
        }

    }
    public void deleteNote(String title) {
        String sql = "DELETE FROM notes WHERE title = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error: Unable to update screen time.");
            e.printStackTrace();
        }
    }
    public int checkCount(){
        int check = 0;
        String sql = "SELECT COUNT(*) FROM notes";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String checkString = rs.getString("COUNT(*)");
                check = Integer.parseInt(checkString);
            }

        } catch (SQLException e) {
            System.err.println("Error: Unable to retrieve data from 'vault' table.");
            e.printStackTrace();
        }

        return check;
    }
    public static List<String> searchNotess(String searchString) {
        List<String> notes = new ArrayList<>();
        String sql = "SELECT title FROM notes WHERE title LIKE ? OR description LIKE ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + searchString + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("title");
                    notes.add(title);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error: Unable to search notes.");
            e.printStackTrace();
        }

        return notes;
    }
    public static List<String> searchNotesByDate(LocalDate searchDate) {
        List<String> notes = new ArrayList<>();
        String sql = "SELECT title FROM notes WHERE DATE(lastEdited) = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Convert LocalDate to SQL Date
            java.sql.Date sqlDate = java.sql.Date.valueOf(searchDate);
            pstmt.setDate(1, sqlDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("title");
                    notes.add(title);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error: Unable to search notes by date.");
            e.printStackTrace();
        }

        return notes;
    }
    public static List<String> searchNotesCombined(String searchString, LocalDate searchDate) {
        List<String> notes = new ArrayList<>();
        String sql = "SELECT title FROM notes WHERE (title LIKE ? OR description LIKE ?) AND DATE(lastEdited) = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the search pattern for title and description
            String searchPattern = "%" + searchString + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            // Set the date
            java.sql.Date sqlDate = java.sql.Date.valueOf(searchDate);
            pstmt.setDate(3, sqlDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("title");
                    notes.add(title);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error: Unable to search notes by combined criteria.");
            e.printStackTrace();
        }

        return notes;
    }
}
