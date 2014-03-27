package com.wordswithcheats.board.parser.exception;

import org.apache.commons.lang.Validate;

import com.wordswithcheats.board.parser.BoardParser;

/**
 * Exception is thrown when a {@link BoardParser} is unable to parse input due to errors such
 * as illegal characters or the input unexpectedly ends.
 * 
 * @author Matt Sidesinger
 */
public class BoardParseException extends Exception {

	private int lineNumber;
	
	private static final long serialVersionUID = 6431739884494691708L;
	
	/**
	 * @see Exception#Exception(String)
	 */
	public BoardParseException(final String message) {
		super(message);
	}
	
	/**
	 * Includes the line number.
	 * 
	 * @param message		The detailed message.
	 * @param lineNumber	The line number where the parse error occurred.
	 * 
	 * @see Exception#Exception(String)
	 */
	public BoardParseException(final String message, final int lineNumber) {
		this(message, lineNumber, null);
	}
	
	/**
	 * Includes the line number.
	 * 
	 * @param message		The detailed message.
	 * @param lineNumber	The line number where the parse error occurred.
	 * @param cause			The cause.
	 * 
	 * @see Exception#Exception(String, Throwable)
	 */
	public BoardParseException(final String message, final int lineNumber, final Throwable cause) {
		super(message, cause);
		Validate.isTrue(lineNumber > 0, "Invalid line number");
		this.lineNumber = lineNumber;
	}
	
	/**
	 * @return	The line number where the parse error occurred.
	 */
	public int getLineNumber() {
		return lineNumber;
	}
}