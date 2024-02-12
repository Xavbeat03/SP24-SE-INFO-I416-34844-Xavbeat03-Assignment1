package server.requests;

/**
 * Class representing a set request in the server-client architecture.
 * A set request is used to set a value associated with a key.
 * Each set request has a key, a client ID, a value size in bytes, and a value.
 */
public class SetRequest extends Request{
	private final int valueSizeBytes;
	private final String value;

	private final String key;

	private final int clientId;
	private static final RequestType requestType = RequestType.SET;


	/**
	 * Constructs a new SetRequest with the given key, client ID, value size in bytes, and value.
	 *
	 * @param k the key
	 * @param clientId the client ID
	 * @param vSB the value size in bytes
	 * @param v the value
	 */
	public SetRequest(String k, int clientId, int vSB, String v){
		this.key = k;
		this.clientId = clientId;
		this.valueSizeBytes = vSB;
		this.value = v;
	}

	/**
	 * Returns the value size in bytes associated with this set request.
	 *
	 * @return the value size in bytes
	 */
	public int getValueSizeBytes() {
		return valueSizeBytes;
	}

	/**
	 * Returns the value associated with this set request.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
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
