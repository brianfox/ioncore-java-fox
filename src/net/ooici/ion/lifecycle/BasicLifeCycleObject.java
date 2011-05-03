package net.ooici.ion.lifecycle;

import java.util.Observable;
import java.util.Observer;

public class BasicLifeCycleObject extends Observable implements Observer {

	public enum LifeCycleState {
		
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
		
		LifeCycleState(int ordinal) {
			this.ordinal = ordinal;
		}
	}
	
	protected LifeCycleState state = LifeCycleState.NEW;
	
	
	public void slc_init() 
	throws LifeCycleException 
	{
		changeState(state, LifeCycleState.INIT);
	}
	
	
	public void slc_activate() 
	throws LifeCycleException 
	{
		changeState(state, LifeCycleState.ACTIVE);
	}
	
	
	public void slc_deactivate() 
	throws LifeCycleException 
	{
		changeState(state, LifeCycleState.INACTIVE);
	}
	
	
	public void slc_terminate() 
	throws LifeCycleException 
	{
		changeState(state, LifeCycleState.TERMINATED);
	}
	
	
	public void slc_error() 
	throws LifeCycleException 
	{
		changeState(state, LifeCycleState.ERROR);
	}
	
	
	private void changeState(LifeCycleState original, LifeCycleState next) 
	throws LifeCycleException 
	{
		System.out.println("Observers count: " + this.countObservers());
		if (next != LifeCycleState.ERROR && next.ordinal <= original.ordinal)
			throw new LifeCycleException(String.format(
					"Cannot progress from LifeCycleState %s to %s", 
					original, 
					next
			));
		state = next;
		setChanged();
		notifyObservers(state);
	}


	/**
	 * This should be overrided if this class is meant to observe other 
	 * classes and take action on changes. 
	 */
	@Override
	public void update(Observable o, Object arg) {
	}
}
