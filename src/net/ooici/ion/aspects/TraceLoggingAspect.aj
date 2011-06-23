package net.ooici.ion.aspects;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public aspect TraceLoggingAspect pertypewithin(*) {

	private Logger logger;
	
	after() returning: staticinitialization(*) { 
		logger = Logger.getLogger(getWithinTypeName());
		logger.setLevel(Level.DEBUG);
		logger.addAppender(new ConsoleAppender(new PatternLayout("%d{ISO8601} %-5p [%t]: %m%n")));
	}
	
	pointcut any()
	: execution(public * *.*(..));

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