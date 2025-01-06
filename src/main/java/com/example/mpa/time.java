package com.example.mpa;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import javafx.application.Platform;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class time {
    public static void main(String[] args) throws InterruptedException {

        ScreenTimeManager manager = new ScreenTimeManager();

        User32 user32 = User32.INSTANCE;

        char[] buffer = new char[512];

        boolean condition = true;
        long lastUpdateTime = System.currentTimeMillis();
        while (condition) {

            LocalTime currentTime = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            String formattedTime = currentTime.format(formatter);

            System.out.println("Current Time: " + formattedTime);

            HWND hwnd = user32.GetForegroundWindow();

            user32.GetWindowText(hwnd, buffer, 512);

            String windowTitle = Native.toString(buffer);
            System.out.println("Active Window Title: " + windowTitle);

            long currentTimeMillis = System.currentTimeMillis();
            long timeSpentInMillis = currentTimeMillis - lastUpdateTime;

            manager.updateAppScreenTime(windowTitle, timeSpentInMillis);


            System.out.println("Current Screen Time Data:");
            manager.printScreenTime();

            lastUpdateTime = currentTimeMillis;

            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
