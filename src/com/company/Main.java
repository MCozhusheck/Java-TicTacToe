package com.company;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("choose 1 to create server on port 6666");
        System.out.println("choose 2 to create client");

        String input = d.readLine();

        if (input.equals("1")) {
            ServerPool serverPool = new ServerPool(10,1);
            serverPool.startServer();
        } else {
            new Client("localhost",6666).startGame();
        }
    }
}
