package com.example.mpa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class scheduleManager {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public void addSchedule(int userId, String title, String timeFrom, String timeTo, LocalDate dateFrom, LocalDate dateTo, String topics) {
        String sql = "INSERT INTO Schedule (user_id, title, time_from, time_to, date_from, date_to, topics) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, title);
            pstmt.setString(3, timeFrom);
            pstmt.setString(4, timeTo);
            pstmt.setDate(5, java.sql.Date.valueOf(dateFrom));
            pstmt.setDate(6, java.sql.Date.valueOf(dateTo));
            pstmt.setString(7, topics);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String getTitle(int userId) {
        String sql = "SELECT title FROM Schedule WHERE user_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("title");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String getTimeFrom(int userId) {
        String sql = "SELECT time_from FROM Schedule WHERE user_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getTime("time_from").toString();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public String getTimeTo(int userId) {
        String sql = "SELECT time_to FROM Schedule WHERE user_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getTime("time_to").toString();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public List<timeController.Schedule> getAllSchedules(int userId) {
        List<timeController.Schedule> schedules = new ArrayList<>();
        String sql = "SELECT title, time_from, time_to FROM Schedule WHERE user_id = ? AND ? BETWEEN DATE(date_from) AND DATE(date_to)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

            pstmt.setInt(1, userId);
            pstmt.setDate(2, currentDate);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String timeFrom = rs.getTime("time_from").toLocalTime().format(TIME_FORMATTER);
                String timeTo = rs.getTime("time_to").toLocalTime().format(TIME_FORMATTER);
                schedules.add(new timeController.Schedule(title, timeFrom, timeTo));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return schedules;
    }
    public List<timeController.Schedule> getCurrentSchedules(int userId) {
        List<timeController.Schedule> schedules = new ArrayList<>();
        String sql = "SELECT title, time_from, time_to FROM Schedule WHERE user_id = ? AND ? BETWEEN DATE(date_from) AND DATE(date_to) AND ? BETWEEN time_from AND time_to";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
            java.sql.Time currentTime = new java.sql.Time(System.currentTimeMillis());
            System.out.println(currentTime);
            pstmt.setInt(1, userId);
            pstmt.setDate(2, currentDate);
            pstmt.setTime(3, currentTime);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String timeFrom = rs.getTime("time_from").toLocalTime().format(TIME_FORMATTER);
                String timeTo = rs.getTime("time_to").toLocalTime().format(TIME_FORMATTER);
                schedules.add(new timeController.Schedule(title, timeFrom, timeTo));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return schedules;
    }
    public List<String> getCurrentTopics(int userId) {
        List<String> topics = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        String currentDate = now.toLocalDate().toString();
        String currentTime = now.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        System.out.println("Fetching topics for:");
        System.out.println("User ID: " + userId);
        System.out.println("Current Date: " + currentDate);
        System.out.println("Current Time: " + currentTime);

        String query = "SELECT topics FROM Schedule " +
                "WHERE user_id = ? " +
                "AND ? >= DATE(date_from) " +
                "AND ? <= DATE(date_to) " +
                "AND ? >= time_from " +
                "AND ? <= time_to";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, currentDate);
            pstmt.setString(3, currentDate);
            pstmt.setString(4, currentTime);
            pstmt.setString(5, currentTime);

            System.out.println("Executing query: " + query);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String topicsStr = rs.getString("topics");
                    System.out.println("Found topics: " + topicsStr);

                    if (topicsStr != null && !topicsStr.isEmpty()) {
                        Arrays.stream(topicsStr.split(","))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .forEach(topic -> {
                                    topics.add(topic);
                                    System.out.println("Added topic: " + topic);
                                });
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Total topics found: " + topics.size());
        return topics;
    }


}
