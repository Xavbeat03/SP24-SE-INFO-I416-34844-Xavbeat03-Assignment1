package server.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class RequestQueueTest {

	@BeforeEach
	void beforeEachStep() {
		RequestQueue.clearQueue();
	}

	@Test
	void shouldAddRequestToQueue() {
		Request request = new SetRequest("key1", 1, 5, "value1");
		RequestQueue.addRequest(request);
		assertEquals(1, RequestQueue.getRequestQueueLength());
	}

	@Test
	void shouldRetrieveRequestFromQueue() {
		Request request = new SetRequest("key1", 1, 5, "value1");
		RequestQueue.addRequest(request);
		Request retrievedRequest = RequestQueue.retrieveRequest();
		assertEquals(request, retrievedRequest);
	}

	@Test
	void shouldReturnCorrectQueueLength() {
		Request request1 = new SetRequest("key1", 1, 5, "value1");
		Request request2 = new GetRequest("key2", 2);
		RequestQueue.addRequest(request1);
		RequestQueue.addRequest(request2);
		assertEquals(2, RequestQueue.getRequestQueueLength());
	}

	@Test
	void shouldReturnTrueWhenQueueIsEmpty() {
		assertTrue(RequestQueue.isNoRequests());
	}

	@Test
	void shouldReturnFalseWhenQueueIsNotEmpty() {
		Request request = new SetRequest("key1", 1, 5, "value1");
		RequestQueue.addRequest(request);
		assertFalse(RequestQueue.isNoRequests());
	}

	@Test
	void shouldThrowExceptionWhenRetrievingFromEmptyQueue() {
		assertThrows(NoSuchElementException.class, RequestQueue::retrieveRequest);
	}
}
