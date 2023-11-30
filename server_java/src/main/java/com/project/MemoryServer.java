package com.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

class MemoryServer extends WebSocketServer {
    private boolean isAlive;
    private static BufferedReader inputReader;

    public MemoryServer(int port) {
        super();

        isAlive = false;
        inputReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        
    }

    @Override
    public void onStart() {
        
    }

    public void runServerBucle() {
        isAlive = true;
        String input = "";
        
        start();
        while (isAlive) {
            try {
                input = inputReader.readLine();

                if(input.equals("stop")) {
                    isAlive = false;
                }

            } catch (IOException e)  {
                // implement Log
            }
        }

        try {
            stop(1000);
        } catch (InterruptedException e) {
            // implement log
        }
    }

    
}