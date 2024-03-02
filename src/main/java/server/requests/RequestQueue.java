package server.requests;

import org.jetbrains.annotations.NotNull;
import server.client.Client;

import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * This class represents a queue for holding requests in the server-client architecture.
 * It uses a PriorityQueue to store the requests.
 * This class is implemented as a singleton, meaning there can only be one instance of this class in the application.
 */
public class RequestQueue{
	private static final ArrayBlockingQueue<Request> requests = new ArrayBlockingQueue<>(100, true);

	private static final ExecutorService putExecutor = Executors.newSingleThreadExecutor();
	private static final ExecutorService retrieveExecutor = Executors.newSingleThreadExecutor();

	static private final Object obj = new Object();

	/**
	 * Private constructor to prevent instantiation of this class.
	 */
	private RequestQueue(){}

	/**
	 * Shuts down the internal thread executor service
	 */
	public static synchronized void shutdownExecutor(){
		putExecutor.submit(putExecutor::shutdown);
		retrieveExecutor.submit(retrieveExecutor::shutdown);
	}

	/**
	 * Offers up the next request to be retrieved
	 *
	 * @param r the request being offered
	 */
	public static void addRequest(Request r) {
		try {
			while (requests.remainingCapacity() == 0) {
				Thread.sleep(250);
			}
			putExecutor.submit(() -> requests.add(r));

		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the currently offered item from the queue
	 *
	 * @return the request being retrieved
	 */
	public static synchronized Request retrieveRequest() throws InterruptedException {
		return requests.take();
	}

	/**
	 * Gets the size of the array
	 *
	 * @return the size of the array
	 */
	public static synchronized int getSize() {
		return requests.size();
	}

}
