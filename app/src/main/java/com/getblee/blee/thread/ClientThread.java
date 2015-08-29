package com.getblee.blee.thread;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by malhar on 25/4/15.
 */
public class ClientThread implements Runnable {

    private Socket socket;
    private static int SERVERPORT = 6000;
    private String SERVER_IP = "192.168.43.48";
    private String data = "";

    public ClientThread(String SERVER_IP, String data)
    {
        this.SERVER_IP = SERVER_IP;
        this.data = data;
    }


    @Override
    public void run() {
        try {
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
            socket = new Socket(serverAddr, SERVERPORT);
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println(data);

        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}