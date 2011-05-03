package net.ooici.ion.cc.message.payload;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class Header {

	private HashMap<String, String> map;
	
	public Header(HashMap<String,String> map) {
		this.map = new HashMap<String, String>();
		if (map != null)
			for (String key : map.keySet()) {
				this.map.put(key, map.get(key));
			}
	}
	
	public Set<String> keySet() {
		TreeSet<String> set = new TreeSet<String>();
		for (String s : map.keySet())
			set.add(s);
		return set;
	}

	public String get(String key) {
		if (map.containsKey(key))
			return map.get(key);
		return null;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (String key : map.keySet()) {
			sb.append(String.format("\t%-20s\t%-40s%n", key, map.get(key)));
		}
		return sb.toString();
	}
}
