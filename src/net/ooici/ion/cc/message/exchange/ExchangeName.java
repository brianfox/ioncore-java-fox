package net.ooici.ion.cc.message.exchange;

/**
 * 
 * @author brianfox
 *
 */
public class ExchangeName {
	
	private ExchangeType et;
	private String name;

	public ExchangeName(ExchangeType et, String name) {
		this.et = et;
		this.name = name;
	}
	
	public ExchangeType getExchangeType() {
		return et;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return String.format("%s", name);
	}
}
