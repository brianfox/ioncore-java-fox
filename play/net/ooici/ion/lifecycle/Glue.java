package net.ooici.ion.lifecycle;

public class Glue {
	 
    public static void main(String args[]) throws LifeCycleException {
 
        // create an event source - reads from stdin
        final ObservableClass evSrc = new ObservableClass();
 
        // create an observer
        final ObservingClass.ResponseHandler respHandler = new ObservingClass.ResponseHandler();
 
        // subscribe the observer to the event source
        evSrc.addObserver( respHandler );
        evSrc.slc_init();
        evSrc.slc_activate();
        evSrc.slc_deactivate();
        evSrc.slc_terminate();
    }
}