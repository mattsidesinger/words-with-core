package com.wordswithcheats.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test case for the {@link BlankTile} class.
 * 
 * @author Matt Sidesinger
 */
public class BlankTileTest {

	@Test
	public void testBlankTile() {
		
		BlankTile tile = new BlankTile();
		assertEquals(0, tile.getLetter());
		
		tile = new BlankTile('A');
		assertEquals('a', tile.getLetter());
		
		tile = new BlankTile('b');
		assertEquals('b', tile.getLetter());
		
		try {
			tile = new BlankTile('4');
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
	}
	
	@Test
	public void testSetLetter() {
		
		BlankTile tile = new BlankTile();
		BlankTile.setLetter(tile, 'a');
		assertEquals('a', tile.getLetter());
		
		BlankTile.setLetter(tile, 'b');
		assertEquals('b', tile.getLetter());
		
		try {
			BlankTile.setLetter(tile, (char) 0);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			BlankTile.setLetter(tile, '4');
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
	}
	
	@Test
	public void testGetPoints() {

		// a blank tile is always worth 0 points
		
		BlankTile tile = new BlankTile();
		assertEquals(0, tile.getPointValue());
		
		tile = new BlankTile('a');
		assertEquals(0, tile.getPointValue());
	}
	
	@Test
	public void testToChar() {
	
		BlankTile tile = new BlankTile();
		assertEquals(0, tile.toChar());
		
		tile = new BlankTile('a');
		assertEquals('a', tile.toChar());
		
		tile = new BlankTile('A');
		assertEquals('a', tile.toChar());
		
		BlankTile.setLetter(tile, 'b');
		assertEquals('b', tile.toChar());
	}
	
	@Test
	public void testToString() {
		
		BlankTile tile = new BlankTile();
		assertEquals("[ :0]", tile.toString());

		tile = new BlankTile('a');
		assertEquals("[a:0]", tile.toString());
		
		tile = new BlankTile('A');
		assertEquals("[a:0]", tile.toString());
		
		BlankTile.setLetter(tile, 'b');
		assertEquals("[b:0]", tile.toString());
	}
	
	@Test
	public void testClone() throws Exception {
		
		BlankTile tile = new BlankTile();
		BlankTile clone = (BlankTile) tile.clone();
		assertTrue(tile != clone);
		assertEquals(tile, clone);
		
		tile = new BlankTile('a');
		clone = (BlankTile) tile.clone();
		assertTrue(tile != clone);
		assertEquals(tile, clone);
	}
}