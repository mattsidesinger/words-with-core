package com.wordswithcheats.board.multiplier;

import com.wordswithcheats.board.Tile;

/**
 * A multiplier to be applied to a group of {@link Tile} objects (a word) during scoring, such as a
 * "double word score" or a "triple word score." 
 * 
 * @author Matt Sidesinger
 */
public class WordMultiplier extends Multiplier {

	private static WordMultiplier DOUBLE_WORD_SCORE;
	private static WordMultiplier TRIPLE_WORD_SCORE;
	static {
		DOUBLE_WORD_SCORE = new WordMultiplier(2);
		TRIPLE_WORD_SCORE = new WordMultiplier(3);
	}

	private static final long serialVersionUID = -3244872899536759581L;
	
	protected WordMultiplier(final int value) {
		super(value);
	}
	
	/**
	 * Obtains an instance of a WordMultiplier as the constructor is private.
	 *  
	 * @param value		The value to be applied to the Multiplier.
	 * 
	 * @return	A WordMultiplier with the given value. 
	 */
	public static WordMultiplier instance(final int value) {
		WordMultiplier instance = null;
		if (value == 2) {
			instance = DOUBLE_WORD_SCORE;
		} else if (value == 3) {
			instance = TRIPLE_WORD_SCORE;
		} else {
			instance = new WordMultiplier(value);
		}
		return instance;
	}
	
	/**
	 * If value is 2, then "DW"<br/>
	 * If value is 3, then "TW"<br/>
	 * Otherwise, "WORDx" + value
	 */
	@Override
	public String toString() {
		
		String s = null;
		
		int value = getValue();
		if (value == 2) {
			s = "DW";
		} else if (value == 3) {
			s = "TW";
		} else {
			s =  new StringBuilder(Integer.toString(value)).append('W').toString();
		}
		
		return s;
	}
}
