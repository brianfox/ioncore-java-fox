package net.ooici.util.msgpack;

import java.nio.charset.Charset;
import java.util.SortedMap;

public class DefaultCharset {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(Charset.defaultCharset());

		SortedMap<String, Charset> map = Charset.availableCharsets();
		for (String key : map.keySet())
			System.out.println(key);
	}

}
