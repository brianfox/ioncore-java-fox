package net.ooici.ion.cc.message;

public enum QosPriority {
	LOW, 
	MEDIUM, 
	HIGH,
	NONE;

	public static QosPriority fromString(String d) {
		for (QosPriority p : QosPriority.values())
			if (p.toString().toLowerCase().compareTo(d.toLowerCase()) == 0)
				return p;
		return NONE;
	}
}
