package server.requests;

import server.client.Client;

import java.util.NoSuchElementException;
import java.util.PriorityQueue;

/**
 * This class represents a queue for holding requests in the server-client architecture.
 * It uses a PriorityQueue to store the requests.
 * This class is implemented as a singleton, meaning there can only be one instance of this class in the application.
 */
public class RequestQueue{
	private static final PriorityQueue<Request> requests = new PriorityQueue<>();

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
	public static synchronized void addRequest(Request r){
		requests.add(r);
		Client.getClientById(r.getClientId()).getClientHandler().setRequestProcessing(false);
	}

	/**
	 * Retrieves and removes the next request from the RequestQueue.
	 * If the queue is empty, this method will throw a NoSuchElementException.
	 *
	 * @return the next request in the queue
	 */
	public static synchronized Request retrieveRequest(){
		if(isNoRequests()) throw new NoSuchElementException("Illegal state invoked by retrieving from empty queue.");
		return requests.remove();
	}

	/**
	 * Returns the number of requests currently in the RequestQueue.
	 *
	 * @return the size of the Request Queue
	 */
	public static synchronized int getRequestQueueLength() {return requests.size();}

	/**
	 * Returns whether the RequestQueue is empty.
	 *
	 * @return true if the RequestQueue is empty, false otherwise
	 */
	public static synchronized boolean isNoRequests() {return requests.isEmpty();}

	/**
	 * Clears the queue, mainly intended for testing.
	 */
	public static synchronized void clearQueue() {
		requests.clear();
	}
}
