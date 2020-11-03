package com.example.snapfind.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static final int APP_PORT = 8888;

    private static Server instance = null;

    public static Server get() throws IOException {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    private ServerSocket accepter;
    private ArrayList<ServerListener> listeners = new ArrayList<ServerListener>();

    private String incomingIpAddress;

    private Server() throws IOException {
        accepter = new ServerSocket(APP_PORT);
    }

    public void addListener(ServerListener listener) {
        this.listeners.add(listener);
    }

    public void listen() throws IOException {
        for (;;) {
            listenOnce().start();
        }
    }

    public SocketEchoThread listenOnce() throws IOException {
        Socket s = accepter.accept();
        String incomingIP = s.getInetAddress().toString();
        incomingIpAddress =  incomingIP.substring(1, incomingIP.length());
        SocketEchoThread echoer = new SocketEchoThread(s, listeners);
        return echoer;
    }

    public String getIncomingIpAddress() {
        return incomingIpAddress;
    }
}
