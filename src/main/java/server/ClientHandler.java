package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread{
	final Socket socket;
	final DataInputStream dis;
	final DataOutputStream dos;

	public ClientHandler(Socket clientSocket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
		this.socket = clientSocket;
		this.dis = dataInputStream;
		this.dos = dataOutputStream;
	}

	public void run() {

	}
}
