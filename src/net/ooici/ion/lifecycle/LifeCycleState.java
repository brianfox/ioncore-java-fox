package net.ooici.ion.lifecycle;

public enum LifeCycleState {
	
	//  Initial state. From the instance the container 
	// starts the process from source code
	NEW,
	
	// Process has correctly initialized. All downstream
	INIT,
	
	// Process has been activated
	ACTIVE,
	
	// Process has been deactivated
	INACTIVE,
	
	// Terminal state. Process has shut down and will 
	// not respond to further requests.
	TERMINATED,
	
	// Terminal state. An error occurred during process 
	// life cycle management.
	ERROR
}
