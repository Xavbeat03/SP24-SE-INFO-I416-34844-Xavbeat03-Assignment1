package server.requests;

import java.util.PriorityQueue;

/**
 * This class represents a queue for holding requests in the server-client architecture.
 * It uses a PriorityQueue to store the requests.
 * This class is implemented as a singleton, meaning there can only be one instance of this class in the application.
 */
public class RequestQueue{
	private static PriorityQueue<Request> requests;

	/**
	 * Private constructor to prevent instantiation of this class.
	 */
	private RequestQueue(){}

	/**
	 * Adds a request to the RequestQueue.
	 * The request is added to the PriorityQueue, which automatically sorts the requests based on their priority.
	 *
	 * @param r the request to add to the queue
	 */
	public static synchronized void addRequest(Request r){requests.add(r);}

	/**
	 * Retrieves and removes the next request from the RequestQueue.
	 * If the queue is empty, this method will throw a NoSuchElementException.
	 *
	 * @return the next request in the queue
	 */
	public static synchronized Request retrieveRequest(){return requests.remove();}

	/**
	 * Returns the number of requests currently in the RequestQueue.
	 *
	 * @return the size of the Request Queue
	 */
	public static synchronized int getRequestQueueLength() {return requests.size();}
}
