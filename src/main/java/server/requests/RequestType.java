package server.requests;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Enum representing the type of a request in the server-client architecture.
 * The request type can be either GET or SET.
 */
public enum RequestType {
	/**
	 * Represents a GET request, used to retrieve a value associated with a key.
	 */
	GET(
		"GET",
		"get [a-zA-Z0-9_]{1,99} \\r\\n"
	),

	/**
	 * Represents a SET request, used to set a value associated with a key.
	 */
	SET(
		"SET",
		"set [a-zA-Z0-9_]{1,99} [0-9]{1,3} \\r\\n[a-zA-Z0-9_]{1,99} \\r\\n"
	);

	// The name of the request type
	private final String requestName;
	private final String regexPattern;

	/**
	 * Constructor for the RequestType Enum
	 * @param requestName the String representation of the requesttype Name
	 */
	RequestType(String requestName, String regexPattern){
		this.requestName = requestName;
		this.regexPattern = regexPattern;
	}


	/**
	 * Gets the request type name
	 *
	 * @return a string representation of the request type
	 */
	public String getRequestName() {
		return requestName;
	}

	/**
	 * Checks if the string has a valid request type
	 *
	 * @param s the string to be checked
	 * @return true if the string is a valid request type, false otherwise
	 */
	public static boolean checkIfStringIsRequestType(String s){
		for(RequestType value: RequestType.values())
			if((s.toUpperCase()).equals(value.getRequestName())) return true;
		return false;
	}

	/**
	 * Converts a string to a requestType
	 *
	 * @param s the string to be converted
	 * @return the request Type if it exists
	 * @throws IllegalArgumentException if string is not a valid request Type
	 */
	public static RequestType convertStringToRequestType(String s){
		for(RequestType value: RequestType.values())
			if(s.equals(value.getRequestName())) return value;
		throw new IllegalArgumentException("String is not a valid request type");
	}

	/**
	 * Checks if the string s matches the RequestType requestType's regex
	 *
	 * @param s String to check against the regex
	 * @param requestType the RequestType to get the regex of
	 * @return true if the string matches the regex, false otherwise
	 */
	public static boolean checkIfStringMatchesRegex(String s, RequestType requestType){
		Pattern pattern = Pattern.compile(requestType.regexPattern, Pattern.MULTILINE + Pattern.UNICODE_CASE);
		Matcher matcher = pattern.matcher(s);
		return matcher.find();
	}
}