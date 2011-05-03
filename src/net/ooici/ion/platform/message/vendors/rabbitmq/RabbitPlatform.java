package net.ooici.ion.platform.message.vendors.rabbitmq;

import org.apache.log4j.Logger;

import net.ooici.ion.cc.message.stack.broker.Broker;
import net.ooici.ion.cc.message.stack.broker.BrokerException;
import net.ooici.ion.cc.message.stack.dispatcher.Dispatcher;
import net.ooici.ion.platform.message.Platform;
import net.ooici.ion.platform.message.PlatformException;
import net.ooici.ion.properties.LocalProperties;
import net.ooici.ion.properties.PropertiesException;



/**
 * 
 * @author brianfox
 *
 */
public class RabbitPlatform implements Platform {
	
	private static Logger log = Logger.getLogger(RabbitPlatform.class);

	@Override
	public Broker createBroker(LocalProperties properties)
	throws 
		PlatformException, 
		BrokerException, 
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