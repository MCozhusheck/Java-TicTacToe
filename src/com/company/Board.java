package com.company;

public class Board {
    public static final String[] PLAYERS = {"X","O"};
    private String[][] board;
    private String currentPlayer;
    private String winner;

    public Board(){
        board = new String[3][3];
        currentPlayer = PLAYERS[0];
    }
    public int putSign(int[] field){
        if (fieldIsEmpty(field)) {
            board[field[0]][field[1]] = currentPlayer;
            showBoard();
            String winner = checkWinner();
            if (winner != null){
                //System.out.println("Player: " + winner + " won match!\n");
                this.winner = winner;
                return 1;
            }
            switchPlayer();
            return 0;
        } else {
            return -1;
        }
    }
    public String sendBoard(){
        String boardString ="";
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                if(board[i][j] != null){
                    boardString += board[i][j];
                } else {
                    boardString += "/";
                }
            }
        }
        return boardString;
    }
    public void loadBoard(String input) {
        int j = -1;
        String[][] newBoard = new String[3][3];
        for (int i = 0; i < input.length(); i++) {
            if((i%3) == 0)
                j++;
            String sign = input.substring(i,i+1);
            //System.out.println(sign);
            if(!sign.equals("/")) {
                newBoard[j][i%3] = sign;
            } else {
                newBoard[j][i%3] = null;
            }
        }
        this.board = newBoard;
    }
    private void showBoard(){
        for (int i=0; i<3; i++) {
            for (int j=0; j<2; j++){
                if (board[i][j] != null) {
                    System.out.print(board[i][j] + " | ");
                } else {
                    System.out.print("  | ");
                }
            }
            if (board[i][2] != null) {
                System.out.print(board[i][2] + "\n");
            } else {
                System.out.print(" \n");
            }
        }
        System.out.println();
    }
    private boolean fieldIsEmpty(int[] field){
        return (board[field[0]][field[1]] == null);
    }
    private void switchPlayer(){
        if (currentPlayer .equals(PLAYERS[0])){
            currentPlayer = PLAYERS[1];
        } else if (currentPlayer.equals(PLAYERS[1])){
            currentPlayer = PLAYERS[0];
        }
    }
    public String checkWinner(){
        String winner = null;

        for (int i=0; i<3; i++){
            if(board[i][0] != null && board[i][0].equals(board[i][1])
                    && board[i][1].equals(board[i][2])){
                winner = board[i][0];
                return winner;
            } else if (board[0][i] != null && board[0][i].equals(board[1][i])
                    && board[1][i].equals(board[2][i])){
                winner = board[0][i];
                return winner;
            }
        }
        if(board[0][0] != null && board[0][0].equals(board[1][1])
                && board[1][1].equals(board[2][2])){
            winner = board[0][0];
            return winner;
        } else if (board[0][2] != null && board[0][2].equals(board[1][1])
                && board[1][1].equals(board[2][0])){
            winner = board[0][2];
            return winner;
        }
        return winner;
    }
    private void clearBoard(){
        board = new String[3][3];
    }
}
