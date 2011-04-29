package net.ooici.util.msgpack.test;


import org.junit.AfterClass;
import org.junit.Test;

import java.io.IOException;


import net.ooici.util.msgpack.MessagePackException;
import net.ooici.util.msgpack.MessagePackType;
import net.ooici.util.msgpack.elements.MPElement;
import net.ooici.util.msgpack.elements.MPLong;
import net.ooici.util.msgpack.elements.MPMap;



public class MPMapTest {

	@AfterClass
	public static void printSeparator() {
		System.out.println(String.format("%n%n"));
	}

	public static void printHeader() {
    	System.out.println(String.format(
    			"%-10s  %-10s  %-8s  %-10s  %-10s  %-10s  %-10s", 
    			"BYTE:", "TYPE:", "SIZE", "KEY:", "INPUT:", "OUTPUT:", "RESULT:"
    	));
    	System.out.println(String.format(
    			"%-10s  %-10s  %-8s  %-10s  %-10s  %-10s  %-10s", 
    			"---- ", "---- ", "---- ", "--- ", "----- ", "------ ", "------ "
    	));
	}

	public static void printResults(MPMap map, MPMap result, byte[] bytes, int max) {
    	
		int count = 0;
    	for (String s : result.keySet()) {
	    	System.out.println(
	    			String.format(
	    			    	"%-10s  %-10s  %-8s  %-10s  %-10s  %-10s  %-10s",
	    			    	String.format("%02x", bytes[0]),
	    			    	MessagePackType.getType(bytes[0]),
	    			    	map.size(),
	    			    	s,
	    					map.get(s),
	    					result.get(s),
	    					map.get(s).equals(result.get(s)) ? "Ok" : "Fail"
	    			)
	    	);
	    	if (++count > max)
	    		break;
    	}
		System.out.println(String.format("%n%n"));
	}
	
	@Test
	public void testFixedArray() 
	throws IOException, MessagePackException {
		System.out.println("ROUND TRIP CONVERSION OF FIXED MPMAP (up to 15 elements).");


		printHeader();
		MPMap map = new MPMap();
		for (int i=0; i < 15; i++) {
			MPLong L1 = new MPLong(i+1);
			MPLong L2 = new MPLong(100+i+1);
			map.put(L1, L2);
		}
    	
		byte[] bytes = map.encode();
    	MPMap result = (MPMap)MPElement.decode(bytes);
    	printResults(map, result, bytes, 20);

	}

	@Test
	public void testMap16() 
	throws IOException, MessagePackException {
		System.out.println("ROUND TRIP CONVERSION OF MAP16 (up to 0xFFFF elements).");


		printHeader();
		MPMap map = new MPMap();
		for (int i=0; i < 0xFFFF; i++) {
			MPLong L1 = new MPLong(i+1);
			MPLong L2 = new MPLong(100000+i+1);
			map.put(L1, L2);
		}
    	
		byte[] bytes = map.encode();
    	MPMap result = (MPMap)MPElement.decode(bytes);
    	printResults(map, result, bytes, 20);
	}

	
	@Test
	public void testMap32() 
	throws IOException, MessagePackException {
		System.out.println("ROUND TRIP CONVERSION OF MAP32 (up to 0xFFFFFF elements).");


		printHeader();
		MPMap map = new MPMap();
		for (int i=0; i < 0x11000; i++) {
			MPLong L1 = new MPLong(i+1);
			MPLong L2 = new MPLong(100000+i+1);
			map.put(L1, L2);
		}
    	
		byte[] bytes = map.encode();
    	MPMap result = (MPMap)MPElement.decode(bytes);
    	printResults(map, result, bytes, 20);
	}
}
