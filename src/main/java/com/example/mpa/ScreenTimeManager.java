package com.example.mpa;

import java.sql.*;
import java.util.concurrent.TimeUnit;
public class ScreenTimeManager {

    public void updateAppScreenTime(String applicationName, long activeTime, int userId) {
        String sql = """
            INSERT INTO screenmonitoring (applicationName, activeTime, userId)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE activeTime = activeTime + ?;
        """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, applicationName);
            pstmt.setLong(2, activeTime);
            pstmt.setInt(3, userId);
            pstmt.setLong(4, activeTime);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public double printSumTime(int userId) {
        String sql = "SELECT SUM(activeTime) FROM screenmonitoring WHERE userId = ?";
        double screenSumData = 0.0;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                double activeTimeInMillis = rs.getDouble("SUM(activeTime)");
                screenSumData = activeTimeInMillis / 86400000;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return screenSumData;
    }

    public String printScreenTime(int userId) {
        String sql = "SELECT * FROM screenmonitoring WHERE userId = ? ORDER BY activeTime DESC";
        StringBuilder screenTimeData = new StringBuilder();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String applicationName = rs.getString("applicationName");
                long activeTimeInMillis = rs.getLong("activeTime");
                String formattedTime = conversion(activeTimeInMillis);
                screenTimeData.append(String.format("%s, %s%n", applicationName, formattedTime));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return screenTimeData.toString();
    }
    private String conversion(long millis) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }


    public long getActiveTime(int userId) {
        String sql = "SELECT activeTime FROM screenmonitoring WHERE userId = ?";
        long activeTime = 0;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                activeTime = rs.getLong("activeTime");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activeTime;
    }


    public void deleteAppScreenTime(String applicationName, int userId) {
        String sql = "DELETE FROM screenmonitoring WHERE applicationName = ? AND userId = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, applicationName);
            pstmt.setInt(2, userId);

            int rowsAffected = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: Unable to delete screen time data.");
            e.printStackTrace();
        }
    }
    public int getUserId(String userName) {
        String sql = "SELECT userId FROM userdata WHERE login = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("userId");
            }

        } catch (SQLException e) {
            System.out.println("Error: Unable to retrieve userId.");
            e.printStackTrace();
        }
        return -1;
    }
}

