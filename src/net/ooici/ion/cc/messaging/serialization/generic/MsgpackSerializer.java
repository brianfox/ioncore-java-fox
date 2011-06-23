package net.ooici.ion.cc.messaging.serialization.generic;

import java.util.HashMap;

import net.ooici.ion.cc.messaging.MessagingException;
import net.ooici.ion.cc.messaging.message.Body;
import net.ooici.ion.cc.messaging.message.Header;
import net.ooici.ion.cc.messaging.message.Message;
import net.ooici.ion.cc.messaging.serialization.Serializer;
import net.ooici.util.msgpack.MessagePackException;
import net.ooici.util.msgpack.elements.MPElement;
import net.ooici.util.msgpack.elements.MPMap;
import net.ooici.util.msgpack.elements.MPRaw;

public class MsgpackSerializer implements Serializer {

	@Override
	public Message deserialize(byte[] bytes) 
	throws MessagingException 
	{
		try {
			HashMap<String,String> map = new HashMap<String,String>();
			MPMap kv = (MPMap)MPElement.decode(bytes);
			for (String next : kv.keySet()) {
				map.put(next, kv.get(next).toString());
			}
			Header header = new Header(map);
			Body body = new Body();
			return new Message(header, body);
		} catch (MessagePackException e) {
			throw new MessagingException("Could not deserialize message: " + e);
		}
	}

	
	@Override
	public byte[] serialize(Message message) 
	throws MessagingException 
	{
		Header header = message.getHeader();
		// Body body = message.getBody();

		byte[] encHeader;
		// byte[] encBody;
		
		MPMap mmap = new MPMap();
		try {
			for (String key : header.keySet())
				mmap.put(new MPRaw(key), new MPRaw(header.get(key)));
			encHeader = mmap.encode();
		} catch (MessagePackException e) {
			throw new MessagingException("Could not encode message", e);
		}
		return encHeader;
	}

}
