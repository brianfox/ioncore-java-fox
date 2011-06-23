package net.ooici.ion.cc.messaging;

import net.ooici.ion.cc.ContainerException;
import net.ooici.ion.cc.messaging.exchange.ExchangeName;
import net.ooici.ion.cc.messaging.exchange.ExchangeSpace;
import net.ooici.ion.cc.messaging.exchange.Queue;
import net.ooici.ion.cc.messaging.message.Message;
import net.ooici.ion.lifecycle.LifeCycle;
import net.ooici.ion.lifecycle.LifeCycleException;


public abstract class Mailbox extends LifeCycle {
	
	// private static Logger log = Logger.getLogger(Mailbox.class);
	
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
		ContainerException, LifeCycleException	
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
