package server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FileHandlerTest {

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
}
