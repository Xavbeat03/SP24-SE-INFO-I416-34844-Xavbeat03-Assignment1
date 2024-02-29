package server.requests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

public class RequestQueueTest {
	@Test
	void shouldAddRequestToQueue() {
		Request request = new SetRequest("key1", 1, 5, "value1");
        RequestQueue.addRequest(request);
        Request r2 = RequestQueue.retrieveRequest();
        Assertions.assertEquals(request, r2);
        RequestQueue.shutdownExecutor();
    }

    @Test
    void shouldAddRequestsToQueue() {
        Request request = new SetRequest("key1", 1, 5, "value1");
        Request request2 = new SetRequest("key1", 2, 5, "value1");
        Request request3 = new SetRequest("key1", 3, 5, "value1");
        RequestQueue.addRequest(request);
        RequestQueue.addRequest(request2);
        RequestQueue.addRequest(request3);
        Request r2 = RequestQueue.retrieveRequest();
        r2 = RequestQueue.retrieveRequest();
        r2 = RequestQueue.retrieveRequest();
        Assertions.assertEquals(request3, r2);
        RequestQueue.shutdownExecutor();
    }
}
