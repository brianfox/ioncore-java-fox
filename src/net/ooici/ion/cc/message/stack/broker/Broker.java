package net.ooici.ion.cc.message.stack.broker;

import net.ooici.ion.cc.message.stack.mailbox.Mailbox;


/**
 * The Broker interface defines the message broker requirements needed to 
 * couple an arbitrary messaging broker technology to the Integrated 
 * Observatory Network.  
 *    
 * @author brianfox
 *
 */
public interface Broker {

	/**
	 * Prompts this Broker to create an ExchangeSpace representation in the 
	 * underlying messaging infrastructure.  
	 * 
	 * @param mailbox specifies the parameters for the new ExchangeSpace.
	 * @throws BrokerException thrown if the ExchangeSpace cannot be created.
	 */
	void createExchangeSpace(Mailbox mailbox)
	throws BrokerException;
	

	
	/**
	 * Prompts this Broker to create an ExchangeName representation in the 
	 * underlying messaging infrastructure.  
	 * 
	 * @param mailbox specifies the parameters for the new ExchangeName.
	 * @throws BrokerException thrown if the binding cannot be created.
	 */
	void createExchangeName(Mailbox mailbox) 
	throws BrokerException;
	
	
	
	/**
	 * Prompts this Broker to create a Queue representation in the 
	 * underlying messaging infrastructure.  
	 * 
	 * @param mailbox specifies the parameters for the new Queue.
	 * @throws BrokerException thrown if the binding cannot be created.
	 */
	void createQueue(Mailbox mailbox)
	throws BrokerException;

	
	
	/**
	 * Prompts this Broker to create a Binding representation in the 
	 * underlying messaging infrastructure.  
	 * 
	 * @param mailbox specifies the parameters for the new Binding.
	 * @throws BrokerException thrown if the binding cannot be created.
	 */
	void createBinding(Mailbox mailbox)
	throws BrokerException;

	
	
	/**
	 * Closes the broker connection.
	 * 
	 * @throws BrokerException thrown if this broker cannot be closed.
	 */
	void close() 
	throws BrokerException;



	void createMailbox(Mailbox mailbox) throws BrokerException;
	
	
}
