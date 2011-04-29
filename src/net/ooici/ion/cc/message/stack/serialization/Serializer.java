package net.ooici.ion.cc.message.stack.serialization;

import net.ooici.ion.cc.message.payload.Message;
import net.ooici.ion.cc.message.payload.MessageException;

public interface Serializer {

	Message deserialize(byte[] b) 
	throws SerializationException, MessageException;
	
	byte[] serialize(Message m)
	throws SerializationException;

}
