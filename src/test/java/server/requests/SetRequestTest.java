package server.requests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SetRequestTest {
	@Test
	void shouldReturnCorrectValueSizeBytes() {
		SetRequest setRequest = new SetRequest("key1", 1, 5, "value1");
		assertEquals(5, setRequest.getValueSizeBytes());
	}

	@Test
	void shouldReturnCorrectValue() {
		SetRequest setRequest = new SetRequest("key1", 1, 5, "value1");
		assertEquals("value1", setRequest.getValue());
	}

	@Test
	void shouldReturnCorrectKey() {
		SetRequest setRequest = new SetRequest("key1", 1, 5, "value1");
		assertEquals("key1", setRequest.getKey());
	}

	@Test
	void shouldReturnCorrectClientId() {
		SetRequest setRequest = new SetRequest("key1", 1, 5, "value1");
		assertEquals(1, setRequest.getClientId());
	}

	@Test
	void shouldReturnCorrectRequestType() {
		SetRequest setRequest = new SetRequest("key1", 1, 5, "value1");
		assertEquals(RequestType.SET, setRequest.getRequestType());
	}

	@Test
	void shouldThrowExceptionForEmptyKey() {
		assertThrows(IllegalArgumentException.class, () -> new SetRequest("", 1, 5, "value1"));
	}

	@Test
	void shouldThrowExceptionForEmptyValue() {
		assertThrows(IllegalArgumentException.class, () -> new SetRequest("key1", 1, 5, ""));
	}

	@Test
	void shouldHandleZeroValueSizeBytes() {
		SetRequest setRequest = new SetRequest("key1", 1, 0, "value1");
		assertEquals(0, setRequest.getValueSizeBytes());
	}

	@Test
	void shouldHandleNegativeClientId() {
		SetRequest setRequest = new SetRequest("key1", -1, 5, "value1");
		assertEquals(-1, setRequest.getClientId());
	}

	@Test
	void shouldCorrectlyCompareToOtherRequestInSetRequest() {
		SetRequest setRequest1 = new SetRequest("key1", 1, 5, "value1");
		SetRequest setRequest2 = new SetRequest("key2", 2, 5, "value2");
		assertEquals(0, setRequest1.compareTo(setRequest2));
	}
}
