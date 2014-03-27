package com.wordswithcheats.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Test case for the {@link TileBag} class.
 * 
 * @author Matt Sidesinger
 */
public class TileBagTest {
	
	@Test
	public void testTileBag() {
		
		TileBag bag = new TileBag();
		assertTrue(Arrays.equals(TileBag.DEFAULT_TILE_COUNTS, bag.getTileCounts()));
		assertEquals(TileBag.DEFAULT_BLANK_TILE_COUNT, bag.getBlankTileCount());
		
		Map<Tile, Integer> tileCounts = new HashMap<Tile, Integer>();
		tileCounts.put(Tile.valueOf('a'), Integer.valueOf(1));
		bag = new TileBag(tileCounts);
		assertEquals(1, bag.getTileCount(Tile.valueOf('a')));
		assertEquals(0, bag.getTileCount(Tile.valueOf('b')));
		assertEquals(0, bag.getTileCount(new BlankTile()));

		tileCounts.put(new BlankTile(), Integer.valueOf(1));
		bag = new TileBag(tileCounts);
		assertEquals(1, bag.getTileCount(new BlankTile()));
		
		// ensure that a Tile with a count of 0 can explicitly be added
		tileCounts.put(Tile.valueOf('z'), Integer.valueOf(0));
		assertEquals(0, bag.getTileCount(Tile.valueOf('z')));
		
		tileCounts = new HashMap<Tile, Integer>();
		try {
			bag = new TileBag(tileCounts);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		tileCounts = null;
		try {
			bag = new TileBag(tileCounts);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
	}
	
	@Test
	public void testGetSetTileCount () {
		
		TileBag bag = new TileBag();
		assertEquals(9, bag.getTileCount(Tile.valueOf('a')));
		assertEquals(2, bag.getTileCount(new BlankTile()));
		
		bag.setTileCount('x', 11);
		assertEquals(11,  bag.getTileCount(Tile.valueOf('x')));
		
		bag.setTileCount(Tile.valueOf('y'), 22);
		assertEquals(22,  bag.getTileCount(Tile.valueOf('y')));
		
		bag.setTileCount(new BlankTile(), 33);
		assertEquals(33,  bag.getTileCount(new BlankTile()));
		
		try {
			bag.setTileCount('@', 1);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		bag.setTileCount('x', 0);
		assertEquals(0,  bag.getTileCount(Tile.valueOf('x')));
		
		try {
			bag.setTileCount('x', -1);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			bag.getTileCount(null);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
	}
	
	@Test
	public void testIsEmpty() {
		
		TileBag bag = new TileBag();
		assertFalse(bag.isEmpty());
		
		Map<Tile, Integer> tileCounts = new HashMap<Tile, Integer>();
		tileCounts.put(Tile.valueOf('a'), Integer.valueOf(1));
		bag = new TileBag(tileCounts);
		assertFalse(bag.isEmpty());
		bag.fillRack(new Rack());
		assertTrue(bag.isEmpty());
	}
	
	@Test
	public void testGetTilesRemaining() {
		
		Map<Tile, Integer> tileCounts = new HashMap<Tile, Integer>();
		tileCounts.put(Tile.valueOf('a'), Integer.valueOf(1));
		
		TileBag bag = new TileBag(tileCounts);
		assertEquals(1, bag.getTilesRemaining().length);
		
		bag.fillRack(new Rack());
		assertEquals(0, bag.getTilesRemaining().length);
		
		tileCounts.put(new BlankTile(), Integer.valueOf(1));
		bag = new TileBag(tileCounts);
		assertEquals(2, bag.getTilesRemaining().length);
	}
	
	@Test
	public void testGetTilesRemainingAsList() {
	
		Map<Tile, Integer> tileCounts = new HashMap<Tile, Integer>();
		tileCounts.put(Tile.valueOf('a'), Integer.valueOf(1));
		
		TileBag bag = new TileBag(tileCounts);
		assertEquals(1, bag.getTilesRemainingAsList().size());
		
		bag.fillRack(new Rack());
		assertEquals(0, bag.getTilesRemainingAsList().size());
		
		tileCounts.put(new BlankTile(), Integer.valueOf(1));
		bag = new TileBag(tileCounts);
		assertEquals(2, bag.getTilesRemainingAsList().size());
	}
	
	@Test
	public void testFillRack() {
		
		TileBag bag = new TileBag();
		int startTileCount = bag.getTilesRemaining().length;
		Rack r1 = new Rack();
		bag.fillRack(r1);
		assertEquals(7, r1.tileCount());
		assertEquals(startTileCount - 7, bag.getTilesRemaining().length);
		
		// try an empty TileBag
		Map<Tile, Integer> tileCounts = new HashMap<Tile, Integer>();
		tileCounts.put(Tile.valueOf('a'), Integer.valueOf(1));
		bag = new TileBag(tileCounts);
		r1 = new Rack();
		bag.fillRack(r1);
		assertTrue(bag.isEmpty());
		int rackTileCount = r1.tileCount();
		bag.fillRack(r1);
		assertEquals(rackTileCount, r1.tileCount());
		
		// ensure randomization
		bag = new TileBag();
		TileBag bag2 = new TileBag();
		r1 = new Rack();
		Rack r2 = new Rack();
		bag.fillRack(r1);
		bag2.fillRack(r2);
		// ensure not equal
		assertFalse(Arrays.equals(r1.toArray(), r2.toArray()));
		
		// certain methods are not allowed once fillRack() has been called
		try {
			bag.setTileCount('a', 1);
			fail("IllegalStateException should have been thrown");
		} catch (IllegalStateException e) {
			// success
		}
		
		try {
			bag.setTileCount(Tile.valueOf('a'), 1);
			fail("IllegalStateException should have been thrown");
		} catch (IllegalStateException e) {
			// success
		}
	}
}
