package com.wordswithcheats.board.parser;

import com.wordswithcheats.board.Board;
import com.wordswithcheats.board.Tile;
import com.wordswithcheats.board.parser.exception.BoardParseException;

/**
 * A parser that creates a multidimensional {@link Tile} array that represents a {@link Board}.  The Tile array
 * can be used to directly instantiate a {@link Board} object by calling the {@link Board#Board(Tile[][])}
 * constructor.
 * <p>
 * This interface does not dictate an input format.
 * 
 * @author Matt Sidesinger
 */
public interface BoardParser {

	/**
	 * Parses a CharSequence and creates a multidimensional {@link Tile} array, where the first index is the y-axis
	 * and the second index is the x-axis.  The coordinates 0,0 represent the top left corner of the board. 
	 * 
	 * @param input		A character sequence that represents the board.
	 * 
	 * @return A multidimensional {@link Tile} array
	 * 
	 * @throws BoardParseException	When there is an error parsing the input, such as
	 * 								as an illegal character is encountered or there is
	 * 								an unexpected end to the input.
	 */
	public Tile[][] parse(final CharSequence input) throws BoardParseException;
}