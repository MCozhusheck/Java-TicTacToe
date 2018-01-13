package com.company;

import javax.xml.crypto.Data;
import java.net.*;
import java.io.*;

public class Server extends Thread {
    private ServerSocket serverSocket;
    private Socket player1;
    private Socket player2;
    private DataInputStream in1;
    private DataInputStream in2;
    private DataOutputStream out1;
    private DataOutputStream out2;
    private Board board;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        board = new Board();
    }
    public void run() {
        while (true) {
            try {
                connectToPlayers();
                open();
                boolean done = false;

                while (!done) {
                    try {
                        readAndUpdateBoard(in1,out1);
                        if (checkIfSomeoneWon()) {
                            announceWinner();
                            done = true;
                        }
                        sendBoardToPlayers();
                        readAndUpdateBoard(in2,out2);
                        if (checkIfSomeoneWon()) {
                            announceWinner();
                            done = true;
                        }
                        sendBoardToPlayers();
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
    private void connectToPlayers() throws IOException{
        System.out.println("Waiting for client on port " +
                serverSocket.getLocalPort() + "...");

        player1 = serverSocket.accept();
        System.out.println("Connected to player 1");
        player2 = serverSocket.accept();
        System.out.println("Connected to player 2");
    }
    private void open() throws IOException{
        in1 = new DataInputStream(new BufferedInputStream(player1.getInputStream()));
        in2 = new DataInputStream(new BufferedInputStream(player2.getInputStream()));
        out1 = new DataOutputStream(player1.getOutputStream());
        out2 = new DataOutputStream(player2.getOutputStream());
    }
    private void close() throws IOException{
        if (player1 != null) player1.close();
        if (player2 != null) player2.close();
        if (in1 != null) in1.close();
        if (in2 != null) in2.close();
        if (out1 != null) out1.close();
        if (out2 != null) out2.close();
    }
    private int[] readSign(DataOutputStream out, DataInputStream in) throws IOException{
        int[] pos;

        out.writeUTF("Write row: ");
        out.flush();
        System.out.println("line 84 server");
        int row = in.readInt();
        System.out.println("line 86 server");

        out.writeUTF("Write column: ");
        out.flush();
        int col = in.readInt();
        pos = new int[]{row,col};

        return pos;
    }
    private boolean putSign (int[] pos){
        boolean isOk = true;

        if (pos.length > 2){
            return false;
        } else if (pos[0] > 2) {
            return false;
        } else if (pos[1] > 2) {
            return false;
        }
        if(board.putSign(pos) < 0)
            isOk = false;

        return isOk;
    }
    private boolean checkIfSomeoneWon(){
        return (board.checkWinner() != null);
    }
    private void  readAndUpdateBoard(DataInputStream in, DataOutputStream out) throws IOException{
        boolean done = false;

        while (!done){
            int[] position = readSign(out,in);
            done = putSign(position);
            if (!done) {
                out.writeUTF("Invalid position!");
                out.flush();
            }
        }
    }
    private void sendBoardToPlayers() throws IOException{
        out1.writeUTF(board.sendBoard());
        out1.flush();
        out2.writeUTF(board.sendBoard());
        out2.flush();
        System.out.println(board.sendBoard());
    }
    private void announceWinner() throws IOException{
        String winner = board.checkWinner();
        if (winner.equals(Board.PLAYERS[0])) {
            out1.writeUTF("You won!");
            out1.flush();
            out2.writeUTF("Player 1 won game!");
            out2.flush();
        } else {
            out1.writeUTF("Player 2 won game!");
            out1.flush();
            out2.writeUTF("You won!");
            out2.flush();
        }
    }
}
