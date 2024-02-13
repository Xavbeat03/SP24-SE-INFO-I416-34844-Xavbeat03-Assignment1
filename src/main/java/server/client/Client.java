package server.client;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a client in the server-client architecture.
 * Each client is assigned a unique ID upon creation.
 * The ID is used to distinguish between different clients.
 * The client is also associated with a ClientHandler which manages its connection.
 */
public class Client {

	private static final Map<Integer, Client> clientMap = new HashMap<>();
	private static int latestId = 0;
	private int id = latestId;
	private final ClientHandler clientHandler;

	/**
	 * Constructs a new Client, assigns it a unique ID, and associates it with a ClientHandler.
	 * The new client is also added to the clientMap.
	 *
	 * @param clientHandler1 the ClientHandler to be associated with this client
	 */
	public  Client(ClientHandler clientHandler1) {
		this.clientHandler = clientHandler1;
		this.id = latestId++;
		clientMap.put(this.id, this);
		clientHandler1.setClient(this);
	}

	/**
	 * Returns the unique ID of this client.
	 *
	 * @return the unique ID of this client
	 */
	public int getId(){
		return id;
	}

	/**
	 * Returns the client handler for a specific client
	 *
	 * @return the client handler for a specific client
	 */
	public ClientHandler getClientHandler(){ return clientHandler;}

	/**
	 * Returns the Client associated with the given ID.
	 *
	 * @param id the ID of the client to retrieve
	 * @return the Client associated with the given ID, or null if no such client exists
	 */
	public static Client getClientById(int id){return Client.clientMap.get(id);}

	/**
	 * Returns true if there is no clients within the client list, false otherwise.
	 *
	 * @return true if no clients, false otherwise
	 */
	public static boolean isNoClients(){return Client.clientMap.isEmpty();}
}
