package net.ooici.ion.cc.message.stack.interceptor;

import net.ooici.ion.cc.ContainerException;

@SuppressWarnings("serial")
public class InterceptorException extends ContainerException {

	public InterceptorException(String err) {
		super(err);
	}

}
