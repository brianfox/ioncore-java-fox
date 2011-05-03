package net.ooici.ion.cc.message.payload;


/*
 * ------- IMMUTABLE -------
 * It is critical that this class be kept immutable.  Do not change the final class
 * modifier.  Do not create any setters.  Do not make any public fields.
 */


public class Message {

	private Header header;
	private Body body;

	
	public Message(Header header, Body body) {
		this.header = header;
		this.body = body;
	}

	
	public Header getHeader() {
		return header;
	}
	
	
	public Body getBody() {
		return body;
	}

	
	public Object oneLiner() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		return String.format("HEADER:%n%s%nBODY:%n%s", header, body);
	}
}
