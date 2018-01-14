package com.company;

import javax.xml.crypto.Data;
import java.net.*;
import java.io.*;

public class Server extends Thread {
    private ServerSocket server;
    private Socket player1;
    private Socket player2;
    private DataInputStream in1;
    private DataInputStream in2;
    private DataOutputStream out1;
    private DataOutputStream out2;
    private Board board;

    public Server(int port) throws IOException {
        try {
            System.out.println("Binding to port " + port + ", please wait  ...");
            server = new ServerSocket(port);
            System.out.println("Server started: " + server);
            board = new Board();
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
    public void run() {
        try {
            connectToPlayers();
            open();
            boolean done = false;
            while (!done) {
                try {
                    out2.writeUTF("player 1 move");
                    insertSign(readPosition(in1,out1));
                    sendBoardToPlayers();
                    done = (board.checkWinner() != null);
                    if (done){
                        out1.writeUTF(".bye");
                        out2.writeUTF(".bye");
                    }
                    out1.writeUTF("player 2 move");
                    insertSign(readPosition(in2,out2));
                    sendBoardToPlayers();
                    done = (board.checkWinner() != null);
                    if (done){
                        out1.writeUTF(".bye");
                        out2.writeUTF(".bye");
                    }
                } catch (IOException ioe) {
                    done = true;
                }
            }
            close();
        } catch (IOException ie) {
            System.out.println("Acceptance Error: " + ie);
        }
    }
    private void connectToPlayers(){
        try {
            System.out.println("Waiting for a client ...");
            player1 = server.accept();
            System.out.println("Client accepted: " + player1);

            System.out.println("Waiting for a client ...");
            player2 = server.accept();
            System.out.println("Client accepted: " + player2);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    private void open() throws IOException {
        in1 = new DataInputStream(new BufferedInputStream(player1.getInputStream()));
        in2 = new DataInputStream(new BufferedInputStream(player2.getInputStream()));

        out1 = new DataOutputStream(player1.getOutputStream());
        out2 = new DataOutputStream(player2.getOutputStream());
    }
    private void close() throws IOException {
        if (player1 != null) player1.close();
        if (in1 != null) in1.close();
        if (out1 != null) out1.close();

        if (player2 != null) player2.close();
        if (in2 != null) in2.close();
        if (out2 != null) out2.close();
    }
    private int[] readPosition(DataInputStream in, DataOutputStream out) throws IOException {
        int[] position = new int[2];
        boolean inputIsCorrect = false;
        while (!inputIsCorrect) {
            out.writeUTF("write row (from 1 to 3): ");
            position[0] = Integer.parseInt(in.readUTF()) - 1;
            out.writeUTF("write column (from 1 to 3): ");
            position[1] = Integer.parseInt(in.readUTF()) - 1;
            inputIsCorrect = validatePosition(position);
            if (!inputIsCorrect)
                out.writeUTF("This position is invalid!");
        }
        return position;
    }
    private boolean validatePosition(int[] pos){
        if (pos[0] > 2 || pos[1] > 2)
            return false;
        if (!board.fieldIsEmpty(pos))
            return false;
        return true;
    }
    private void insertSign(int[] pos){
        board.putSign(pos);
    }
    private void sendBoardToPlayers() throws IOException{
        out1.writeUTF(board.showBoard());
        out2.writeUTF(board.showBoard());
    }
}
