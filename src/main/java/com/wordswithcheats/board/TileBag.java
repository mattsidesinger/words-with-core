package com.wordswithcheats.board;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.Validate;

import com.wordswithcheats.board.exception.RackFullException;

/**
 * Maintains a count of tile objects and provides the ability to fill a {@link Rack} based on these tile counts.
 * <p>
 * Using the default constructor create a bag with the following tile counts:
 * <ul>
 * 	<li><strong>a</strong>: 9</li>
 *  <li><strong>b</strong>: 2</li>
 *  <li><strong>c</strong>: 2</li>
 *  <li><strong>d</strong>: 4</li>
 *  <li><strong>e</strong>: 12</li>
 *  <li><strong>f</strong>: 2</li>
 *  <li><strong>g</strong>: 3</li>
 *  <li><strong>h</strong>: 3</li>
 *  <li><strong>i</strong>: 9</li>
 *  <li><strong>j</strong>: 1</li>
 *  <li><strong>k</strong>: 1</li>
 *  <li><strong>l</strong>: 4</li>
 *  <li><strong>m</strong>: 2</li>
 *  <li><strong>n</strong>: 6</li>
 *  <li><strong>o</strong>: 8</li>
 *  <li><strong>p</strong>: 2</li>
 *  <li><strong>q</strong>: 1</li>
 *  <li><strong>r</strong>: 6</li>
 *  <li><strong>s</strong>: 4</li>
 *  <li><strong>t</strong>: 6</li>
 *  <li><strong>u</strong>: 4</li>
 *  <li><strong>v</strong>: 2</li>
 *  <li><strong>w</strong>: 2</li>
 *  <li><strong>x</strong>: 1</li>
 *  <li><strong>y</strong>: 2</li>
 *  <li><strong>z</strong>: 1</li>
 * </ul>
 * Tile counts can also be set using the {@link #TileBag(Map)} contructor or by calling the
 * {@link #setTileCount(char, int)}, {@link #setTileCount(Tile, int)}, or {@link #setTileCounts(int[])} methods.
 * Once {@link #fillRack(Rack)} has been called once, then these methods can no longer be called without an
 * Exception being raised. 
 * 
 * @author Matt Sidesinger
 */
public class TileBag implements Serializable {
	
	private int[] tileCounts;
	private int blankTileCount;
	private boolean locked = false;
	
	private static final long serialVersionUID = 7633201073030121461L;
	
	/**
	 * Default tile distributions according to
	 * <a href="http://en.wikipedia.org/wiki/Scrabble_letter_distributions">Wikipedia</a>.
	 */
	protected static final int[] DEFAULT_TILE_COUNTS = new int[] {
    	/* a */ 9, /* b */ 2, /* c */ 2, /* d */ 4, /* e */ 12, /* f */ 2, /* g */ 3, /* h */ 2, 
    	/* i */ 9, /* j */ 1, /* k */ 1, /* l */ 4, /* m */ 2, /* n */ 6, /* o */ 8, /* p */ 2, 
    	/* q */ 1, /* r */ 6, /* s */ 4, /* t */ 6, /* u */ 4, /* v */ 2, /* w */ 2, /* x */ 1, 
    	/* y */ 2, /* z */ 1
	};
	/**
	 * Default blank tile distribution according to
	 * <a href="http://en.wikipedia.org/wiki/Scrabble_letter_distributions">Wikipedia</a>.
	 */
	protected static final int DEFAULT_BLANK_TILE_COUNT = 2;
	
	/**
	 * Creates a bag of tiles each with a default count.
	 */
	public TileBag() {
		tileCounts = new int[DEFAULT_TILE_COUNTS.length];
		for (int i = 0; i < DEFAULT_TILE_COUNTS.length; i++) {
			tileCounts[i] = DEFAULT_TILE_COUNTS[i];
		}
		blankTileCount = DEFAULT_BLANK_TILE_COUNT;
	}
	
	/**
	 * Creates a bag of tiles from the given map to Tile objects to tile counts. Any tiles 
	 * that are not included (A-Z or blank) will be set to 0.
	 * <p>
	 * The Map cannot be <code>null</code> and it must be at least a size of 1.
	 * 
	 * @param tileCounts	A Map of Tile objects mapped to tile counts.
	 */
	public TileBag(final Map<Tile, Integer> tileCounts) {
		
		Validate.notNull(tileCounts, "Map of tile counts cannot be null");
		Validate.isTrue(tileCounts.size() > 0, "Map of tile counts must be at least size 1");
		
		initTileCounts(tileCounts);
	}
	
	/**
	 * Creates a bag of tiles from the given map to Tile objects to tile counts. Any tiles 
	 * that are not included (A-Z or blank) will be set to 0.
	 * <p>
	 * The Map cannot be <code>null</code> and it must be at least a size of 1.
	 * 
	 * @param tileCountsMap	A Map of Tile objects mapped to tile counts.
	 */
	protected void initTileCounts(final Map<Tile, Integer> tileCountsMap) {
		
		// set all counts to 0
		tileCounts = new int[DEFAULT_TILE_COUNTS.length];
		for (int i = 0; i < DEFAULT_TILE_COUNTS.length; i++) {
			tileCounts[i] = 0;
		}
		blankTileCount = 0;
		
		// update tile counts using Map
		if (tileCountsMap != null) {
    		for (Map.Entry<Tile, Integer> entry : tileCountsMap.entrySet()) {
    			Tile tile = entry.getKey();
    			int count = entry.getValue() == null ? 0 : entry.getValue().intValue();
    			if (tile.isBlankTile()) {
    				blankTileCount = count;
    			} else {
    				// letter is guaranteed to be lower case
    				tileCounts[tile.getLetter() - 'a'] = count;
    			}
    		}
		}
	}
	
	/**
	 * Sets the count for a specific {@link Tile}.
	 * <p>
	 * This method cannot be used once {@link #fillRack(Rack)} has been called.
	 * <p>
	 * This TileBag instance is returned to allow chaining of methods:
	 * <pre>
	 * aTileBag.setTileCount(...).setTileCount(...);
	 * </pre>
	 * 
	 * @param tile		The Tile to set the count for
	 * @param count		The count
	 * 
	 * @return		This TileBag object.
	 */
	public TileBag setTileCount(final Tile tile, final int count) {
		
		validateLock();
		Validate.notNull(tile, "Tile cannot be null");
		Validate.isTrue(count >= 0, "count must be greater than or equal to 0");
		
		if (tile.isBlankTile()) {
			this.blankTileCount = count;
		} else {
			// letter is guaranteed to be lower case
			this.tileCounts[tile.getLetter() - 'a'] = count;
		}
		
		return this;
	}

	/**
	 * Sets the count for the {@link Tile} that maps to the given character.
	 * <p>
	 * This method cannot be used once {@link #fillRack(Rack)} has been called.
	 * <p>
	 * This TileBag instance is returned to allow chaining of methods:
	 * <pre>
	 * aTileBag.setTileCount(...).setTileCount(...);
	 * </pre>
	 * 
	 * @param letter	The letter to set the Tile count for
	 * @param count		The count
	 * 
	 * @return		This TileBag object.
	 */
	public TileBag setTileCount(final char letter, final int count) {
		return setTileCount(Tile.valueOf(letter), count);
	}
	
	/**
	 * Obtains the number of tiles remaining for the given {@link Tile}.
	 * 
	 * @param tile	The Tile to check for.
	 * 
	 * @return		The number of tiles that remain for the given Tile type.
	 */
	public int getTileCount(final Tile tile) {
		
		Validate.notNull(tile, "Tile cannot be null");
		
		int tileCount = 0;
		if (tile.isBlankTile()) {
			tileCount = this.blankTileCount;
		} else {
			// letter is guaranteed to be lower case
			tileCount = this.tileCounts[tile.getLetter() - 'a'];
		}
		
		return tileCount;
	}
	
	/**
	 * @throws IllegalStateException	If <code>this.locked</code> is <code>true</code>
	 */
	protected void validateLock() throws IllegalStateException {
		if (this.locked) {
			throw new IllegalStateException("Cannot change state once fillRack() has been called");
		}
	}
	
	/**
	 * Adds tiles to the empty spaces in a {@link Rack}.  The number of empty spaces can be determined by calling
	 * {@link Rack#emptyCount()}.  If the {@link TileBag} is empty ({@link #isEmpty()}, then no tiles are added to
	 * the Rack. 
	 * <p>
	 * If the rack is already full, no Exception is thrown.
	 * 
	 * @see Rack#add(Tile)
	 * 
	 * @param rack	The Rack to fill
	 */
	public void fillRack(final Rack rack) {
		
		this.locked = true;
		
		int fillCount = rack.emptyCount();
		if (fillCount > 0) {
			
			List<Tile> tilesRemaining = getTilesRemainingAsList();
			Random randomIndexGenerator = new Random();
			
    		for (int i = 0; i < fillCount && tilesRemaining.size() > 0; i++) {
    			int randomIndex = randomIndexGenerator.nextInt(tilesRemaining.size());
    			Tile t = tilesRemaining.remove(randomIndex);
    			remove(t);
    			try {
    				rack.add(t);
    			} catch (RackFullException e) {
    				// ignore
    			}
    		}
		}
	}
	
	/**
	 * Decrement the available count available for the specific tile.
	 * 
	 * @param t		The type of tile to remove from the bag
	 * 
	 * @return	<code>true</code> if the Tile was available to remove, otherwise <code>false</code>
	 */
	public boolean remove(Tile t) {
		
		boolean result = false;
		
		if (t instanceof BlankTile) {
			if (this.blankTileCount > 0) {
				this.blankTileCount--;
				result = true;
			}
		} else {
			// letter is guaranteed to be lower case
			int index = t.getLetter() - 'a';
			if (this.tileCounts[index] > 0) {
				this.tileCounts[index]--;
				result = true;
			}
		}
		
		return result;
	}
	
	/**
	 * Determines whether there are any more tiles left to dispense when {@link #fillRack(Rack)} is called.
	 * If no tiles, including blank tiles, are left to dispense then the method will return <code>true</code>.
	 * 
	 * @return	<code>true</code> if there are no tiles remaining, otherwise <code>false</code>.
	 */
	public boolean isEmpty() {
		
		boolean empty = true;
		
		for (int i = 0; i < this.tileCounts.length; i++) {
			if (this.tileCounts[i] > 0) {
				empty = false;
				break;
			}
		}
		
		if (empty) {
			empty = this.blankTileCount == 0;
		}
		
		return empty;
	}
	
	/**
	 * Obtains the {@link Tile} objects that remain given the original counts.  Tile counts are reduced as Tile are
	 * removed using {@link #fillRack(Rack)}. 
	 * 
	 * @return	The Tile objects that remain.  If no tiles remain, then an empty array is returned instead of
	 * 			<code>null</code>.
	 */
	public Tile[] getTilesRemaining() {
		// List is guaranteed not to be null
		List<Tile> tilesRemaining = getTilesRemainingAsList();
		assert (tilesRemaining != null);
		return tilesRemaining.toArray(new Tile[] {});
	}
	
	/**
	 * Obtains the {@link Tile} objects that remain given the original counts.  Tile counts are reduced as Tile are
	 * removed using {@link #fillRack(Rack)}. 
	 * 
	 * @return	The Tile objects that remain.  If no tiles remain, then an empty List is returned instead of
	 * 			<code>null</code>.
	 */
	public List<Tile> getTilesRemainingAsList() {
		
		List<Tile> tilesRemaining = new ArrayList<Tile>();
		
		for (int i = 0; i < this.tileCounts.length; i++) {
    		int tileCount = this.tileCounts[i];
    		for (int j = 0; j < tileCount; j++) {
    			tilesRemaining.add(Tile.valueOf((char) ('a' + i)));
    		}
		}
		
		for (int i = 0; i < this.blankTileCount; i++) {
			tilesRemaining.add(new BlankTile());
		}
		
		return tilesRemaining;
	}
	
	protected int[] getTileCounts() {
		return tileCounts;
	}

	protected void setTileCounts(int[] tileCounts) {	
		this.tileCounts = tileCounts;
	}
	
	protected int getBlankTileCount() {	
		return blankTileCount;
	}

	protected void setBlankTileCount(int blankTileCount) {	
		this.blankTileCount = blankTileCount;
	}

	protected boolean isLocked() {	
		return locked;
	}

	protected void setLocked(boolean locked) {	
		this.locked = locked;
	}
}
