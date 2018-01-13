package com.company;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client extends Thread{
    private Socket socket = null;
    private Scanner console = null;
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;
    private boolean gameEnds;

    public Client(String serverName, int serverPort) {
        System.out.println("Establishing connection. Please wait ...");
        gameEnds = false;
        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            open();
        } catch (UnknownHostException uhe) {
            System.out.println("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe) {
            System.out.println("Unexpected exception: " + ioe.getMessage());
        }
        String line = "";
        while (!line.equals(".bye")) {
            try {
                run();
                line = console.nextLine();
                streamOut.writeUTF(line);
                streamOut.flush();
            } catch (IOException ioe) {
                System.out.println("Sending error: " + ioe.getMessage());
            }
        }
        gameEnds = true;
        close();
    }
    public void run(){

        while (!gameEnds){
            try {
                String message = streamIn.readUTF();
                System.out.println(message);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    private void open() throws IOException {
        console = new Scanner(System.in);
        streamOut = new DataOutputStream(socket.getOutputStream());
        streamIn = new DataInputStream(socket.getInputStream());
    }
    private void close() {
        try {
            if (console != null) console.close();
            if (streamOut != null) streamOut.close();
            if (socket != null) socket.close();
            if (streamIn != null) streamIn.close();
        } catch (IOException ioe) {
            System.out.println("Error closing ...");
        }
    }
}
