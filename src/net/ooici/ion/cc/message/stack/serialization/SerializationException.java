package net.ooici.ion.cc.message.stack.serialization;

import net.ooici.ion.cc.ContainerException;
import net.ooici.util.msgpack.MessagePackException;

@SuppressWarnings("serial")
public class SerializationException extends ContainerException {

	public SerializationException(String err) {
		super(err);
	}

	public SerializationException(String err, Exception e) {
		super(err, e);
	}

}
