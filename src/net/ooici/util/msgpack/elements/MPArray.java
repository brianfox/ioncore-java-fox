package net.ooici.util.msgpack.elements;

import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import net.ooici.util.msgpack.MessagePackException;

public class MPArray extends MPElement implements Iterable<MPElement>{

	ArrayList<MPElement> array;

	public MPArray() {
		array = new ArrayList<MPElement>();
	}

	public MPArray(DataInput in, int size) 
	throws MessagePackException 
	{
		array = new ArrayList<MPElement>();
		for (int i=0; i < size; i++)
			try {
				array.add(MPElement.decodeFrom(in));
			} catch (IOException e) {
				throw new MessagePackException(
						String.format("Could not fully decode array.  Stopped at %d of %d elements.  Cause: %s ",
								i, size, e.getMessage(), e));
			}
	}
	
	public ArrayList<MPElement> getArray() {
		return array;
	}

	
	public void add(MPElement e) {
		array.add(e);
	}
	

	public int size() {
		return array.size();
	}

	

	@Override
	public void encodeTo(DataOutputStream out) 
	throws MessagePackException {
		try {
			int size = array.size();
			if (size <= 15) {
				out.writeByte((byte)(0x90 | size));
			}
			else if (size <= 0xFFFF) {
				out.writeByte(0xDC);
				out.writeByte((byte)((size >> 8) & 0xFF));
				out.writeByte((byte)((size) & 0xFF));
			}
			else {
				out.writeByte(0xDD);
				out.writeByte((byte)((size >> 24) & 0xFF));
				out.writeByte((byte)((size >> 16) & 0xFF));
				out.writeByte((byte)((size >> 8) & 0xFF));
				out.writeByte((byte)((size) & 0xFF));
			}
			for (MPElement e : array)
				e.encodeTo(out);
		}
		catch (IOException io) {
			throw new MessagePackException("Could not write to DataOutputStream: " + io.getMessage(), io);
		}
	}
	
	@Override 
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (MPElement e : array)
			sb.append(String.format("%s%n", e.toString()));
		return sb.toString();
	}

	@Override
	public Iterator<MPElement> iterator() {
		return array.iterator();
	}

	public MPElement get(int i) {
		return array.get(i);
	}

}
