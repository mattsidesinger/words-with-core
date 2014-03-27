package com.wordswithcheats.board;

import java.io.Serializable;

import org.apache.commons.lang.Validate;

/**
 * A Tile is an object that represents a letter and its associated point value.  A letters are stored as lower case.
 * <p>
 * Unless a unique object is needed, the {@link #valueOf(char)} method should be used over the {@link #Tile(char)}
 * constructor.
 * <p>
 * Default point values are as follows:
 * <ul>
 * 	<li><strong>a</strong>: 1</li>
 * 	<li><strong>b</strong>: 3</li>
 * 	<li><strong>c</strong>: 3</li>
 * 	<li><strong>d</strong>: 2</li>
 * 	<li><strong>e</strong>: 1</li>
 * 	<li><strong>f</strong>: 4</li>
 * 	<li><strong>g</strong>: 2</li>
 * 	<li><strong>h</strong>: 4</li>
 * 	<li><strong>i</strong>: 1</li>
 * 	<li><strong>j</strong>: 8</li>
 * 	<li><strong>k</strong>: 5</li>
 * 	<li><strong>l</strong>: 1</li>
 * 	<li><strong>m</strong>: 3</li>
 * 	<li><strong>n</strong>: 1</li>
 * 	<li><strong>o</strong>: 1</li>
 * 	<li><strong>p</strong>: 3</li>
 * 	<li><strong>q</strong>: 10</li>
 * 	<li><strong>r</strong>: 1</li>
 * 	<li><strong>s</strong>: 1</li>
 * 	<li><strong>t</strong>: 1</li>
 * 	<li><strong>u</strong>: 1</li>
 * 	<li><strong>v</strong>: 4</li>
 * 	<li><strong>w</strong>: 4</li>
 * 	<li><strong>x</strong>: 8</li>
 * 	<li><strong>y</strong>: 4</li>
 * 	<li><strong>z</strong>: 10</li>
 * </ul>
 * <p>
 * Point values can be updated by calling {@link #setPointValue(char, int)}.
 * 
 * @author Matt Sidesinger
 */
public class Tile implements Cloneable, Comparable<Tile>, Serializable {
	
	private char letter = 0;
	
	private static final long serialVersionUID = 3849118518558827873L;
	
	// http://www.thekatespanos.com/scrabble-score-calculator/
	private static final int[] POINTS = new int[] {
		1 /* a */, 3 /* b */, 3 /* c */, 2 /* d */, 1 /* e */, 4 /* f */, 2 /* g */, 4 /* h */,
		1 /* i */, 8 /* j */, 5 /* k */, 1 /* l */, 3 /* m */, 1 /* n */, 1 /* o */, 3 /* p */,
		10/* q */, 1 /* r */, 1 /* s */, 1 /* t */, 1 /* u */, 4 /* v */, 4 /* w */, 8 /* x */,
		4 /* y */, 10/* z */
	};
	
	// Tiles for the tile cache used by the valueOf(char) method.
	public static Tile A = new Tile('a');
	public static Tile B = new Tile('b');
	public static Tile C = new Tile('c');
	public static Tile D = new Tile('d');
	public static Tile E = new Tile('e');
	public static Tile F = new Tile('f');
	public static Tile G = new Tile('g');
	public static Tile H = new Tile('h');
	public static Tile I = new Tile('i');
	public static Tile J = new Tile('j');
	public static Tile K = new Tile('k');
	public static Tile L = new Tile('l');
	public static Tile M = new Tile('m');
	public static Tile N = new Tile('n');
	public static Tile O = new Tile('o');
	public static Tile P = new Tile('p');
	public static Tile Q = new Tile('q');
	public static Tile R = new Tile('r');
	public static Tile S = new Tile('s');
	public static Tile T = new Tile('t');
	public static Tile U = new Tile('u');
	public static Tile V = new Tile('v');
	public static Tile W = new Tile('w');
	public static Tile X = new Tile('x');
	public static Tile Y = new Tile('y');
	public static Tile Z = new Tile('z');
	
	// allows for lookup of static Tile object using an integer index, such as 'a' - 26
	private static final Tile[] CACHE = new Tile[] {
		A, B, C, D ,E, F, G, H,I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z
	};
	
	/**
	 * For subclasses.
	 */
	protected Tile() {
	}
	
	/**
	 * Creates a Tile for this letter with the appropriate number of points.
	 * <p>
	 * The letter, when stored, will be converted to lower case so that {@link #getLetter()} will return the lower
	 * case equivalent of the letter.
	 * 
	 * @param letter	A <code>char</code> value a-z or A-Z
	 * 
	 * @throws IllegalArgumentException	If the <code>letter</code> is not a value a-z or A-Z
	 */
	public Tile(final char letter) {
		setLetter(letter);
	}
	
	/**
	 * Determine whether this Tile is a blank (or wildcard) Tile.
	 * <p>
	 * Always returns <code>false</code> for Tile instances.
	 */
	public boolean isBlankTile() {
		return false;
	}
	
	/**
	 * Sets the point value for all <code>Tile</code> objects for the given letter.
	 * 
	 * @param letter		The letter to set the points for
	 * @param pointValue	The number of points the corresponding tile is worth
	 */
	public static void setPointValue(final char letter, int pointValue) {
		try {
			POINTS[Character.toLowerCase(letter) - 'a'] = pointValue;
		} catch (ArrayIndexOutOfBoundsException e) {
			// ignore
		}
	}
	
	/**
	 * Obtains the point value of a <code>Tile</code> for the given letter.
	 * 
	 * @param letter	The letter to get the points for
	 * 
	 * @return		The points for this letter, or <code>0</code> if the <code>char</code> is not a letter.
	 */
	public static int getPointValue(final char letter) {
		int points = 0;
		try {
			points = POINTS[Character.toLowerCase(letter) - 'a'];
		} catch (ArrayIndexOutOfBoundsException e) {
			// return 0
		}
		return points;
	}
	
	/**
	 * Obtains an already instantiated <code>Tile</code> instance for this <code>letter</code>.  This method
	 * should be used in preference to the {@link #Tile(char)} constructor. 
	 * 
	 * @param letter	A <code>char</code> value a-z or A-Z
	 * 
	 * @return	A {@link Tile} instance representing <code>letter</code>
	 * 
	 * @throws IllegalArgumentException	If the <code>letter</code> is not a value a-z or A-Z
	 */
	public static Tile valueOf(final char letter) {
		
		Tile tile = null;
		try {
			tile = CACHE[Character.toLowerCase(letter) - 'a'];
		} catch (ArrayIndexOutOfBoundsException e) {
			// return null
		}
		return tile;
	}

	/**
	 * Sets the letter that represents this tile.  It may be a value a-z or A-Z.
	 * <p>
	 * This method is provided for subclasses. 
	 * 
	 * @param letter	A <code>char</code> value a-z or A-Z
	 * 
	 * @throws IllegalArgumentException	If the <code>letter</code> is not a value a-z or A-Z
	 */
	protected void setLetter(final char letter) {
		Validate.isTrue(Character.isLetter(letter), "letter must be a char A-Z, a-z");
		this.letter = Character.toLowerCase(letter);
	}
	
	/**
	 * @see Tile#setLetter(char)
	 */
	public char getLetter() {	
		return letter;
	}
	
	/**
	 * Returns the point value of the {@link Tile},
	 * <p>
	 * Point values can be registered by calling {@link #setPointValue(char, int)}
	 * 
	 * @return	The registered point value of this tile.
	 */
	public int getPointValue() {	
		return getPointValue(this.letter);
	}
	
	/**
	 * Performs a comparison using the value returned from {@link #toChar()}.
	 * 
	 * @see Comparable#compareTo(Object)
	 */
	@Override
	public int compareTo(Tile other) {
		
		if (other == null) {
			throw new ClassCastException("Invalid or null object");
		}
		
		int result = 0;
		if (other.isBlankTile() && !this.isBlankTile()) {
			result = 1;
		} else if (!other.isBlankTile() && this.isBlankTile()) {
			result = -1;
		} else {
			result = this.toChar() - (other.toChar());
		}
		
		return result; 
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new Tile(getLetter());
	}

	/**
	 * Returns the letter as a char, but always the upper-case equivalent.
	 * 
	 * @return	The letter, upper-cased
	 */
	public char toChar() {
		return Character.toUpperCase(letter);
	}
	
	/**
	 * Returns the letter and point value separated by a colon.
	 * <p>
	 * Example: "[A:1]"
	 */
	@Override
	public String toString() {
		return "[" + toChar() + ":" + getPointValue() + "]";
	}

	@Override
	public int hashCode() {
		return 31 * letter;
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (letter != other.letter)
			return false;
		return true;
	}
}
