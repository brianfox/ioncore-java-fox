package net.ooici.util.msgpack;

import java.util.HashMap;

import org.apache.log4j.Logger;

import net.ooici.ion.cc.messaging.MessageManager;
import net.ooici.ion.cc.messaging.message.Body;
import net.ooici.ion.cc.messaging.message.Header;
import net.ooici.ion.cc.messaging.message.Message;
import net.ooici.ion.cc.messaging.platform.rabbitmq.RabbitBroker;
import net.ooici.ion.cc.messaging.serialization.generic.MsgpackSerializer;

public class Container {

	private static Logger log = Logger.getLogger(RabbitBroker.class);
	
	private MessageManager msgManager;

	public static void main(String[] args) 
	throws 
		Exception
	{

		HashMap<String,String> map = new HashMap<String,String>();
		map.put("a","A");
		map.put("b","B");
		String s = new String(new byte[10000]);
		map.put("c", s);
		Header header = new Header(map);
		Message m = new Message(header, new Body());
		MsgpackSerializer serializer = new MsgpackSerializer();
		for (int i=0; i < 500000; i++) {
			byte[] bytes = serializer.serialize(m);
			Message m2 = serializer.deserialize(bytes);
		}
			
		Thread.sleep(60000);
	}
	
	
}

