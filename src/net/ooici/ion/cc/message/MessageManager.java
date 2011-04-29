package net.ooici.ion.cc.message;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import net.ooici.ion.cc.ContainerException;
import net.ooici.ion.cc.message.stack.MessageStack;
import net.ooici.ion.cc.message.stack.mailbox.Mailbox;
import net.ooici.ion.config.LocalProperties;
import net.ooici.ion.config.PropertiesException;

public class MessageManager {

	private static Logger log = Logger.getLogger(MessageManager.class);

	public static final String CONFIG_SECTION = "MessageManager";
	ConcurrentHashMap<QosPriority,MessageStack> systems;
	
	public MessageManager(LocalProperties properties) 
	throws 
		ContainerException, 
		PropertiesException
	{
		systems = new ConcurrentHashMap<QosPriority,MessageStack>();
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
				if (systems.containsKey(q)) {
					String err = String.format("QosPriority division is already defined: %s", d);
					log.error(err);
					throw new PropertiesException(err);
				}
				systems.put(QosPriority.fromString(d), new MessageStack(p));
			}
		}
	}

	
	public void registerMailbox(QosPriority q, Mailbox mailbox) 
	throws 
		ContainerException
	{
		if (!systems.containsKey(q)) {
			String err = String.format("unknown QosPriority: %s", q);
			log.error(err); 
			throw new MessageManagerException(err);
		}
		systems.get(q).registerMailbox(mailbox);
	}
	
	
	
	
	public void close() 
	throws 
		ContainerException 
	{
		for (MessageStack s : systems.values())
			s.close();
	}


	public MessageStack getMessageSystem(QosPriority priority) 
	throws 
		MessageManagerException 
	{
		
		if (!systems.containsKey(priority)) {
			String err = String.format(
					"Could not create mailbox, unknown QosPriority: %s", 
					priority
			);
			log.error(err); 
			throw new MessageManagerException(err);
		}
		return systems.get(priority);
	}


}
