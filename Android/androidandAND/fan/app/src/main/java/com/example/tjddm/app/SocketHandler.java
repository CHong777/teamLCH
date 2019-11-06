package com.example.tjddm.app;

import android.app.Application;
import android.os.Bundle;
import java.io.Serializable;
import java.net.Socket;

@SuppressWarnings("serial")
public class SocketHandler extends Application{
    private static Socket socket;

    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket){
        SocketHandler.socket = socket;
    }
}
