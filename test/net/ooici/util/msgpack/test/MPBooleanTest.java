package net.ooici.util.msgpack.test;

import org.junit.Test;

import java.io.IOException;


import net.ooici.util.msgpack.MessagePackException;
import net.ooici.util.msgpack.MessagePackType;
import net.ooici.util.msgpack.elements.MPBoolean;
import net.ooici.util.msgpack.elements.MPElement;
import net.ooici.util.string.HexPrettyPrint;



public class MPBooleanTest {

	@Test
	public void testBooleans() 
	throws IOException, MessagePackException {

		System.out.println(String.format("%s%n", "ROUND TRIP CONVERSION OF BOOLEANS."));
		printHeader();
		encodeBoolean(false);
		encodeBoolean(true);
		System.out.println(String.format("%n%n"));
    }

    
	private static void printHeader() {
    	System.out.println(String.format("%-8s  %-8s  %-10s  %-10s  %-6s", "INPUT:", "OUTPUT:", "MSGPACK:", "MPTYPE:", "RESULT:"));
    	System.out.println(String.format("%-8s  %-8s  %-10s  %-10s  %-6s", "----- ", "------ ", "------- ", "------ ", "------ "));
	}
    
    private static void encodeBoolean(boolean b) throws IOException, MessagePackException {
    	MPBoolean B = new MPBoolean(b);
    	byte[] bytes = B.encode();
    	MPElement result = MPElement.decode(bytes);
    	System.out.println(
    			String.format(
    			    	"%-8s  %-8s  %-10s  %-10s  %-6s",
    					b, 
    					((MPBoolean)result).getBoolean(),
    					HexPrettyPrint.hexDumpSimple(bytes),
    					MessagePackType.getType(bytes[0] & 0xFF),
    					B.getBoolean() == ((MPBoolean)result).getBoolean() ? "Ok" : "Fail"
    			)
    	);
    }
}
