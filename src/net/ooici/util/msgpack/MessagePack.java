package net.ooici.util.msgpack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;

import net.ooici.util.msgpack.elements.MPElement;


/**
 * Partial MessagePack implementation.  This class is written to address some 
 * of the pitfalls of the official MessagePack release (http://msgpack.org/), 
 * namely, the size, complexity, and typing strategy.<br> 
 * <br>
 * This MessagePack class aims for simplicity.  It can correctly parse all 
 * the types defined by the MessagePack protocol 
 * (http://redmine.msgpack.org/projects/msgpack/wiki/FormatSpec) but does not
 * yet provide a Java translation for each of those types.  In particular, 
 * floating point numbers and signed integers greater than 31 bits are not
 * handled.  This should be fairly easy to implement.<br>
 * <br>
 * 
 * @author brianfox
 *
 */
public class MessagePack {
	
	/**
	 * Convenience method for decoding the entire contents of a byte[].
	 * 
	 * @param raw a byte[] to decode
	 * @return a List of Elements decoded
	 * @throws MessagePackException if any element cannot be decoded properly.
	 * @throws EOFException 
	 */
	public static MPElement decode(byte[] raw) 
	throws MessagePackException
	{
    	DataInputStream dis = new DataInputStream(new ByteArrayInputStream(raw));
		return decodeFrom(dis);
	}


	public static MPElement decodeFrom(DataInputStream dis) 
	throws 
		MessagePackException 
	{
		try {
    		return MPElement.decodeFrom(dis);
    	}
		catch (EOFException e1) {
			return null;
		}
		catch (IOException e) {
			throw new MessagePackException("Could not read from byte array: " + e.getMessage(), e);
		}
	}


	public static byte[] encode(MPElement e) 
	throws MessagePackException 
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		encodeTo(bos, e);
    	return bos.toByteArray();
	}


	public static void encodeTo(OutputStream dos, MPElement e) 
	throws MessagePackException 
	{
    	try {
			dos.write(e.encode());
		} catch (IOException e1) {
			throw new MessagePackException("Could not write to byte array: " + e1.getMessage(), e1);
		}
	}

	


}
