package server.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


public class ClientHandler extends Thread{
	final Socket socket;
	final DataInputStream dis;
	final DataOutputStream dos;

	public ClientHandler(Socket clientSocket,  DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
		this.socket = clientSocket;
		this.dis = dataInputStream;
		this.dos = dataOutputStream;
	}

	public void run() {
		String recieved;
		String toReturn;


	}

	/**
	 * Returns the socket associated with this client handler.
	 *
	 * @return the socket
	 */
	public Socket getSocket() {
		return this.socket;
	}

	/**
	 * Returns the data input stream associated with this client handler.
	 *
	 * @return the data input stream
	 */
	public DataInputStream getDataInputStream() {
		return this.dis;
	}

	/**
	 * Returns the data output stream associated with this client handler.
	 *
	 * @return the data output stream
	 */
	public DataOutputStream getDataOutputStream() {
		return this.dos;
	}


}
