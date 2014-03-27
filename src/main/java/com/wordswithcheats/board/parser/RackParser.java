package com.wordswithcheats.board.parser;

import org.apache.commons.lang.Validate;

import com.wordswithcheats.board.BlankTile;
import com.wordswithcheats.board.Rack;
import com.wordswithcheats.board.Tile;
import com.wordswithcheats.board.exception.RackFullException;
import com.wordswithcheats.board.parser.exception.RackParseException;

/**
 * Parses a String and creates a {@link Rack}.  See the {@link #parse(String)} method.
 * 
 * @author Matt Sidesinger
 */
public class RackParser {
	
	private int rackSize = 0;
	
	/**
	 * The character, that when encountered, will create a {@link BlankTile} and add it to the {@link Rack}.
	 */
	public static final char BLANK_TILE_CHAR = '*';
	
	/**
	 * Creates a parser, which creates {@link Rack} objects with the default rack size of {@value Rack#DEFAULT_SIZE}.
	 */
	public RackParser() {
		setRackSize(Rack.DEFAULT_SIZE);
	}

	/**
	 * Creates a parser, which creates {@link Rack} objects with the specified size.
	 */
	public RackParser(int rackSize) {
		setRackSize(rackSize);
	}
	
	/**
	 * Parses the given String and creates a rack containing tiles for each of the letters in the String.
	 * A '{@value #BLANK_TILE_CHAR}' indicates a blank tile.
	 * 
	 * @param rackString	The String to parse for letters
	 * 
	 * @return	A Rack with the given letters.
	 */
	public Rack parse(final String rackString) throws RackParseException {
		
		String trimmed = rackString.trim();
		if (trimmed.length() > Rack.DEFAULT_SIZE) {
			throw new IllegalArgumentException("The String cannot be longer than 7 characters");
		}
		
		Rack rack = new Rack(getRackSize());
		
		try {
			char[] letters = rackString.toCharArray();
			for (int i = 0; i < letters.length; i++) {
				if (letters[i] == BLANK_TILE_CHAR) {
					rack.add(new BlankTile());
				} else {
					Tile tile = Tile.valueOf(letters[i]);
					if (tile == null) {
						throw new RackParseException("valid rack characters are A-Z, a-z, and " + BLANK_TILE_CHAR);
					}
					rack.add(tile);
				}
			}
		} catch (RackFullException e) {
			// ignore
		}
		
		return rack;
	}

	/**
	 * @return the rackSize
	 */
	public int getRackSize() {	
		return rackSize;
	}

	/**
	 * @param rackSize the rackSize to set
	 */
	public void setRackSize(int rackSize) {
		Validate.isTrue(rackSize > 0, "size must be an integer greater than 0");
		this.rackSize = rackSize;
	}
}