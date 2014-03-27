package com.wordswithcheats.board.format;

import java.io.Serializable;

import com.wordswithcheats.board.Board;

/**
 * Converts the board to a String to a format specified by the implementation.
 * 
 * @author Matt Sidesinger
 */
public abstract class BoardFormat implements Serializable {
	
	private static final long serialVersionUID = -6809415464926452991L;

	public BoardFormat() {
	}
	
	/**
	 * Formats the {@link Board} to the format specified by the implementation and returns
	 * the formatted board as a String.
	 * 
	 * @param board		The board to format
	 * 
	 * @return	The board formatted as a String
	 */
	public String format(final Board board) {
		// 36 characters x 17 rows
		StringBuffer sb = new StringBuffer(612);
		format(board, sb);
		return sb.toString();
	}
	
	/**
	 * Formats the {@link Board} to the format specified by the implementation and returns
	 * the formatted board as a StringBuffer.
	 * 
	 * @param board		The board to format
	 * @param sb		A StringBuffer, which cannot be null
	 * 
	 */
	public abstract void format(final Board board, final StringBuffer sb);
}
