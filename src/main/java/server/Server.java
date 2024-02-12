package server;

import server.client.Client;
import server.client.ClientHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{

    // initialize socket and input stream
    private Socket              socket      = null;
    private ServerSocket        server      = null;



     /**
     * The main server task, creates a server on the specified port
     * @param port the port the server is started on
     * @throws IOException
     */
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

                // create a new thread object
                ClientHandler t = new ClientHandler(socket, dataInputStream, dataOutputStream);

                // create a new client
                Client c = new Client(t);

                t.start();

                if(Client.isNoClients()){break;}
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