package net.ooici.ion.cc;

@SuppressWarnings("serial")
public class ContainerException extends Exception {
	
	public ContainerException(String err) {
		super(err);
	}
	
	public ContainerException(String err, Exception e) {
		super(err, e);
	}
}
