package net.ooici.util.msgpack.elements;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

import net.ooici.util.msgpack.MessagePackException;
import net.ooici.util.msgpack.elements.MPElement;
import net.ooici.util.string.HexPrettyPrint;

/**
 * Supports the "raw bytes" types in the MessagePack specification.  This
 * includes fix raw, raw16 and raw32.  Used in its simpler manner, MPRaw
 * provides a way to send and receive byte arrays.<br>
 * <br>
 * Some MessagePack implementations use the raw type to represent Strings.  
 * Unfortunately, there is no formal encoding procedures.  For example, 
 * Python's msgpack module does not specify whether the string is encoding 
 * in ASCII or unicode.  (It's actually UTF-8 by default, but this is 
 * unspecified and not guaranteed on all platforms).  MPRaw provides a set
 * of convenience methods to "guess" what encoding was used when treating
 * byte arrays as text.
 *
 * @author brianfox
 *
 */
public class MPRaw extends MPElement {
	
	/*
	 * MPRaw supports the String class, which is otherwise unsupported in
	 * the MessagePack protocol.  For proper String support, both remote and
	 * local platforms must agree on a common character encoding.  This is 
	 * complicated by platform support of all character encodings.<br>
	 * <br>
	 * The PREFERRED_CHARSET variable is available to help different 
	 * platforms resolve an acceptable character set encoding.  This array 
	 * is sorted in order of preference with the first supported character
	 * set being set to the default character set.<br>
	 * <br>
	 * The encoding choices prefer UTF-8, which is an overlapping superset 
	 * of ASCII, as a first choice.  ASCII follows.  Finally, Latin is
	 * considered although it is not readily compatible with the prior
	 * encodings.<br>
	 * <br>
	 * See IANA character set assignments and preferred MIME name for an 
	 * explanation of the Strings below: 
	 * http://www.iana.org/assignments/character-sets<br>
	 * <br>
	 * An alternative strategy is to define an explicit MPString class with
	 * a Charset marker.  Unfortunately, this would confuse the protocol and
	 * would only be 
	 */
	public static final String[] PREFERRED_CHARSET = { 
		"UTF-8", 
		"ISO-8859-1", 
		"US-ASCII" 
	};
	
	public static final int MAX_MPRAW_SIZE = 256*1024*1024;
	private static String defaultCharset = null; 

	static {
		for (String s : PREFERRED_CHARSET) {
			if (Charset.isSupported(s)) {
				defaultCharset = s;
				break;
			}
		}
	}

	public static void setCharset(String charsetName) throws Exception {
		if (Charset.isSupported(charsetName))
			defaultCharset = charsetName;
		else
			throw new MessagePackException("Character set is not supported: " + charsetName);
	}

	
	
	private byte[] raw;

	public MPRaw(byte[] raw) throws MessagePackException  {
		this.raw = raw;
	}

	public MPRaw(byte[] raw, int start, int len) throws MessagePackException  {
		try {
			if (len > MAX_MPRAW_SIZE)
				throw new IllegalArgumentException(String.format("MPRaw size exceeds limit.  Size: %d Limit: %d", len, MAX_MPRAW_SIZE));
			this.raw = Arrays.copyOfRange(raw, start, start + len);
		}
		catch (IllegalArgumentException e) {
			throw new MessagePackException(
					String.format(
							"Cannot create a element: byte[] length=%d  start=%d  len=%d",
							raw.length, start, len)
			);
			
		}
	}

	public MPRaw(String s) throws MessagePackException {
		try {
			this.raw = s.getBytes(defaultCharset);
		} catch (UnsupportedEncodingException e) {
			throw new MessagePackException("Charset not supported: " + defaultCharset);
		}
	}
	
	

	@Override
	public String toString() {
		try {
			return new String(raw, defaultCharset);
		} catch (UnsupportedEncodingException e) {
			return String.format("UNSUPPORTED CHARSET%nHEX DUMP:%n%s%n", HexPrettyPrint.hexDump(raw));
		}
	}
	

	public boolean isPrintable() {
		for (byte b : raw)
			if (!HexPrettyPrint.isPrintable(b))
				return false;
		return true;
	}
	
	
	public byte[] getBytes() {
		return Arrays.copyOf(raw, raw.length);
	}

	
	@Override
	public void encodeTo(DataOutputStream out) 
	throws MessagePackException 
	{
		try {
			if (raw.length < 32) {
				out.writeByte((byte)((0xA0 | raw.length) & 0xFF));
			}
			else if (raw.length < 0x10000) {
				out.writeByte((byte)0xDA);
				out.writeByte((byte)((raw.length >> 8) & 0xFF));
				out.writeByte((byte)((raw.length) & 0xFF));
			}		
			else {
				out.writeByte((byte)0xDB);
				out.writeByte((byte)((raw.length >> 24) & 0xFF));
				out.writeByte((byte)((raw.length >> 16) & 0xFF));
				out.writeByte((byte)((raw.length >> 8) & 0xFF));
				out.writeByte((byte)((raw.length) & 0xFF));
			}		
			for (byte b : raw)
				out.writeByte(b);
		}
		catch (IOException io) {
			throw new MessagePackException(
					"Could not write to DataOutputStream: " 
					+ io.getMessage(), io
			);
		}
	}
}

