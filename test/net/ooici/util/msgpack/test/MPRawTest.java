package net.ooici.util.msgpack.test;

import org.junit.Test;

import java.io.IOException;


import net.ooici.util.msgpack.MessagePackException;
import net.ooici.util.msgpack.MessagePackType;
import net.ooici.util.msgpack.elements.MPElement;
import net.ooici.util.msgpack.elements.MPRaw;
import net.ooici.util.string.HexPrettyPrint;



public class MPRawTest {

	@Test
	public void testBooleans() 
	throws IOException, MessagePackException {

		System.out.println(String.format("%s%n", "ROUND TRIP CONVERSION OF MPRAW."));
		printHeader();
		encodeRaw(new byte[0]);
		encodeRaw(new byte[31]);
		
		byte[] bytes = new byte[31];
		for (int i=0; i < 31; i++) {
			bytes[i] = (byte)i;
		}
		encodeRaw(bytes);

		
		bytes = new byte[32];
		for (int i=0; i < 32; i++) {
			bytes[i] = (byte)i;
		}
		encodeRaw(bytes);

		bytes = new byte[0xFFFF];
		for (int i=0; i < 0xFFFF; i++) {
			bytes[i] = (byte)i;
		}
		encodeRaw(bytes);

		
		bytes = new byte[0x7FFFF];
		for (int i=0; i < 0x7FFFF; i++) {
			bytes[i] = (byte)i;
		}
		encodeRaw(bytes);

		
		System.out.println(String.format("%n%n"));
    }

    
	private static void printHeader() {
    	System.out.println(String.format(
    			"%8s  %-36s  %-36s  %-36s  %-10s  %-6s", 
    			"LEN", "INPUT:", "OUTPUT:", "MSGPACK:", "MPTYPE:", "RESULT:"
    	));
    	System.out.println(String.format(
    			"%8s  %-36s  %-36s  %-36s  %-10s  %-6s", 
    			"---", "----- ", "------ ", "------- ", "------ ", "------ "
    	));
	}
    
    private static void encodeRaw(byte[] raw) throws IOException, MessagePackException {
    	MPRaw mr = new MPRaw(raw);
    	byte[] bytes = mr.encode();
    	MPRaw result = (MPRaw)MPElement.decode(bytes);
    	byte[] resultBytes = result.getBytes();
    	
    	boolean success = raw.length == resultBytes.length;
    	if (success)
    		for (int i=0; i < raw.length; i++)
    			if (raw[i] != resultBytes[i])
    				success = false;
    	
    	System.out.println(
    			String.format(
    			    	"%8d  %-36s  %-36s  %-36s  %-10s  %-6s",
    			    	raw.length,
    					"[" + trim(HexPrettyPrint.hexDumpSimple(raw), 30) + "]",
    					"[" + trim(HexPrettyPrint.hexDumpSimple(result.getBytes()), 30) + "]",
    					trim(HexPrettyPrint.hexDumpSimple(bytes), 30),
    					MessagePackType.getType(bytes[0] & 0xFF),
    					success ? "Ok" : "Fail"
    			)
    	);
    	// assertTrue(success);
    }


	private static String trim(String string, int i) {
		if (string.length() <= i)
			return string;
		return string.substring(0,i) + "...";
	}
}
