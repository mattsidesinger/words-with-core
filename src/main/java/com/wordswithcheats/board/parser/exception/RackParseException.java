package com.wordswithcheats.board.parser.exception;

import com.wordswithcheats.board.parser.RackParser;

/**
 * Exception is thrown when a {@link RackParser} is unable to parse input due to errors such
 * as illegal characters.
 * 
 * @author Matt Sidesinger
 */
public class RackParseException extends Exception {
	
	private static final long serialVersionUID = -8304374142596926354L;

	/**
	 * @see Exception#Exception(String)
	 */
	public RackParseException(final String message) {
		super(message);
	}
	
	/**
	 * @see Exception#Exception(String, Throwable)
	 */
	public RackParseException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
