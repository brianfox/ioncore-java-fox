package net.ooici.util.msgpack.elements;

import java.io.DataOutputStream;
import java.io.IOException;

import net.ooici.util.msgpack.MessagePackException;


public class MPDouble extends MPElement {
	double d;
	public MPDouble(double d) {
		this.d = d;
	}

	@Override
	public String toString() {
		return new Double(d).toString();
	}

	public double getDouble() {
		return d;
	}


	@Override
	public void encodeTo(DataOutputStream dos) 
	throws MessagePackException 
	{
		try {
		long n = Double.doubleToLongBits(d);
		
		dos.writeByte((byte)(0xCB & 0xFF));
		dos.writeByte((byte)((n >> 56) & 0xFF));
		dos.writeByte((byte)((n >> 48) & 0xFF));
		dos.writeByte((byte)((n >> 40) & 0xFF));
		dos.writeByte((byte)((n >> 32) & 0xFF));
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

