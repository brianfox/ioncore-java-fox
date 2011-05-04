package net.ooici.ion.platform.message;

import net.ooici.ion.cc.message.stack.broker.Broker;
import net.ooici.ion.cc.message.stack.broker.BrokerException;
import net.ooici.ion.cc.message.stack.dispatcher.Dispatcher;
import net.ooici.ion.properties.LocalProperties;
import net.ooici.ion.properties.PropertiesException;

/**
 * MessagingPlatform is an factory interface outlining the responsibilities for 
 * the MessagingPlatformFactory class.
 *   
 * @author brianfox
 */
abstract public class Platform {
	
	/**
	 * Creates a new Broker.
	 * 
	 * @param settings a BrokerConfig.
	 * @return a Broker.
	 * @throws PlatformException
	 *         occurs if an error is encountered while trying to create the 
	 *         new BrokerController.
	 * @throws PropertiesException 
	 * @throws BrokerException 
	 */
	abstract public Broker createBroker(LocalProperties properties) 
	throws 
		PlatformException, 
		BrokerException, 
		PropertiesException;

	
	/**
	 * Creates a new Dispatcher.
	 * @param scheduler 
	 * 
	 * @param controller a previously constructed BrokerController.
	 * @param messageSystem 
	 * @param settings a specific DispatcherSettings object containing 
	 *                 all the parameters for this Dispatcher.
	 * @return a Dispatcher.
	 * @throws PlatformException
	 *         occurs if an error is encountered while trying to create the 
	 *         new Dispatcher.
	 * @throws BrokerException 
	 * @throws PropertiesException 
	 */
     abstract public Dispatcher createDispatcher(
			LocalProperties config, 
			Broker controller 
//			MessageSystem messageSystem
	) 
	throws PlatformException;
}
