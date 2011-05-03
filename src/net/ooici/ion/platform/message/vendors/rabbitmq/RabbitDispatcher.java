package net.ooici.ion.platform.message.vendors.rabbitmq;

import java.io.IOException;

import org.apache.log4j.Logger;

import net.ooici.ion.cc.message.stack.broker.*;
import net.ooici.ion.cc.message.stack.dispatcher.*;
import net.ooici.ion.cc.message.stack.mailbox.Mailbox;
import net.ooici.ion.properties.*;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

/**
 * 
 * @author brianfox
 *
 */
public class RabbitDispatcher extends Dispatcher {

	private static Logger log = Logger.getLogger(RabbitDispatcher.class);
	
	public static final String[] REQUIRED_PROPERTIES = 
		new String[]
		{
		};

	
	
	private Channel channel;
	private RabbitBroker rbroker;
	private LocalProperties properties;
	
	public RabbitDispatcher(
			LocalProperties properties,
			Broker broker
	) 
	throws 
		BrokerException, 
		PropertiesException 
	{
		// super(broker, scheduler);
		super(properties, broker);
		log.info(String.format("%s starting", this.getClass().getSimpleName()));

		// Properties setup
		checkProperties(properties);
		this.properties = properties;
		this.rbroker = (RabbitBroker)broker;
		
		// We need a working channel for obvious reasons
		this.channel = rbroker.getChannel();

		
		log.info(String.format("%s started successfully", this.getClass().getSimpleName()));

	}


	private void checkProperties(LocalProperties properties) 
	throws
		PropertiesException
	{
		for (String s : REQUIRED_PROPERTIES) {
			if (!properties.containsKey(s)) {
				String err = String.format("required field missing: %s", this.getClass().getSimpleName());
				log.error(err);
				throw new PropertiesException(err);
			}
		}
	}
	

	@Override
	public void registerMailbox(Mailbox mailbox) 
	throws 
		DispatcherException, 
		BrokerException 
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
			log.error(err);
			throw new DispatcherException(err, e);
		}
		log.debug("Created consumer for queue: " + queueName);
	}

	
	@Override
	public void unregisterMailbox(Mailbox m) {
		// TODO Auto-generated method stub
		
	}


	private void resetChannel() 
	throws BrokerException 
	{
		if (!channel.isOpen()) {
			channel = rbroker.getChannel();
			log.info(
					"Forced to reset RabbitMQ channel in dispatcher.  " 
					+ "Check AMQP spec for events that might close a channel."
			);

		}
	}


	


	@Override
	protected void publish(String address, String routingKey, byte[] raw) 
	throws 
		DispatcherException 
	{
		// basicPublish(
		//     java.lang.String exchange, 
		//     java.lang.String routingKey, 
		//     boolean mandatory, 
		//     boolean immediate, 
		//     AMQP.BasicProperties props, 
		//     byte[] body
		// ) 
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
				log.info("Reset RabbitMQ Broker channel after failing to publish message.");
				try {
					resetChannel();
				}
				catch (BrokerException e1) {
					String err = String.format("Could not reset RabbitMQ Broker channel.");
					log.error(err);
					throw new DispatcherException(err);
				}
			}
		}
		String err = String.format("Could not send rabbit message.  Addr: %s  RoutingKey: %s", address, routingKey);
		log.error(err);
		throw new DispatcherException(err);
	}


}

