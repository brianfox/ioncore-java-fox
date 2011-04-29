package net.ooici.util.msgpack.elements;

import java.io.DataOutputStream;
import java.io.IOException;
import net.ooici.util.msgpack.MessagePackException;

public class MPNull extends MPElement {
	
	public MPNull() throws MessagePackException  {
	}

	@Override
	public String toString() {
		return "null";
	}


	public Object getNull() {
		return null;
	}

	@Override
	public byte[] encode() {
		return new byte[]{(byte)0xC0};
	}

	@Override
	public void encodeTo(DataOutputStream out) 
	throws MessagePackException 
	{
		try {
			out.writeByte((byte)0xC0);
		}
		catch (IOException io) {
			throw new MessagePackException(
					"Could not write to DataOutputStream: " 
					+ io.getMessage(), io
			);
		}
	}


}