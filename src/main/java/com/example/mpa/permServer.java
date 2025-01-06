package com.example.mpa;

import java.io.*;
import java.net.*;

public class permServer {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(9000);
            System.out.println("Server started. Waiting for clients to connect...");

            while (true) {
                try {
                    socket = serverSocket.accept();
                    System.out.println(" [-] Client connected");

                    InputStreamReader in = new InputStreamReader(socket.getInputStream());
                    BufferedReader br = new BufferedReader(in);
                    PrintWriter pr = new PrintWriter(socket.getOutputStream());

                    String str;
                    while (true) {
                        try {
                            str = br.readLine();
                            if (str == null) {
                                System.out.println("[-] Client disconnected.");
                                break;
                            }
//                            receiveImage(socket);

                            System.out.println("Client: " + str);
                            pr.println(str);
                            pr.flush();
                        } catch (IOException e) {
                            System.out.println("[-] Error reading from client: " + e.getMessage());
                            break;
                        }
                    }
                    br.close();
                    pr.close();
                    socket.close();
                    System.out.println("[-] Connection closed.");

                } catch (IOException e) {
                    System.out.println("[-] Error accepting client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("[-] Error starting server: " + e.getMessage());
        } finally {
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.out.println("[-] Error closing server socket: " + e.getMessage());
            }
        }
    }
    // Function to receive an image from the client
    private static void receiveImage(Socket socket) {
        try {
            // Get the input stream to read data from the client
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            // Read the length of the image data (first 4 bytes)
            int length = dataInputStream.readInt();
            byte[] imageData = new byte[length];

            // Read the image data
            dataInputStream.readFully(imageData);

            // Save the image data to a file named "received_image.jpg"
            FileOutputStream fileOutputStream = new FileOutputStream("received_image.jpg");
            fileOutputStream.write(imageData);
            fileOutputStream.close();

            System.out.println("[-] Image received and saved.");

        } catch (IOException e) {
            // Handle error when receiving the image
            System.out.println("[-] Error receiving image: " + e.getMessage());
        }
    }
}

