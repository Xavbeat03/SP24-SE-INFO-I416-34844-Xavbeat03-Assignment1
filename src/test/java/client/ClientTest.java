package client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.ClientHandler;
import server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientTest {
	@Test
	public void clientSendsExitTerminatesConnection() throws IOException {
		Socket mockSocket = Mockito.mock(Socket.class);
		DataInputStream mockDis = Mockito.mock(DataInputStream.class);
		DataOutputStream mockDos = Mockito.mock(DataOutputStream.class);
		Mockito.when(mockDis.readUTF()).thenReturn("Exit");
		ClientHandler clientHandler = new ClientHandler(mockSocket, mockDis, mockDos, 0);
		Server.addHandler(clientHandler);
		clientHandler.run();
		Mockito.verify(mockSocket).close();
		Assertions.assertEquals(0, Server.getHandlers().size());
	}

	@Test
	public void clientSendsSetCommandStoresValue() throws IOException {
		Socket mockSocket = Mockito.mock(Socket.class);
		DataInputStream mockDis = Mockito.mock(DataInputStream.class);
		DataOutputStream mockDos = Mockito.mock(DataOutputStream.class);
		Mockito.when(mockDis.readUTF()).thenReturn("set key", "value", "Exit");
		ClientHandler clientHandler = new ClientHandler(mockSocket, mockDis, mockDos, 0);
		Server.addHandler(clientHandler);
		clientHandler.run();
		Mockito.verify(mockDos).writeUTF("STORED\r\nEND\r\n");
	}
}
