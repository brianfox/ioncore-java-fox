package net.ooici.ion.cc.messaging.platform.rabbitmq;

import java.io.IOException;
import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooici.ion.cc.messaging.MessageQueue;
import net.ooici.ion.lifecycle.LifeCycleException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import net.ooici.ion.cc.messaging.Broker;
import net.ooici.ion.cc.messaging.Dispatcher;
import net.ooici.ion.cc.messaging.Mailbox;
import net.ooici.ion.cc.messaging.MessagingException;
import net.ooici.ion.lifecycle.LifeCycle;
import net.ooici.ion.properties.*;


/**
 * 
 * @author brianfox
 *
 */
public class RabbitDispatcher extends Dispatcher {

	Logger logger = LoggerFactory.getLogger(RabbitDispatcher.class);
	
	public static final String[] REQUIRED_PROPERTIES = 
		new String[]
		{
		};

	
	
	private Channel channel;
	private RabbitBroker rbroker;
	// private LocalProperties properties;
	
	public RabbitDispatcher(
			LocalProperties properties,
			Broker broker
	) 
	throws 
		MessagingException, 
		PropertiesException 
	{
		// super(broker, scheduler);
		super(properties, broker);

		// Properties setup
		checkProperties(properties);
		// this.properties = properties;
		this.rbroker = (RabbitBroker)broker;
	}


	private void checkProperties(LocalProperties properties) 
	throws
		PropertiesException
	{
		for (String s : REQUIRED_PROPERTIES) {
			if (!properties.containsKey(s)) {
				String err = String.format("required field missing: %s", this.getClass().getSimpleName());
				logger.error(err);
				throw new PropertiesException(err);
			}
		}
	}
	

	@Override
	public void registerMailbox(Mailbox mailbox) 
	throws MessagingException
	{
		String queueName = String.format(
				"%s.%s.%s",
				mailbox.getExchangeSpace().getName(), 
				mailbox.getExchangeName().getName(),
				mailbox.getQueue().getName()
		);
		MessageQueue queue = new MessageQueue(this, mailbox);
		RabbitConsumer consumer = new RabbitConsumer(channel, queue);
		boolean autoAck = true;
		
		try {
			channel.basicConsume(queueName, autoAck, consumer);
		}
		catch (IOException e) {
			String err = String.format("could not create consumer for queue: %s, err: %s", queueName, e.getMessage());
			logger.error(err);
			throw new MessagingException(err, e);
		}
		logger.debug("Created consumer for queue: " + queueName);
	}

	
	@Override
	public void unregisterMailbox(Mailbox m) throws LifeCycleException {
		checkState(logger, "publish", LifeCycle.State.ACTIVE);

		// TODO Auto-generated method stub
		
	}


	private void resetChannel() 
	throws MessagingException 
	{
		if (!channel.isOpen()) {
			channel = rbroker.getChannel();
			logger.info(
					"Forced to reset RabbitMQ channel in dispatcher.  " 
					+ "Check AMQP spec for events that might close a channel."
			);

		}
	}


	


	@Override
	protected void publish(String address, String routingKey, byte[] raw) 
	throws 
		MessagingException, 
		LifeCycleException 
	{
		checkState(logger, "publish", LifeCycle.State.ACTIVE);

		for (int i=0; i < 2; i++) {
			try {
				channel.basicPublish(
						address, 
						routingKey, 
						false,  // isMandatory 
						false,  // isImmediate 
						MessageProperties.BASIC, 
						raw
				);
				return;
			}
			catch (Exception e) {
				logger.info("Reset RabbitMQ Broker channel after failing to publish message.");
				try {
					resetChannel();
				}
				catch (MessagingException e1) {
					String err = String.format("Could not reset RabbitMQ Broker channel.");
					logger.error(err);
					throw new MessagingException(err);
				}
			}
		}
		String err = String.format("Could not send rabbit message.  Addr: %s  RoutingKey: %s", address, routingKey);
		logger.error(err);
		throw new MessagingException(err);
	}

	
	/*
	 * LIFE CYCLE METHODS 
	 */

	@Override
	public void update(Observable o, Object arg) {
		// System.err.print(o + "   " + arg);
	}
	
	
	@Override
	public void slc_activate() 
	throws LifeCycleException 
	{			
		super.slc_activate();
		try {
			channel = rbroker.getChannel();
		} catch (MessagingException e) {
			slc_error(e);
		}
	}

	
	@Override
	public void slc_terminate() 
	throws LifeCycleException 
	{
		super.slc_terminate();
		try {
			if (channel.isOpen()) 
				channel.close();
		}
		catch (IOException e) {
			slc_error(e);
		}
	}


}

