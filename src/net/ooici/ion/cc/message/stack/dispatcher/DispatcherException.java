package net.ooici.ion.cc.message.stack.dispatcher;

import net.ooici.ion.cc.ContainerException;


@SuppressWarnings("serial")
public class DispatcherException extends ContainerException {

	public DispatcherException(String message) {
		super(message);
	}

	public DispatcherException(String message, Exception e) {
		super(message, e);
	}

}
