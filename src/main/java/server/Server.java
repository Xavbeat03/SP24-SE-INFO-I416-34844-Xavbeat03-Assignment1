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
    private DataInputStream     in          = null;

    // Constructor with port
    public Server(int port) {
        //Start server and wait for a connection
        try
        {
            server = new ServerSocket(port);
            System.out.println("Server started...");

            System.out.println("Waiting for a client...");

            socket = server.accept();
            List<Client> clientList = new ArrayList<>();
            Client client = new Client();
            clientList.add(client.getId(), client);

            System.out.println("Client Accepted");

            // Take input from the client socket
            in = new DataInputStream(
                    new BufferedInputStream(
                            socket.getInputStream()
                    )
            );

            String line = "";

            // Reads message from client until "Over" is sent
            while(!line.equals("")){}

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