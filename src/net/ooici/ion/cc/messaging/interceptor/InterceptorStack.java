package net.ooici.ion.cc.messaging.interceptor;

import java.util.ArrayList;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooici.ion.cc.messaging.MessagingException;
import net.ooici.ion.cc.messaging.message.Message;
import net.ooici.ion.cc.messaging.serialization.Serializer;
import net.ooici.ion.cc.messaging.serialization.generic.MsgpackSerializer;
import net.ooici.ion.properties.LocalProperties;


public class InterceptorStack {

	public enum Result {
		ACCEPTED,
		REJECTED
	};

	public class OutgoingDecision {
		private Result result;
		private String reason;
		private byte[] raw;

		public OutgoingDecision(
				Result result, 
				String reason, 
				byte[] raw
		) {
			this.result = result;
			this.reason = reason;
			this.raw = raw;
		}

		public Result getResult() {
			return result;
		}
		public String getReason() {
			return reason;
		}
		public byte[] getRaw() {
			return raw;
		}

		@Override 
		public String toString() {
			return String.format("%-10s %-40s", result, reason);
		}
	}
	
	public class IncomingDecision {
		private Result result;
		private String reason;
		private Message message;

		public IncomingDecision(
				Result result, 
				String reason, 
				Message message
		) {
			this.result = result;
			this.reason = reason;
			this.message = message;
		}
		
		public Result getResult() {
			return result;
		}
		public String getReason() {
			return reason;
		}
		public Message getMessage() {
			return message;
		}
	}
	
	Logger logger = LoggerFactory.getLogger(InterceptorStack.class);
	
	protected ArrayList<SortableSerializer> serializers;

	public InterceptorStack(LocalProperties section) {
		this.serializers = new ArrayList<SortableSerializer>();
		// this.serializers.add(new SortableSerializer(new NullSerializer(), 0));
		this.serializers.add(new SortableSerializer(new MsgpackSerializer(), 0));
	}


	public void addIncomingInterceptor(IncomingInterceptor interceptor) {
		
	}

	public void addOutgoingInterceptor(OutgoingInterceptor interceptor) {
		
	}

	public void removeInterceptor(IncomingInterceptor interceptor) {
		
	}


	public OutgoingDecision interceptOutgoing(Message message) {
		try {
			byte[] raw = this.serialize(message);
			return new OutgoingDecision(
					Result.ACCEPTED, 
					Result.ACCEPTED.toString(), 
					raw
			);
		} catch (MessagingException e) {
			return new OutgoingDecision(
					Result.REJECTED, 
					e.getMessage(), 
					null
			);
		}
		
	}


	public IncomingDecision interceptIncoming(byte[] raw) {
		try {
			Message m = deserialize(raw);
			return new IncomingDecision(
					Result.ACCEPTED, 
					Result.ACCEPTED.toString(), 
					m
			);
		} catch (Exception e) {
			return new IncomingDecision(
					Result.REJECTED, 
					e.getMessage(), 
					null
			);
		}
		
	}

	
	synchronized
	public void addSerializer(Serializer s, int priority) {
		serializers.add(new SortableSerializer(s,priority));
		Collections.sort(serializers);
	}
	
	
	private static class SortableSerializer implements Comparable<SortableSerializer> {
		private Serializer serializer;
		private int priority;
		
		SortableSerializer(Serializer serializer, int priority) {
			this.serializer = serializer;
			this.priority = priority;
		}

		@Override
		public int compareTo(SortableSerializer s) {
			return this.priority - s.priority;
		}
	}
	
	
	protected byte[] serialize(Message m) 
	throws MessagingException 
	{
		for (SortableSerializer s : serializers) 
			try{
					return s.serializer.serialize(m);
			}
			catch (MessagingException e) {
				continue;
			}
		String err = String.format("No serializer available to serialize message.");
		logger.error(err);
		throw new MessagingException(err);
	}

	
	protected Message deserialize(byte[] b) 
	throws MessagingException 
	{
		for (SortableSerializer s : serializers) 
			try{
					return s.serializer.deserialize(b);
			}
			catch (MessagingException e) {
				continue;
			}
		String err = String.format("No serializer available to deserialize byte array.");
		logger.error(err);
		throw new MessagingException(err);
	}





}
