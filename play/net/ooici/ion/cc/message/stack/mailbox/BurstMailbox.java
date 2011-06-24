package net.ooici.ion.cc.message.stack.mailbox;


import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooici.ion.cc.messaging.Mailbox;
import net.ooici.ion.cc.messaging.exchange.ExchangeName;
import net.ooici.ion.cc.messaging.exchange.ExchangeSpace;
import net.ooici.ion.cc.messaging.exchange.Queue;
import net.ooici.ion.cc.messaging.message.Body;
import net.ooici.ion.cc.messaging.message.Header;
import net.ooici.ion.cc.messaging.message.Message;


public class BurstMailbox extends Mailbox {
	
	int count = 0;
	long time = 0;
	long burstSize = 50;
	
	Logger logger = LoggerFactory.getLogger(BurstMailbox.class);
	
	public BurstMailbox(ExchangeSpace es, ExchangeName en, Queue q) {
		super(es, en, q);
		time = System.nanoTime();
	}

	@Override
	public void onMail(Message m) {
		// log.debug("Received message, keys: " + m.getHeader().keySet().size());

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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		count++;
		if (count % 1000 == 0) {
			long nano = System.nanoTime() - time;
			long sec = nano / 1000000000L;
			float mps = count * 1.0F / sec;
			logger.info(String.format("Count:  %20d    Msg/s:  %5.2f", count, mps));
			//log.info(String.format("%n%s%n", m));
		}
		// for (String s : m.getHeader().keySet())
		//	System.out.println(s);
	}
}
