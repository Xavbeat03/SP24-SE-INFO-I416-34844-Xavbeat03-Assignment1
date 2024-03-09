package server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

	}

}
