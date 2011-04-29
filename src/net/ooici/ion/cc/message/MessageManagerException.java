package net.ooici.ion.cc.message;

import net.ooici.ion.cc.ContainerException;

@SuppressWarnings("serial")
public class MessageManagerException extends ContainerException {

	public MessageManagerException(String err) {
		super(err);
	}
	
	public MessageManagerException(String err, Exception e) {
		super(err, e);
	}

}
