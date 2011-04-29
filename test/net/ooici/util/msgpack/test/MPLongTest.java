package net.ooici.util.msgpack.test;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;


import net.ooici.util.msgpack.MessagePackException;
import net.ooici.util.msgpack.MessagePackType;
import net.ooici.util.msgpack.elements.MPElement;
import net.ooici.util.msgpack.elements.MPLong;
import net.ooici.util.string.PrettyPrint;



public class MPLongTest {

	long[] positiveTestValues = new long[] {
	            0x0L,               0x7FL,     // Positive Fix Number: 0 .. 127  // 0XXXXXXX
	   	       0x80L,               0xFFL,     // UINT8: 128 .. 255              // 0XCC XXXXXXXX
	          0x100L,             0xFFFFL,     // UINT16: 256 .. 2^16 - 1		 // 0XCD XXXXXXXX
            0x10000L,         0xFFFFFFFFL,     // UINT32: 2^16 .. 2^32 - 1       // 0XCE XXXXXXXX    	
	    0x100000000L, 0x7FFFFFFFFFFFFFFFL      // INT64: 2^32 .. 2^63 - 1        // 0XCF XXXXXXXX
	};

	long[] negativeTestValues = new long[] {
            0x1L,               0x20L,     // Positive Fix Number: 0 .. 127  // 0XXXXXXX
   	       0x21L,               0x80L,     // UINT8: 128 .. 255              // 0XCC XXXXXXXX
           0x81L,             0x8000L,     // UINT16: 256 .. 2^16 - 1		 // 0XCD XXXXXXXX
         0x8001L,         0x80000000L,     // UINT32: 2^16 .. 2^32 - 1       // 0XCE XXXXXXXX    	
     0x80000001L, 0x8000000000000000L      // INT64: 2^32 .. 2^63 - 1        // 0XCF XXXXXXXX
};


	@Test
	public void testPositiveIntegers() 
	throws IOException, MessagePackException {

		System.out.println(String.format("%s%n", "ROUND TRIP CONVERSION OF POSITIVE INTEGERS."));
		printHeader();
		for (long l : positiveTestValues)
	    	encodeLong(l);
		System.out.println(String.format("%n%n"));
    }

	@Test
	public void testNegativeIntegers() 
	throws IOException, MessagePackException {

		System.out.println(String.format("%s%n", "ROUND TRIP CONVERSION OF NEGATIVE INTEGERS."));
		printHeader();
		for (long l : negativeTestValues)
	    	encodeLong(-l);
		System.out.println(String.format("%n%n"));
    }

    
	private static void printHeader() {
    	System.out.println(String.format("%-20s  %-20s %-20s  %-30s  %-18s  %-6s", "INPUT:", "OUTPUT:", "[HEX:]", "MSGPACK:", "MPTYPE:", "RESULT:"));
    	System.out.println(String.format("%-20s  %-20s %-20s  %-30s  %-18s  %-6s", "----- ", "------ ", " ---  ", "------- ", "------ ", "------ "));
	}
    
    private static void encodeLong(long n) throws IOException, MessagePackException {
    	MPLong l = new MPLong(n);
    	byte[] b = l.encode();
    	MPElement result = MPElement.decode(b);
    	System.out.println(
    			String.format(
    					"%-20s  %-20s %-20s  %-30s  %-18s  %-6s", 
    					l.getLong(), 
    					((MPLong)result).getLong(),
    					String.format("[%x]",l.getLong()), 
    					PrettyPrint.hexDumpSimple(b),
    					MessagePackType.getType(b[0] & 0xFF),
    					l.getLong() == ((MPLong)result).getLong() ? "Ok" : "Fail"
    					
    			)
    	);
    	assertTrue(l.getLong() == ((MPLong)result).getLong());
    }


}
