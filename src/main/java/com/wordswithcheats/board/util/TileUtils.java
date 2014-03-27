package com.wordswithcheats.board.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wordswithcheats.board.Tile;

/**
 * Contains methods that act on {@link Tile} objects, such as coversion.
 * 
 * @author Matt Sidesinger
 */
public final class TileUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(TileUtils.class);
	
	/**
	 * Converts the two dimensional {@link Tile} array to the equivalent two dimensional character array.
	 * <p>
	 * Each <code>Tile</code> is converted to a char, by calling {@link Tile#toChar()}. If a index in the
	 * <code>Tile</code> array is null, the index at the char array is set to ' ' (space). 
	 * 
	 * @param tiles	The two dimensional array to convert.
	 * 
	 * @return	The equivalent two dimensional char array.
	 */
	public static char[][] toCharArray(final Tile[][] tiles) {
		
		char[][] chars = null;
		
		if (tiles != null) {
			chars = new char[tiles.length][];
			for (int i = 0; i < tiles.length; i++) {
				chars[i] = new char[tiles[i].length];
				for (int j = 0; j < tiles[i].length; j++) {
					
					if (tiles[i][j] != null) {
						// use toChar(), not getLetter()
						chars[i][j] = tiles[i][j].toChar(); 
					} else {
						chars[i][j] = ' ';
					}
					
				}
			}
		}
		
		return chars;
	}
	
	/**
	 * Compares two Tiles, returning <code>true</code> if they are equal.
	 * <p>
	 * <code>nulls</code> are handled without exceptions. Two <code>null</code> references are considered to be equal.
	 * 
	 * @param t1	the first Tile, may be null
	 * @param t2	the second Tile, may be null
	 * 
	 * @return	<code>true</code> if the Tiles are equal, <code>false</code> otherwise
	 */
	public static boolean equals(final Tile t1, final Tile t2) {
		
		boolean equal = true;
		
		if (t1 != null || t2 != null) {
			if (t1 == null || t2 == null) {
				equal = false;
			} else {
				equal = t1.equals(t2);
			}
		}
		
		return equal;
	}
	
	/**
	 * Compares a Tile to a char, returning <code>true</code> if {@link Tile#toChar()} is equal to char.
	 * <p>
	 * A <code>null</code> Tile is considered to be equal to char 0 and ' ' (a space).
	 * 
	 * @param t	the Tile to compare, may be null
	 * @param c	the char to compare
	 * 
	 * @return	<code>true</code> if the Tile is considered equal to the char, otherwise <code>false</code>
	 */
	public static boolean equals(final Tile t, final char c) {
		
		boolean equal = true;
		
		if (t == null) {
			equal = (c == 0 || c == ' ');
		} else {
			equal = t.toChar() == c;
		}
		
		return equal;
	}
	
	/**
	 * Compares two dimensional {@link Tile} arrays, handling <code>nulls</code> without exception.
	 * <p>
	 * The arrays are considered equal if they are both <code>null</code>, or they are the same length (both
	 * dimensions) and the Tile objects at each index are equal.  Tile comparisons are done with
	 * {@link #equals(Tile, Tile)}. 
	 * 
	 * @param tiles1	the first Tile array, may be null
	 * @param tiles2	the second Tile array, may be null
	 * 
	 * @return	<code>true</code> if the arrays are equal, otherwise <code>false</code>
	 */
	public static boolean equals(final Tile[][] tiles1, final Tile[][] tiles2) {
		
		boolean equal = true;
		
		if (tiles1 != null || tiles2 != null) {
    		if (tiles1 == null || tiles2 == null) {
    			equal = false;
    		} else {
    			
    			if (tiles1.length != tiles2.length) {
    				equal = false;
    			} else {
    			
    				outer:
        			for (int i = 0; i < tiles1.length; i++) {
        				
        				if (tiles1[i].length != tiles2[i].length) {
        					equal = false;
        					break;
        				}
        				
        				// inner:
        				for (int j = 0; j < tiles1[i].length; j++) {
        					if (!equals(tiles1[i][j], tiles2[i][j])) {
        						equal = false;
        						logger.info("Tiles are not equal. Index is: {},{}. Tile 1: {} Tile 2: {}",
        								new Object[] {
        									Integer.valueOf(i), Integer.valueOf(j),
        									tiles1[i][j], tiles2[i][j]
        								}
        						);
        						break outer;
        					}
        				}
        			} // ~for
    			}
    			
    		}
		}
		
		return equal;
	}
	
	/**
	 * Compares a two dimensional {@link Tile} array to a two dimensional char array, handling
	 * <code>nulls</code> without exception.
	 * <p>
	 * The arrays are considered equal if they are both <code>null</code>, or they are the same length (both
	 * dimensions) and the Tile object at each index is equal to the char at the same index in the char array.
	 * Comparisons are done with {@link #equals(Tile, char)}. 
	 * 
	 * @param tiles	the Tile array, may be null
	 * @param chars	the char array, may be null
	 * 
	 * @return	<code>true</code> if the arrays are considered equal, otherwise <code>false</code>
	 */
	public static boolean equals(final Tile[][] tiles, final char[][] chars) {
		
		boolean equal = true;
		
		if (tiles != null || chars != null) {
    		if (tiles == null || chars == null) {
    			equal = false;
    		} else {
    			
    			if (tiles.length != chars.length) {
    				equal = false;
    			} else {
    			
    				outer:
        			for (int i = 0; i < tiles.length; i++) {
        				
        				if (tiles[i].length != chars[i].length) {
        					equal = false;
        					break;
        				}
        				
        				// inner:
        				for (int j = 0; j < tiles[i].length; j++) {
        					if (!equals(tiles[i][j], chars[i][j])) {
        						equal = false;
        						logger.info("Tile is not equal to char. Index is: {},{}. Tile: {} char: {}",
        								new Object[] {
        									Integer.valueOf(i), Integer.valueOf(j),
        									tiles[i][j], Character.valueOf(chars[i][j])
        								}
        						);
        						break outer;
        					}
        				}
        			} // ~for
    			}
    			
    		}
		}
		
		return equal;
	}
}
