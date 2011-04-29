package net.ooici.ion.platform.serialization.generic;

import java.util.HashMap;

import net.ooici.ion.cc.message.payload.Body;
import net.ooici.ion.cc.message.payload.Header;
import net.ooici.ion.cc.message.payload.Message;
import net.ooici.ion.cc.message.payload.MessageException;
import net.ooici.ion.cc.message.stack.serialization.SerializationException;
import net.ooici.ion.cc.message.stack.serialization.Serializer;

public class NullSerializer implements Serializer {

	@Override
	public Message deserialize(byte[] b) 
	throws 
		SerializationException, 
		MessageException 
	{
		Message ret = new Message(
				new Header(new HashMap<String,String>()),
				new Body()
		);
		return ret;
		// return new Message(new Header(), new Body());
	}

	@Override
	public byte[] serialize(Message m) throws SerializationException {
		return new byte[0];
	}

}
