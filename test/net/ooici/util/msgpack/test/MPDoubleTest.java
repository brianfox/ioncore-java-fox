package net.ooici.util.msgpack.test;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;


import net.ooici.util.msgpack.MessagePackException;
import net.ooici.util.msgpack.MessagePackType;
import net.ooici.util.msgpack.elements.MPDouble;
import net.ooici.util.msgpack.elements.MPElement;
import net.ooici.util.string.PrettyPrint;



public class MPDoubleTest {


	@Test
	public void testPositiveIntegers() 
	throws IOException, MessagePackException {

		System.out.println(String.format("%s%n", "ROUND TRIP CONVERSION OF DOUBLES."));
		printHeader();
    	encodeDouble(-Double.MAX_VALUE);
    	encodeDouble(-Double.MAX_VALUE/2);
    	encodeDouble(-1);
    	encodeDouble(-Double.MIN_VALUE);
    	encodeDouble(0);
    	encodeDouble(Double.MIN_VALUE);
    	encodeDouble(1);
    	encodeDouble(Double.MAX_VALUE/2);
    	encodeDouble(Double.MAX_VALUE);
		System.out.println(String.format("%n%n"));
    }

    
	private static void printHeader() {
    	System.out.println(String.format("%-25s  %-25s  %-20s  %-18s  %-6s", "INPUT:", "OUTPUT:", "MSGPACK:", "MPTYPE:", "RESULT:"));
    	System.out.println(String.format("%-25s  %-25s  %-20s  %-18s  %-6s", "----- ", "------ ", "------- ", "------ ", "------ "));
	}
    
    private static void encodeDouble(double d) throws IOException, MessagePackException {
    	MPDouble D = new MPDouble(d);
    	byte[] b = D.encode();
    	MPDouble result = (MPDouble)MPElement.decode(b);
    	System.out.println(
    			String.format(
    					"%-25s  %-25s  %-20s  %-18s  %-6s", 
    					D.getDouble(), 
    					result.getDouble(),
    					PrettyPrint.hexDumpSimple(b),
    					MessagePackType.getType(b[0] & 0xFF),
    					D.getDouble() == result.getDouble() ? "Ok" : "Fail"
    					
    			)
    	);
    	assertTrue(D.getDouble() == result.getDouble());
    }


}
