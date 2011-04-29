package net.ooici.util.msgpack.elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.ooici.util.msgpack.MessagePackException;
import net.ooici.util.msgpack.MessagePackType;

/**
 * An abstract class that provides the basic functionality for representing
 * message pack type-data and type-length-data. See the msgpack specification
 * {@link http://wiki.msgpack.org/display/MSGPACK/Format+specification} for
 * more information on the various type-data available.<br>
 * <br>
 * Basic functionality includes a general decoder, which can parse a valid
 * message pack formatted byte stream or array, and some support for encoding
 * an MPElement to a byte array or stream.  The specific encoding details are
 * left to each of the subclasses.<br>
 * <br> 
 * @author brianfox
 *
 */
public abstract class MPElement {
	
	public MPElement() {
	}
	

	/**
	 * Returns a byte array representation of this MPElement.
	 * 
	 * @return a byte array
	 * @throws MessagePackException thrown if the encoding fails
	 */
	public byte[] encode() 
	throws MessagePackException 
	{		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		encodeTo(dos);
		return bos.toByteArray();
	}
	

	/**
	 * Writes a byte array representation of this MPElement to 
	 * the DataOutputStream provided.  
	 * 
	 * @param out a previously constructed DataOutputStream
	 * @throws MessagePackException thrown in the encoding fails
	 */
	public abstract void encodeTo(DataOutputStream out) 
	throws MessagePackException;


	/**
	 * Decodes a MPElement from a given byte array.  This method
	 * will only process a single MPElement regardless of how many
	 * elements may be encoded in the byte array.  If more granular
	 * control is needed, use decodeFrom with a ByteArrayInputStream
	 * instead.
	 * 
	 * @param b
	 * @return
	 * @throws MessagePackException
	 */
	public static MPElement decode(byte[] b) 
	throws MessagePackException {
		ByteArrayInputStream bos = new ByteArrayInputStream(b);
		DataInputStream dos = new DataInputStream(bos);
		try {
			return decodeFrom(dos);
		} catch (IOException e) {
			throw new MessagePackException("Could not decode byte array: " + e.getMessage(), e);
		}
	}
	
	public static MPElement decodeFrom(DataInput in) 
	throws 
		MessagePackException,
		IOException
	{
		
		int op;
		try {
			op = in.readByte() & 0xFF;
			long opSize = 0;
			MessagePackType opType = MessagePackType.getType(op);
	
			switch (opType) {
				case POSITIVE_FIX_NUM:
					return new MPLong(op & 0x7F);
				case NEGATIVE_FIX_NUM:
					return new MPLong(- ((~(op | 0xFFE0) + 1) & 0xFF));
				case NIL:
					return new MPNull();
				case TRUE:
					return new MPBoolean(true);
				case FALSE:
					return new MPBoolean(false);
				case FIX_ARRAY:
					return new MPArray(in, op & 0x0F);
				case FIX_MAP:
					return new MPMap(op & 0x0F, in);
	
				case FIX_RAW:
					opSize = op & 0x1F;
					byte[] _fixraw = new byte[(int)opSize];
					in.readFully(_fixraw);
					return new MPRaw(_fixraw);
	
					
				case UINT_8:
					return new MPLong(in.readByte() & 0xFF);
				case INT_8:
					return new MPLong(in.readByte());
	
					
				case UINT_16:
					return new MPLong(toLong(false, in, 2));
				case INT_16:
					return new MPLong(toLong(true, in, 2));
	
				case RAW_16:
					opSize = (int)toLong(false, in, 2);
					byte[] _raw16 = new byte[(int)opSize];
					in.readFully(_raw16);
					return new MPRaw(_raw16);
				case ARRAY_16:
					opSize = (int)toLong(false, in, 2);
					return new MPArray(in, (int)opSize);
				case MAP_16:
					opSize = (int)toLong(false, in, 2);
					return new MPMap(opSize, in);
	
					
				case INT_32:
					long _int32 = toLong(true, in, 4);;
					return new MPLong((int)_int32);
				case UINT_32:
					// todo
					long _uint32 = toLong(false, in, 4);;
					return new MPLong(_uint32);
				case RAW_32:
					opSize = toLong(false, in, 4);;
					byte[] _raw32 = new byte[(int)opSize];
					in.readFully(_raw32);
					return new MPRaw(_raw32);
				case ARRAY_32:
					opSize = toLong(false, in, 4);;
					return new MPArray(in, (int)opSize);
				case MAP_32:
					opSize = toLong(false, in, 4);;
					return new MPMap(opSize, in);
	
				
				case UINT_64:
					long _uint64 = toLong(false, in, 8);;
					return new MPLong(_uint64);
				case INT_64:
					long _int64 = toLong(true, in, 8);;
					return new MPLong(_int64);
	
				case FLOAT:
					int _float = (int)toLong(false, in, 4);
					return new MPFloat(Float.intBitsToFloat(_float));
				case DOUBLE:
					long _double = toLong(false, in, 8);;
					return new MPDouble(Double.longBitsToDouble(_double));

					
				default:
					throw new MessagePackException(
							String.format("Unknown message pack encoding: value %02x", op)
					);
			}
		} catch (NegativeArraySizeException ne) {
			throw new MessagePackException("Received negative parameter size.", ne);
		}
	}


	private static long toLong(boolean signed, DataInput in, int count) 
	throws IOException {
		long ret = 0;
		byte next = in.readByte();
		ret = signed ? next : (next & 0xFF);
		for (int i=1; i < count; i++) {
			next = in.readByte();
			ret = ret << 8 | (next & 0xFF);
		}
		return ret;
	}
}