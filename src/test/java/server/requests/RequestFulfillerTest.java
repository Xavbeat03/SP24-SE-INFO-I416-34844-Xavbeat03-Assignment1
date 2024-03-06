package server.requests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.Server;
import server.client.ClientHandler;
import server.file.FileHandler;

import java.io.*;
import java.net.Socket;

public class RequestFulfillerTest {

	private RequestFulfiller requestFulfiller;
	private ClientHandler clientHandler;

	@BeforeEach
	public void setUp() throws IOException {
		requestFulfiller = new RequestFulfiller();
		Socket mockSocket = Mockito.mock(Socket.class);
		OutputStream mockOutputStream = new ByteArrayOutputStream();
		Mockito.when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);
		PrintWriter mockWriter = new PrintWriter(mockOutputStream, true);
		clientHandler = new ClientHandler(mockSocket);
		clientHandler.setWriter(mockWriter);

		// Initialize the ./data/map.txt file
		File file = new File("./data/map.txt");
		file.getParentFile().mkdirs(); // Create the directories if they don't exist
	}

	@Test
	public void fulfillsSetRequestSuccessfully() {
		SetRequest request = new SetRequest("key", 1, 4, "value");
		RequestQueue.addRequest(request);
		requestFulfiller.start();
		while(requestFulfiller.isFulfillingRequest()){
			try {
				Thread.sleep(250);
			} catch (InterruptedException i){
				i.printStackTrace();
			}
		}

		if(clientHandler.getLatestMessage().equals("STORED\r\n")) {
			Assertions.assertTrue(true);
			return;
		}

		Assertions.fail("Failed to get the Latest Message in a reasonable time period.");
	}

	@Test
	public void fulfillsGetRequestSuccessfully() {
		FileHandler.storeValue("key", "value");
		GetRequest request = new GetRequest("key", 1);
		RequestQueue.addRequest(request);
		requestFulfiller.start();
		Assertions.assertEquals("VALUE key 3 \r\nvalue \r\nEND\r\n", clientHandler.getLatestMessage());
	}


}
