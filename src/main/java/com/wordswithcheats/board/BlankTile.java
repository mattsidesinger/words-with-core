package com.wordswithcheats.board;


/**
 * A blank tile is a tile that is worth 0 points.  When in the tile is played or loaded on a {@link Board},
 * it must have a set letter.  A letter can be set through the {@link #BlankTile(char)} constructor or the static
 * {@link #setLetter(BlankTile, char)} method.
 * 
 * @author Matt Sidesinger
 */
public class BlankTile extends Tile implements Cloneable {
	
	private static final long serialVersionUID = -4228072126023170830L;

	/**
	 * Creates a blank tile that does not have a letter chosen yet.
	 */	
	public BlankTile() {
	}
	
	/**
	 * Creates a <code>Tile</code> with the given letter, but the tile is backed by a blank tile,
	 * therefore the tile is worth 0 points.
	 * 
	 * @param letter	The letter that this blank tile will represent.
	 */
	public BlankTile(final char letter) {
		super(letter);
	}

	/**
	 * Always returns <code>true</code> for BlankTile instances.
	 */
	@Override
	public boolean isBlankTile() {
		return true;
	}
	
	/**
	 * @return A blank tile will always return <code>0</code>
	 */
	@Override
	public int getPointValue() {
		return 0;
	}
	
	/**
	 * Sets the letter that the given blank tile will back.  It may be a value a-z or A-Z.
	 * <p>
	 * This method prevents having to instantiate a new blank tile using {@link #BlankTile(char)}.
	 * 
	 * @param tile		The blank tile to set the letter for
	 * @param letter	A <code>char</code> value a-z or A-Z
	 * 
	 * @throws IllegalArgumentException	If the <code>letter</code> is not a value a-z or A-Z
	 */
	public static void setLetter(final BlankTile tile, final char letter) {
		tile.setLetter(letter);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		BlankTile clone = null;
		if (getLetter() == 0) {
			clone = new BlankTile();
		} else {
			clone = new BlankTile(getLetter());
		}
		return clone;
	}
	
	/**
	 * Returns the letter as a char, but always the lower-case equivalent.
	 * 
	 * @return	The letter, lower-cased
	 */
	@Override
	public char toChar() {
		return getLetter();
	}
	
	/**
	 * Returns the letter and point value separated by a colon.
	 * <p>
	 * Example: "a:1"
	 * <p>
	 * In the case of a blank tile that does not yet have a letter chosen, the format
	 * will be as follows: [ :0]
	 */
	@Override
	public String toString() {
		if (getLetter() == 0) {
			return "[ :0]";
		}
		return super.toString();
	}
}
