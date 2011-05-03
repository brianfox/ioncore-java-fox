package net.ooici.ion.cc.message;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import net.ooici.ion.cc.ContainerException;
import net.ooici.ion.cc.message.stack.MessageStack;
import net.ooici.ion.cc.message.stack.mailbox.Mailbox;
import net.ooici.ion.properties.LocalProperties;
import net.ooici.ion.properties.PropertiesException;
import net.ooici.util.msgpack.Container;

/**
 * Provides a single management point for a {@link CapabilityContainer}.  Management 
 * includes initializing the mesasging system, closing the messaging system, registering
 * mailboxes, unregistering mailboxes, and logging message related activity.
 * 
 * @author brianfox
 *
 */
public class MessageManager {

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
			throw new MessageManagerException(err);
		}
		stacks.get(q).registerMailbox(mailbox);
	}
	
	
	
	/**
	 * 
	 * @throws ContainerException
	 */
	public void close() 
	throws 
		ContainerException 
	{
		for (MessageStack s : stacks.values())
			s.close();
	}

}
