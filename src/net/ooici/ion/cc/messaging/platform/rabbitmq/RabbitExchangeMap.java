package net.ooici.ion.cc.messaging.platform.rabbitmq;

import java.io.IOException;
import com.rabbitmq.client.Channel;

import net.ooici.ion.cc.messaging.Mailbox;
import net.ooici.ion.cc.messaging.MessagingException;


/**
 * 
 * @author brianfox
 *
 */
public class RabbitExchangeMap {
	


	
	/**
	 * Helper function that takes care of some of the more unreadable 
	 * boilerplate code. 
	 * 
	 * @param mailbox
	 * @throws IOException
	 */
	public static void declareExchange(Channel ch, Mailbox mailbox) 
	throws MessagingException 
	{
		String exchangeName = getExchangeName(mailbox);

		// The most elaborate RabbitMQ method signature:
		// exchangeDeclare(
		//		java.lang.String exchange, 
		//		java.lang.String type, 
		//		boolean durable, 
		//		boolean autoDelete, 
		//		boolean internal, 
		//		java.util.Map<java.lang.String,java.lang.Object> arguments
		//	)
		
		try {
			switch (mailbox.getExchangeName().getExchangeType()) {
			case PROCESS:
				ch.exchangeDeclare(exchangeName, "direct", false, true, false, null);
				break;
			case SERVICE:
				ch.exchangeDeclare(exchangeName, "direct", false, true, false, null);
				break;
			case EXCHANGEPOINT:
				ch.exchangeDeclare(exchangeName, "topic",  false, true, false, null);
				break;
			default:
				throw new MessagingException(
						"undefined exchange type: " 
						+ mailbox.getExchangeName().getExchangeType()
				);
			}
		}
		catch (IOException e) {
			throw new MessagingException("could not define RabbitMQ exchange: " + e.getMessage(), e);
		}
		// ch.exchangeDeclare(exchangeName, "direct", false);
		// ch.exchangeDeclarePassive(exchangeName);

	}

	
	
	/**
	 * Helper function that takes care of some of the more unreadable 
	 * boilerplate code. 
	 * 
	 * @param mailbox
	 * @throws IOException
	 */
	public static void declareQueue(Channel ch, Mailbox mailbox) 
	throws MessagingException 
	{
		String queueName = getQueueName(mailbox);

		
		// The most elaborate RabbitMQ method signature:
		// queueDeclare(
		//		java.lang.String queue, 
		//		boolean durable, 
		//		boolean exclusive, 
		//		boolean autoDelete, 
		//		java.util.Map<java.lang.String, java.lang.Object> arguments
		//	) 
		try {
			switch (mailbox.getExchangeName().getExchangeType()) {
			case PROCESS:
				ch.queueDeclare(queueName, false, false, true, null);
				break;
			case SERVICE:
				ch.queueDeclare(queueName, false, false, true, null);
				break;
			case EXCHANGEPOINT:
				ch.queueDeclare(queueName, false, false, true, null);
				break;
			default:
				throw new MessagingException(
						"undefined exchange type: " 
						+ mailbox.getExchangeName().getExchangeType()
				);
			}
		}
		catch (IOException e) {
			throw new MessagingException("could not define RabbitMQ exchange: " + e.getMessage(), e);
		}
	}
	

	
	public static void declareBinding(Channel ch, Mailbox mailbox) 
	throws MessagingException 
	{
		try {
			String queueName = getQueueName(mailbox);
			String exchangeName = getExchangeName(mailbox);
			ch.queueBind(queueName, exchangeName, exchangeName);
		}
		catch (IOException e) {
			throw new MessagingException("could not define RabbitMQ exchange: " + e.getMessage(), e);
		}
	}




	/**
	 * Helper function that maps a Mailbox to AMQP/ION messaging 
	 * name conventions.
	 * 
	 * @param mailbox
	 * @return
	 */
	public static String getExchangeName(Mailbox mailbox) {
		return String.format(
				"%s.%s",
				mailbox.getExchangeSpace().getName(),
				mailbox.getExchangeName().getName()
		);
	}

	
	/**
	 * Helper function that maps a Mailbox to AMQP/ION messaging 
	 * name conventions.
	 * 
	 * @param mailbox
	 * @return
	 */
	public static String getQueueName(Mailbox mailbox) {
		return String.format(
				"%s.%s.%s",
				mailbox.getExchangeSpace().getName(),
				mailbox.getExchangeName().getName(),
				mailbox.getQueue().getName()
		);
	}


}
