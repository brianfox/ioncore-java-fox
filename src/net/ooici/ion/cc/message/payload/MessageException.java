package net.ooici.ion.cc.message.payload;

@SuppressWarnings("serial")
public class MessageException extends Exception {

	public MessageException(String msg) {
		super(msg);
	}

	public MessageException(String msg, Exception e) {
		super(msg, e);
	}
}
