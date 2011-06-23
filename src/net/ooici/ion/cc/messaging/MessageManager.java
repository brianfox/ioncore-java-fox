package net.ooici.ion.cc.messaging;

import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import net.ooici.ion.cc.ContainerException;
import net.ooici.ion.lifecycle.LifeCycle;
import net.ooici.ion.lifecycle.LifeCycleException;
import net.ooici.ion.properties.LocalProperties;
import net.ooici.ion.properties.PropertiesException;


/**
 * Provides a single management point for a {@link CapabilityContainer}.  Management 
 * includes initializing the mesasging system, closing the messaging system, registering
 * mailboxes, unregistering mailboxes, and logging message related activity.
 * 
 * @author brianfox
 *
 */
public class MessageManager extends LifeCycle {

	private static Logger log = Logger.getLogger(MessageManager.class);

	public static final String CONFIG_SECTION = "MessageManager";
	
	
	/*
	 * There is a one to many relationship between a MessageManager and
	 * MessageStacks. A single MessageStack establishes basic messaging.
	 * Multiple MessageStacks lend some interesting configuration options 
	 * for QoS.
	 */
	ConcurrentHashMap<QosPriority,MessageStack> stacks;
	
	
	/**
	 * 
	 * @param properties
	 * @throws ContainerException
	 * @throws PropertiesException
	 */
	public MessageManager(LocalProperties properties) 
	throws 
		ContainerException, 
		PropertiesException
	{
		stacks = new ConcurrentHashMap<QosPriority,MessageStack>();
		
		if (properties.containsKey("divisions")) {
			String[] divisions = properties.getString("divisions").split(",");
			for (String d : divisions) {
				LocalProperties p = properties.getSection("MessageSystem.division." + d.trim());
				QosPriority q = QosPriority.fromString(d);
				if (q == QosPriority.NONE) {
					String err = String.format("unknown QosPriority: %s", d);
					log.error(err);
					throw new PropertiesException(err);
				}
				if (stacks.containsKey(q)) {
					String err = String.format("QosPriority division is already defined: %s", d);
					log.error(err);
					throw new PropertiesException(err);
				}
				stacks.put(QosPriority.fromString(d), new MessageStack(p));
			}
		}
	}

	
	/**
	 * 
	 * @param q
	 * @param mailbox
	 * @throws ContainerException
	 */
	public void registerMailbox(QosPriority q, Mailbox mailbox) 
	throws 
		ContainerException
	{
		if (!stacks.containsKey(q)) {
			String err = String.format("unknown QosPriority: %s", q);
			log.error(err); 
			throw new MessagingException(err);
		}
		stacks.get(q).registerMailbox(mailbox);
	}
	
	
	
	
	
	/*
	 * LIFE CYCLE METHODS 
	 */

	@Override
	public void update(Observable o, Object arg) {
		// System.err.print(arg);
	}
	
	
	@Override
	public void slc_activate() 
	throws LifeCycleException 
	{
		super.slc_activate();
		for (MessageStack s : this.stacks.values())
			s.slc_activate();
	}

	
	@Override
	public void slc_terminate() 
	throws LifeCycleException 
	{
		super.slc_terminate();
		for (MessageStack s : stacks.values())
			s.slc_terminate();
	}


}
