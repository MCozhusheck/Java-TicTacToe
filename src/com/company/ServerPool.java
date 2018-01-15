package com.company;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerPool {
    private int maxThreads;
    private int activeThreads;
    private ExecutorService executor;

    public ServerPool(int maxThreads, int activeThreads) {
        this.maxThreads = maxThreads;
        this.activeThreads = activeThreads;
        executor = Executors.newFixedThreadPool(activeThreads);
    }
    public void startServer(){
        for (int i = 0; i < maxThreads; i++) {
            Runnable worker = new Server(6666);
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
    }
}
