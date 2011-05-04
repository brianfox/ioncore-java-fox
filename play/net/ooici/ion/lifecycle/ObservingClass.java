package net.ooici.ion.lifecycle;

import java.util.Observable;
import java.util.Observer;

public class ObservingClass {

	public static class ResponseHandler implements Observer {

		public void update(Observable obj, Object arg) {
			System.out.println("\nReceived Response: " + obj + "  " + arg);
		}
	}

}
