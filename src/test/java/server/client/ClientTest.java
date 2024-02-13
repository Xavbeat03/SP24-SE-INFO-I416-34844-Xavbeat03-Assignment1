package server.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ClientTest {

		private ClientHandler mockClientHandler;
		private Client client;

		@BeforeEach
		void setUp() {
			mockClientHandler = Mockito.mock(ClientHandler.class);
			client = new Client(mockClientHandler);
		}

		@Test
		void clientCreationAssignsUniqueIds() {
			Client anotherClient = new Client(mockClientHandler);
			assertNotEquals(client.getId(), anotherClient.getId());
		}

		@Test
		void clientCreationAddsToClientMap() {
			assertNotNull(Client.getClientById(client.getId()));
		}

		@Test
		void getClientHandlerReturnsCorrectHandler() {
			assertEquals(mockClientHandler, client.getClientHandler());
		}

		@Test
		void getClientByIdReturnsNullForNonexistentId() {
			assertNull(Client.getClientById(-1));
		}

		@Test
		void isNoClientsReturnsTrueWhenNoClients() {
			Client.clearClientMap();
			assertTrue(Client.isNoClients());
		}

		@Test
		void isNoClientsReturnsFalseWhenThereAreClients() {
			assertFalse(Client.isNoClients());
		}

		@Test
		void isNoClientsReturnsTrueWhenThereAreNoClients() {
			Client.clearClientMap();
			assertTrue(Client.isNoClients());
		}

}
