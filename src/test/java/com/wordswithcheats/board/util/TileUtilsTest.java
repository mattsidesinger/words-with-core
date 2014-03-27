package com.wordswithcheats.board.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.wordswithcheats.board.BlankTile;
import com.wordswithcheats.board.Tile;

/**
 * Test case for the {@link TileUtils} class.
 * 
 * @author Matt Sidesinger
 */
public class TileUtilsTest {
	
	/**
	 * Tests the {@link TileUtils#toCharArray(Tile[][])} method.
	 */
	@Test
	public void testToCharArray() {
		
		Tile[][] tiles = null;
		char[][] chars = null;
		
		chars = TileUtils.toCharArray(null);
		assertNull(chars);
		
		chars = TileUtils.toCharArray(new Tile[0][0]);
		assertEquals(0, chars.length);
		
		tiles = new Tile[][] { {null} };
		chars = TileUtils.toCharArray(tiles);
		char[][] confirm = new char[][] { {' '} };
		assertTrue(Arrays.deepEquals(confirm, chars));
		
		tiles = new Tile[][] { {null} };
		chars = TileUtils.toCharArray(tiles);
		confirm = new char[][] { {'a'} };
		assertFalse(Arrays.deepEquals(confirm, chars));
		
		tiles = new Tile[][] { {Tile.valueOf('a')} };
		chars = TileUtils.toCharArray(tiles);
		confirm = new char[][] { {'A'} };
		assertTrue(Arrays.deepEquals(confirm, chars));
		
		tiles = new Tile[][] { {Tile.valueOf('a')} };
		chars = TileUtils.toCharArray(tiles);
		confirm = new char[][] { {'B'} };
		assertFalse(Arrays.deepEquals(confirm, chars));
		
		tiles = new Tile[][] { { new BlankTile('b') } };
		chars = TileUtils.toCharArray(tiles);
		confirm = new char[][] { {'b'} };
		assertTrue(Arrays.deepEquals(confirm, chars));
		
		tiles = new Tile[][] { { new BlankTile('a') } };
		chars = TileUtils.toCharArray(tiles);
		confirm = new char[][] { {'b'} };
		assertFalse(Arrays.deepEquals(confirm, chars));
		
		tiles = new Tile[][] { { Tile.valueOf('A'), new BlankTile('b') } };
		chars = TileUtils.toCharArray(tiles);
		confirm = new char[][] { {'A', 'b'} };
		assertTrue(Arrays.deepEquals(confirm, chars));
		
		tiles = new Tile[][] { { Tile.valueOf('A'), new BlankTile('b') } };
		chars = TileUtils.toCharArray(tiles);
		confirm = new char[][] { {'b', 'A'} };
		assertFalse(Arrays.deepEquals(confirm, chars));
		
		tiles = new Tile[][] { { Tile.valueOf('A'), null}, {null, new BlankTile('b') } };
		chars = TileUtils.toCharArray(tiles);
		confirm = new char[][] { {'A', ' '}, {' ', 'b'} };
		assertTrue(Arrays.deepEquals(confirm, chars));
		
		tiles = new Tile[][] { { Tile.valueOf('A'), null}, {null, new BlankTile('b') } };
		chars = TileUtils.toCharArray(tiles);
		confirm = new char[][] { {' ', 'A'}, {'b', ' '} };
		assertFalse(Arrays.deepEquals(confirm, chars));
	}
	
	/**
	 * Tests the {@link TileUtils#equals(Tile, char)}  method.
	 */
	@Test
	public void testEquals1() {
		
		assertTrue(TileUtils.equals(null, (char) 0));
		assertTrue(TileUtils.equals(null, ' '));
		assertFalse(TileUtils.equals(null, 'a'));
		assertFalse(TileUtils.equals(Tile.valueOf('a'), (char) 0));
		assertFalse(TileUtils.equals(Tile.valueOf('b'), ' '));
		assertTrue(TileUtils.equals(Tile.valueOf('a'), 'A'));
		assertTrue(TileUtils.equals(Tile.valueOf('A'), 'A'));
		assertTrue(TileUtils.equals(new BlankTile(), (char) 0));
		assertTrue(TileUtils.equals(new BlankTile('a'), 'a'));
		assertTrue(TileUtils.equals(new BlankTile('A'), 'a'));
		assertFalse(TileUtils.equals(new BlankTile('a'), (char) 0));
		assertFalse(TileUtils.equals(new BlankTile(), 'a'));
	}

	/**
	 * Tests the {@link TileUtils#equals(Tile, Tile)} method.
	 */
	@Test
	public void testEquals2() {
		
		assertTrue(TileUtils.equals((Tile) null, (Tile) null));
		assertFalse(TileUtils.equals(Tile.valueOf('a'), (Tile) null));
		assertFalse(TileUtils.equals((Tile) null, Tile.valueOf('a')));
		assertTrue(TileUtils.equals(new Tile('a'), new Tile('a')));
		assertFalse(TileUtils.equals(new Tile('a'), new Tile('b')));
		assertFalse(TileUtils.equals(new Tile('b'), new Tile('a')));
	}
	
	/**
	 * Tests the {@link TileUtils#equals(Tile[][], char[][])} method.
	 */
	@Test
	public void testEquals3() {

		Tile[][] tiles = null;
		char[][] chars = null;
		
		assertTrue(TileUtils.equals(tiles, chars));
		
		tiles = new Tile[][] { {Tile.valueOf('a')} };
		chars = null;
		assertFalse(TileUtils.equals(tiles, chars));
		
		tiles = new Tile[][] { {null} };
		chars = new char[][] { {'a'} };
		assertFalse(TileUtils.equals(tiles, chars));
		
		tiles = new Tile[][] { {null} };
		chars = new char[][] { {' '} };
		assertTrue(TileUtils.equals(tiles, chars));
		
		tiles = new Tile[][] { {null} };
		chars = new char[][] { {'a'} };
		assertFalse(TileUtils.equals(tiles, chars));
		
		tiles = new Tile[][] { {Tile.valueOf('a')} };
		chars = new char[][] { {'A'} };
		assertTrue(TileUtils.equals(tiles, chars));
		
		tiles = new Tile[][] { {Tile.valueOf('a')} };
		chars = new char[][] { {'B'} };
		assertFalse(TileUtils.equals(tiles, chars));
		
		tiles = new Tile[][] { { new BlankTile('b') } };
		chars = new char[][] { {'b'} };
		assertTrue(TileUtils.equals(tiles, chars));
		
		tiles = new Tile[][] { { new BlankTile('a') } };
		chars = new char[][] { {'b'} };
		assertFalse(TileUtils.equals(tiles, chars));
		
		tiles = new Tile[][] { { Tile.valueOf('A'), new BlankTile('b') } };
		chars = new char[][] { {'A', 'b'} };
		assertTrue(TileUtils.equals(tiles, chars));
		
		tiles = new Tile[][] { { Tile.valueOf('A'), new BlankTile('b') } };
		chars = new char[][] { {'b', 'A'} };
		assertFalse(TileUtils.equals(tiles, chars));
		
		tiles = new Tile[][] { { Tile.valueOf('A'), null}, {null, new BlankTile('b') } };
		chars = new char[][] { {'A', ' '}, {' ', 'b'} };
		assertTrue(TileUtils.equals(tiles, chars));
		
		tiles = new Tile[][] { { Tile.valueOf('A'), null}, {null, new BlankTile('b') } };
		chars = new char[][] { {' ', 'A'}, {'b', ' '} };
		assertFalse(TileUtils.equals(tiles, chars));
	}

	/**
	 * Tests the {@link TileUtils#equals(Tile[][], Tile[][])} method.
	 */
	public void testEquals4() {

		Tile[][] tiles1 = null;
		Tile[][] tiles2 = null;
		
		assertTrue(TileUtils.equals(tiles1, tiles2));
		
		tiles1 = new Tile[][] { {Tile.valueOf('a')} };
		tiles2 = null;
		assertFalse(TileUtils.equals(tiles1, tiles2));
		
		tiles1 = null;
		tiles2 = new Tile[][] { {Tile.valueOf('a')} };
		assertFalse(TileUtils.equals(tiles1, tiles2));
		
		tiles1 = new Tile[][] { {null} };
		tiles2 = new Tile[][] { {Tile.valueOf('a')} };
		assertFalse(TileUtils.equals(tiles1, tiles2));

		tiles1 = new Tile[][] { {Tile.valueOf('a')} };
		tiles2 = new Tile[][] { {null} };
		assertFalse(TileUtils.equals(tiles1, tiles2));
		
		tiles1 = new Tile[][] { {new Tile('a')} };
		tiles2 = new Tile[][] { {new Tile('a')} };
		assertTrue(TileUtils.equals(tiles1, tiles2));
		
		tiles1 = new Tile[][] { {Tile.valueOf('a')} };
		tiles2 = new Tile[][] { {Tile.valueOf('B')} };
		assertFalse(TileUtils.equals(tiles1, tiles2));
		
		tiles1 = new Tile[][] { { new BlankTile('a') } };
		tiles2 = new Tile[][] { {Tile.valueOf('a')} };
		assertFalse(TileUtils.equals(tiles1, tiles2));
		assertFalse(TileUtils.equals(tiles2, tiles1));
		
		tiles1 = new Tile[][] { { Tile.valueOf('A'), new BlankTile('b') } };
		tiles2 = new Tile[][] { { Tile.valueOf('A'), new BlankTile('b') } };
		assertTrue(TileUtils.equals(tiles1, tiles2));
		
		tiles1 = new Tile[][] { { Tile.valueOf('A'), new BlankTile('b') } };
		tiles1 = new Tile[][] { { Tile.valueOf('b'), new BlankTile('A') } };
		assertFalse(TileUtils.equals(tiles1, tiles2));
		
		tiles1 = new Tile[][] { { Tile.valueOf('A'), null}, {null, new BlankTile('b') } };
		tiles1 = new Tile[][] { { Tile.valueOf('A'), null}, {null, new BlankTile('b') } };
		assertTrue(TileUtils.equals(tiles1, tiles2));
		
		tiles1 = new Tile[][] { { Tile.valueOf('A'), null}, {null, new BlankTile('b') } };
		tiles1 = new Tile[][] { { null, Tile.valueOf('A')}, {new BlankTile('b'), null } };
		assertFalse(TileUtils.equals(tiles1, tiles2));
	}
}
