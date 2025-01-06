package com.example.mpa;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class client {
    public static void main(String[] args) throws IOException {
        Socket s = new Socket("localhost", 9000);

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader br = new BufferedReader(in);

        while(true){
            PrintWriter pr = new PrintWriter(s.getOutputStream());
            Scanner sc = new Scanner(System.in);
            String line = sc.nextLine();
            pr.println(line);
            pr.flush();



            String str = br.readLine();
            System.out.println("server : " + str);

        }


    }
}