package server.requests;

/**
 * Abstract class representing a request in the server-client architecture.
 * Each request has a key, a client ID, and a request type.
 */
public abstract class Request {
	/**
	 * Returns the key associated with this request.
	 *
	 * @return the key
	 */
	public abstract String getKey();

	/**
	 * Returns the client ID associated with this request.
	 *
	 * @return the client ID
	 */
	public abstract int getClientId();

	/**
	 * Returns the type of this request.
	 *
	 * @return the request type
	 */
	public abstract RequestType getRequestType();
}
