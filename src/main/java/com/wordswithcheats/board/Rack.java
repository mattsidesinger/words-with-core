package com.wordswithcheats.board;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.lang.Validate;

import com.wordswithcheats.board.exception.RackFullException;

/**
 * A rack is a container for a player's tiles.  It provides the ability to add and take tiles.  The rack
 * is initialized with the number of available slots.
 * 
 * @author Matt Sidesinger
 */
public class Rack implements Cloneable, Iterable<Tile>, Serializable {
	
	// Rack is backed by LinkedList
	private LinkedList<Tile> rack;
	private int length;
	
	public static final int DEFAULT_SIZE = 7;
	
	private static final long serialVersionUID = 2950183468918944800L;
	
	/**
	 * Creates an empty rack of size {@value #DEFAULT_SIZE}.
	 */
	public Rack() {
		this(DEFAULT_SIZE);
	}
	
	/**
	 * Creates an empty rack with size of the given parameter.
	 * 
	 * @param size
	 */
	public Rack(final int size) {
		Validate.isTrue(size > 0, "size must be an integer greater than 0");
		init(size);
	}
	
	/**
	 * Creates an empty rack of size {@value #DEFAULT_SIZE}. The tiles from the  {@link Tile} array are then
	 * added to the rack.
	 * 
	 * @param tiles	The tiles used to load the rack.
	 */
	public Rack(final Tile[] tiles) {
		
		Validate.notNull(tiles, "Tile array cannot be null");
		
		init(DEFAULT_SIZE);
		
		for (int i = 0; i < tiles.length; i++) {
			if (tiles[i] != null) {
				rack.add(tiles[i]);
			}
		}
	}

	/**
	 * Creates an empty rack of size {@value #DEFAULT_SIZE}. The tiles from the {@link Collection} are then
	 * added to the rack.
	 * 
	 * @param tiles	The tiles used to initialize the size of the rack and load it.
	 */
	public Rack(final Collection<Tile> tiles) {
		
		Validate.notNull(tiles, "Collection cannot be null");
		
		init(DEFAULT_SIZE);
		
		for (Tile tile : tiles) {
			if (tile != null) {
    			rack.add(tile);
			}
		}
	}
	
	/**
	 * Initializes important instance variables.
	 * 
	 * @param size	The size used to initialize the backing array.
	 */
	protected void init(final int size) {
		this.rack = new LinkedList<Tile>();
		this.length = size;
	}
	
	/**
	 * @return	The number of slots in the rack.
	 */
	public int size() {
		return this.length;
	}
	
	/**
	 * @return	<code>true</code> if there are no tiles in any of the slots, otherwise <code>false</code>.
	 */
	public boolean isEmpty() {
		return rack.isEmpty();
	}

	/**
	 * @return	The number of tiles in the rack slots.
	 */
	public int tileCount() {
		return rack.size();
	}
	
	/**
	 * @return	The number of empty slots in the rack.
	 */
	public int emptyCount() {
		return length - rack.size();
	}
	
	/**
	 * Adds the given {@link Tile} to the rack.  The Tile cannot be null and the rack must have at least one slot free.
	 * If {@link #emptyCount()} return a value greater than or equal to 1, then a Tile can be added.
	 * 
	 * @param tile	The {@link Tile} to add to the rack.
	 *
	 * @returns		This Rack object to allow for chaining of commands.
	 *
	 * @throws RackFullException	When the tile count is already equal to the rack size.
	 */
	public Rack add(final Tile tile) throws RackFullException {
		
		Validate.notNull(tile, "tile cannot be null");
		if (rack.size() == this.length) {
			throw new RackFullException();
		}
		
		rack.add(tile);
		
		return this;
	}
	
	/**
	 * Removes a Tile from the beginning of the rack.
	 */
	public Tile take() {
		Tile take = rack.remove();
		return take;
	}
	
	/**
	 * Removes an equal Tile from the rack.
	 * 
	 * @param tile	The tile to take from the rack, if it exists.
	 * 
	 * @return	The found tile, or <code>null</code> if the tile could not be found or the rack is empty.
	 */
	public Tile take(final Tile tile) {
		
		Validate.notNull(tile, "tile cannot be null");
		
		Tile take = remove(tile);
		
		return take;
	}
	
	@Override
	public Iterator<Tile> iterator() {
		return rack.iterator();
	}
	
	/**
	 * Removes equal Tiles from the rack.
	 * 
	 * @param tiles	The tiles to take from the rack, if it exists.
	 * 
	 * @return	The found tile, or <code>null</code> if the tile could not be found or the rack is empty.
	 */
	public Tile[] take(final Tile[] tiles) {
		
		Validate.notNull(tiles, "the Tile array cannot be null");
		
		Tile[] taken = null;
		
		if (rack.size() > 0 && tiles.length > 0) {
			
			// validate that no entries are null before removing any tiles
			for (int i = 0; i < tiles.length; i++) {
				Validate.notNull(tiles[i], "the Tile array cannot contain any null entries");
			}
			
			taken = new Tile[tiles.length];
			
			int takeCount = 0;
    		for (int i = 0; i < tiles.length; i++) {
    			Tile take = remove(tiles[i]);
    			if (take != null) {
    				taken[takeCount] = take;
    				takeCount++;
    			}
    		}
    		
    		if (takeCount != tiles.length) {
    			// resize the array
    			Tile[] temp = new Tile[takeCount];
    			System.arraycopy(taken, 0, temp, 0, takeCount);
    			taken = temp;
    		}
    		
		} else {
			taken = new Tile[0];
		}
		
		return taken;
	}
	
	/**
	 * Removes an equal Tile from the rack.
	 * 
	 * @param tile	The tile to take from the rack, if it exists.
	 * 
	 * @return	The found tile, or <code>null</code> if the tile could not be found or the rack is empty.
	 */
	protected Tile remove(final Tile tile) {
		
		Tile take = null;
		
		int index = -1;
		
		if (tile.isBlankTile()) {
			// guarantee that the BlankTile passed in has no letter
			index = rack.indexOf(new BlankTile());
		} else {
			index = rack.indexOf(tile);
		}
		
		if (index >= 0) {
			take = rack.get(index);
			rack.remove(index);
		}
		
		return take;
	}
	
	/**
	 * Removes all tiles from the rack.
	 */
	public void clear() {
		rack.clear();
	}
	
	/**
	 * Randomizes the order of the tiles in the rack.
	 */
	public void shuffle() {
		Collections.shuffle(this.rack);
	}
	
	/**
	 * Alias for {@link #rotateLeft()}.
	 */
	public void rotate() {
		rotateLeft();
	}
	
	/**
	 * Removes the first tile from the rack, slides the remaining tiles to the left, and adds
	 * the tile that was removed to the end of the rack.
	 */
	public void rotateLeft() {
		Tile head = this.rack.remove();
		this.rack.add(head);
	}
	
	/**
	 * Removes the last tile from the rack, slides the remaining tiles to the right, and adds
	 * the tile that was removed to the front of the rack.
	 */
	public void rotateRight() {
		Tile tail = this.rack.removeLast();
		this.rack.add(0, tail);
	}
	
	/**
	 * Sorts the tiles in the rack.
	 * 
	 * @see	Tile#compareTo(Object)
	 */
	public void sort() {
		Collections.sort(this.rack);
	}
	
	@Override
	public Rack clone() throws CloneNotSupportedException {
		return new Rack(this.toArray());
	}

	/**
	 * @return	An array of {@link Tile} objects that make up the rack.
	 */
	public Tile[] toArray() {
		
		Tile[] tiles = new Tile[this.length];
		
		int i = 0;
		for (Tile tile : rack) {
    		tiles[i] = tile;
    		i++;
		}
		
		return tiles;
	}
	
	/**
	 * Example: [ [A:1]|[ :0]| | | | | ] 
	 */
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		sb.append(' ');
		
		int printCount = 0;
		for (Tile t : rack) {
			
			sb.append(t.toString());
			printCount++;
			if (printCount != this.length) {
				sb.append('|');
			}
		}
		
		for (int i = printCount; i < this.length; i++) {
			sb.append(' ');
			printCount++;
			if (printCount != this.length) {
				sb.append('|');
			}
		}		
		
		sb.append(' ');
		sb.append(']');
		return sb.toString();
	}
	
	/**
	 * Unlike {@link #toString()}, this method prints the rack's tile values without any special characters.
	 * A '*' is used to signigy a blank tile. Examples:
	 * <ul>
	 * 	<li>A full rack: ABCDEFG</li>
	 *  <li>A rack with a blank tile: ABC*D</li>
	 *  <li>An empty rack: </li>
	 * </ul>
	 * 
	 * @return	the rack as a String
	 */
	public String toSimpleString() {
	
		StringBuilder sb = new StringBuilder(tileCount());
		final char BLANK_TILE_CHAR = '*';
		
		for (Tile t : rack) {
			if (t.isBlankTile()) {
				sb.append(BLANK_TILE_CHAR);
			} else {
				sb.append(t.toChar());
			}
		}		
		return sb.toString();
	}
}
