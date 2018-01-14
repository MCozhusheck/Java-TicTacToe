package com.company;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client extends Thread{
    private Socket socket = null;
    private BufferedReader console = null;
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;
    private boolean gameEnds;

    public Client(String serverName, int serverPort) {
        gameEnds = false;
        System.out.println("Establishing connection. Please wait ...");
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
        start();
        while (!line.equals(".bye")) {
            try {
                line = console.readLine();
                streamOut.writeUTF(line);
                streamOut.flush();
            } catch (IOException ioe) {
                System.out.println("Sending error: " + ioe.getMessage());
                gameEnds = true;
            }
        }
        gameEnds = true;
        close();
    }
    public void run(){
        String inputFromServer;
        while (!gameEnds){
            try {
                inputFromServer = streamIn.readUTF();
                System.out.println(inputFromServer);
                if (inputFromServer.equals(".bye"))
                    gameEnds = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void open() throws IOException {
        console = new BufferedReader(new InputStreamReader(System.in));
        streamOut = new DataOutputStream(socket.getOutputStream());
        streamIn = new DataInputStream(socket.getInputStream());
    }
    public void close() {
        try {
            if (console != null) console.close();
            if (streamOut != null) streamOut.close();
            if (streamIn != null) streamIn.close();
            if (socket != null) socket.close();
        } catch (IOException ioe) {
            System.out.println("Error closing ...");
        }
    }
}
