package com.example.mpa;
import java.sql.*;
import java.util.concurrent.TimeUnit;


public class loginManager {
    public void addUser(String name, String password, String email) {
        String sql = """
            INSERT INTO userdata(login, pass, email)
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
            System.out.println("Error: Unable to add user.");
            e.printStackTrace();
        }
    }

    public boolean isUserRegistered(String name) {
        String sql = "SELECT COUNT(*) FROM userdata WHERE login = ?";

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
        String sql = "SELECT COUNT(*) FROM userdata WHERE login = ? AND pass = ?";

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

    public String getUsername(String username) {
        String sql = "SELECT login FROM userdata WHERE login = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("login");
            }
        } catch (SQLException e) {
            System.out.println("Error: Unable to retrieve username.");
            e.printStackTrace();
        }
        return null;
    }
    public String getPass(String username) {
        String sql = "SELECT pass FROM userdata WHERE login = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("pass");
            }
        } catch (SQLException e) {
            System.out.println("Error: Unable to retrieve username.");
            e.printStackTrace();
        }
        return null;
    }


}
