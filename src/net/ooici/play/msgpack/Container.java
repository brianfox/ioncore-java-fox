package net.ooici.play.msgpack;

import java.util.HashMap;

import org.apache.log4j.Logger;

import net.ooici.ion.cc.message.MessageManager;
import net.ooici.ion.cc.message.QosPriority;
import net.ooici.ion.cc.message.exchange.ExchangeName;
import net.ooici.ion.cc.message.exchange.ExchangeSpace;
import net.ooici.ion.cc.message.exchange.ExchangeType;
import net.ooici.ion.cc.message.exchange.Queue;
import net.ooici.ion.cc.message.payload.Body;
import net.ooici.ion.cc.message.payload.Header;
import net.ooici.ion.cc.message.payload.Message;
import net.ooici.ion.cc.message.stack.MessageStack;
import net.ooici.ion.cc.message.stack.mailbox.Mailbox;
import net.ooici.ion.cc.message.stack.mailbox.ProcessMailbox;
import net.ooici.ion.config.LocalProperties;
import net.ooici.ion.platform.message.vendors.rabbitmq.RabbitBroker;
import net.ooici.ion.platform.serialization.generic.MsgpackSerializer;
import net.ooici.util.msgpack.elements.MPElement;
import net.ooici.util.msgpack.elements.MPMap;

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
			
		Thread.sleep(300000);
	}
	
	
}

