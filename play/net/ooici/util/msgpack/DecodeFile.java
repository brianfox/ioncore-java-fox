package net.ooici.util.msgpack;


/**
 * Our serialization sucks.  Attemps to reverse engineer it are below.
 * Note that this code isn't meant to be pretty.
 * 
 */
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import net.ooici.util.msgpack.MessagePack;
import net.ooici.util.msgpack.MessagePackException;
import net.ooici.util.msgpack.elements.MPElement;


public class DecodeFile {
	
    public static void main(String[] args) throws IOException, MessagePackException {
    	FileInputStream fis = new FileInputStream("/Users/brianfox/test.out");
    	DataInputStream dis = new DataInputStream(fis);
    	int count = 0;
    	while (true) {
	    	MPElement e = MessagePack.decodeFrom(dis);
	    	if (e == null)
	    		break;
	   		System.out.println(e.getClass().getSimpleName() + "   " + e.toString()) ;
	   		count++;
    	}
    	System.out.println("COUNT: " + count);
    	
    }
}

