package net.ooici.ion.cc.message.stack.dispatcher;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import net.ooici.ion.cc.message.payload.Header;
import net.ooici.ion.cc.message.payload.Message;
import net.ooici.ion.cc.message.stack.broker.Broker;
import net.ooici.ion.cc.message.stack.broker.BrokerException;
import net.ooici.ion.cc.message.stack.interceptor.InterceptorStack;
import net.ooici.ion.cc.message.stack.interceptor.InterceptorStack.IncomingDecision;
import net.ooici.ion.cc.message.stack.interceptor.InterceptorStack.OutgoingDecision;
import net.ooici.ion.cc.message.stack.mailbox.Mailbox;
import net.ooici.ion.cc.message.stack.serialization.SerializationException;
import net.ooici.ion.lifecycle.BasicLifeCycleObject;
import net.ooici.ion.properties.LocalProperties;
import net.ooici.ion.properties.PropertiesException;


public abstract class Dispatcher extends BasicLifeCycleObject {

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
	
	private static Logger log = Logger.getLogger(Dispatcher.class);
	
	
	public Dispatcher(
			LocalProperties properties,
			Broker broker
	) 
	throws 
		BrokerException, 
		PropertiesException 
	{

		// Properties setup
		checkProperties(properties);
		this.properties = properties;
		int core_thread_count =  this.properties.getInt("core_thread_count");
		int max_thread_count =   this.properties.getInt("max_thread_count");
		int keep_alive_time_ms = this.properties.getInt("keep_alive_time_ms");


		log.info(String.format(
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
		schedulerThread.start();

		log.info(String.format(
				"%s started successfully",
				this.getClass().getSimpleName()
		));
		
		this.interceptorStack = new InterceptorStack(null);
		
	}


	abstract public void registerMailbox(Mailbox m) 
	throws 
		DispatcherException, 
		BrokerException;

	
	abstract public void unregisterMailbox(Mailbox m);
	

	public void dispatch(MessageQueue queue) {
		schedule(queue);
	}

	
	public void send(Message message) 
	throws 
		DispatcherException, 
		BrokerException, 
		SerializationException 
	{
		if (message == null)
			throw new DispatcherException("Could not send null rabbit packet.");

		Header header = message.getHeader();
		log.trace(String.format(
				"Sending a message, exchange=%s routingKey=%s", 
				header.get("destination"), 
				header.get("routingkey")
		));
		
		OutgoingDecision out = interceptorStack.interceptOutgoing(message);
		log.trace(out.toString());
		if (out.getResult() == InterceptorStack.Result.ACCEPTED) {
			publish("estest.entest", "estest.entest", out.getRaw());
		}
	}




	abstract protected void publish(
			String address,
			String routingKey, 
			byte[] raw
	) 
	throws DispatcherException;
	
	
	
	
	public void schedule(MessageQueue queue) {
		schedulerThread.schedule(queue);
	}
	
	
	public void close() {
		schedulerThread.cancel();
		log.info(String.format("Closed %s.", this.getClass().getSimpleName()));
	}
	
	
	
	private void checkProperties(LocalProperties properties) 
	throws
		PropertiesException
	{
		for (String s : REQUIRED_PROPERTIES) {
			if (!properties.containsKey(s)) {
				String err = String.format("required field missing: %s", this.getClass().getSimpleName());
				log.error(err);
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
			executor = new ThreadPoolExecutor(
					coreThreadCount,
					maxThreadCount,
					keepAliveTimeMS,
					TimeUnit.MILLISECONDS,
					runnables
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
	
}
