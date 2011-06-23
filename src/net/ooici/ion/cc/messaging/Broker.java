package net.ooici.ion.cc.messaging;

import java.util.Observable;

import net.ooici.ion.lifecycle.LifeCycle;
import net.ooici.ion.lifecycle.LifeCycleException;


/**
 * The Broker interface defines the message broker requirements needed to 
 * couple an arbitrary messaging broker technology to the Integrated 
 * Observatory Network.  
 *    
 * @author brianfox
 *
 */
public abstract class Broker extends LifeCycle {

	/**
	 * Prompts this Broker to create an ExchangeSpace representation in the 
	 * underlying messaging infrastructure.  
	 * 
	 * @param mailbox specifies the parameters for the new ExchangeSpace.
	 * @throws BrokerException thrown if the ExchangeSpace cannot be created.
	 * @throws LifeCycleException 
	 */
	abstract public void createExchangeSpace(Mailbox mailbox)
	throws MessagingException, LifeCycleException;
	

	
	/**
	 * Prompts this Broker to create an ExchangeName representation in the 
	 * underlying messaging infrastructure.  
	 * 
	 * @param mailbox specifies the parameters for the new ExchangeName.
	 * @throws BrokerException thrown if the binding cannot be created.
	 * @throws LifeCycleException 
	 */
	abstract public void createExchangeName(Mailbox mailbox) 
	throws MessagingException, LifeCycleException;
	
	
	
	/**
	 * Prompts this Broker to create a Queue representation in the 
	 * underlying messaging infrastructure.  
	 * 
	 * @param mailbox specifies the parameters for the new Queue.
	 * @throws BrokerException thrown if the binding cannot be created.
	 * @throws LifeCycleException 
	 */
	abstract public void createQueue(Mailbox mailbox)
	throws MessagingException, LifeCycleException;

	
	
	/**
	 * Prompts this Broker to create a Binding representation in the 
	 * underlying messaging infrastructure.  
	 * 
	 * @param mailbox specifies the parameters for the new Binding.
	 * @throws BrokerException thrown if the binding cannot be created.
	 * @throws LifeCycleException 
	 */
	abstract public void createBinding(Mailbox mailbox)
	throws MessagingException, LifeCycleException;

	
	
	abstract public void createMailbox(Mailbox mailbox) 
	throws MessagingException, LifeCycleException;
	
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
	}

	
	@Override
	public void slc_terminate() 
	throws LifeCycleException 
	{
		super.slc_terminate();
	}
}
