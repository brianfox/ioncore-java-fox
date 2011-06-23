package net.ooici.ion.cc.messaging;

import java.util.concurrent.ConcurrentLinkedQueue;

import net.ooici.ion.lifecycle.LifeCycleException;

public class MessageQueue {
	private Mailbox mailbox;
	private ConcurrentLinkedQueue<byte[]> queue;
	private Dispatcher owner;
	
	public MessageQueue(Dispatcher dispatcher, Mailbox mailbox) {
		this.queue = new ConcurrentLinkedQueue<byte[]>();
		this.owner = dispatcher;
		this.mailbox = mailbox;
	}
	
	public Mailbox getMailbox() {
		return mailbox;
	}
	
	public byte[] next() {
		return queue.poll();
	}

	public void add(byte[] body) {
		queue.add(body);
		
	}

	public void dispatch() 
	throws LifeCycleException 
	{
		owner.dispatch(this);
	}
}
