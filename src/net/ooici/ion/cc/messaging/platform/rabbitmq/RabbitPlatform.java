package net.ooici.ion.cc.messaging.platform.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	Logger logger = LoggerFactory.getLogger(RabbitPlatform.class);

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
			logger.error(err);
			throw new PlatformException(err, e);
		} 

	}
}