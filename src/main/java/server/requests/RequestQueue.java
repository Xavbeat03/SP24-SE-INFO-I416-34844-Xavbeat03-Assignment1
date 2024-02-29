package server.requests;

import org.jetbrains.annotations.NotNull;
import server.client.Client;

import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.concurrent.*;

/**
 * This class represents a queue for holding requests in the server-client architecture.
 * It uses a PriorityQueue to store the requests.
 * This class is implemented as a singleton, meaning there can only be one instance of this class in the application.
 */
public class RequestQueue{
	private static final BlockingQueue<Request> requests = new ArrayBlockingQueue<>(20, true);

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
	public static synchronized void addRequest(Request r){
		putExecutor.submit(()-> {
            try {
				synchronized (obj){
					obj.notify();
				}
                requests.put(r);
            } catch (InterruptedException e) {
				Thread.currentThread().interrupt();
                throw new RuntimeException("Request Interrupted");
            }
        });
	}

	/**
	 * Retrieves the currently offered item from the queue
	 *
	 * @return the request being retrieved
	 */
	public static synchronized Request retrieveRequest() {
		try {
			synchronized (obj){
				try {
					obj.wait();
				} catch (Exception e){}
			}
			return requests.take();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
    }
}
