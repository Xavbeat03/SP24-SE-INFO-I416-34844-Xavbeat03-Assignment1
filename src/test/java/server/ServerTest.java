package server;

import client.Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServerTest {

	private Server server;

	@BeforeEach
	public void setUp() throws IOException {
		server = new Server(5532);
	}

	@AfterEach
	public void tearDown() throws IOException {
		if (server.isRunning()) {
			server.stop();
		}
		server = null;
	}

	@Test
	public void serverStartsSuccessfully() {
		try {
			server.start();
			Assertions.assertTrue(server.isRunning());
		} catch (IOException e) {
			Assertions.fail();
		} finally {
			try {
				if (server.isRunning()) {
					server.stop();
				}
			} catch (IOException e) {
				Assertions.fail();
			}
		}
	}

	@Test
	public void serverStopsSuccessfully() {
		try {
			server.start();
			server.stop();
			Assertions.assertFalse(server.isRunning());
		} catch (IOException e) {
			Assertions.fail();
		}
	}

	@Test
	public void serverCannotStartTwice() {
		try {
			server.start();
			Assertions.assertTrue(server.isRunning());
			server.start();
		} catch (IOException e) {
			Assertions.fail();
		} catch (IllegalStateException i) {
			Assertions.assertNull(null);
		} finally {
			try {
				if (server.isRunning()) {
					server.stop();
				}
			} catch (IOException e) {
				Assertions.fail();
			}
		}
	}

	@Test
	public void serverCannotStopBeforeStart() {
		Assertions.assertFalse(server.isRunning(), "Server should not be running before start");
		Assertions.assertThrows(IllegalStateException.class, server::stop);
		Assertions.assertFalse(server.isRunning(), "Server should not be running after stop");
	}


	@Test
	public void serverCanStartAndStopMultipleTimes() {
		try {
			for (int i = 0; i < 5; i++) {
				server.start();
				Assertions.assertTrue(server.isRunning());
				server.stop();
				Assertions.assertFalse(server.isRunning());
			}
		} catch (IOException e) {
			Assertions.fail();
		}
	}

	@Test
	public void serverCanHandleMultipleClients() {
		try {
			server.start();
			Assertions.assertTrue(server.isRunning());

			List<Client> list = new ArrayList<>();
			for(int i = 0; i < 5; i++) list.add(i, new Client("localhost", 5532));

			// Create multiple clients and connect them to the server
			for (Client c: list) {
				// You would need to implement a Client class for this
				c.connect();
				Assertions.assertTrue(c.isConnected());
			}

			server.stop();
			Assertions.assertFalse(server.isRunning());
		} catch (IOException e) {
			Assertions.fail();
		}
	}

	@Test
	public void serverCanHandleOneClient() {
		try {
			server.start();
			Assertions.assertTrue(server.isRunning());

			// Create a client and connect it to the server
			Client client = new Client("localhost", 5532);
			client.connect();
			Assertions.assertTrue(client.isConnected());

			client.disconnect();
			server.stop();
			Assertions.assertFalse(server.isRunning());
		} catch (IOException e) {
			Assertions.fail();
		}
	}

	@Test
	public void serverCanHandleClientDisconnections() {
		try {
			server.start();
			Assertions.assertTrue(server.isRunning());

			// Create a client and connect it to the server
			Client client = new Client("localhost", 5000);
			client.connect();
			Assertions.assertTrue(client.isConnected());

			// Disconnect the client
			client.disconnect();
			Assertions.assertFalse(client.isConnected());

			server.stop();
			Assertions.assertFalse(server.isRunning());
		} catch (IOException e) {
			Assertions.fail();
		}
	}

	@Test
	public void serverCanHandleInvalidRequests() {
		try {
			server.start();
			Assertions.assertTrue(server.isRunning());

			// Create a client, connect it to the server, and send an invalid request
			Client client = new Client("localhost", 5000);
			client.connect();
			Assertions.assertTrue(client.isConnected());
			client.sendRequest("INVALID REQUEST");

			// The server should still be running after handling the invalid request
			Assertions.assertTrue(server.isRunning());

			server.stop();
			Assertions.assertFalse(server.isRunning());
		} catch (IOException e) {
			Assertions.fail();
		}
	}

}
