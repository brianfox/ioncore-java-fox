package net.ooici.util.msgpack.elements;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import net.ooici.util.msgpack.MessagePackException;
import net.ooici.util.msgpack.elements.MPElement;
import net.ooici.util.string.PrettyPrint;

public class MPString extends MPElement {
	
	public static final int MAX_MPRAW_SIZE = 256*1024*1024;

	byte[] raw;

	public MPString(byte[] raw) throws MessagePackException  {
		this.raw = raw;
	}

	public MPString(byte[] raw, int start, int len) throws MessagePackException  {
		try {
			if (len > MAX_MPRAW_SIZE)
				throw new IllegalArgumentException(String.format("MPRaw size exceeds limit.  Size: %d Limit: %d", len, MAX_MPRAW_SIZE));
			this.raw = Arrays.copyOfRange(raw, start, start + len);
		}
		catch (IllegalArgumentException e) {
			throw new MessagePackException(
					String.format(
							"Cannot create a element: byte[] length=%d  start=%d  len=%d",
							raw.length, start, len)
			);
			
		}
	}


	@Override
	public String toString() {
		for (byte b : raw)
			if (!PrettyPrint.isPrintable(b)) { 
				return String.format("HEX DUMP:%n%s%n", PrettyPrint.hexDump(raw));
			}
		return new String(raw);
	}

	public String getString() {
		return new String(raw);
	}

	public boolean isPrintable() {
		for (byte b : raw)
			if (!PrettyPrint.isPrintable(b))
				return false;
		return true;
	}
	
	
	public byte[] getBytes() {
		return Arrays.copyOf(raw, raw.length);
	}

	@Override
	public void encodeTo(DataOutputStream out) 
	throws MessagePackException 
	{
		try {
			if (raw.length < 32) {
				out.writeByte((byte)((0xA0 | raw.length) & 0xFF));
			}
			else if (raw.length < 0x10000) {
				out.writeByte((byte)0xDA);
				out.writeByte((byte)((raw.length >> 8) & 0xFF));
				out.writeByte((byte)((raw.length) & 0xFF));
			}		
			else {
				out.writeByte((byte)0xDB);
				out.writeByte((byte)((raw.length >> 24) & 0xFF));
				out.writeByte((byte)((raw.length >> 16) & 0xFF));
				out.writeByte((byte)((raw.length >> 8) & 0xFF));
				out.writeByte((byte)((raw.length) & 0xFF));
			}		
			for (byte b : raw)
				out.writeByte(b);
		}
		catch (IOException io) {
			throw new MessagePackException(
					"Could not write to DataOutputStream: " 
					+ io.getMessage(), io
			);
		}
	}
}

