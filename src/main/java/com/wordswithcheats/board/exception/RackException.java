package com.wordswithcheats.board.exception;

import com.wordswithcheats.board.Rack;

/**
 * The base class for Exceptions thrown by {@link Rack}.
 * 
 * @author Matt Sidesinger
 */
public abstract class RackException extends Exception {

	private static final long serialVersionUID = 4845398519077168381L;

	public RackException() {
		super();
	}
	
	public RackException(final String message) {
		super(message);
	}
}
