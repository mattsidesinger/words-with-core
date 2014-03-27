package com.wordswithcheats.board.multiplier;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

/**
 * A container for {@link Multiplier} objects that stores location and can be used during a scoring operation.
 * 
 * @author Matt Sidesinger
 */
public class Multipliers implements Serializable {
	
	private Map<String, Multiplier> multipliers;
	private static final char KEY_DELIM = ',';
	
	private static final long serialVersionUID = -742988560609148642L;
	
	public Multipliers() {
		multipliers = new HashMap<String, Multiplier>(60);
	}
	
	/**
	 * @return	<code>true</code> if no multipliers have yet been added.
	 */
	public boolean isEmpty() {
		return multipliers.isEmpty();
	}
	
	/**
	 * Adds a Multiplier at the given x, y position.
	 * 
	 * @param x	The x coordinate
	 * @param y	The y coordinate
	 * @param m	The Multipliers to add
	 * 
	 * @return	this Multipliers instance
	 */
	public Multipliers add(int x, int y, Multiplier m) {
		
		Validate.isTrue(x > 0, "x coordinate my be greater than equal to 1");
		Validate.isTrue(y > 0, "y coordinate my be greater than equal to 1");
		Validate.notNull(m, "Multiplier cannot be null");
		
		multipliers.put(key(x, y), m);
		
		return this;
	}
	
	/**
	 * Adds a {@link LetterMultiplier} with a value of 2 at the given x, y position.
	 * 
	 * @param x	The x coordinate
	 * @param y	The y coordinate
	 * 
	 * @return	this Multipliers instance
	 */
	public Multipliers addDoubleLetterScore(int x, int y) {
		return add(x, y, LetterMultiplier.instance(2));
	}
	
	/**
	 * Adds a {@link LetterMultiplier} with a value of 3 at the given x, y position.
	 * 
	 * @param x	The x coordinate
	 * @param y	The y coordinate
	 * 
	 * @return	this Multipliers instance
	 */
	public Multipliers addTripleLetterScore(int x, int y) {
		return add(x, y, LetterMultiplier.instance(3));
	}
	
	/**
	 * Adds a {@link WordMultiplier} with a value of 2 at the given x, y position.
	 * 
	 * @param x	The x coordinate
	 * @param y	The y coordinate
	 * 
	 * @return	this Multipliers instance
	 */
	public Multipliers addDoubleWordScore(int x, int y) {
		return add(x, y, WordMultiplier.instance(2));
	}
	
	/**
	 * Adds a {@link WordMultiplier} with a value of 3 at the given x, y position.
	 * 
	 * @param x	The x coordinate
	 * @param y	The y coordinate
	 * 
	 * @return	this Multipliers instance
	 */
	public Multipliers addTripleWordScore(int x, int y) {
		return add(x, y, WordMultiplier.instance(3));
	}
	
	/**
	 * Obtains the {@link Multiplier} at the given x, y position.
	 * 
	 * @param x	The x coordinate
	 * @param y	The y coordinate
	 * 
	 * @return	The multiplier at the given position or <code>null</code> if a multiplier does not exist
	 * 			at the given position.
	 */
	public Multiplier get(int x, int y) {
		return multipliers.get(key(x, y));
	}
	
	/**
	 * Generates the key that is used to store the Multiplier
	 * 
	 * @param x	The x coordinate to use in the key.
	 * @param y	The y coordinate to use in the key.
	 */
	protected String key(int x, int y) {
		return new StringBuilder().append(x).append(KEY_DELIM).append(y).toString();
	}

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		
		// sort the multipliers by key
		SortedSet<String> sortedKeys = new TreeSet<String>(new KeyComparator());
		sortedKeys.addAll(this.multipliers.keySet());
		
		Multiplier m = null;
		int i = 0;
		int numKeys = sortedKeys.size();
		for (String key : sortedKeys) {
			
			m = this.multipliers.get(key);
			
			sb.append('(');
			sb.append(key);
			sb.append(':');
			sb.append(m.toString());
			sb.append(')');
			
			i++;
			if (i != numKeys) {
				sb.append(',');
			}
		}
		
		sb.append('}');
		return sb.toString();
	}
	
	/**
	 * This Comparator is used to compare keys formatted as two integers
	 * separated by a '{@value Multipliers#KEY_DELIM}' character.
	 * 
	 * @author Matt Sidesinger
	 */
	private static class KeyComparator implements Comparator<String>, Serializable {

		private static final long serialVersionUID = -4752549489342717956L;

		@Override
		public int compare(final String key1, final String key2) {
			
			int result = 0;
			
			int x1, y1 = 0;
			int x2, y2 = 0;
			
			String[] x1y1 = StringUtils.split(key1, KEY_DELIM);
			String[] x2y2 = StringUtils.split(key2, KEY_DELIM);
			
			x1 = Integer.parseInt(x1y1[0]);
			y1 = Integer.parseInt(x1y1[1]);
			x2 = Integer.parseInt(x2y2[0]);
			y2 = Integer.parseInt(x2y2[1]);
			
			if (x1 == x2) {
				result = y1 - y2;
			} else {
				result = x1 - x2;
			}
			
			return result;
		}		
	}
}
