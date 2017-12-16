package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Board game1 = new Board();
        game1.putSign(new int[]{0,0});
        game1.putSign(new int[]{1,1});
        game1.putSign(new int[]{0,2});
        game1.putSign(new int[]{1,2});
        game1.putSign(new int[]{0,1});
    }
}
