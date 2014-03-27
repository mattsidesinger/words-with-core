package com.wordswithcheats.board.exception;

import com.wordswithcheats.board.Board;

/**
 * A base class for Exceptions thrown by the {@link Board} class.
 * 
 * @author Matt Sidesinger
 */
public abstract class BoardException extends Exception {
	
	private static final long serialVersionUID = 4311926824415049593L;
	
	public BoardException() {
		super();
	}

	public BoardException(final String message) {
		super(message);
	}
}