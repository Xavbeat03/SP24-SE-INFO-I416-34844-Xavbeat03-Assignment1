package server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server{

    // initialize socket and input stream
    //TODO: Replace with server config file
    private Socket              socket      = null;
    private ServerSocket        server      = null;

    private HashMap<Client, Thread> idToClientHandlerMap = new HashMap<>();

    // Constructor with port
    public Server(int port) throws IOException{
        // server is listening on port <port>
        ServerSocket s = new ServerSocket(port);

        // running infinite loop for getting
        // client request
        while (true) {
            socket = null;
            try
            {
                // socket object to receive incoming client requests
                socket = server.accept();

                System.out.println("A new client is connected : " + s);

                // obtaining input and out streams
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                System.out.println("Assigning new thread for this client");

                Client c = new Client();

                // create a new thread object
                Thread t = new ClientHandler(socket, dataInputStream, dataOutputStream);

                idToClientHandlerMap.put(c,t);

                t.start();
            }
            catch (Exception e)
            {
                socket.close();
                e.printStackTrace();
            }
        }


    }
    public static void main(String[] args) throws IOException {
        // server is listening on port 5000
        new Server(5000);
    }

}