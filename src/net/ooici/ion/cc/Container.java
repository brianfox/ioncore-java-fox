package net.ooici.ion.cc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;

import org.apache.log4j.Logger;

import net.ooici.ion.cc.ContainerException;
import net.ooici.ion.cc.message.MessageManager;
import net.ooici.ion.cc.message.QosPriority;
import net.ooici.ion.cc.message.stack.mailbox.Mailbox;
import net.ooici.ion.lifecycle.LifeCycle;
import net.ooici.ion.lifecycle.LifeCycleException;
import net.ooici.ion.platform.message.vendors.rabbitmq.RabbitBroker;
import net.ooici.ion.properties.LocalProperties;

public class Container extends LifeCycle {

	private static Logger log = Logger.getLogger(RabbitBroker.class);
	
	protected MessageManager msgManager;
	
	
	public Container() {
		try {
			LocalProperties properties = new LocalProperties();
			properties.load("./properties/container.properties");
			
			msgManager = new MessageManager(properties.getSection(MessageManager.CONFIG_SECTION));
			msgManager.addObserver(this);
		
		} catch (Exception e) {
			slc_error(e);
		} 
	}
	
	public void registerMailbox(QosPriority priority, Mailbox mailbox) 
	throws 
		ContainerException
	{
		msgManager.registerMailbox(priority, mailbox);
	}

	
	
	/*
	 * LIFE CYCLE METHODS 
	 */

	@Override
	public void update(Observable o, Object arg) {
		//System.err.print(o + "   " + arg);
	}

	
	@Override
	public void slc_activate() 
	throws LifeCycleException 
	{
		super.slc_activate();
		try {
			msgManager.slc_activate();
		} catch (Exception e) {
			super.slc_error(e);
		}
	}

	@Override
	public void slc_terminate() 
	throws LifeCycleException 
	{
		super.slc_terminate();
		msgManager.slc_terminate();
	}


}

