package net.ooici.ion.cc.messaging.serialization.generic;

import java.util.HashMap;

import net.ooici.ion.cc.messaging.MessagingException;
import net.ooici.ion.cc.messaging.message.Body;
import net.ooici.ion.cc.messaging.message.Header;
import net.ooici.ion.cc.messaging.message.Message;
import net.ooici.ion.cc.messaging.serialization.Serializer;

public class NullSerializer implements Serializer {

	@Override
	public Message deserialize(byte[] b) 
	throws MessagingException 
	{
		Message ret = new Message(
				new Header(new HashMap<String,String>()),
				new Body()
		);
		return ret;
	}

	@Override
	public byte[] serialize(Message m) throws MessagingException {
		return new byte[0];
	}
}