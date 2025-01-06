package com.example.mpa;
import java.sql.*;
import java.util.concurrent.TimeUnit;


public class loginManager {
    public void addUser(String name, String password, String email) {
        String sql = """
            INSERT INTO Login(userName, userPswd, userEmail)
            VALUES (?, ?, ?);     
        """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, password);
            pstmt.setString(3, email);

            pstmt.executeUpdate();
            System.out.println("User " + name + " has been added to the database");
        } catch (SQLException e) {
            System.out.println("Error: Unable to add user .");
            e.printStackTrace();
        }
    }
    public boolean isUserRegistered(String name) {

        String sql = "SELECT COUNT(*) FROM Login WHERE userName = ?";


        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error: Unable to check user registration.");
            e.printStackTrace();
        }
        return false;
    }
    public boolean verifyLogin(String username, String password) {

        String sql = "SELECT COUNT(*) FROM Login WHERE userName = ? AND userPswd = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error: Unable to verify user credentials.");
            e.printStackTrace();
        }
        return false;
    }
}
