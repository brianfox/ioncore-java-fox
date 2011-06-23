package net.ooici.ion.cc.messaging;


/**
 * A Quality of Service enumeration used to control messaging throughput on a
 * Capability Container.<br>
 * <br>
 * Background: ION messaging is handled by one or more
 * {@link net.ooici.ion.cc.messaging.MessageStack} objects, each of which is
 * a collection of messaging utilities that manage the broker, intercept
 * messages, and dispatch threads. When more than one MessageStack is available,
 * quality of service can be implemented allowing different purposed messages to
 * be sent through different MessageStacks with different tuning parameters.<br>
 * <br>
 * The simple strategy below breaks messages into three categories: LOW, MEDIUM,
 * HIGH, and NONE (aliased to LOW). HIGH ranked messages include heart beat,
 * logging, and diagnostic messages. MEDIUM ranked messages include service
 * messaging and other system messages. LOW ranked messages include bulk data
 * transfers.<br>
 * <br>
 * 
 * @author brianfox
 * 
 */

public enum QosPriority {
	LOW, 	
	MEDIUM, 
	HIGH,
	NONE;

	/**
	 * Parses a QosPriority from a given String.  This is useful for parsing
	 * plain text configuration files.
	 * 
	 * @param d
	 * @return
	 */
	public static QosPriority fromString(String d) {
		for (QosPriority p : QosPriority.values())
			if (p.toString().toLowerCase().compareTo(d.toLowerCase()) == 0)
				return p;
		return NONE;
	}
}
