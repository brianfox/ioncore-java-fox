package net.ooici.ion.platform.message.vendors.rabbitmq;

import java.io.IOException;

import org.apache.log4j.Logger;

import net.ooici.ion.cc.message.stack.dispatcher.MessageQueue;
import net.ooici.ion.lifecycle.LifeCycleException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.utility.Utility;

/**
 * 
 * @author brianfox
 *
 */
public class RabbitConsumer extends DefaultConsumer {

	private static Logger log = Logger.getLogger(RabbitConsumer.class);
	
	private MessageQueue queue;
	private volatile ShutdownSignalException shutdown;

	
	public RabbitConsumer(
			Channel channel,
			MessageQueue queue
	) {
	    super(channel);
	    this.queue = queue;
		log.debug("Created RabbitConsumer: " + queue.getMailbox().oneLiner());
	}



	@Override 
	public void handleShutdownSignal(
			String consumerTag,
	        ShutdownSignalException sig
	) 
	{
	    shutdown = sig;
		log.debug("Closed RabbitConsumer: " + queue.getMailbox().oneLiner());
	}
	
	
	
	@Override 
	public void handleDelivery(
			String consumerTag,
	        Envelope envelope,
	        AMQP.BasicProperties properties,
	        byte[] body 
	) 
	throws IOException
	{
	    checkShutdown();
	    queue.add(body);
	    try {
			queue.dispatch();
		} catch (LifeCycleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	
	/**
	 * Check if we are in shutdown mode and if so throw an exception.
	 */
	private void checkShutdown() {
	    if (shutdown != null)
	        throw Utility.fixStackTrace(shutdown);
	}



	
//	/**
//	 * If this is a non-POISON non-null delivery simply return it.
//	 * If this is POISON we are in shutdown mode, throw _shutdown
//	 * If this is null, we may be in shutdown mode. Check and see.
//	 */
//	private Delivery handle(Delivery delivery) {
//	    if (delivery == POISON ||
//	        delivery == null && _shutdown != null) {
//	        if (delivery == POISON) {
//	            _queue.add(POISON);
//	            if (_shutdown == null) {
//	                throw new IllegalStateException(
//	                    "POISON in queue, but null _shutdown. " +
//	                    "This should never happen, please report as a BUG");
//	            }
//	        }
//	        throw Utility.fixStackTrace(_shutdown);
//	    }
//	    return delivery;
//	}
//	
//	/**
//	 * Main application-side API: wait for the next message delivery and return it.
//	 * @return the next message
//	 * @throws InterruptedException if an interrupt is received while waiting
//	 * @throws ShutdownSignalException if the connection is shut down while waiting
//	 */
//	public Delivery nextDelivery()
//	    throws InterruptedException, ShutdownSignalException
//	{
//	    return handle(_queue.take());
//	}
//	
//	/**
//	 * Main application-side API: wait for the next message delivery and return it.
//	 * @param timeout timeout in millisecond
//	 * @return the next message or null if timed out
//	 * @throws InterruptedException if an interrupt is received while waiting
//	 * @throws ShutdownSignalException if the connection is shut down while waiting
//	 */
//	public Delivery nextDelivery(long timeout)
//	    throws InterruptedException, ShutdownSignalException
//	{
//	    return handle(_queue.poll(timeout, TimeUnit.MILLISECONDS));
//	}
}
