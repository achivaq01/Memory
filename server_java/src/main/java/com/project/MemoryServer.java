package com.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.UUID;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

class MemoryServer extends WebSocketServer implements Colors {
    // need to implement logType constants
    private final String HIDDEN = "-";
    private final String REVEALED = "O";
    private final String FOUND = "+";

    private final HashMap<String, WebSocket> connectionMap;

    private static BufferedReader inputReader;
    private boolean isAlive;
    private String[][] cardArray;

    /**
     * class constructor
     * 
     * @param port
     */
    public MemoryServer(int port) {
        super(new InetSocketAddress(port));

        isAlive = false;
        inputReader = new BufferedReader(new InputStreamReader(System.in));
        connectionMap = new HashMap<String, WebSocket>();
    }

    /**
     * server bucle
     * 
     */
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
                serverLog(e.getMessage(), 0);
            }
        }

        try {
            stop(1000);
        } catch (InterruptedException e) {
            serverLog(e.getMessage(), 0);
        }
    }

    /**
     * server custom log method
     * 
     * @param exception
     * @param logType
     */
    private void serverLog(String log, int logType) {

        switch (logType) {
            default:
                System.out.println(log);
                break;

        }
    }

    /**
     * returns a random UUID
     * 
     * @return
     */
    public String getClientUUID() {
        UUID clientUUID = UUID.randomUUID();

        return clientUUID.toString();
    }
    
    public void startCardArray() {
        cardArray = new String[8][2];

        for(String[] card : cardArray) {
            card[0] = HIDDEN;
        }
    }

    /**
     * sends a greetings message
     * 
     * @param connection
     * @param id
     */
    private void sendGreetingsMessage(WebSocket connection, String id) {
        JSONObject greetingsMessage = new JSONObject();

        greetingsMessage.put("type", "greetings");
        greetingsMessage.put("id", id);

        connection.send(greetingsMessage.toString());
    }

    @Override
    public void onOpen(WebSocket connection, ClientHandshake handshake) {
        String clientUUID = getClientUUID();
        sendGreetingsMessage(connection, clientUUID);

        serverLog("New client with ID: " + clientUUID + " connected!", 0);

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
        String host = getAddress().getAddress().getHostAddress();
        int port = getAddress().getPort();
        System.out.println("WebSockets server running at: ws://" + host + ":" + port);
        System.out.println("Type 'stop' to stop and exit server.");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);        

    }
    
}