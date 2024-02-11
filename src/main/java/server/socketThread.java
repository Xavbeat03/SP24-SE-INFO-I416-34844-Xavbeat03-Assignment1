package server;

import java.net.Socket;

public class socketThread extends Thread{
	protected Socket socket;

	public socketThread(Socket clientSocket) {
		this.socket = clientSocket;
	}

	public void run() {

	}
}
