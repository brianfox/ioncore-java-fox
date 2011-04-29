package net.ooici.util.msgpack;

@SuppressWarnings("serial")
public class MessagePackException extends Exception {

	public MessagePackException(String string) {
		super(string);
	}

	public MessagePackException(String string, Exception e) {
		super(string, e);
	}

}
