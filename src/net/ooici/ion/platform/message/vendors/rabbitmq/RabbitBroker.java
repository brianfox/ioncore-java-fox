package net.ooici.ion.platform.message.vendors.rabbitmq;

import java.io.IOException;
import java.util.Observable;

import org.apache.log4j.Logger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import net.ooici.ion.cc.messaging.Broker;
import net.ooici.ion.cc.messaging.Mailbox;
import net.ooici.ion.cc.messaging.MessagingException;
import net.ooici.ion.lifecycle.LifeCycle;
import net.ooici.ion.lifecycle.LifeCycleException;
import net.ooici.ion.properties.LocalProperties;
import net.ooici.ion.properties.PropertiesException;

/**
 * 
 * @author brianfox
 *
 */
public class RabbitBroker extends Broker {

	private static Logger log = Logger.getLogger(RabbitBroker.class);

	
	public static final String[] REQUIRED_PROPERTIES = 
		new String[]{"host","port","username","password","vhost"};
	
	private LocalProperties properties;

	private Connection connection;
	private ConnectionFactory factory; 
	private String username;
	private String password;
	private String vhost;
	private String host;
	private int port;

	/**
	 * RabbitBroker only permits factory pattern object construction.  
	 * 
	 * @param properties
	 * @return
	 * @throws MessagingException
	 * @throws PropertiesException
	 */
	public static Broker createBroker(LocalProperties properties) 
	throws 
		MessagingException, 
		PropertiesException 
	{
		return new RabbitBroker(properties);
	}

	
	
	/**
	 * Breaks away from the interface, which is why it should be hidden.
	 * 
	 * @param properties
	 * @throws MessagingException
	 * @throws PropertiesException
	 */
	private RabbitBroker(LocalProperties properties) 
	throws 
		MessagingException, 
		PropertiesException 
	{
		this.properties = properties;
		checkProperties(this.properties);

		username = properties.getString("username");
		password = properties.getString("password");
		vhost = properties.getString("vhost");
		host = properties.getString("host");
		port = properties.getInt("port");

		factory = new ConnectionFactory();
		factory.setUsername(username);
		factory.setPassword(password);
		factory.setVirtualHost(vhost);
		factory.setHost(host);
		factory.setPort(port);

		log.info(String.format("%s started successfully", this.getClass().getSimpleName()));

	}

	

	/**
	 * Helper method which looks for missing properties.
	 * 
	 * @param properties
	 * @throws PropertiesException
	 */
	private void checkProperties(LocalProperties properties) 
	throws
		PropertiesException
	{
		for (String s : REQUIRED_PROPERTIES) {
			if (!properties.containsKey(s)) {
				String err = String.format("required field missing from properties: %s", s);
				log.error(err);
				throw new PropertiesException(err);
			}
		}
	}



	/**
	 * Creates a RabbitMQ Broker Channel.
	 * 
	 * @return
	 * @throws MessagingException
	 */
	public Channel getChannel() throws MessagingException {
		try {
			checkState(log, "getChannel", LifeCycle.State.ACTIVE);
			return connection.createChannel();
		} catch (Exception e) {
			String err = String.format("Could not create rabbit channel: %s", e.getMessage());
			log.error(err);
			throw new MessagingException(err);
		}
	}

	
	/**
	 * Uses this RabbitMQ broker connection to create a Mailbox and
	 * bind it to a Queue.  This method combines the calls of 
	 * createExchangeSpace, createExchangeName, createQueue, 
	 * and createBinding into one convenience call.
	 * @throws LifeCycleException 
	 *   
	 */
	@Override
	public void createMailbox(Mailbox mailbox) 
	throws 
		MessagingException, 
		LifeCycleException 
	{
		checkState(log, "createMailbox", LifeCycle.State.ACTIVE);
		
		createExchangeSpace(mailbox);
		createExchangeName(mailbox);
		createQueue(mailbox);
		createBinding(mailbox);
	}

	
	
	/**
	 * Uses this RabbitMQ broker connection to create an ExchangeSpace 
	 * messaging representation within the RabbitMQ broker.  The 
	 * parameters for the ExchangeSpace are drawn from the Mailbox 
	 * parameter.
	 *   
	 */
	@Override
	public void createExchangeSpace(Mailbox mailbox) 
	throws 
		LifeCycleException
	{
		// This is a purposeful no op.  There is no ExchangeSpace equivalent 
		// representation in AMQP

		checkState(log, "createExchangeSpace", LifeCycle.State.ACTIVE);
		log.debug("Created exchange space: " + mailbox.getExchangeSpace().getName());
	}

	

	/**
	 * Uses this RabbitMQ broker connection to create an ExchangeName 
	 * messaging representation within the RabbitMQ broker.  The 
	 * parameters for the ExchangeName are drawn from the Mailbox 
	 * parameter.
	 *   
	 */
	@Override
	public void createExchangeName(Mailbox mailbox) 
	throws 
		MessagingException, 
		LifeCycleException
	{
		checkState(log, "createExchangeName", LifeCycle.State.ACTIVE);
		try {
			Channel channel = null;
			channel = connection.createChannel();
			RabbitExchangeMap.declareExchange(channel, mailbox);
			channel.close();
			log.debug("Created exchange name: " + mailbox.getExchangeName().getName());
		} 
		catch (IOException e) {
			String err = String.format(
					"could not create exchange: %s  error: %s", 
					RabbitExchangeMap.getExchangeName(mailbox), 
					e.getMessage()
			);
			log.error(err);
			throw new MessagingException(err, e);
		} 
	}

	
	
	/**
	 * Uses this RabbitMQ broker connection to create a Queue messaging 
	 * representation within the RabbitMQ broker.  The parameters for the 
	 * Queue are drawn from the Mailbox parameter.
	 *   
	 */
	@Override
	public void createQueue(Mailbox mailbox) 
	throws 
		MessagingException, 
		LifeCycleException
	{
		checkState(log, "createQueue", LifeCycle.State.ACTIVE);
		try {
			Channel channel = null;
			channel = connection.createChannel();
			RabbitExchangeMap.declareQueue(channel, mailbox);
			channel.close();
			log.debug("Created queue: " + mailbox.getQueue().getName());
		} 
		catch (Exception e) {
			String err = String.format(
					"could not create queue: %s  error: %s", 
					RabbitExchangeMap.getQueueName(mailbox), 
					e.getMessage()
			);
			log.error(err);
			throw new MessagingException(err, e);
		} 
	}


	@Override
	public void createBinding(Mailbox mailbox)
	throws 
		MessagingException, 
		LifeCycleException
	{
		checkState(log, "createBinding", LifeCycle.State.ACTIVE);
		try {
			Channel channel = null;
			channel = connection.createChannel();
			RabbitExchangeMap.declareBinding(channel, mailbox);
			channel.close();
			log.debug(String.format(
					"Created binding %s.%s -> %s",
					mailbox.getExchangeSpace().getName(),
					mailbox.getExchangeName().getName(),
					mailbox.getQueue().getName()
			));
		} 
		catch (Exception e) {
			String err = String.format(
					"could not create binding: %s  error: %s", 
					RabbitExchangeMap.getQueueName(mailbox), 
					e.getMessage()
			);
			log.error(err);
			throw new MessagingException(err, e);
		} 
	}

	
	/*
	 * LIFE CYCLE METHODS 
	 */

	@Override
	public void update(Observable o, Object arg) {
		// System.err.print(o + "   " + arg);
	}
	
	
	@Override
	public void slc_init() 
	throws LifeCycleException 
	{
		
	}
	

	@Override
	public void slc_activate() 
	throws LifeCycleException 
	{			
		super.slc_activate();
		try {
			connection = factory.newConnection();
		} catch (IOException e) {
			slc_error(e);
		}
	}

	
	@Override
	public void slc_terminate() 
	throws LifeCycleException 
	{
		super.slc_terminate();
		try {
			connection.close();
		}
		catch (IOException e) {
			slc_error(e);
		}
	}


}
