package com.wordswithcheats.board.exception;

import com.wordswithcheats.board.Board;
import com.wordswithcheats.board.Tile;
import com.wordswithcheats.board.TilePlacement;

/**
 * Thrown when an illegal tile placement is made on a {@link Board}.  This may include the case where a {@link Tile}
 * already exists on the board for the given x, y coordinates, another {@link TilePlacement} with the same x, y
 * coordinates has been placed within the same turn, or when {@link TilePlacement} or group of placements are
 * illegal; they do not connect to each other or do not connect to an already existing {@link Tile} on the
 * board.
 * 
 * @author Matt Sidesinger
 */
public class IllegalTilePlacementException extends BoardException {

	private TilePlacement tilePlacement;

	private static final long serialVersionUID = -2613586993456960481L;
	
	public IllegalTilePlacementException(final String message) {
		super(message);
	}
	
	public IllegalTilePlacementException(final String message, final TilePlacement p) {
		super(message);
		setTilePlacement(p);
	}

	/**
	 * @return the TilePlacement
	 */
	public TilePlacement getTilePlacement() {	
		return tilePlacement;
	}

	/**
	 * @param tilePlacement the TilePlacement to set
	 */
	protected void setTilePlacement(final TilePlacement tilePlacement) {
		this.tilePlacement = tilePlacement;
	}
}
