package net.ooici.ion.config;

@SuppressWarnings("serial")
public class PropertiesException extends Exception {

	public PropertiesException(String message) {
		super(message);
	}

	public PropertiesException(String message, Exception e) {
		super(message, e);
	}
}
