package server.requests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GetRequestTest {
	@Test
	void shouldReturnCorrectKey() {
		GetRequest getRequest = new GetRequest("key1", 1);
		assertEquals("key1", getRequest.getKey());
	}

	@Test
	void shouldReturnCorrectClientId() {
		GetRequest getRequest = new GetRequest("key1", 1);
		assertEquals(1, getRequest.getClientId());
	}

	@Test
	void shouldReturnCorrectRequestType() {
		GetRequest getRequest = new GetRequest("key1", 1);
		assertEquals(RequestType.GET, getRequest.getRequestType());
	}

	@Test
	void shouldHandleEmptyKey() {
		assertThrows(IllegalArgumentException.class, () -> new GetRequest("", 1));
	}

	@Test
	void shouldHandleNegativeClientId() {
		GetRequest getRequest = new GetRequest("key1", -1);
		assertEquals(-1, getRequest.getClientId());
	}

	@Test
	void shouldCorrectlyCompareToOtherRequest() {
		GetRequest getRequest1 = new GetRequest("key1", 1);
		GetRequest getRequest2 = new GetRequest("key2", 2);
		assertEquals(0, getRequest1.compareTo(getRequest2));
	}
}
