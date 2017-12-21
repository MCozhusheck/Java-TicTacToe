package com.company;

import java.net.*;
import java.io.*;

public class Server extends Thread {
    private ServerSocket serverSocket;
    private Socket player1;
    private Socket player2;
    private DataInputStream in1;
    private DataInputStream in2;
    private SocketAddress player1Address;
    private SocketAddress player2Address;
    private Board board;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        board = new Board();
    }

    public void run() {
        while (true) {
            try {
                System.out.println("Waiting for client on port " +
                        serverSocket.getLocalPort() + "...");

                player1 = serverSocket.accept();
                player1Address = player1.getRemoteSocketAddress();
                System.out.println("Player1: " + player1Address);

                player2 = serverSocket.accept();
                player2Address = player2.getRemoteSocketAddress();
                System.out.println("Player2: " + player2Address);

                open();
                boolean done = false;
                while (!done) {
                    try{
                        String inputLine1 = in1.readUTF();
                        System.out.println("player1: " + inputLine1);
                        String inputLine2 = in2.readUTF();
                        System.out.println("player2: " + inputLine2);
                        done = (inputLine1.equals(".bye") || inputLine2.equals(".bye"));
                    } catch (IOException e){
                        done = true;
                    }
                }
                close();
            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
    private void open() throws IOException{
        in1 = new DataInputStream(new BufferedInputStream(player1.getInputStream()));
        in2 = new DataInputStream(new BufferedInputStream(player2.getInputStream()));
    }
    private void close() throws IOException{
        if (player1 != null) player1.close();
        if (player2 != null) player2.close();
        if (in1 != null) in1.close();
        if (in2 != null) in2.close();
    }
    private boolean validateInput(int[] field){

    }
}
