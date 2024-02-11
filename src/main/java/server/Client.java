package server;

/**
 * A client object that the server can create upon connecting to a new client to store information about it.
 */
public class Client {

	static private int latestId = -1;
	int id = latestId;
	Client() {
		id = latestId++;
	}

	int getId(){
		return id;
	}
}
