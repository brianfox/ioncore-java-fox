package net.ooici.util.msgpack.elements;

import java.io.DataOutputStream;
import java.io.IOException;

import net.ooici.util.msgpack.MessagePackException;

public class MPBoolean extends MPElement {
	boolean b;
	public MPBoolean(boolean b) {
		this.b = b;
	}

//	@Override
//	public int nest(ArrayList<MPElement> flat, int start) {
//		return 0;
//	}

	@Override
	public String toString() {
		return b ? "TRUE" : "FALSE";
	}

	public boolean getBoolean() {
		return b;
	}

	@Override
	public byte[] encode() {
		return new byte[]{b ? (byte)0xC3 : (byte)0xC2};
	}

	@Override
	public void encodeTo(DataOutputStream out) 
	throws MessagePackException 
	{
		try {
		out.writeByte(b ? (byte)0xC3 : (byte)0xC2);
		}
		catch (IOException io) {
			throw new MessagePackException("Could not write to DataOutputStream: " + io.getMessage(), io);
		}	
	}


}