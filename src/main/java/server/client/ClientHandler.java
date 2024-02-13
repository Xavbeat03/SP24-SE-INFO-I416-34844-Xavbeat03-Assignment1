package server.client;

import java.io.*;
import java.net.Socket;


public class ClientHandler extends Thread{
	final Socket socket;

	public ClientHandler(Socket clientSocket) {
		this.socket = clientSocket;
	}

	public void run() {
		PrintWriter out = null;
		BufferedReader in = null;

		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while(true) {
				String input = in.readLine();
				if (input.equals("exit")) {
					break;
				}
				if (input.equals(""))
				out.println(input);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (out != null) out.close();
				if (in != null) {
					in.close();
					this.socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}




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
