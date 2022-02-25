package com.company;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


//java -jar Lab0.jar
public class Main {
    public static void main(String[] args) {
        System.out.println("Enter your username:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String login = null;
        try {
            login = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Lab0Chat");
        Client client = new Client(login);
        client.run();
        System.out.println("End");
    }
}
