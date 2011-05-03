package net.ooici.ion.platform.serialization.generic;

import java.util.HashMap;

import net.ooici.ion.cc.message.payload.Body;
import net.ooici.ion.cc.message.payload.Header;
import net.ooici.ion.cc.message.payload.Message;
import net.ooici.ion.cc.message.payload.MessageException;
import net.ooici.ion.cc.message.stack.serialization.SerializationException;
import net.ooici.ion.cc.message.stack.serialization.Serializer;
import net.ooici.util.msgpack.MessagePackException;
import net.ooici.util.msgpack.elements.MPElement;
import net.ooici.util.msgpack.elements.MPMap;
import net.ooici.util.msgpack.elements.MPRaw;

public class MsgpackSerializer implements Serializer {

	@Override
	public Message deserialize(byte[] bytes) 
	throws 
		SerializationException,
		MessageException 
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
			throw new SerializationException("Could not deserialize message: " + e);
		}
	}

	
	@Override
	public byte[] serialize(Message message) 
	throws 
		SerializationException 
	{
		Header header = message.getHeader();
		Body body = message.getBody();

		byte[] encHeader;
		byte[] encBody;
		
		MPMap mmap = new MPMap();
		try {
			for (String key : header.keySet())
				mmap.put(new MPRaw(key), new MPRaw(header.get(key)));
			encHeader = mmap.encode();
		} catch (MessagePackException e) {
			throw new SerializationException("Could not encode message", e);
		}
		return encHeader;
	}

}
