package server.requests;

/**
 * Class representing a get request in the server-client architecture.
 * A get request is used to retrieve a value associated with a key.
 * Each get request has a key and a client ID.
 */
public class GetRequest extends Request{

	private final String key;
	private final int clientId;
	private static final RequestType requestType = RequestType.GET;

	/**
	 * Constructs a new GetRequest with the given key and client ID.
	 *
	 * @param k the key
	 * @param clientId the client ID
	 */
	public GetRequest(String k, int clientId){
		this.key = k;
		this.clientId = clientId;
	}


	@Override
	public String getKey() {
		return key;
	}

	@Override
	public int getClientId() {
		return clientId;
	}

	@Override
	public RequestType getRequestType(){
		return requestType;
	}
}
