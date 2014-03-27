package com.wordswithcheats.algorithm.gaddag;

import static com.wordswithcheats.board.Tile.A;
import static com.wordswithcheats.board.Tile.B;
import static com.wordswithcheats.board.Tile.C;
import static com.wordswithcheats.board.Tile.D;
import static com.wordswithcheats.board.Tile.E;
import static com.wordswithcheats.board.Tile.F;
import static com.wordswithcheats.board.Tile.G;
import static com.wordswithcheats.board.Tile.I;
import static com.wordswithcheats.board.Tile.J;
import static com.wordswithcheats.board.Tile.K;
import static com.wordswithcheats.board.Tile.L;
import static com.wordswithcheats.board.Tile.M;
import static com.wordswithcheats.board.Tile.N;
import static com.wordswithcheats.board.Tile.O;
import static com.wordswithcheats.board.Tile.R;
import static com.wordswithcheats.board.Tile.S;
import static com.wordswithcheats.board.Tile.T;
import static com.wordswithcheats.board.Tile.U;
import static com.wordswithcheats.board.Tile.X;
import static com.wordswithcheats.board.Tile.Y;
import static com.wordswithcheats.board.Tile.Z;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.wordswithcheats.board.Board;
import com.wordswithcheats.board.Rack;
import com.wordswithcheats.board.TileBag;
import com.wordswithcheats.board.TilePlacement;

/**
 * Test case for the {@link GaddagAlgorithm} class.
 * 
 * @author Matt Sidesinger
 */
//TODO finish
public class GaddagAlgorithmTest {

	@Ignore
	@Test
	public void calculateHighestScorePlacement() throws Exception {
		
		// Need a few things first: a Board and a Rack.
		// TileBag can be null - we will manually fill the rack.
		Board board = new Board(15, 15);
		Rack rack = new Rack();
		TileBag tileBag = null;
		
		// This is what we will be testing.
		GaddagAlgorithm gaddag = new GaddagAlgorithm();
		List<TilePlacement> tp = null;
		int score = 0;
		
		// TODO: apply this type of logic to other test classes
		// R S T L N E - Wheel of Fortune style, bitches!
		// and an I for good luck?
		rack.add(R).add(S).add(T).add(L).add(N).add(E).add(I);
		tp = gaddag.calculateHighestScorePlacement(board, rack);
		
		// play LINTERS
		score += board.place(tp);
		rack.clear();
		
		System.out.print(tp);
		System.out.print(board);
		
		rack.add(B).add(A).add(K).add(F).add(E).add(Y).add(D);
		tp = gaddag.calculateHighestScorePlacement(board, rack);
		
		score += board.place(tp);
		rack.clear();
		
		System.out.print(tp);
		System.out.print(board);
		
		rack.add(J).add(U).add(A).add(M).add(X).add(L).add(G);
		tp = gaddag.calculateHighestScorePlacement(board, rack);
		
		score += board.place(tp);
		rack.clear();
		
		System.out.print(tp);
		System.out.print(board);
		
		rack.add(A).add(O).add(F).add(R).add(U).add(Z).add(C);
		tp = gaddag.calculateHighestScorePlacement(board, rack);
		
		score += board.place(tp);
		rack.clear();
		
		System.out.print(tp);
		System.out.print(board);
	}
}
