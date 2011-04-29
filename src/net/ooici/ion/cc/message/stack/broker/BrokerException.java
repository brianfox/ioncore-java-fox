package net.ooici.ion.cc.message.stack.broker;

import net.ooici.ion.cc.ContainerException;


@SuppressWarnings("serial")
public class BrokerException extends ContainerException {

	public BrokerException(String message) {
		super(message);
	}

	public BrokerException(String message, Exception e) {
		super(message, e);
	}

}
