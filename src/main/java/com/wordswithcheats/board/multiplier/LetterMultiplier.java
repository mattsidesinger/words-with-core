package com.wordswithcheats.board.multiplier;

import com.wordswithcheats.board.Tile;

/**
 * A multiplier to be applied to only one {@link Tile} during scoring, such as a
 * "double letter score" or a "triple letter score." 
 * 
 * @author Matt Sidesinger
 */
public class LetterMultiplier extends Multiplier {

	private static final LetterMultiplier DOUBLE_LETTER_SCORE;
	private static final LetterMultiplier TRIPLE_LETTER_SCORE;
	static {
		DOUBLE_LETTER_SCORE = new LetterMultiplier(2);
		TRIPLE_LETTER_SCORE = new LetterMultiplier(3);
	}
	
	private static final long serialVersionUID = 1492437284358700873L;
	
	protected LetterMultiplier(final int value) {
		super(value);
	}
	
	/**
	 * Obtains an instance of a LetterMultiplier as the constructor is private.
	 *  
	 * @param value		The value to be applied to the Multiplier.
	 * 
	 * @return	A LetterMultiplier with the given value. 
	 */
	public static LetterMultiplier instance(final int value) {
		LetterMultiplier instance = null;
		if (value == 2) {
			instance = DOUBLE_LETTER_SCORE;
		} else if (value == 3) {
			instance = TRIPLE_LETTER_SCORE;
		} else {
			instance = new LetterMultiplier(value);
		}
		return instance;
	}
	
	/**
	 * If value is 2, then "DL"<br/>
	 * If value is 3, then "TL"<br/>
	 * Otherwise, "LETTERx" + value
	 */
	@Override
	public String toString() {
		
		String s = null;
		
		int value = getValue();
		if (value == 2) {
			s = "DL";
		} else if (value == 3) {
			s = "TL";
		} else {
			s =  new StringBuilder(Integer.toString(value)).append('L').toString();
		}
		
		return s;
	}
}