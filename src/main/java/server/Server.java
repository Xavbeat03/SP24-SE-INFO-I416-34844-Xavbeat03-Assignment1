package server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server{

    // initialize socket and input stream
    //TODO: Replace with server config file
    private Socket              socket      = null;
    private ServerSocket        server      = null;

    // Constructor with port
    public Server(int port) {
        try {
            server = new ServerSocket(port);
        } catch(IOException i) {
            System.out.println(i);
        }

        //Start server and wait for a connection
        try
        {
            socket = server.accept();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }

    }
    public static void main(String[] args){
        System.out.println("Hello, this is the Server");
        Server s = new Server(5000);
    }

}