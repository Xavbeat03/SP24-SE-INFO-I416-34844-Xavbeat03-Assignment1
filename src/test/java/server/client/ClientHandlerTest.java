package server.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientHandlerTest {

	private ClientHandler clientHandler;
	private PrintWriter mockWriter;
	private ByteArrayOutputStream mockOutputStream;
	private Socket mockSocket;

	@BeforeEach
	public void setUp() throws IOException {
		mockSocket = Mockito.mock(Socket.class);
		mockOutputStream = new ByteArrayOutputStream();
		Mockito.when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);
		BufferedReader mockReader = Mockito.mock(BufferedReader.class);
		Mockito.when(mockReader.ready()).thenReturn(true); // Simulate that the input is ready
		Mockito.when(mockReader.readLine()).thenReturn(""); // Return an empty string when readLine() is called
		Mockito.when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
		mockWriter = new PrintWriter(mockOutputStream, true);
		clientHandler = new ClientHandler(mockSocket);
		clientHandler.setWriter(mockWriter);
	}

	@Test
	public void outputsMessageSuccessfully() {
		String message = "Hello, World!";
		clientHandler.outputMessage(message);
		assertEquals(message + "\n", mockOutputStream.toString());
	}

	@Test
	public void storesLatestMessage() {
		String message = "Hello, World!";
		clientHandler.outputMessage(message);
		assertEquals(message, clientHandler.getLatestMessage());
	}

	@Test
	public void setsClientSuccessfully() {
		Client mockClient = Mockito.mock(Client.class);
		clientHandler.setClient(mockClient);
		// No exception means success
	}

	@Test
	public void handlesSetRequestCorrectly() throws IOException {
		String input = "SET key 0 5\r\nvalue\r\n";
		BufferedReader mockReader = new BufferedReader(new StringReader(input));
		Mockito.when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream(input.getBytes()));
		Mockito.when(mockReader.readLine()).thenCallRealMethod(); // Call the real readLine() method
		clientHandler.run();
		assertEquals("STORED\r\n", mockOutputStream.toString());
	}

	@Test
	public void handlesGetRequestCorrectly() throws IOException {
		String input = "GET key\r\n";
		BufferedReader mockReader = new BufferedReader(new StringReader(input));
		Mockito.when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream(input.getBytes()));
		Mockito.when(mockReader.readLine()).thenCallRealMethod(); // Call the real readLine() method
		clientHandler.run();
		assertEquals("VALUE key 5 \r\nvalue\r\nEND\r\n", mockOutputStream.toString());
	}

	@Test
	public void handlesInvalidRequest() throws IOException {
		String input = "INVALID key\r\n";
		BufferedReader mockReader = new BufferedReader(new StringReader(input));
		Mockito.when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream(input.getBytes()));
		Mockito.when(mockReader.readLine()).thenCallRealMethod(); // Call the real readLine() method
		clientHandler.run();
		assertEquals("", mockOutputStream.toString());
	}

	@Test
	public void handlesExitCommand() throws IOException {
		String input = "exit\r\n";
		BufferedReader mockReader = new BufferedReader(new StringReader(input));
		Mockito.when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream(input.getBytes()));
		Mockito.when(mockReader.readLine()).thenCallRealMethod(); // Call the real readLine() method
		clientHandler.run();
		assertEquals("", mockOutputStream.toString());
	}
}
