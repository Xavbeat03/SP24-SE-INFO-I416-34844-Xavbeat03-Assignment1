package server.requests;

/**
 * Enum representing the type of a request in the server-client architecture.
 * The request type can be either GET or SET.
 */
public enum RequestType {
	/**
	 * Represents a GET request, used to retrieve a value associated with a key.
	 */
	GET,

	/**
	 * Represents a SET request, used to set a value associated with a key.
	 */
	SET
}