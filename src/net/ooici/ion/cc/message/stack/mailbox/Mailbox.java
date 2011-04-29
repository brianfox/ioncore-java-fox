package net.ooici.ion.cc.message.stack.mailbox;


import org.apache.log4j.Logger;

import net.ooici.ion.cc.ContainerException;
import net.ooici.ion.cc.message.exchange.ExchangeName;
import net.ooici.ion.cc.message.exchange.ExchangeSpace;
import net.ooici.ion.cc.message.exchange.Queue;
import net.ooici.ion.cc.message.payload.Message;
import net.ooici.ion.cc.message.stack.broker.BrokerException;
import net.ooici.ion.cc.message.stack.dispatcher.Dispatcher;
import net.ooici.ion.cc.message.stack.dispatcher.DispatcherException;
import net.ooici.ion.cc.message.stack.serialization.SerializationException;


public abstract class Mailbox {
	
	private static Logger log = Logger.getLogger(Mailbox.class);
	
	private Dispatcher dispatcher;
	private ExchangeSpace es;
	private ExchangeName en;
	private Queue q;
	
	
	public Mailbox(ExchangeSpace es, ExchangeName en, Queue q) {
		this.es = es;
		this.en = en;
		this.q = q;
	}
	
	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}
	
	public ExchangeSpace getExchangeSpace() {
		return es;
	}

	
	public ExchangeName getExchangeName() {
		return en;
	}

	
	public Queue getQueue() {
		return q;
	}


	
	
	public void send(Message message) 
	throws 
		ContainerException	
	{
		dispatcher.send(message);
	}

	
	abstract public void onMail(Message m);

	
	public String oneLiner() {
		return toString();
	}
	
	
	@Override
	public String toString() {
		return String.format("[%s] %s.%s.%s", en.getExchangeType(), es.getName(), en.getName(), q.getName());
	}
}
