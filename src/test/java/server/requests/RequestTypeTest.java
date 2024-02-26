package server.requests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestTypeTest {
	@Test
	void shouldReturnCorrectRequestName() {
		Assertions.assertEquals("GET", RequestType.GET.getRequestName());
		Assertions.assertEquals("SET", RequestType.SET.getRequestName());
	}

	@Test
	void shouldCheckIfStringIsRequestType() {
		assertTrue(RequestType.checkIfStringIsRequestType("GET"));
		assertTrue(RequestType.checkIfStringIsRequestType("SET"));
		assertFalse(RequestType.checkIfStringIsRequestType("INVALID"));
	}

	@Test
	void shouldConvertStringToRequestType() {
		Assertions.assertEquals(RequestType.GET, RequestType.convertStringToRequestType("GET"));
		Assertions.assertEquals(RequestType.SET, RequestType.convertStringToRequestType("SET"));
	}

	@Test
	void shouldThrowExceptionForInvalidRequestType() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> RequestType.convertStringToRequestType("INVALID"));
	}

	@Test
	void shouldCheckIfStringMatchesRegex() {
		assertTrue(RequestType.checkIfStringMatchesRegex("get key1 \r\n", RequestType.GET));
		assertTrue(RequestType.checkIfStringMatchesRegex("set key1 1 \r\nvalue1 \r\n", RequestType.SET));
		assertFalse(RequestType.checkIfStringMatchesRegex("get key1", RequestType.GET));
		assertFalse(RequestType.checkIfStringMatchesRegex("set key1 1 value1", RequestType.SET));
	}

	@Test
	void shouldCheckIfStringMatchesRegex1() {
		assertTrue(RequestType.checkIfStringMatchesRegex("set key 5 \r\n" +
			"value \r\n", RequestType.SET));
	}
}
