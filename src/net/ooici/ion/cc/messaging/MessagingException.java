package net.ooici.ion.cc.messaging;

import net.ooici.ion.cc.ContainerException;


@SuppressWarnings("serial")
public class MessagingException extends ContainerException {

	public MessagingException(String message) {
		super(message);
	}

	public MessagingException(String message, Exception e) {
		super(message, e);
	}

}
