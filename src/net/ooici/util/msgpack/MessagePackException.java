package net.ooici.util.msgpack;

/**
 * An umbrella exception that catches various IO and numeric conversion
 * exceptions that can occur when encoding/decoding.
 * 
 * @author brianfox
 *
 */
@SuppressWarnings("serial")
public class MessagePackException extends Exception {

	public MessagePackException(String string) {
		super(string);
	}

	public MessagePackException(String string, Exception e) {
		super(string, e);
	}

}
