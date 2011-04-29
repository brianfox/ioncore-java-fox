package net.ooici.ion.platform.message;

import net.ooici.ion.platform.message.vendors.rabbitmq.RabbitPlatform;

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
