package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client{

    private Socket socket = null;
    private Scanner input = null;
    private PrintWriter output = null;
    private String host;
    private int port;




    public Client(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        this.socket = new Socket(host, port);
        this.output = new PrintWriter(socket.getOutputStream(), true);
        this.input = new Scanner(socket.getInputStream());
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 5532);

        Scanner userInput = new Scanner(System.in);

        while (true) {
            String request = userInput.nextLine() + "/r/n";
            if(request.split(" ")[0].equals("set")) request+= userInput.nextLine() + "/r/n";
            client.sendRequest(request);

            String response = client.receiveResponse();
            System.out.println("Response: " + response);
            if(client.socket.isClosed()) return;
        }

    }


    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public String receiveResponse() {
        if (isConnected()) {
            return input.nextLine();
        } else {
            System.out.println("Client is not connected to the server.");
            return null;
        }
    }

    public void sendRequest(String request) {
        if (isConnected()) {
            output.println(request);
        } else {
            System.out.println("Client is not connected to the server.");
        }
    }

    public void disconnect() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (input != null) {
            input.close();
        }
        if (output != null) {
            output.close();
        }
    }

}
