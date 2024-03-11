package server;

import kotlin.jvm.Synchronized;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.Mockito;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Execution(ExecutionMode.SAME_THREAD)
public class ServerTest {

	@BeforeEach
	public void clean(){
		List<ClientHandler> handlerList = new ArrayList<>(Server.getHandlers());
		for(ClientHandler c : handlerList){
			Server.removeHandler(c);
		}
	}

	@Test
	public void startWithZeroHandlers(){
		Assertions.assertEquals(0, Server.getHandlers().size());
	}

	@Test
	public void serverAddsHandlerSuccessfully() throws IOException {
		Socket mockSocket = Mockito.mock(Socket.class);
		DataInputStream mockDis = Mockito.mock(DataInputStream.class);
		DataOutputStream mockDos = Mockito.mock(DataOutputStream.class);
		Server.addHandler(new ClientHandler(mockSocket, mockDis, mockDos, 0));
		Assertions.assertEquals(1, Server.getHandlers().size());
	}

	@Test
	public void serverRemovesHandlerSuccessfully() throws IOException {
		Socket mockSocket = Mockito.mock(Socket.class);
		DataInputStream mockDis = Mockito.mock(DataInputStream.class);
		DataOutputStream mockDos = Mockito.mock(DataOutputStream.class);
		ClientHandler clientHandler = new ClientHandler(mockSocket, mockDis, mockDos, 0);
		Server.addHandler(clientHandler);
		Server.removeHandler(clientHandler);
		Assertions.assertEquals(0, Server.getHandlers().size());
	}

	@Test
	public void serverDoesNotRemoveNonExistentHandler() throws IOException {
		Socket mockSocket = Mockito.mock(Socket.class);
		DataInputStream mockDis = Mockito.mock(DataInputStream.class);
		DataOutputStream mockDos = Mockito.mock(DataOutputStream.class);
		ClientHandler clientHandler = new ClientHandler(mockSocket, mockDis, mockDos, 0);
		Server.removeHandler(clientHandler);
		Assertions.assertEquals(0, Server.getHandlers().size());
	}
}
