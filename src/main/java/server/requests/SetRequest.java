package server.requests;

public class SetRequest extends Request{
	private final int valueSizeBytes;
	private final String value;

	private final String key;

	private final int clientId;

	public SetRequest(String k, int clientId, int vSB, String v){
		this.key = k;
		this.clientId = clientId;
		this.valueSizeBytes = vSB;
		this.value = v;
	}

	public int getValueSizeBytes() {
		return valueSizeBytes;
	}

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
}
