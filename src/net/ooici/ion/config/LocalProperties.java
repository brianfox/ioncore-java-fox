package net.ooici.ion.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


/**
 * A simple wrapper to the Java Properties class.  This class purposefully hides
 * the setter methods of the Properties class to make the class "more" immutable.
 * This class does not offer a dynamic configuration solution.  Rather, it's meant
 * for startup configurations.  LocalProperties also offers wildcard support and
 * built-in casting methods for extracting various basic types.<br>
 * <br>
 * This class is not thread safe.<br><br>
 * 
 * @author brianfox
 *
 */
public class LocalProperties {

	private Properties properties;
	
	public LocalProperties() {
		this.properties = new Properties();
	}
	
	public void load(String filename) throws FileNotFoundException, IOException {
		properties.load(new FileInputStream(filename));
	}
	
	public float getFloat(String key) throws PropertiesException {
		try {
			Object val = properties.get(key);
			if (val == null)
				throw new PropertiesException("No such key: " + key);
			return Float.parseFloat(properties.get(key).toString());
		} 
		catch (NumberFormatException e) {
			String msg = String.format("Could not parse float: %s", properties.get(key).toString());
			throw new PropertiesException(msg);
		}
	}

	public double getDouble(String key) throws PropertiesException {
		try {
			Object val = properties.get(key);
			if (val == null)
				throw new PropertiesException("No such key: " + key);
			return Double.parseDouble(properties.get(key).toString());
		} 
		catch (NumberFormatException e) {
			String msg = String.format("Could not parse float: %s", properties.get(key).toString());
			throw new PropertiesException(msg);
		}
	}

	public int getInt(String key) throws PropertiesException {
		return getInt(key, 10);
	}

	public int getInt(String key, int base) throws PropertiesException {
		try {
			Object val = properties.get(key);
			if (val == null)
				throw new PropertiesException("No such key: " + key);
			return Integer.parseInt(properties.get(key).toString(), base);
		} 
		catch (NumberFormatException e) {
			String msg = String.format("Could not parse float: %s", properties.get(key).toString());
			throw new PropertiesException(msg);
		}

	}

	
	public double getLong(String key) throws PropertiesException {
		return getLong(key, 10);
	}
		
	
	public double getLong(String key, int base) throws PropertiesException {
		try {
			return Long.parseLong(properties.get(key).toString(), base);
		} 
		catch (NumberFormatException e) {
			String msg = String.format("Could not parse float: %s", properties.get(key).toString());
			throw new PropertiesException(msg);
		}

	}

	public String getString(String key) throws PropertiesException {
		Object val = properties.get(key);
		if (val == null)
			throw new PropertiesException("No such key: " + key);
		return val.toString();
	}

	public LocalProperties getSection(String configSection) {
		LocalProperties ret = new LocalProperties();
		String[] desiredPieces = configSection.split("\\.");
		
		outer:
		for (Object key : properties.keySet()) {
			String finalKey = key.toString();
			String[] candidatePieces = key.toString().split("\\.");
			
			for (int i=0; i < candidatePieces.length && i < desiredPieces.length; i++) {
				if (candidatePieces[i].compareTo("*") == 0) {
					finalKey = finalKey.replaceFirst("\\*", desiredPieces[i]);
					continue;
				}
				if (candidatePieces[i].compareTo(desiredPieces[i]) != 0)
					continue outer;
			}
			try {
				ret.properties.put(finalKey.substring(configSection.length() + 1), properties.get(key));
			}
			catch (StringIndexOutOfBoundsException e) {
				ret.properties.put(finalKey, properties.get(key));
			}
		}
		return ret;
	}


	public TimeUnit getTimeUnit(String key) throws PropertiesException {
		Object val = properties.get(key);
		if (val == null)
			throw new PropertiesException("No such key: " + key);
		try {
			return TimeUnit.valueOf(key);
		}
		catch (Exception e) {
			throw new PropertiesException("could not parse a TimeUnit: " + val.toString());
		}
	}

	
	public boolean containsKey(String string) {
		return properties.containsKey(string);
	}
	
	@Override
	public String toString() {
		if (properties.isEmpty())
			return "<empty>";
		ArrayList<String> list = new ArrayList<String>();
		for (Object o : properties.keySet()) {
			list.add(o.toString());
		}
		Collections.sort(list);
		StringBuilder sb = new StringBuilder();
		for (String s : list)
			sb.append(String.format("Key: [%-8s] %-80s   Val: [%-8s] %-50s%n",s.getClass().getSimpleName(), s, properties.get(s).getClass().getSimpleName(), properties.get(s)));
		return sb.toString();
	}

	public String oneLiner() {
		if (properties.isEmpty())
			return "<empty>";
		ArrayList<String> list = new ArrayList<String>();
		for (Object o : properties.keySet()) {
			list.add(o.toString());
		}
		Collections.sort(list);
		StringBuilder sb = new StringBuilder();
		for (String s : list)
			sb.append(String.format("%s=%s  ", s, properties.get(s)));
		return sb.toString();
	}

}
