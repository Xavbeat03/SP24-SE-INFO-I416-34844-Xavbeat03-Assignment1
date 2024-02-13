package server.client;

import server.requests.GetRequest;
import server.requests.RequestQueue;
import server.requests.RequestType;
import server.requests.SetRequest;

import java.io.*;
import java.net.Socket;
import java.util.PriorityQueue;

import static server.requests.RequestType.GET;
import static server.requests.RequestType.SET;


public class ClientHandler extends Thread{
	final Socket socket;
	Client client;

	PrintWriter out = null;

	PriorityQueue<String> outgoingMessages = new PriorityQueue<>();

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
				if (RequestType.checkIfStringIsRequestType(input.split(" ")[0].toUpperCase())) {
					String[] strings = input.split(" ");
					switch(RequestType.convertStringToRequestType(strings[0].toUpperCase())){
						case SET -> {
							String line1 = input;
							String line2 = in.readLine();
							if (!RequestType.checkIfStringMatchesRegex(line1 + line2, SET)) continue;
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

				while(!outgoingMessages.isEmpty()){
					out.println(outgoingMessages.remove());
				}
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
	 * Outputs a message via the Filewriter
	 *
	 * @param s the string to be outputted
	 */
	public void outputMessage(String s){
		if(out == null) return;
		out.println(s);
	}

}
