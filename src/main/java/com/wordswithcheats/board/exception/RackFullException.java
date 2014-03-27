package com.wordswithcheats.board.exception;

import com.wordswithcheats.board.Rack;
import com.wordswithcheats.board.Tile;

/**
 * Thrown when a {@link Tile} is added to the {@link Rack}, but the rack's {@link Tile} count is already
 * equal to the rack's capacity.
 * 
 * @author Matt Sidesinger
 */
public class RackFullException extends RackException {

	private static final long serialVersionUID = 7858640745233325931L;

	public RackFullException() {
		super();
	}	
}