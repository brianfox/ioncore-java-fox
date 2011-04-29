package net.ooici.util.string;

/**
 * Provides hexadecimal static utility methods.
 * 
 * @author brianfox
 *
 */
public class PrettyPrint {

	
	/**
	 * Determines if a byte is printable, specifically, within the range
	 * of hex 20 and hex 7E (inclusive).  Other characters such as tab, 
	 * carriage return, and line feed are excluded from the range because
	 * of their potential to be problematic in fixed column displays.
	 * 
	 * @param b the byte value to check
	 * @return true is the byte is printable
	 */
	public static boolean isPrintable(byte b) {
		return (b >= 0x20 && b <= 0x7E); //  || b == 0x09 || b == 0x0A || b == 0x0D;
	}
	


	/** 
	 * Creates a BSD hexdump(1) style multi-line multi-column display from
	 * the byte array provided.  The first column in the display shows the
	 * current row/first column offset in the file.  The next column shows
	 * the hex values of the next 16 bytes in the file.  The last row shows
	 * each of the hex values displayed in ASCII or a period if it the byte
	 * is not printable.
	 *  
	 * @param raw
	 * @return
	 */
	public static String hexDump(byte[] raw) {
		if (raw == null)
			return "null";
		if (raw.length == 0)
			return "empty array";
		
		StringBuilder all = new StringBuilder();
		StringBuilder col1 = new StringBuilder();
		StringBuilder col2 = new StringBuilder();

		int count = 0;
		for (byte b : raw) {
			if (count > 0 && count % 16 == 0) {
				all.append(String.format("%05x     %-48s   %-16s%n", count - 16, col1, col2));
				col1 = new StringBuilder();
				col2 = new StringBuilder();
			}
			col1.append(String.format("%02x ", b));
			col2.append(b > 20 && b < 128 ? (char)b : ".");
			count++;
		}
		if (col1.length() > 0) {
			all.append(String.format("%05x     %-48s   %-16s", count - (count%16), col1, col2));
		}
		return all.toString();
	}


	
	public static String pad(String s, int pad) {
		String p = String.format("%" + Integer.toString(pad) + "s", " ");
		StringBuilder sb = new StringBuilder();
		for (String next : s.split("\n"))
			sb.append(p + next + "\n");
		return sb.toString();
			
	}
	
	

	/**
	 * Produces a quick and dirty hex dump of a SHA1 byte array.
	 * 
	 * @param byteArray
	 * @return a String
	 */
	public static String shaDump(byte[] byteArray) {
		StringBuilder all = new StringBuilder();
		for (byte b : byteArray) {
			all.append(String.format("%02x", b));
		}
		return all.toString();
	}



	public static String hexDumpSimple(byte[] byteArray) {
		StringBuilder all = new StringBuilder();
		for (byte b : byteArray) {
			all.append(String.format("%02x", b));
		}
		return all.toString();
	}
}
