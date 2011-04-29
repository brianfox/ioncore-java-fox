package net.ooici.util.msgpack.elements;

import java.io.DataOutputStream;
import java.io.IOException;

import net.ooici.util.msgpack.MessagePackException;


public class MPLong extends MPElement implements Comparable<MPLong>{
	
	long n;
	
	public MPLong(long n) {
		this.n = n;
	}



	@Override
	public String toString() {
		return new Long(n).toString();
	}

	public long getLong() {
		return n;
	}


	@Override
	public void encodeTo(DataOutputStream dos) 
	throws MessagePackException 
	{
		try {
			if (n >= 0) {
				 if (n < 128) {
					 dos.writeByte((byte)n);
				 }
				else if (n < 0x100) {
					dos.writeByte((byte)0xCC);
					dos.writeByte((byte)n & 0xFF);
				}
				else if (n < 0x10000) {
					dos.writeByte((byte)0xCD);
					dos.writeByte((byte)((n >> 8) & 0xFF));
					dos.writeByte(((byte)n) & 0xFF);
				}
				else if (n < 0x100000000L) {
					dos.writeByte((byte)0xCE);
					dos.writeByte((byte)((n >> 24) & 0xFF));
					dos.writeByte((byte)((n >> 16) & 0xFF));
					dos.writeByte((byte)((n >> 8) & 0xFF));
					dos.writeByte(((byte)n) & 0xFF);
				}
				else if (n <= 0x7FFFFFFFFFFFFFFFL) {
					dos.writeByte((byte)0xD3);
					dos.writeByte((byte)((n >> 56) & 0xFF));
					dos.writeByte((byte)((n >> 48) & 0xFF));
					dos.writeByte((byte)((n >> 40) & 0xFF));
					dos.writeByte((byte)((n >> 32) & 0xFF));
					dos.writeByte((byte)((n >> 24) & 0xFF));
					dos.writeByte((byte)((n >> 16) & 0xFF));
					dos.writeByte((byte)((n >> 8) & 0xFF));
					dos.writeByte(((byte)n) & 0xFF);
				}
			}
			else if (n < 0) {
				if (n >= -0x20 ) {
					dos.writeByte((byte)((n & 0x1F) | 0xE0));
				}
				else if (n >= -0x80 ) {
					dos.writeByte((byte)0xD0);
					dos.writeByte(((byte)n) & 0xFF);
				}
				else if (n >= -0x8000 ) {
					dos.writeByte((byte)0xD1);
					dos.writeByte((byte)((n >> 8) & 0xFF));
					dos.writeByte(((byte)n) & 0xFF);
				}
				else if (n >= -0x80000000L ) {
					dos.writeByte((byte)0xD2);
					dos.writeByte((byte)((n >> 24) & 0xFF));
					dos.writeByte((byte)((n >> 16) & 0xFF));
					dos.writeByte((byte)((n >> 8) & 0xFF));
					dos.writeByte(((byte)n) & 0xFF);
				}
				else {
					dos.writeByte((byte)0xD3);
					dos.writeByte((byte)((n >> 56) & 0xFF));
					dos.writeByte((byte)((n >> 48) & 0xFF));
					dos.writeByte((byte)((n >> 40) & 0xFF));
					dos.writeByte((byte)((n >> 32) & 0xFF));
					dos.writeByte((byte)((n >> 24) & 0xFF));
					dos.writeByte((byte)((n >> 16) & 0xFF));
					dos.writeByte((byte)((n >> 8) & 0xFF));
					dos.writeByte(((byte)n) & 0xFF);
				}
			}
		}
		catch (IOException io) {
			throw new MessagePackException(
					"Could not write to DataOutputStream: " 
					+ io.getMessage(), io
			);
		}
	}



	@Override
	public int compareTo(MPLong e) {
		return (int)(this.n - e.n);
	}


	@Override
	public boolean equals(Object o) {
		MPLong e = (MPLong)o;
		return this.n == e.n;
	}
}
