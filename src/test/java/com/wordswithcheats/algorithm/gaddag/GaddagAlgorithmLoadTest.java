package com.wordswithcheats.algorithm.gaddag;

import java.util.List;

import org.junit.Ignore;

import com.wordswithcheats.board.Board;
import com.wordswithcheats.board.Rack;
import com.wordswithcheats.board.Tile;
import com.wordswithcheats.board.TileBag;
import com.wordswithcheats.board.TilePlacement;
import com.wordswithcheats.board.exception.IllegalTilePlacementException;

/**
 * This class does not contain any unit tests, but can be used to determine
 * how long it takes for two computer players to play each other in a game.
 * 
 * @author Matt Sidesinger
 */
@Ignore
public class GaddagAlgorithmLoadTest {
	
	public static void main(String args[]) {
		
		Board board = new Board(15, 15);
		TileBag tileBag = new TileBag();
		
		int score1 = 0;
		int score2 = 0;
		Rack rack1 = new Rack();
		Rack rack2 = new Rack();
		
		tileBag.fillRack(rack1);
		tileBag.fillRack(rack2);
		
		GaddagAlgorithm algorithm = new GaddagAlgorithm();
		
		int i = 0;
		int couldNotPlayCount = 0;
		while (rack1.tileCount() > 0 && rack2.tileCount() > 0) {
			
			Rack rack = null;
			if (i % 2 == 1) {
				rack = rack2;
			} else {
				rack = rack1;
			}
			
			System.out.println("RACK: " + rack);
			
			List<TilePlacement>placements = algorithm.calculateHighestScorePlacement(board, rack);
			System.out.println("MAX placement is: " + placements);
			if (placements == null) {
				System.out.println("Could not play.");
				couldNotPlayCount++;
				if (couldNotPlayCount == 2) {
					System.out.println("Done.");
					break;
				}
			} else {
    			try {
        			if (i % 2 == 1) {
        				score2 += board.place(placements);
        				System.out.println("SCORE:" + score2);
        			} else {
        				score1 += board.place(placements);
        				System.out.println("SCORE:" + score1);
        			}
        			
        			for (TilePlacement tp : placements) {
        				Tile tile = rack.take(tp.getTile());
        				if (tile == null) {
        					throw new RuntimeException("Not able to remove the following tile: " + tp.getTile());
        				}
        			}
        			
        			tileBag.fillRack(rack);
        			
    			} catch (IllegalTilePlacementException e) {
    				System.out.print("Board.place() generated an illegal placement.");
    				System.out.println("Board: " + board);
    				System.out.println("Placements: " + placements);
    				System.exit(1);
    			}
    			
    			// board after play
    			System.out.println("AFTER:");
    			System.out.println(board);
			}
			
			i++;
			
		} // ~while
		
		System.out.println("score 1:" + score1);
		System.out.println("score 2:" + score2);
	}
}
