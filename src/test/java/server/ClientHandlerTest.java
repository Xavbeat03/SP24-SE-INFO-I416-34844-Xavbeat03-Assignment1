package server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandlerTest {

	@Test
	public void clientHandlerIdIsCorrect() throws IOException {
		Socket mockSocket = Mockito.mock(Socket.class);
		DataInputStream mockDis = Mockito.mock(DataInputStream.class);
		DataOutputStream mockDos = Mockito.mock(DataOutputStream.class);
		ClientHandler clientHandler = new ClientHandler(mockSocket, mockDis, mockDos, 123);
		Assertions.assertEquals(123, clientHandler.getHandlerId());
	}
}
