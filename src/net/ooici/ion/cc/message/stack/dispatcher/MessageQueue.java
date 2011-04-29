package net.ooici.ion.cc.message.stack.dispatcher;

import java.util.concurrent.ConcurrentLinkedQueue;

import net.ooici.ion.cc.message.stack.mailbox.Mailbox;

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

	public void dispatch() {
		owner.dispatch(this);
	}
}
