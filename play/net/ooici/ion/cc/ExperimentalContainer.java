package net.ooici.ion.cc;

import java.util.HashMap;

import org.apache.log4j.Logger;

import net.ooici.ion.cc.ContainerException;
import net.ooici.ion.cc.message.stack.mailbox.BurstMailbox;
import net.ooici.ion.cc.messaging.Mailbox;
import net.ooici.ion.cc.messaging.MessageManager;
import net.ooici.ion.cc.messaging.MessageStack;
import net.ooici.ion.cc.messaging.QosPriority;
import net.ooici.ion.cc.messaging.exchange.ExchangeName;
import net.ooici.ion.cc.messaging.exchange.ExchangeSpace;
import net.ooici.ion.cc.messaging.exchange.ExchangeType;
import net.ooici.ion.cc.messaging.exchange.Queue;
import net.ooici.ion.cc.messaging.message.Body;
import net.ooici.ion.cc.messaging.message.Header;
import net.ooici.ion.cc.messaging.message.Message;
import net.ooici.ion.platform.message.vendors.rabbitmq.RabbitBroker;
import net.ooici.ion.properties.LocalProperties;

public class ExperimentalContainer {

	public static void main(String[] args) 
	throws 
		Exception
	{
		Container container = new Container();
		container.slc_activate();
		

		ExchangeSpace es = new ExchangeSpace("estest");
		ExchangeName en = new ExchangeName(ExchangeType.PROCESS, "entest");
		Queue qu = new Queue("qutest"); 

		BurstMailbox m = new BurstMailbox(es, en, qu);
		container.registerMailbox(QosPriority.HIGH, m);

		HashMap<String,String> map = new HashMap<String,String>();
		map.put("a","A");
		map.put("b","B");
		String s = new String(new byte[2000]);
		map.put("c", s);
		Header header = new Header(map);
		for (int i=0; i < 500; i++)
			new Message(header, new Body());
		m.send(new Message(header, new Body()));
		Thread.sleep(300000);
		container.slc_terminate();
		
	}
	
}

