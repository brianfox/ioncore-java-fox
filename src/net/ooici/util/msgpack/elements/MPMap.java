package net.ooici.util.msgpack.elements;

import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.ooici.util.msgpack.MessagePackException;
import net.ooici.util.string.PrettyPrint;

public class MPMap extends MPElement {
	HashMap<MPElement, MPElement> map;
	HashMap<String, MPElement> smap;
	
	public MPMap() {
		this.map = new HashMap<MPElement, MPElement>();
		this.smap = new HashMap<String, MPElement>();
	}

	public MPMap(long opSize, DataInput in) 
	throws MessagePackException 
	{
		this();
		try {
			for (int i=0; i < opSize; i++) {
				MPElement key = decodeFrom(in);
				MPElement val = decodeFrom(in);
				map.put(key, val);
				smap.put(key.toString(), val);
			}
		}
		catch (IOException e) {
			throw new MessagePackException("Input stream ran out of elements before MPMap could be completed.");
		}
	}

	public int size() {
		return map.size();
	}
	
	
	public List<String> keySet() {
		return new ArrayList<String>(smap.keySet());
	}

	public MPElement get(String key) {
		return smap.get(key);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("msgpack map:%n"));
		sb.append(String.format("size: %d%n", map.size()));
		ArrayList<String> l = new ArrayList<String>(smap.keySet());
		Collections.sort(l);
		for (String s : l) {
			String val = smap.get(s).toString();
			if (val.indexOf('\n') < 0)
				sb.append(String.format("    key: %-30s  val: %-80s%n", s, val));
			else
				sb.append(String.format("    key: %-30s  val:%n%s%n", s, PrettyPrint.pad(val,8)));
		}
		return sb.toString();
	}


	public HashMap<String, MPElement> getStringMap() {
		HashMap<String, MPElement> ret = new HashMap<String, MPElement>();
		for (String s : smap.keySet()) {
			ret.put(s, smap.get(s));
		}
		return ret;
	}

	
	public boolean hasKey(String key) {
		return smap.keySet().contains(key);
	}




	@Override
	public void encodeTo(DataOutputStream out) 
	throws MessagePackException {
		try {
			int size = map.size();
			if (size <= 15) {
				out.writeByte((byte)(0x80 | size));
			}
			else if (size <= 0xFFFF) {
				out.writeByte(0xDE);
				out.writeByte((byte)((size >> 8) & 0xFF));
				out.writeByte((byte)((size) & 0xFF));
			}
			else {
				out.writeByte(0xDF);
				out.writeByte((byte)((size >> 24) & 0xFF));
				out.writeByte((byte)((size >> 16) & 0xFF));
				out.writeByte((byte)((size >> 8) & 0xFF));
				out.writeByte((byte)((size) & 0xFF));
			}
			for (MPElement key : map.keySet()) {
				key.encodeTo(out);
				map.get(key).encodeTo(out);
			}
		}
		catch (IOException io) {
			throw new MessagePackException(
					"Could not write to DataOutputStream: " 
					+ io.getMessage(), io
			);
		}
		
	}

	public void put(MPElement key, MPElement val) {
		map.put(key, val);
		smap.put(key.toString(), val);
	}
	
}

