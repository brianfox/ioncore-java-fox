package net.ooici.ion.cc.messaging;

import java.util.Observable;

import org.apache.log4j.Logger;

import net.ooici.ion.cc.messaging.interceptor.InterceptorStack;
import net.ooici.ion.cc.messaging.platform.Platform;
import net.ooici.ion.cc.messaging.platform.PlatformFactory;
import net.ooici.ion.cc.messaging.platform.PlatformType;
import net.ooici.ion.lifecycle.LifeCycle;
import net.ooici.ion.lifecycle.LifeCycleException;
import net.ooici.ion.properties.LocalProperties;

/**
 * Provides a colelction of messaging utilities.  The stack can handle both incoming 
 * and outgoing traffic.  It is comprised of the following pieces:
 * <ol>
 * <li>{@link Broker} A generic messaging broker which can perform broker ExchangeSpace, ExchangeName, Queue,
 * Binding, and Mailbox declarations.</li>
 * <li>{@link Dispatcher} A multi-threaded message dispatcher which sends and receives messages.</li>
 * <li>{@link InterceptorStack} A series of filters which allow or disallow messages from being sent.</li>
 * </ol>
 *   
 * @author brianfox
 *
 */
public class MessageStack  extends LifeCycle {

	private static Logger log = Logger.getLogger(MessageStack.class);

	private Platform platform;
	private Broker broker;
	private Dispatcher dispatcher;

	public MessageStack(LocalProperties p) 
	throws 
		MessagingException
	{
		try {
			PlatformType type = PlatformType.fromString(p.getString("platform"));
			log.info(String.format(
					"%s starting: type=%s", 
					this.getClass().getSimpleName(), 
					type
			));
			
			
			platform = PlatformFactory.createPlatform(type);
			broker = platform.createBroker(p.getSection("Broker"));
			dispatcher = platform.createDispatcher(p.getSection("Dispatcher"), broker);
			
			broker.addObserver(this);
			dispatcher.addObserver(this);
			
			log.info(String.format("%s started successfully", this.getClass().getSimpleName()));
		}
		catch (Exception e) {
			this.slc_error(e);
		}
	}


	
	public void registerMailbox(Mailbox mailbox) 
	throws 
		MessagingException
	{
		if (state != LifeCycle.State.ACTIVE) 
			throw new MessagingException(String.format(
					"Cannot register mailbox while %s is in the %s state",
					this.getClass().getSimpleName(),
					state
					));
					
		try {
			broker.createMailbox(mailbox);
			dispatcher.registerMailbox(mailbox);
			mailbox.setDispatcher(dispatcher);
		}
		catch (Exception e) {
			throw new MessagingException("Could not register mailbox", e);
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
		broker.slc_activate();
		dispatcher.slc_activate();
	}

	
	@Override
	public void slc_terminate() 
	throws LifeCycleException 
	{
		super.slc_terminate();
		broker.slc_terminate();
		dispatcher.slc_terminate();
	}


}
