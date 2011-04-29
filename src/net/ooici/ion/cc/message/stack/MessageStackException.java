package net.ooici.ion.cc.message.stack;

import net.ooici.ion.cc.ContainerException;

@SuppressWarnings("serial")
public class MessageStackException extends ContainerException {

	public MessageStackException(String err) {
		super(err);
	}
	
	public MessageStackException(String err, Exception e) {
		super(err, e);
	}
}
