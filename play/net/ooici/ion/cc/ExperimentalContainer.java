package net.ooici.ion.cc;

import java.util.HashMap;

import org.apache.log4j.Logger;

import net.ooici.ion.cc.ContainerException;
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
import net.ooici.ion.cc.message.stack.mailbox.BurstMailbox;
import net.ooici.ion.platform.message.vendors.rabbitmq.RabbitBroker;
import net.ooici.ion.properties.LocalProperties;

public class ExperimentalContainer extends Container {

	private static Logger log = Logger.getLogger(RabbitBroker.class);
	
	private MessageManager msgManager;

	public static void main(String[] args) 
	throws 
		Exception
	{
		Container container = new Container();
		container.start();
		

		ExchangeSpace es = new ExchangeSpace("estest");
		ExchangeName en = new ExchangeName(ExchangeType.PROCESS, "entest");
		Queue qu = new Queue("qutest"); 

		BurstMailbox m = new BurstMailbox(es, en, qu);
		container.msgManager.registerMailbox(QosPriority.HIGH, m);

		HashMap<String,String> map = new HashMap<String,String>();
		map.put("a","A");
		map.put("b","B");
		String s = new String(new byte[2000]);
		map.put("c", s);
		Header header = new Header(map);
		for (int i=0; i < 500; i++)
			new Message(header, new Body());
		m.send(new Message(header, new Body()));
		Thread.sleep(3000);
		container.stop();
		
	}
	
	
	public void registerMailbox(QosPriority priority, Mailbox mailbox) 
	throws 
		ContainerException
	{
		msgManager.registerMailbox(priority, mailbox);
	}


	public void start() {
		try {
			log.info(String.format("%s starting", this.getClass().getSimpleName()));
			LocalProperties properties = new LocalProperties();
			properties.load("./properties/container.properties");
			msgManager = new MessageManager(properties.getSection(MessageManager.CONFIG_SECTION));
			log.info(String.format("%s started successfully", this.getClass().getSimpleName()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			log.info("Container stopping.");
			msgManager.close();
			log.info("Container stopped successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

