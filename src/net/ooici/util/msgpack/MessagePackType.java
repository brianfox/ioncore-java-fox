package net.ooici.util.msgpack;

/**
 * See http://wiki.msgpack.org/display/MSGPACK/Format+specification for
 * a complete explanation of this code.
 * 
 * @author brianfox
 *
 */

public enum MessagePackType {

	POSITIVE_FIX_NUM,
	NEGATIVE_FIX_NUM,
	VARIABLE,
	NIL,
	RESERVED_1,
	FALSE,
	TRUE,
	RESERVED_2,
	RESERVED_3,
	RESERVED_4,
	RESERVED_5,
	RESERVED_6,
	RESERVED_7,
	FLOAT,
	DOUBLE,
	UINT_8,
	UINT_16,
	UINT_32,
	UINT_64,
	INT_8,
	INT_16,
	INT_32,
	INT_64,
	RESERVED_8,
	RESERVED_9,
	RESERVED_10,
	RESERVED_11,
	RESERVED_12,
	RESERVED_13,
	RAW_16,
	RAW_32,
	ARRAY_16,
	ARRAY_32,
	MAP_16,
	MAP_32,
	FIX_RAW,
	FIX_ARRAY,
	FIX_MAP,
	UNDEFINED;
	
	public static MessagePackType getType(int unsigned) {
		
		if (unsigned < 0)
			unsigned = unsigned & 0xFF;
		if (((unsigned ^ 0x00) & 0x80 )== 0) 
			return POSITIVE_FIX_NUM;
		if (((unsigned ^ 0xE0) & 0xE0 )== 0) 
			return NEGATIVE_FIX_NUM;
		
		if ((unsigned ^ 0xC0) == 0)
			return NIL;
		if ((unsigned ^ 0xC1) == 0)
			return RESERVED_1;
		if ((unsigned ^ 0xC2) == 0)
			return FALSE;
		if ((unsigned ^ 0xC3) == 0)
			return TRUE;
		if ((unsigned ^ 0xC4) == 0)
			return RESERVED_2;
		if ((unsigned ^ 0xC5) == 0)
			return RESERVED_3;
		if ((unsigned ^ 0xC6) == 0)
			return RESERVED_4;
		if ((unsigned ^ 0xC7) == 0)
			return RESERVED_5;
		if ((unsigned ^ 0xC8) == 0)
			return RESERVED_6;
		if ((unsigned ^ 0xC9) == 0)
			return RESERVED_7;
		if ((unsigned ^ 0xCA) == 0)
			return FLOAT;
		if ((unsigned ^ 0xCB) == 0)
			return DOUBLE;
		if ((unsigned ^ 0xCC) == 0)
			return UINT_8;
		if ((unsigned ^ 0xCD) == 0)
			return UINT_16;
		if ((unsigned ^ 0xCE) == 0)
			return UINT_32;
		if ((unsigned ^ 0xCF) == 0)
			return UINT_64;

		if ((unsigned ^ 0xD0) == 0)
			return INT_8;
		if ((unsigned ^ 0xD1) == 0)
			return INT_16;
		if ((unsigned ^ 0xD2) == 0)
			return INT_32;
		if ((unsigned ^ 0xD3) == 0)
			return INT_64;
		
		
		if ((unsigned ^ 0xD4) == 0)
			return RESERVED_8;
		if ((unsigned ^ 0xD5) == 0)
			return RESERVED_9;
		if ((unsigned ^ 0xD6) == 0)
			return RESERVED_10;
		if ((unsigned ^ 0xD7) == 0)
			return RESERVED_11;
		if ((unsigned ^ 0xD8) == 0)
			return RESERVED_12;
		if ((unsigned ^ 0xD9) == 0)
			return RESERVED_13;

		if ((unsigned ^ 0xDA) == 0)
			return RAW_16;
		if ((unsigned ^ 0xDB) == 0)
			return RAW_32;
		if ((unsigned ^ 0xDC) == 0)
			return ARRAY_16;
		if ((unsigned ^ 0xDD) == 0)
			return ARRAY_32;
		if ((unsigned ^ 0xDE) == 0)
			return MAP_16;
		if ((unsigned ^ 0xDF) == 0)
			return MAP_32;
		
		if (((unsigned ^ 0xA0) & 0xE0 )== 0)
			return FIX_RAW;
		if (((unsigned ^ 0x90) & 0xF0 )== 0) 
			return FIX_ARRAY;
		if (((unsigned ^ 0x80) & 0xF0 )== 0) 
			return FIX_MAP;
		
		return UNDEFINED;
	}
	
}
