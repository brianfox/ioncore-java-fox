package net.ooici.ion.cc.message.stack.mailbox;


import java.util.HashMap;

import org.apache.log4j.Logger;

import net.ooici.ion.cc.ContainerException;
import net.ooici.ion.cc.message.exchange.ExchangeName;
import net.ooici.ion.cc.message.exchange.ExchangeSpace;
import net.ooici.ion.cc.message.exchange.Queue;
import net.ooici.ion.cc.message.payload.Body;
import net.ooici.ion.cc.message.payload.Header;
import net.ooici.ion.cc.message.payload.Message;


public class BurstMailbox extends Mailbox {
	
	int count = 0;
	long time = 0;
	long burstSize = 50;
	
	private static Logger log = Logger.getLogger(BurstMailbox.class);
	
	public BurstMailbox(ExchangeSpace es, ExchangeName en, Queue q) {
		super(es, en, q);
		time = System.nanoTime();
	}

	@Override
	public void onMail(Message m) {
		log.trace("Received message, keys: " + m.getHeader().keySet().size());

		HashMap<String,String> map = new HashMap<String,String>();
		map.put("a","A");
		map.put("b","B");
		map.put("long",new String(new byte[10000]));
		Header header = new Header(map);
		try {
			if (count % burstSize == 0) {
				for (int i=0; i < burstSize; i++)
					send(new Message(header, new Body()));
			}
		} catch (ContainerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		count++;
		if (count % 1000 == 0) {
			long nano = System.nanoTime() - time;
			long sec = nano / 1000000000L;
			float mps = count * 1.0F / sec;
			log.info(String.format("Count:  %20d    Msg/s:  %5.2f", count, mps));
			log.info(String.format("%n%s%n", m));
		}
		// for (String s : m.getHeader().keySet())
		//	System.out.println(s);
	}
}
