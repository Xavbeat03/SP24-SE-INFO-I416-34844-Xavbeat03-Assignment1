package server.requests;

public class GetRequest extends Request{

	private String key;
	private int clientId;

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public int getClientId() {
		return clientId;
	}
}
