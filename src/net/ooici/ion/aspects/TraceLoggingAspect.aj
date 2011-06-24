package net.ooici.ion.aspects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public aspect TraceLoggingAspect pertypewithin(*) {

	
	private Logger logger;
	
	after() returning: staticinitialization(*) { 
		logger = LoggerFactory.getLogger(getWithinTypeName());
	}
	
	pointcut any(): execution(public * *.*(..));

	before() : any() {
		if (logger.isTraceEnabled()) {
			String s = String.format("Entering method: %s",
					thisJoinPointStaticPart.getSignature());
			logger.trace(s);
		}
	}

	after() : any() {
		if (logger.isTraceEnabled()) {
			String s = String.format("Leaving method:  %s",
					thisJoinPointStaticPart.getSignature());
			logger.trace(s);
		}
	}
}