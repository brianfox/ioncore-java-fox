package net.ooici.util.msgpack.test;

import org.junit.Test;

import java.io.IOException;


import net.ooici.util.msgpack.MessagePackException;
import net.ooici.util.msgpack.MessagePackType;
import net.ooici.util.msgpack.elements.MPElement;
import net.ooici.util.msgpack.elements.MPNull;
import net.ooici.util.string.PrettyPrint;



public class MPNullTest {

	@Test
	public void testBooleans() 
	throws IOException, MessagePackException {

		System.out.println(String.format("%s%n", "ROUND TRIP CONVERSION OF NULLS."));
		printHeader();
		encodeNull();
		System.out.println(String.format("%n%n"));
    }

    
	private static void printHeader() {
    	System.out.println(String.format("%-8s  %-8s  %-10s  %-10s  %-6s", "INPUT:", "OUTPUT:", "MSGPACK:", "MPTYPE:", "RESULT:"));
    	System.out.println(String.format("%-8s  %-8s  %-10s  %-10s  %-6s", "----- ", "------ ", "------- ", "------ ", "------ "));
	}
    
    private static void encodeNull() throws IOException, MessagePackException {
    	MPNull N = new MPNull();
    	byte[] bytes = N.encode();
    	MPNull result = (MPNull)MPElement.decode(bytes);
    	System.out.println(
    			String.format(
    			    	"%-8s  %-8s  %-10s  %-10s  %-6s",
    					null, 
    					result.getNull(),
    					PrettyPrint.hexDumpSimple(bytes),
    					MessagePackType.getType(bytes[0] & 0xFF),
    					N.getNull() == result.getNull() ? "Ok" : "Fail"
    			)
    	);
    }
}
