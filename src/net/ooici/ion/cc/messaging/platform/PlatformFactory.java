package net.ooici.ion.cc.messaging.platform;

import net.ooici.ion.cc.messaging.platform.rabbitmq.RabbitPlatform;


/**
 * 
 * @author brianfox
 *
 */
public class PlatformFactory {
	
	public static Platform createPlatform(PlatformType type) 
	throws PlatformException {
	
		if (type == null)
			throw new PlatformException("Platform type cannot be null");

		switch (type.getBase()) {
			case RABBITMQ:
				return new RabbitPlatform();
			default:
				throw new PlatformException("Unhandled platform type: " + type);
		}
	}
}
