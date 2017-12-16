package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        GreetingServer greetingServer = new GreetingServer(6666);
        greetingServer.run();
        //GreetingClient greetingClient = new GreetingClient();
        //greetingClient.greet();
    }
}
