package net.ooici.ion.lifecycle;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LifeCycle extends Observable implements Observer {

	public enum State {
		
		//  Initial state. From the instance the container 
		// starts the process from source code
		NEW(0),
		
		// Process has correctly initialized. All downstream
		INIT(1),
		
		// Process has been activated
		ACTIVE(2),
		
		// Process has been deactivated
		INACTIVE(3),
		
		// Terminal state. Process has shut down and will 
		// not respond to further requests.
		TERMINATED(4),
		
		// Terminal state. An error occurred during process 
		// life cycle management.
		ERROR(Integer.MAX_VALUE);
		
		int ordinal;
		
		State(int ordinal) {
			this.ordinal = ordinal;
		}
	}
	
	protected State state = State.NEW;
	protected Exception exception = null;
	
	private static HashMap<String,Logger> cachedLoggers = new HashMap<String,Logger>();
	
	
	
	protected void slc_init() 
	throws LifeCycleException 
	{
		changeState(state, State.INIT);
	}
	
	
	protected void slc_activate() 
	throws LifeCycleException 
	{
		changeState(state, State.ACTIVE);
	}
	
	
	protected void slc_deactivate() 
	throws LifeCycleException 
	{
		changeState(state, State.INACTIVE);
	}
	
	
	protected void slc_terminate() 
	throws LifeCycleException 
	{
		changeState(state, State.TERMINATED);
	}
	
	
	protected void slc_error(Exception e) 
	{
		exception = e;
		try {
			changeState(state, State.ERROR);
		} catch (LifeCycleException e1) {
			state = State.ERROR;
		}
	}


	private Logger getLogger(String className) {
		if (cachedLoggers.containsKey(className))
			return cachedLoggers.get(className);
		Logger logger = LoggerFactory.getLogger(className);
		cachedLoggers.put(className, logger);
		return logger;
	}
	
	private void changeState(State original, State next) 
	throws LifeCycleException 
	{
		Logger log = getLogger(this.getClass().getName());
		log.info(String.format(
				"State Change for %-45s:  %s -> %s",
				this.getClass().getSimpleName() + 
				String.format(" (@%08x)",
					this.hashCode()
				),
				original,
				next
			));

		if (next != State.ERROR && next.ordinal <= original.ordinal)
			throw new LifeCycleException(String.format(
					"Cannot progress from LifeCycleState %s to %s", 
					original, 
					next
			));
		state = next; 
		setChanged();
		notifyObservers(state);
	}
	

	protected void checkState(Logger log, String method, State... allowed)
	throws LifeCycleException
	{
		for (State s : allowed)
			if (this.state == s)
				return;
		String err = 
			String.format(
				"Cannot use %s, object state is %s",
				// this.getClass().getSimpleName(),
				method,
				state
			);
		log.error(err);
		throw new LifeCycleException(err);
	}

	/**
	 * Override this method if this class is meant to observe other objects. 
	 */
	@Override
	public void update(Observable o, Object arg) {
	}
}
