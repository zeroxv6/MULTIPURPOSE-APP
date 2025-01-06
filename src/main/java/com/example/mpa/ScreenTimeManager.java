package com.example.mpa;

import java.sql.*;
import java.util.concurrent.TimeUnit;
public class ScreenTimeManager {

    public void updateAppScreenTime(String applicationName, long activeTime) {
        String sql = """
            INSERT INTO screenMonitoring (applicationName, activeTime)
            VALUES (?, ?)
            ON DUPLICATE KEY UPDATE activeTime = activeTime + ?;
        """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, applicationName);
            pstmt.setLong(2, activeTime);
            pstmt.setLong(3, activeTime);

            pstmt.executeUpdate();
//            System.out.println("Updated screen time for " + applicationName);
        } catch (SQLException e) {
//            System.out.println("Error: Unable to update screen time.");
            e.printStackTrace();
        }
    }
    public double printSumTime() {
        String sql = "SELECT SUM(activeTime) FROM screenMonitoring";
//        StringBuilder screenSumData = new StringBuilder();
        double screenSumData = 0.0;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                double activeTimeInMillis = rs.getDouble("SUM(activeTime)");

                screenSumData = activeTimeInMillis/86400000;
//                screenSumData.append(String.format("%s", formattedTime));
            }
        } catch (SQLException e) {
//            System.out.println("Error: Unable to retrieve screen sum data.");
            e.printStackTrace();
        }
        return screenSumData;
    }

    public String printScreenTime() {
        String sql = "SELECT * FROM screenMonitoring ORDER BY activeTime DESC";
        StringBuilder screenTimeData = new StringBuilder();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String applicationName = rs.getString("applicationName");
                long activeTimeInMillis = rs.getLong("activeTime");
                String formattedTime = conversion(activeTimeInMillis);

                screenTimeData.append(String.format("%s, %s%n", applicationName, formattedTime));
            }
        } catch (SQLException e) {
//            System.out.println("Error: Unable to retrieve screen time data.");
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


    public long getActiveTime() {
        String sql = "SELECT activeTime FROM screenMonitoring";

        long activeTime = 0;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                rs.getLong("activeTime");
            }
        } catch (SQLException e) {
//            System.out.println("Error: Unable to retrieve screen time data.");
            e.printStackTrace();
        }
        return activeTime;
    }


    public void deleteAppScreenTime(String applicationName) {
        String sql = "DELETE FROM screenMonitoring WHERE applicationName = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, applicationName);


            int rowsAffected = pstmt.executeUpdate();
//            if (rowsAffected > 0) {
//                System.out.println("Deleted screen time data for " + applicationName);
//            } else {
//                System.out.println("No data found for " + applicationName);
//            }
        }
        catch (SQLException e) {
            System.out.println("Error: Unable to delete screen time data.");
            e.printStackTrace();
        }
    }
}

