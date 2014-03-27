package com.wordswithcheats.board;

import java.io.Serializable;

import org.apache.commons.lang.Validate;

/**
 * A TilePlacement is when a {@link Tile} is placed on a {@link Board}.  It is made up of the Tile and the (x,y) coordinates where
 * the tile should be placed on the board.  
 * 
 * @author Matt Sidesinger
 */
public class TilePlacement implements Serializable {
	
	private int x;
	private int y;
	private Tile tile;
	
	private static final long serialVersionUID = -3715921439998660114L;
	
	/**
	 * Creates the tile placement.  The x, y coordinates must both be greater than
	 * 0 (zero) and the {@link Tile} cannot be null.
	 * 
	 * @param x		The x coordinate on the board.
	 * @param y		The y coordinate on the board.
	 * @param tile	The tile to play.
	 */
	public TilePlacement(int x, int y, Tile tile) {
		
		Validate.isTrue(x > 0, "x coordinate must be greater than 0");
		Validate.isTrue(y > 0, "y coordinate must be greater than 0");
		Validate.notNull(tile, "Tile cannot be null");
		
		this.x = x;
		this.y = y;
		this.tile = tile;
	}

	public int getX() {	
		return x;
	}

	protected void setX(final int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(final int y) {
		this.y = y;
	}

	public Tile getTile() {
		return tile;
	}

	protected void setTile(final Tile tile) {
		this.tile = tile;
	}
	
	/**
	 * Display the object as a string in the following format: (x,y):Tile
	 * <p>
	 * Example: (1,2):F
	 */
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append('(');
		sb.append(getX());
		sb.append(',');
		sb.append(getY());
		sb.append(')');
		sb.append(':');
		sb.append(getTile().toString());
		
		return sb.toString();
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((tile == null) ? 0 : tile.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TilePlacement other = (TilePlacement) obj;
		if (tile == null) {
			if (other.tile != null)
				return false;
		} else if (!tile.equals(other.tile))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}	
}
