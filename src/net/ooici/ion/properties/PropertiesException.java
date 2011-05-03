package net.ooici.ion.properties;

@SuppressWarnings("serial")
public class PropertiesException extends Exception {

	public PropertiesException(String message) {
		super(message);
	}

	public PropertiesException(String message, Exception e) {
		super(message, e);
	}
}
