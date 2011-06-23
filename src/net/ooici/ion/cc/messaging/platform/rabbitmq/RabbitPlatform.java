package net.ooici.ion.cc.messaging.platform.rabbitmq;

import org.apache.log4j.Logger;

import net.ooici.ion.cc.messaging.Broker;
import net.ooici.ion.cc.messaging.Dispatcher;
import net.ooici.ion.cc.messaging.MessagingException;
import net.ooici.ion.cc.messaging.platform.Platform;
import net.ooici.ion.cc.messaging.platform.PlatformException;
import net.ooici.ion.properties.LocalProperties;
import net.ooici.ion.properties.PropertiesException;



/**
 * 
 * @author brianfox
 *
 */
public class RabbitPlatform extends Platform {
	
	private static Logger log = Logger.getLogger(RabbitPlatform.class);

	@Override
	public Broker createBroker(LocalProperties properties)
	throws 
		PlatformException, 
		MessagingException, 
		PropertiesException 
	{
		return RabbitBroker.createBroker(properties);
	}

	@Override
	public Dispatcher createDispatcher(
			LocalProperties properties,
			Broker broker
	) 
		throws 
			PlatformException {
		try {
			return new RabbitDispatcher(properties, broker);
		} catch (Exception e) {
			String err = "Unable to create RabbitDispatcher: " + e.getClass().getName();
			log.error(err);
			throw new PlatformException(err, e);
		} 

	}
}