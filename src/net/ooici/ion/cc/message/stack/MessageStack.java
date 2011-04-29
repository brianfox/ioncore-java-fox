package net.ooici.ion.cc.message.stack;

import org.apache.log4j.Logger;

import net.ooici.ion.cc.ContainerException;
import net.ooici.ion.cc.message.stack.broker.Broker;
import net.ooici.ion.cc.message.stack.dispatcher.Dispatcher;
import net.ooici.ion.cc.message.stack.mailbox.Mailbox;
import net.ooici.ion.config.LocalProperties;
import net.ooici.ion.platform.message.Platform;
import net.ooici.ion.platform.message.PlatformFactory;
import net.ooici.ion.platform.message.PlatformType;

public class MessageStack {

	private static Logger log = Logger.getLogger(MessageStack.class);

	private Platform platform;
	private Broker broker;
	private Dispatcher dispatcher;

	public MessageStack(LocalProperties p) 
	throws 
		MessageStackException
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
	
			log.info(String.format("%s started successfully", this.getClass().getSimpleName()));
		}
		
		/* 
		 * Any error is an indication that startup failed. 
		 */
		catch (Exception e) {
			this.close();
			String err = String.format(
					"%s failed to start.  Closing.", 
					this.getClass().getSimpleName()
			);
			log.error(err);
			throw new MessageStackException(err, e);
		}
	}

	
	public void close() 
	{
		/* 
		 * Closing subsystems will always be fragile.  Assume failure is a 
		 * possibility.  Log any errors, but take no other action.         
		 */
		try { broker.close();      } catch (Exception e) { log.error("Error closing broker: " + e.getMessage()); }
		try { dispatcher.close();  } catch (Exception e) { log.error("Error closing dispatcher: " + e.getMessage()); }
		
		log.info(String.format("Closed %s", this.getClass().getSimpleName()));
	}

	
	public void registerMailbox(Mailbox mailbox) 
	throws 
		ContainerException
	{
		broker.createMailbox(mailbox);
		dispatcher.registerMailbox(mailbox);
		mailbox.setDispatcher(dispatcher);
	}


}
