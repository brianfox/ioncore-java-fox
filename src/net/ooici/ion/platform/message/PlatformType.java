package net.ooici.ion.platform.message;

/**
 * 
 * @author brianfox
 *
 */
public enum PlatformType {
	RABBITMQ(null),
	RABBITMQ_2_3_1(RABBITMQ);
	
	PlatformType base;
	
	PlatformType(PlatformType base) {
		this.base = base == null ? this : base;
	}
	
	public PlatformType getBase() {
		return base;
	}
	
	public static PlatformType fromString(String s) {
		for (PlatformType t : PlatformType.values()) {
			if (t.toString().compareTo(s) == 0)
				return t;
		}
		return null;
	}
}
