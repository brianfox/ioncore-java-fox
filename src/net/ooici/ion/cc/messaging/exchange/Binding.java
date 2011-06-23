package net.ooici.ion.cc.messaging.exchange;

import java.util.UUID;

/**
 * 
 * @author brianfox
 *
 */
public final class Binding {
	
	// Binding, ExchangeSpace, ExchangeName, and Queue **must** 
	// all be immutable.  Be careful when changing anything within 
	// these classes.
	
	private UUID id;
	private ExchangeSpace exchangeSpace;
	private ExchangeName exchangeName;
	private Queue queue;
	private String routingKey;
	
	public Binding(
			ExchangeSpace exchangeSpace, 
			ExchangeName exchangeName, 
			Queue queue,
			String routingKey
	) {
		this.id = UUID.randomUUID();
		this.exchangeSpace = exchangeSpace;
		this.exchangeName = exchangeName;
		this.queue = queue;
	}

	public UUID getId() {
		return id;
	}

	public String getExchangeSpace() {
		return exchangeSpace.toString();
	}

	public String getExchangeName() {
		return exchangeName.toString();
	}


	public String getQueue() {
		return queue.toString();
	}

	public String getRoutingKey() {
		return routingKey;
	}

}
