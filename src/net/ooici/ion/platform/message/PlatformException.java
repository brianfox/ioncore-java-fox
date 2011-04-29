package net.ooici.ion.platform.message;

/**
 * MessagingPlatformException provides custom exception handling for vendor
 * or platform related messaging errors.
 * 
 * @author brianfox
 */
@SuppressWarnings("serial")
public class PlatformException extends Exception {

	public PlatformException(String string, Exception e) {
		super(string, e);
	}

	public PlatformException(String string) {
		super(string);
	}

}
