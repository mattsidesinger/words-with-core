package com.wordswithcheats.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wordswithcheats.board.exception.IllegalTilePlacementException;
import com.wordswithcheats.board.multiplier.Multipliers;
import com.wordswithcheats.board.parser.BoardParser;
import com.wordswithcheats.board.parser.SimpleBoardParser;
import com.wordswithcheats.board.parser.exception.BoardParseException;
import com.wordswithcheats.board.util.TileUtils;

/**
 * Test case for the {@link Board} class.
 * 
 * @author Matt Sidesinger
 */
public class BoardTest {

	// the test board does not have to made up of actual words
	private static final String TEST_BOARD =
	//   1 2 3 4 5 6 7 8 9 0 1 2 3 4 5
        "*     *       *       *     *\n" + // 1
        "  *       *       *       *  \n" +
        "    *       *   *       *    \n" +
        "*     *       *       *     *\n" +
        "        *     P     *        \n" + // 5
        "  *       *   Q   *       *  \n" +
        "    *       * R *       *    \n" +
        "*     D E F G H i J K L     *\n" + // 8 - H is center
        "    *       *   *     S *    \n" +
        "  *       *       *   T   *  \n" +
        "        *       X w V U      \n" + // 11
        "*     *       *       *     *\n" +
        "    *       *   *       *    \n" +
        "  *       *       *       *  \n" +
        "*     *       *       *     *";    // 15

	private static final Logger logger = LoggerFactory.getLogger(BoardTest.class);
	
	public Board loadBoard() throws BoardParseException {
		
		BoardParser bp = new SimpleBoardParser(true);
		Tile[][] tiles = bp.parse(TEST_BOARD);
		Board b = new Board(tiles);
		
		return b;
	}
	
	@Test
	public void testBoard() throws Exception {
		
		final Multipliers m = new Multipliers();
		final Tile[][] tiles = new Tile[][] {
				new Tile[] {Tile.valueOf('a'), Tile.valueOf('b')},
				new Tile[] {Tile.valueOf('c'), Tile.valueOf('d')},
				new Tile[] {Tile.valueOf('e'), Tile.valueOf('f')}
		};
		
		Board b = new Board(tiles);
		assertEquals(2, b.getWidth());
		assertEquals(3, b.getHeight());
		assertTrue(Arrays.equals(tiles, b.toArray()));
		assertTrue(!b.isEmpty());
		
		b = new Board(tiles, null); // null should be allowed
		b = new Board(tiles, m);
		assertEquals(2, b.getWidth());
		assertEquals(3, b.getHeight());
		assertTrue(Arrays.equals(tiles, b.toArray()));
		assertEquals(m, b.getMultipliers());
		
		b = new Board(5, 10);
		assertEquals(5, b.getWidth());
		assertEquals(10, b.getHeight());
		assertTrue(TileUtils.equals(new Tile[10][5], b.toArray()));
		
		b = new Board(5, 10, null); // null should be allowed
		b = new Board(5, 10, m);
		assertEquals(5, b.getWidth());
		assertEquals(10, b.getHeight());
		assertTrue(TileUtils.equals(new Tile[10][5], b.toArray()));
		assertEquals(m, b.getMultipliers());
		
		try {
			b = new Board(0, 15);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}

		try {
			b = new Board(15, 0);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			b = new Board(-1, 15);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			b = new Board(15, -1);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
	}
	
	/**
	 * Testing {@link Board#place(TilePlacement...)} also tests
	 * {@link Board#validate(TilePlacement...)} and {@link Board#score(TilePlacement[])}.
	 * <p>
	 * Any change in implementation may void this.
	 */
	@Test
	public void testPlace() throws Exception {
		
		Board b = loadBoard();
		int score = 0;
		
		logger.debug("\n" + b.toString());
		
		// place a tile
		score = b.place(new TilePlacement(13, 10, Tile.valueOf('O'))); // TO
		logger.debug("\n" + b.toString());
		assertEquals(2, score);
		score = b.place(new TilePlacement(6, 9, Tile.valueOf('G'))); //  FG
		logger.debug("\n" + b.toString());
		assertEquals(6, score);
		
		// place multiple tiles
		score = b.place( // TWO
			new TilePlacement(10, 10, Tile.valueOf('T')),
			new TilePlacement(10, 12, Tile.valueOf('O'))
		);
		logger.debug("\n" + b.toString());
		assertEquals(4, score);
		
		// test different arrangements
		score = b.place( // IJK
			new TilePlacement(11, 6, Tile.valueOf('I')),
			new TilePlacement(11, 7, Tile.valueOf('J'))
		);
		logger.debug("\n" + b.toString());
		assertEquals(14, score);
		
		score = b.place( // STOP
			new TilePlacement(5, 5, Tile.valueOf('S')),
			new TilePlacement(6, 5, Tile.valueOf('T')),
			new TilePlacement(7, 5, Tile.valueOf('O'))
		);
		logger.debug("\n" + b.toString());
		assertEquals(12, score);
		
		score = b.place( // HIS
			new TilePlacement(10, 6, Tile.valueOf('H')),
			new TilePlacement(12, 6, Tile.valueOf('S'))
		);
		logger.debug("\n" + b.toString());
		assertEquals(14, score);
		
		score = b.place( // BCDEFGHiJKL
			new TilePlacement(2, 8, Tile.valueOf('B')),
			new TilePlacement(3, 8, Tile.valueOf('C'))
		);
		logger.debug("\n" + b.toString());
		assertEquals(33, score);

		score = b.place( // NOPQRH
			new TilePlacement(8, 3, Tile.valueOf('N')),
			new TilePlacement(8, 4, Tile.valueOf('O'))
		);
		logger.debug("\n" + b.toString());
		assertEquals(21, score);
		
		score = b.place( // BCDEFGHiJKLMNO
			new TilePlacement(13, 8, Tile.valueOf('M')),
			new TilePlacement(14, 8, Tile.valueOf('N')),
			new TilePlacement(15, 8, Tile.valueOf('O'))
		);
		logger.debug("\n" + b.toString());
		assertEquals(114, score);

		score = b.place( // NOPQRHIJ
			new TilePlacement(8, 9,  Tile.valueOf('I')),
			new TilePlacement(8, 10, Tile.valueOf('J'))
		);
		logger.debug("\n" + b.toString());
		assertEquals(29, score);

		score = b.place(
			new TilePlacement(1, 7, Tile.valueOf('X')),
			new TilePlacement(2, 7, Tile.valueOf('Y')),
			new TilePlacement(3, 7, Tile.valueOf('Z'))
		);
		logger.debug("\n" + b.toString());
		assertEquals(62, score);
		
		score = b.place(
			new TilePlacement(1, 9, Tile.valueOf('J')),
			new TilePlacement(2, 9, Tile.valueOf('K')),
			new TilePlacement(3, 9, Tile.valueOf('L'))
		);
		logger.debug("\n" + b.toString());
		assertEquals(42, score);
		
		score = b.place(
			new TilePlacement(9, 2, Tile.valueOf('Q')),
			new TilePlacement(9, 3, Tile.valueOf('V')),
			new TilePlacement(9, 4, Tile.valueOf('R'))
		);
		logger.debug("\n" + b.toString());
		assertEquals(30, score);

		score = b.place(
			new TilePlacement(13, 7, Tile.valueOf('A')),
			new TilePlacement(14, 7, Tile.valueOf('B')),
			new TilePlacement(15, 7, Tile.valueOf('C'))
		);
		logger.debug("\n" + b.toString());
		assertEquals(21, score);
		
		score = b.place(new TilePlacement(12, 7, Tile.valueOf('X')));
		logger.debug("\n" + b.toString());
		assertEquals(23, score);
		
		// place tiles where one tile is placed where one already exists
		try {
			b.place(
				new TilePlacement(4, 5, Tile.valueOf('A')),
				new TilePlacement(5, 5, Tile.valueOf('X'))
			);
			fail("IllegalTilePlacementException should have been thrown");
		} catch (IllegalTilePlacementException e) {
			// ensure that the legal tiles were not placed
			assertEquals(null, b.get(4, 5));
		}
		
		try {
			b.place(
				new TilePlacement(9, 10, Tile.valueOf('A')),
				new TilePlacement(9, 11, Tile.valueOf('X')),
				new TilePlacement(9, 12, Tile.valueOf('E'))
			);
			fail("IllegalTilePlacementException should have been thrown");
		} catch (IllegalTilePlacementException e) {
			// ensure that the legal tiles were not placed
			assertEquals(null, b.get(9, 10));
			assertEquals(null, b.get(9, 12));
		}
		
		// place tiles where no placed tile touches an existing tile
		try {
			b.place(
				new TilePlacement(1, 1, Tile.valueOf('H')),
				new TilePlacement(1, 2, Tile.valueOf('U')),
				new TilePlacement(1, 3, Tile.valueOf('H'))
			);
			fail("IllegalTilePlacementException should have been thrown");
		} catch (IllegalTilePlacementException e) {
			// ensure that the legal tiles were not placed
			assertEquals(null, b.get(1, 1));
			assertEquals(null, b.get(1, 2));
			assertEquals(null, b.get(1, 3));
		}
		
		try {
			b.place(
				new TilePlacement(1, 15, Tile.valueOf('H')),
				new TilePlacement(2, 15, Tile.valueOf('U')),
				new TilePlacement(3, 15, Tile.valueOf('H'))
			);
			fail("IllegalTilePlacementException should have been thrown");
		} catch (IllegalTilePlacementException e) {
			// ensure that the legal tiles were not placed
			assertEquals(null, b.get(1, 15));
			assertEquals(null, b.get(2, 15));
			assertEquals(null, b.get(3, 15));
		}
		
		// place tiles outside the bounds of the bored
		try {
    		b.place(
    			new TilePlacement(9999, 9999, Tile.valueOf('X'))
    		);
    		fail("IllegalTilePlacementException should have been thrown");
    	} catch (IllegalTilePlacementException e) {
    		// success
    	}
	}
	
	@Test
	public void testGet() throws Exception {
		
		Board b = loadBoard();
		
		assertEquals(null, b.get(1, 1));
		assertEquals(Tile.valueOf('P'), b.get(8, 5));
		assertEquals(Tile.valueOf('H'), b.get(8, 8));
		assertEquals(Tile.valueOf('D'), b.get(4, 8));
		assertEquals(new BlankTile('W'), b.get(10, 11));
		
		try {
			b.get(0, 1);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			b.get(1, 0);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			b.get(-1, 1);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			b.get(1, -1);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
	}
	
	@Test
	public void testGetRow() {
		//TODO
	}
	
	@Test
	public void testToArray() throws Exception {
	
		Board b = new Board(15, 15);
		assertTrue(TileUtils.equals(new Tile[15][15],  b.toArray()));

		BoardParser bp = new SimpleBoardParser(true);
		Tile[][] tiles = bp.parse(TEST_BOARD);
		b = new Board(tiles);
		assertTrue(TileUtils.equals(tiles,  b.toArray()));
	}
	
	@Test
	public void testToString() throws Exception {

		Board b = new Board(3, 3);
		assertEquals("*    \n  *  \n    *", b.toString());

		b = loadBoard();
		assertEquals(TEST_BOARD, b.toString());
	}
}

