package net.ooici.ion.cc.messaging;

import java.util.Observable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooici.ion.cc.messaging.interceptor.InterceptorStack;
import net.ooici.ion.cc.messaging.interceptor.InterceptorStack.IncomingDecision;
import net.ooici.ion.cc.messaging.interceptor.InterceptorStack.OutgoingDecision;
import net.ooici.ion.cc.messaging.message.Header;
import net.ooici.ion.cc.messaging.message.Message;
import net.ooici.ion.lifecycle.LifeCycle;
import net.ooici.ion.lifecycle.LifeCycleException;
import net.ooici.ion.properties.LocalProperties;
import net.ooici.ion.properties.PropertiesException;

import net.ooici.util.thread.NamedThreadFactory;

public abstract class Dispatcher extends LifeCycle {

	protected Broker broker;
	// protected MailboxScheduler scheduler;
	private static InterceptorStack interceptorStack;

	public static final String[] REQUIRED_PROPERTIES = 
		new String[]
		{
			"level",
			"core_thread_count",
			"max_thread_count",
			"keep_alive_time_ms"
		};

	
	private SchedulerThread schedulerThread;
	private LocalProperties properties;
	
	Logger logger = LoggerFactory.getLogger(Dispatcher.class);
	
	
	public Dispatcher(
			LocalProperties properties,
			Broker broker
	) 
	throws 
		MessagingException, 
		PropertiesException 
	{

		// Properties setup
		checkProperties(properties);
		this.properties = properties;
		int core_thread_count =  this.properties.getInt("core_thread_count");
		int max_thread_count =   this.properties.getInt("max_thread_count");
		int keep_alive_time_ms = this.properties.getInt("keep_alive_time_ms");


		logger.info(String.format(
				"%s starting: core_thread_count=%d   " + 
				"max_thread_count=%d   keep_alive_time_ms=%d",
				this.getClass().getSimpleName(),
				core_thread_count,
				max_thread_count,
				keep_alive_time_ms
		));
		// A separate thread may be overkill here.  But it does add some
		// flexibility for logging and stopping the thread.
		schedulerThread = new SchedulerThread(
				core_thread_count,
				max_thread_count,
				keep_alive_time_ms
		);
		this.interceptorStack = new InterceptorStack(null);
		
	}


	abstract public void registerMailbox(Mailbox m) 
	throws MessagingException;

	
	abstract public void unregisterMailbox(Mailbox m) throws LifeCycleException;
	

	public void dispatch(MessageQueue queue) throws LifeCycleException {
		checkState(logger, "dispatch", LifeCycle.State.ACTIVE);
		schedule(queue);
	}

	
	public void send(Message message) 
	throws 
		MessagingException, 
		LifeCycleException 
	{
		checkState(logger, "send", LifeCycle.State.ACTIVE);

		if (message == null)
			throw new MessagingException("Could not send null rabbit packet.");

		Header header = message.getHeader();
		logger.trace(String.format(
				"Sending a message, exchange=%s routingKey=%s", 
				header.get("destination"), 
				header.get("routingkey")
		));
		
		OutgoingDecision out = interceptorStack.interceptOutgoing(message);
		logger.trace(out.toString());
		if (out.getResult() == InterceptorStack.Result.ACCEPTED) {
			publish("estest.entest", "estest.entest", out.getRaw());
		}
	}




	abstract protected void publish(
			String address,
			String routingKey, 
			byte[] raw
	) 
	throws MessagingException, LifeCycleException;
	
	
	
	
	public void schedule(MessageQueue queue) 
	throws LifeCycleException 
	{
		checkState(logger, "send", LifeCycle.State.ACTIVE);
		schedulerThread.schedule(queue);
	}
	
	
	
	
	
	private void checkProperties(LocalProperties properties) 
	throws
		PropertiesException
	{
		for (String s : REQUIRED_PROPERTIES) {
			if (!properties.containsKey(s)) {
				String err = String.format("required field missing: %s", this.getClass().getSimpleName());
				logger.error(err);
				throw new PropertiesException(err);
			}
		}
	}
	
	
	
	private static class SchedulerThread extends Thread {
	
		
		private LinkedBlockingQueue<MessageQueue> scheduledQueues;
		private LinkedBlockingQueue<Runnable> runnables;
		private ThreadPoolExecutor executor;
		
		private volatile boolean isCanceled = false;

		
		public SchedulerThread(
				int coreThreadCount, 
				int maxThreadCount, 
				int keepAliveTimeMS
		) {
			scheduledQueues = new LinkedBlockingQueue<MessageQueue>();
			runnables = new LinkedBlockingQueue<Runnable>();
			NamedThreadFactory factory = new NamedThreadFactory("Dispatcher");

			executor = new ThreadPoolExecutor(
					coreThreadCount,
					maxThreadCount,
					keepAliveTimeMS,
					TimeUnit.MILLISECONDS,
					runnables,
					factory
			);
		}
	
		
		public void cancel() {
			isCanceled = true;
			this.interrupt();
		}
		
		
		public void schedule(MessageQueue queue) {
			scheduledQueues.add(queue);
		}
		
		
		@Override
		public void run() {
			MessageQueue q;
			while (!isCanceled) {
				try {
					q = scheduledQueues.poll(1000, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					q = null;
					break;
				}
				if (q != null) {
					executor.execute(new Worker(q));
				}
				else {
		//			if (first_pause_count-- < 0)
		//				first_pause = 1000;
				}
			}
			executor.shutdown();
		}
	}
	
	

	private static class Worker implements Runnable {

		MessageQueue q;
		
		public Worker(MessageQueue q) {
			this.q = q;
		}
		
		@Override
		public void run() {
			// mailbox.onDelivery();
			while (true) {
				byte[] raw = q.next();
				if (raw == null)
					break;
				IncomingDecision d = interceptorStack.interceptIncoming(raw);
				q.getMailbox().onMail(d.getMessage());
			}
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
		schedulerThread.start();
	}

	
	@Override
	public void slc_terminate() 
	throws LifeCycleException 
	{
		super.slc_terminate();
		schedulerThread.cancel();
	}

}
