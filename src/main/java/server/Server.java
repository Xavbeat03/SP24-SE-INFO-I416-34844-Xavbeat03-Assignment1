package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server{

    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        int i = 0;
        // server is listening on port 5056
        ServerSocket ss = new ServerSocket(5056);

        // running infinite loop for getting
        // client request
        while (true)
        {
            Socket s = null;

            try
            {
                // socket object to receive incoming client requests
                s = ss.accept();

                System.out.println("A new client is connected : " + s);

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");

                // create a new thread object
                Thread t = new ClientHandler(s, dis, dos, i++);

                // Invoking the start() method
                t.start();

                System.out.println(clientHandlers.size());

            }
            catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds handler c to the handler list
     * @param c the handler to be added
     */
    public static synchronized void addHandler(ClientHandler c){
        getHandlers().add(c);
    }

    /**
     * Removes handler c from the handler list
     * @param c the handler to be removed
     */
    public static synchronized void removeHandler(ClientHandler c){
        getHandlers().remove(c);
    }

    /**
     * Gets the list of handlers
     * @return the list of handlers
     */
    public static synchronized List<ClientHandler> getHandlers(){
        return clientHandlers;
    }
}

