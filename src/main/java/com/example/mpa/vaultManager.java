package com.example.mpa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class vaultManager {

    public void updatePassword(String userName, String userPassword, String miscData) {
        String sql = """
                INSERT INTO vault(userName, userPassword, websiteName)
                VALUES(?,?,?)
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userName);
            pstmt.setString(2, userPassword);
            pstmt.setString(3, miscData);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error Occured.");
            e.printStackTrace();
        }
    }

    public static int getRows(){
        String sql = """
                SELECT COUNT(*) FROM vault;
                """;

        int rowCount = 0;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                rowCount = rs.getInt("COUNT(*)");
            }

        }
        catch (SQLException e) {
            System.err.println("Error: Unable to get row count from 'vault' table.");
            e.printStackTrace();
        }
        return rowCount;

    }

    public static List<String> getUserNames() {
        List<String> vaultUserName = new ArrayList<>();
        String sql = "SELECT userName FROM vault";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

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

    public static List<String> getUserPasswords() {
        List<String> vaultPasswords = new ArrayList<>();
        String sql = "SELECT userPassword FROM vault";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

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

    public static List<String> getMiscData() {
        List<String> vaultMisc = new ArrayList<>();
        String sql = "SELECT websiteName FROM vault";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

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
