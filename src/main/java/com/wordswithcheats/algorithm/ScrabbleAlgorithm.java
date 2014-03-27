package com.wordswithcheats.algorithm;

import java.util.List;

import com.wordswithcheats.board.Board;
import com.wordswithcheats.board.Rack;
import com.wordswithcheats.board.TilePlacement;

/**
 * Defines the contract that all Scrabble algorithms must meet.
 * <p>
 * A "bonus" is when a player uses the maximum number of tiles allowed on the rack
 * in a single turn.
 * 
 * @author Matt Sidesinger
 */
public interface ScrabbleAlgorithm {
	
	/**
	 * Generates a random, but valid list of tile placements for the given Rack and Board.
	 * 
	 * @param board		The board to use.
	 * @param rack		The rack to pull tiles from
	 * 
	 * @return	A random list of tile placements
	 */
	public List<TilePlacement> randomPlacement(final Board board, final Rack rack);
	
	/**
	 * Generates the longest list of tile placements for the given Rack and Board.
	 * 
	 * @param board		The board to use.
	 * @param rack		The rack to pull tiles from
	 * 
	 * @return	A random and longest list of tile placements
	 */
	public List<TilePlacement> longestPlacement(final Board board, final Rack rack);
	
	/**
	 * Determines the highest score given the current Rack and Board.  The method implementation is not intended
	 * to use artificial intelligence  (play-ahead strategy); this type of logic should be implemented in a
	 * different method. 
	 * 
	 * @param board		The board to use
	 * @param rack		The rack to pull tiles from
	 *  
	 * @return		The highest score that this player can attain with the given rack on the current board's
	 * 				configuration.
	 */
	public List<TilePlacement> calculateHighestScorePlacement(final Board board, final Rack rack);
}