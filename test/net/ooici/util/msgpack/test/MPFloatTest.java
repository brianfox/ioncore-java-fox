package net.ooici.util.msgpack.test;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;


import net.ooici.util.msgpack.MessagePackException;
import net.ooici.util.msgpack.MessagePackType;
import net.ooici.util.msgpack.elements.MPElement;
import net.ooici.util.msgpack.elements.MPFloat;
import net.ooici.util.string.HexPrettyPrint;



public class MPFloatTest {


	@Test
	public void testPositiveIntegers() 
	throws IOException, MessagePackException {

		System.out.println(String.format("%s%n", "ROUND TRIP CONVERSION OF FLOATS."));
		printHeader();
    	encodeFloat(-Float.MAX_VALUE);
    	encodeFloat(-Float.MAX_VALUE/2);
    	encodeFloat(-1);
    	encodeFloat(-Float.MIN_VALUE);
    	encodeFloat(0);
    	encodeFloat(Float.MIN_VALUE);
    	encodeFloat(1);
    	encodeFloat(Float.MAX_VALUE/2);
    	encodeFloat(Float.MAX_VALUE);
		System.out.println(String.format("%n%n"));
    }

    
	private static void printHeader() {
    	System.out.println(String.format("%-20s  %-20s  %-20s  %-18s  %-6s", "INPUT:", "OUTPUT:", "MSGPACK:", "MPTYPE:", "RESULT:"));
    	System.out.println(String.format("%-20s  %-20s  %-20s  %-18s  %-6s", "----- ", "------ ", "------- ", "------ ", "------ "));
	}
    
    private static void encodeFloat(float f) throws IOException, MessagePackException {
    	MPFloat F = new MPFloat(f);
    	byte[] b = F.encode();
    	MPFloat result = (MPFloat)MPElement.decode(b);
    	System.out.println(
    			String.format(
    					"%-20s  %-20s  %-20s  %-18s  %-6s", 
    					F.getFloat(), 
    					result.getFloat(),
    					HexPrettyPrint.hexDumpSimple(b),
    					MessagePackType.getType(b[0] & 0xFF),
    					F.getFloat() == result.getFloat() ? "Ok" : "Fail"
    					
    			)
    	);
    	assertTrue(F.getFloat() == result.getFloat());
    }


}
