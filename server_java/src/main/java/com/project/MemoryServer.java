package com.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

class MemoryServer extends WebSocketServer implements Colors {
    // need to implement logType constants
    private final String GETCARD = "card";
    private final String DISCONNECTION = "disconnection";
    private final String CONNECTION = "connection";
    private final String HIDDEN = "-";
    private final String REVEALED = "O";
    private final String FOUND = "+";
    private final int BOARD_SIZE = 4;

    private final HashMap<String, WebSocket> connectionMap;

    private static BufferedReader inputReader;
    private boolean isAlive;
    private String[][][] memoryBoard;

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
    
    public void startMemoryBoard() {
        List<String> colorList = new ArrayList<>(Arrays.asList(Colors.colorList));
        memoryBoard = new String[BOARD_SIZE][BOARD_SIZE][2];

        for(int i = 0; i < BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++) {
                memoryBoard[i][j][0] = "-";
                memoryBoard[i][j][1] = colorList.get(0);
            }
        }

        //Collections.shuffle(memoryBoard);
    }

    public void sendCardList(WebSocket connection)  {
        JSONObject cardListMessage = new JSONObject();
        cardListMessage.put("type", "cardList");
        cardListMessage.put("list", memoryBoard);

        connection.send(cardListMessage.toString());
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

    private void sendCard(WebSocket connection, int column, int row) {
        JSONObject cardMessage = new JSONObject();

        cardMessage.put("type", "card");

//        String[] card = cardList.
//        cardMessage.put("card", cardMessage);
    }

    @Override
    public void onOpen(WebSocket connection, ClientHandshake handshake) {
        String clientUUID = getClientUUID();
        sendGreetingsMessage(connection, clientUUID);
        connectionMap.put(clientUUID, connection);

        sendCardList(connection);

        serverLog("New client with ID: " + clientUUID + " connected!", 0);

    }
    
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        serverLog("client disconnected", 1);
    }

    @Override
    public void onMessage(WebSocket connection, String message) {
        JSONObject receivedMessage = new JSONObject(message);
        String type = receivedMessage.getString("type");

        switch(type) {
            case DISCONNECTION:
                connectionMap.remove(receivedMessage.getString("id"));
                break;
            
            case GETCARD:

        }

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

        startCardList();
    }
    
}