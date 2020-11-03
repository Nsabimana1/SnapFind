package com.example.snapfind.networking;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class SocketEchoThread extends Thread {
    private Socket socket;
    private ArrayList<ServerListener> listeners = new ArrayList<ServerListener>();

    public SocketEchoThread(Socket socket, ArrayList<ServerListener> listeners) {
        this.socket = socket;
        this.listeners.addAll(listeners);
    }

    @Override
    public void run() {
        try {
            String msg = Communication.receive(socket);
//            Communication.sendOver(socket, msg);
            socket.close();
            for (ServerListener listener: listeners) {
                listener.notifyMessage(msg);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
