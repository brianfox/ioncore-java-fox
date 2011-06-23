package net.ooici.ion.cc.messaging.serialization;

import net.ooici.ion.cc.messaging.MessagingException;
import net.ooici.ion.cc.messaging.message.Message;

public interface Serializer {

	Message deserialize(byte[] b) 
	throws MessagingException;
	
	byte[] serialize(Message m)
	throws MessagingException;

}
