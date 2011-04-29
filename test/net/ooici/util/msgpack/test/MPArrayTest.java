package net.ooici.util.msgpack.test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Test;

import java.io.IOException;


import net.ooici.util.msgpack.MessagePackException;
import net.ooici.util.msgpack.elements.MPArray;
import net.ooici.util.msgpack.elements.MPElement;
import net.ooici.util.msgpack.elements.MPLong;



public class MPArrayTest {

	@AfterClass
	public static void printSeparator() {
		System.out.println(String.format("%n%n"));
	}
	
	@Test
	public void testFixedArray() 
	throws IOException, MessagePackException {
		System.out.println("ROUND TRIP CONVERSION OF FIXED ARRAY (up to 15 elements).");
		
		MPArray array = new MPArray();
		for (int i=0; i < 15; i++) {
			MPLong L = new MPLong(i+1);
			array.add(L);
		}
		byte[] bytes = array.encode();
		MPArray result = (MPArray)MPElement.decode(bytes);
		
		MPLong bottom = (MPLong)result.get(0);
		assertTrue(bottom.getLong() == 1);

		MPLong top = (MPLong)result.get(14);
		assertTrue(top.getLong() == 15);
	}

	

	@Test
	public void testArray16() 
	throws IOException, MessagePackException {
		System.out.println("ROUND TRIP CONVERSION OF ARRAY 16 (up to 2^16-1 elements).");
		
		MPArray array = new MPArray();
		for (int i=0; i < 0xFFFF; i++) {
			MPLong L = new MPLong(i+1);
			array.add(L);
		}
		byte[] bytes = array.encode();
		MPArray result = (MPArray)MPElement.decode(bytes);
		
		MPLong bottom = (MPLong)result.get(0);
		assertTrue(bottom.getLong() == 1);

		MPLong top = (MPLong)result.get(0xFFFF-1);
		assertTrue(top.getLong() == 0xFFFF);
	}

	
	@Test
	public void testArray32() 
	throws IOException, MessagePackException {
		System.out.println("ROUND TRIP CONVERSION OF ARRAY 32 (up to 2^32-1 elements).");
		
		MPArray array = new MPArray();
		for (int i=0; i < 0x1FFFF; i++) {
			MPLong L = new MPLong(i+1);
			array.add(L);
		}
		byte[] bytes = array.encode();
		MPArray result = (MPArray)MPElement.decode(bytes);
		
		MPLong bottom = (MPLong)result.get(0);
		assertTrue(bottom.getLong() == 1);

		MPLong top = (MPLong)result.get(0x1FFFF-1);
		assertTrue(top.getLong() == 0x1FFFF);
	}

}
