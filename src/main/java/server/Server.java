package server;

import server.client.Client;
import server.client.ClientHandler;
import server.requests.RequestFulfiller;
import server.requests.RequestQueue;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * The Server class is responsible for creating a server on a specified port, accepting client connections, and handling client requests.
 * It uses a RequestFulfiller to handle incoming requests and a ClientHandler to manage each client connection.
 */
public class Server{

    /**
     * The socket for the client connection.
     */
    private Socket              socket      = null;
    /**
     * The server socket that listens for client connections.
     */
    private ServerSocket        server      = null;
    /**
     * The RequestFulfiller used to handle incoming client requests.
     */
    private RequestFulfiller requestFulfiller;
    /**
     * A flag indicating whether the server is currently running.
     */
    private boolean isRunning = false;

    private int activeConnections = 0;

    private static List<ServerSocket> serverList = new ArrayList<>();



    /**
     * Constructs a new Server that listens on the specified port.
     *
     * @param port the port the server is started on
     * @throws IOException if an I/O error occurs when opening the socket
     */
    public Server(int port) throws IOException{
        try {
            server = new ServerSocket(port);
            server.setReuseAddress(true);
            Server.serverList.add(server);
            requestFulfiller = new RequestFulfiller();
        } catch (BindException b) {
            System.out.println("Port " + port + " is already in use. Please use a different port or ensure no other instances of the server are running.");
            System.exit(1);
        } catch (IOException i) {
            i.printStackTrace();
        }

    }

    /**
     * The main server loop. Accepts incoming client connections and handles their requests.
     *
     * @param s the server socket
     * @param requestFulfiller the request fulfiller
     * @throws IOException if an I/O error occurs when accepting a connection
     */
    private void run(ServerSocket s, RequestFulfiller requestFulfiller) throws IOException {
        // running infinite loop for getting
        // client request
        while (true) {
            socket = null;
            try
            {
                // socket object to receive incoming client requests
                if(server != null && !server.isClosed()) {
                    socket = server.accept();
                    activeConnections++;

                    System.out.println("A new client is connected : " + s);
                    System.out.println("Assigning new thread for this client");

                    // create a new thread object
                    ClientHandler t = new ClientHandler(socket);
                    // create a new client
                    Client c = new Client(t);

                    t.start();

                    // start handling requests
                    if(!RequestQueue.isNoRequests()) requestFulfiller.start();

                    // Decrement the active connections counter when a client disconnects
                    for(Client client : Client.getClientMap().values()) {
                        if (client == null) continue;
                        if(client.getClientHandler().isClientDisconnected()){
                            Client.getClientMap().put(client.getId(), null);
                            activeConnections--;
                        }
                    }

                    if (activeConnections == 0) {
                        //stop();
                    }

                } else {
                    return;
                }

            }
            catch (Exception e)
            {
                if(isRunning)stop();
                e.printStackTrace();
            }

        }
    }


    public static void main(String[] args) throws IOException {
        // server is listening on port 5000
        new Server(5532);
    }

    /**
     * Starts the server. If the server is already running, throws an IllegalStateException.
     *
     * @throws IOException if an I/O error occurs when opening the socket
     */
    public synchronized void start() throws IOException {
        if(!isRunning) {
            isRunning = true;
            new Thread(() -> {
                try {
                    run(server, requestFulfiller);
                } catch (IOException i) {
                    i.printStackTrace();
                }
            }
            ).start();
        } else {
            throw new IllegalStateException("Tried to start the server while it was on.");
        }
    }

    /**
     * Stops the server. If the server is not running, throws an IllegalStateException.
     *
     * @throws IOException if an I/O error occurs when closing the socket
     */
    public synchronized void stop() throws IOException {
        if(isRunning) {
            if (socket != null) {
                socket.close();
            }
            if (server != null) {
                server.close();
            }
            isRunning = false;
        } else {
            throw new IllegalStateException("Server cannot be stopped before its started.");
        }
    }

    /**
     * Checks whether the server is currently running.
     *
     * @return true if the server is running, false otherwise
     */
    public boolean isRunning(){
        return isRunning;
    }



}