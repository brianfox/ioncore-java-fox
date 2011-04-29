package net.ooici.ion.platform.message.vendors.rabbitmq;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import net.ooici.ion.cc.message.stack.broker.Broker;
import net.ooici.ion.cc.message.stack.broker.BrokerException;
import net.ooici.ion.cc.message.stack.mailbox.Mailbox;
import net.ooici.ion.config.LocalProperties;
import net.ooici.ion.config.PropertiesException;

/**
 * 
 * @author brianfox
 *
 */
public class RabbitBroker implements Broker {

	private static Logger log = Logger.getLogger(RabbitBroker.class);

	
	public static final String[] REQUIRED_PROPERTIES = 
		new String[]{"host","port","username","password","vhost"};
	
	private LocalProperties properties;
	private Connection connection;

	

	/**
	 * RabbitBroker only permits factory pattern object construction.  
	 * 
	 * @param properties
	 * @return
	 * @throws BrokerException
	 * @throws PropertiesException
	 */
	public static Broker createBroker(LocalProperties properties) 
	throws 
		BrokerException, 
		PropertiesException 
	{
		return new RabbitBroker(properties);
	}

	
	
	/**
	 * Breaks away from the interface, which is why it should be hidden.
	 * 
	 * @param properties
	 * @throws BrokerException
	 * @throws PropertiesException
	 */
	private RabbitBroker(LocalProperties properties) 
	throws 
		BrokerException, 
		PropertiesException 
	{
		this.properties = properties;
		checkProperties(this.properties);

		String username = properties.getString("username");
		String password = properties.getString("password");
		String vhost = properties.getString("vhost");
		String host = properties.getString("host");
		int port = properties.getInt("port");

		ConnectionFactory factory = new ConnectionFactory();
		factory = new ConnectionFactory();
		factory.setUsername(username);
		factory.setPassword(password);
		factory.setVirtualHost(vhost);
		factory.setHost(host);
		factory.setPort(port);

		try {
			connection = factory.newConnection();
			log.info(String.format(
					"%s starting:  host=%s   port=%d   username=%s   vhost=%s",
					this.getClass().getSimpleName(),
					host,
					port,
					username,
					vhost
			));
		} 
		catch(IOException e) {
			String err = String.format("failed to create broker: %s", e.getMessage());
			log.error(err);
			throw new BrokerException(err);
		}
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
	 * @throws BrokerException
	 */
	public Channel getChannel() throws BrokerException {
		
		try {
			return connection.createChannel();
		} catch (Exception e) {
			String err = String.format("Could not create rabbit channel: %s", e.getMessage());
			log.error(err);
			throw new BrokerException(err);
		}
	}

	

	
	@Override
	public void close() 
	throws 
		BrokerException 
	{
		try {
			connection.close();
			
			log.info(String.format("Closed %s.", this.getClass().getSimpleName()));
		}
		catch (IOException e) {
			String err = String.format("Could not close broker: %s", e.getMessage());
			log.error(err);
			throw new BrokerException(err, e);
		}
	}

	

	/**
	 * Uses this RabbitMQ broker connection to create a Mailbox and
	 * bind it to a Queue.  This method combines the calls of 
	 * createExchangeSpace, createExchangeName, createQueue, 
	 * and createBinding into one convenience call.
	 *   
	 */
	@Override
	public void createMailbox(Mailbox mailbox) 
	throws BrokerException {
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
	throws BrokerException {
		// This is a purposeful no op.  There is no ExchangeSpace equivalent 
		// representation in AMQP
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
	public void createExchangeName(Mailbox mailbox) throws BrokerException {
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
			throw new BrokerException(err, e);
		} 
	}

	
	
	/**
	 * Uses this RabbitMQ broker connection to create a Queue messaging 
	 * representation within the RabbitMQ broker.  The parameters for the 
	 * Queue are drawn from the Mailbox parameter.
	 *   
	 */
	@Override
	public void createQueue(Mailbox mailbox) throws BrokerException {
		try {
			Channel channel = null;
			channel = connection.createChannel();
			RabbitExchangeMap.declareQueue(channel, mailbox);
			channel.close();
			log.debug("Created queue: " + mailbox.getQueue().getName());
		} 
		catch (IOException e) {
			String err = String.format(
					"could not create queue: %s  error: %s", 
					RabbitExchangeMap.getQueueName(mailbox), 
					e.getMessage()
			);
			log.error(err);
			throw new BrokerException(err, e);
		} 
	}


	@Override
	public void createBinding(Mailbox mailbox) throws BrokerException {
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
		catch (IOException e) {
			String err = String.format(
					"could not create binding: %s  error: %s", 
					RabbitExchangeMap.getQueueName(mailbox), 
					e.getMessage()
			);
			log.error(err);
			throw new BrokerException(err, e);
		} 

	}

}
