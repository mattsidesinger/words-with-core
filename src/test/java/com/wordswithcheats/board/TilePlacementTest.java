package com.wordswithcheats.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test case for the {@link TilePlacement} class.
 * 
 * @author Matt Sidesinger
 */
public class TilePlacementTest {
	
	@Test
	public void testTilePlacement() {
		
		TilePlacement tp = new TilePlacement(1, 2, Tile.valueOf('a'));
		assertEquals(1, tp.getX());
		assertEquals(2, tp.getY());
		assertEquals(Tile.valueOf('a'), tp.getTile());
		
		try {
			tp = new TilePlacement(0, 2, Tile.valueOf('a'));
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			tp = new TilePlacement(1, -2, Tile.valueOf('a'));
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			tp = new TilePlacement(1, 2, null);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
	}
	
	@Test
	public void testToString() {
		
		TilePlacement tp = new TilePlacement(1, 2, Tile.valueOf('a'));
		assertEquals("(1,2):[A:1]", tp.toString());
	}
}
