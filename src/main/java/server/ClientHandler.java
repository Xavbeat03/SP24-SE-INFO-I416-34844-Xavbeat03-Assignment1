package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientHandler extends Thread{
    DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    final int id;

    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, int id)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.id = id;
    }

    @Override
    public void run()
    {
        Server.addHandler(this);
        String received;
        String toreturn;
        while (true)
        {
            try {

                // Ask user what he wants
                dos.writeUTF("What do you want to do?[Get | Set]..\n"+
                        "Type Exit to terminate connection.");


                // receive the answer from client
                received = dis.readUTF();

                if(received.equals("Exit"))
                {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    Server.removeHandler(this);
                    System.out.println(Server.getHandlers().size());
                    break;
                }

                String[] items = received.split(" ");

                // write on output stream based on the
                // answer from the client
                if (items.length == 1) {
                    dos.writeUTF("Invalid input");
                } else {
                    switch (items[0]) {

                        case "set":
                            if (items.length == 1 || items.length == 2 || items.length > 3)
                                dos.writeUTF("Invalid input");
                            String value = dis.readUTF();
                            FileHandler.SetRequest s = new FileHandler.SetRequest(items[1], value);
                            FileHandler.addRequest(s);
                            toreturn = FileHandler.handleRequest(s);

                            dos.writeUTF(toreturn);
                            break;

                        case "get":
                            if (items.length == 1 || items.length > 2) dos.writeUTF("Invalid input");

                            FileHandler.GetRequest g = new FileHandler.GetRequest(items[1]);
                            FileHandler.addRequest(g);
                            toreturn = FileHandler.handleRequest(g);

                            dos.writeUTF(toreturn);
                            break;

                        default:
                            dos.writeUTF("Invalid input");
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try
        {
            // closing resources
            this.dis.close();
            this.dos.close();
            Server.removeHandler(this);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Gets the ID of the handler
     * @return integer representing the id.
     */
    public int getHandlerId() {
        return id;
    }

}
