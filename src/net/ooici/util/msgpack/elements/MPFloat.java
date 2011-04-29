package net.ooici.util.msgpack.elements;

import java.io.DataOutputStream;
import java.io.IOException;

import net.ooici.util.msgpack.MessagePackException;


public class MPFloat extends MPElement {
	float f;
	public MPFloat(float f) {
		this.f = f;
	}


	@Override
	public String toString() {
		return new Float(f).toString();
	}

	public float getFloat() {
		return f;
	}

	@Override
	public byte[] encode() {
		int n = Float.floatToIntBits(f);

		byte[] ret = new byte[5];
		ret[0] = ((byte)0xCA);
		ret[1] = ((byte)((n >> 24) & 0xFF));
		ret[2] = ((byte)((n >> 16) & 0xFF));
		ret[3] = ((byte)((n >> 8) & 0xFF));
		ret[4] = ((byte)(n & 0xFF));
		return ret;
	}

	@Override
	public void encodeTo(DataOutputStream dos) 
	throws MessagePackException 
	{
		try {
			int n = Float.floatToIntBits(f);
			
			dos.writeByte((byte)0xCA);
			dos.writeByte((byte)((n >> 24) & 0xFF));
			dos.writeByte((byte)((n >> 16) & 0xFF));
			dos.writeByte((byte)((n >> 8) & 0xFF));
			dos.writeByte((byte)(n & 0xFF));
		}
		catch (IOException io) {
			throw new MessagePackException(
					"Could not write to DataOutputStream: " 
					+ io.getMessage(), io
			);
		}

		
	}
	


}