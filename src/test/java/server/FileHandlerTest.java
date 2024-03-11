package server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

public class FileHandlerTest {

	@BeforeEach
	public void setup() {
		File file = new File("./map.txt");
		if (file.exists()) {
			file.delete();
		}
	}

	@Test
	public void getRequestTest() {
		String k = "key";
		FileHandler.GetRequest g = new FileHandler.GetRequest(k);
		Assertions.assertEquals(k, g.getKey());
	}

	@Test
	public void setRequestTest() {
		String k = "key";
		String v = "value";
		FileHandler.SetRequest s = new FileHandler.SetRequest(k, v);
		Assertions.assertEquals(k, s.getKey());
		Assertions.assertEquals(v, s.getValue());
	}

	@Test
	public void handleRequestTest() {
		//TODO Delete data/map.txt file if it exists
		File file = new File("data/map.txt");
		file.delete();

		String k = "key";
		String v = "value";
		FileHandler.SetRequest s = new FileHandler.SetRequest(k, v);
		FileHandler.GetRequest g = new FileHandler.GetRequest(k);
		FileHandler.addRequest(s);
		FileHandler.addRequest(g);
		Assertions.assertEquals("STORED\r\nEND\r\n", FileHandler.handleRequest(s));
		Assertions.assertEquals("VALUE %s %d \r\n%s\r\nEND\r\n".formatted(k, 5, v),FileHandler.handleRequest(g));

	}

	@Test
	public void handleRequestReturnsStoredForSetRequest() {
		FileHandler.SetRequest setRequest = new FileHandler.SetRequest("key", "value");
		FileHandler.addRequest(setRequest);
		Assertions.assertEquals("STORED\r\nEND\r\n", FileHandler.handleRequest(setRequest));
	}

	@Test
	public void handleRequestReturnsValueForGetRequest() {
		FileHandler.SetRequest setRequest = new FileHandler.SetRequest("key", "value");
		FileHandler.GetRequest getRequest = new FileHandler.GetRequest("key");
		FileHandler.addRequest(setRequest);
		FileHandler.addRequest(getRequest);
		FileHandler.handleRequest(setRequest);
		Assertions.assertEquals("VALUE key 5 \r\nvalue\r\nEND\r\n", FileHandler.handleRequest(getRequest));
	}

	@Test
	public void handleRequestReturnsEndForGetRequestWithNoValue() {
		FileHandler.GetRequest getRequest = new FileHandler.GetRequest("key");
		FileHandler.addRequest(getRequest);
		Assertions.assertEquals("END\r\n", FileHandler.handleRequest(getRequest));
	}

	@Test
	public void handleRequestReturnsEndForGetRequestWithNoValueThrowsFileNotFoundException() {
		FileHandler.GetRequest getRequest = new FileHandler.GetRequest("key");
		FileHandler.addRequest(getRequest);
		Assertions.assertDoesNotThrow(() -> FileHandler.handleRequest(getRequest));
	}
}
