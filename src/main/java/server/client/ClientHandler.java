package server.client;

import server.requests.GetRequest;
import server.requests.RequestQueue;
import server.requests.RequestType;
import server.requests.SetRequest;

import java.io.*;
import java.net.Socket;

import static server.requests.RequestType.GET;
import static server.requests.RequestType.SET;


public class ClientHandler extends Thread{
	Socket socket;
	Client client;

	PrintWriter out = null;

	String latestMessage = "";

	public ClientHandler(Socket clientSocket) {
		this.socket = clientSocket;
		try {
			this.out = new PrintWriter(this.socket.getOutputStream(), true);
		} catch (IOException i){
			i.printStackTrace();
		}
	}


	public void setClient(Client client) {
		this.client = client;
	}

	public Client getClient() {
		return this.client;
	}

	public void run() {
		BufferedReader in = null;

		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while(true) {
				while(!in.ready()){
					// Wait until a line is availible to read.
					Thread.sleep(100);
				}
				String input = in.readLine();
				if (input.equals("exit")) {
					break;
				}
				if (RequestType.checkIfStringIsRequestType(input.split(" ")[0].toUpperCase())) {
					String[] strings = input.split(" ");
					switch(RequestType.convertStringToRequestType(strings[0].toUpperCase())){
						case SET -> {
							String line1 = input;
							String line2 = in.readLine();
							String combinedLines = line1 + " \r\n" + line2 + " \r\n";
							if (!RequestType.checkIfStringMatchesRegex(combinedLines, RequestType.SET)) continue;
							String[] lines = line1.split(" ");
							String[] lines2 = line2.split(" ");
							SetRequest setRequest = new SetRequest(lines[0], client.getId(), Integer.parseInt(lines[1]), lines2[0]);
							RequestQueue.addRequest(setRequest);
						}

						case GET -> {
							if (!RequestType.checkIfStringMatchesRegex(input,GET)) continue;
							GetRequest getRequest = new GetRequest(strings[1], client.getId());
							RequestQueue.addRequest(getRequest);
						}
					}
				}



			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
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
	 * Outputs a message via the Filewriter
	 *
	 * @param s the string to be outputted
	 */
	public void outputMessage(String s){
		latestMessage = s;
		if(out == null) return;
		out.println(s);
	}


	public String getLatestMessage() {
		return latestMessage;
	}

	/**
	 * Mainly intended for Testing
	 *
	 * @param writer the Writer to be set
	 */
	public void setWriter(PrintWriter writer) {
		this.out = writer;
	}

	public boolean isClientDisconnected() {
		try {
			PrintWriter out1 = new PrintWriter(socket.getOutputStream(), true);
			// Try to send a ping message to the client
			out1.println("PING");
			return false;
		} catch (IOException i){
			return true;
		}
	}
}
