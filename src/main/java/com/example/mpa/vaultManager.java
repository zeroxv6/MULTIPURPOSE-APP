package com.example.mpa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class vaultManager {

    public void updatePassword(String userName, String userPassword, String miscData, int userId) {
        String sql = """
            INSERT INTO vault(userName, userPassword, websiteName, userId)
            VALUES(?,?,?,?)
        """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userName);
            pstmt.setString(2, userPassword);
            pstmt.setString(3, miscData);
            pstmt.setInt(4, userId);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error Occurred.");
            e.printStackTrace();
        }
    }

    public static int getRows(int userId) {
        String sql = "SELECT COUNT(*) FROM vault WHERE userId = ?";

        int rowCount = 0;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                rowCount = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error: Unable to get row count from 'vault' table.");
            e.printStackTrace();
        }
        return rowCount;
    }

    public static List<String> getUserNames(int userId) {
        List<String> vaultUserName = new ArrayList<>();
        String sql = "SELECT userName FROM vault WHERE userId = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String username = rs.getString("userName");
                vaultUserName.add(username);
            }

        } catch (SQLException e) {
            System.err.println("Error: Unable to retrieve data from 'vault' table.");
            e.printStackTrace();
        }

        return vaultUserName;
    }

    public static List<String> getUserPasswords(int userId) {
        List<String> vaultPasswords = new ArrayList<>();
        String sql = "SELECT userPassword FROM vault WHERE userId = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String password = rs.getString("userPassword");
                vaultPasswords.add(password);
            }

        } catch (SQLException e) {
            System.err.println("Error: Unable to retrieve data from 'vault' table.");
            e.printStackTrace();
        }

        return vaultPasswords;
    }

    public static List<String> getMiscData(int userId) {
        List<String> vaultMisc = new ArrayList<>();
        String sql = "SELECT websiteName FROM vault WHERE userId = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String misc = rs.getString("websiteName");
                vaultMisc.add(misc);
            }

        } catch (SQLException e) {
            System.err.println("Error: Unable to retrieve data from 'vault' table.");
            e.printStackTrace();
        }

        return vaultMisc;
    }
}
