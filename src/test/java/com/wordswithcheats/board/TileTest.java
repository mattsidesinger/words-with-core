package com.wordswithcheats.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test case for the {@link Tile} class.
 * 
 * @author Matt Sidesinger
 */
public class TileTest {

	@Test
	public void testTile() {

		Tile t = new Tile('a');
		assertEquals('a', t.getLetter());
		
		t = new Tile('A');
		assertEquals('a', t.getLetter());
		
		try {
			t = new Tile('4');
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			t = new Tile((char) 0);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
	}
	
	@Test
	public void testgetPointValue() {
		
		assertEquals(1, Tile.valueOf('a').getPointValue());
		assertEquals(3, Tile.valueOf('b').getPointValue());
		assertEquals(3, Tile.valueOf('c').getPointValue());
		assertEquals(2, Tile.valueOf('d').getPointValue());
		assertEquals(1, Tile.valueOf('e').getPointValue());
		assertEquals(4, Tile.valueOf('f').getPointValue());
		assertEquals(2, Tile.valueOf('g').getPointValue());
		assertEquals(4, Tile.valueOf('h').getPointValue());
		assertEquals(1, Tile.valueOf('i').getPointValue());
		assertEquals(8, Tile.valueOf('j').getPointValue());
		assertEquals(5, Tile.valueOf('k').getPointValue());
		assertEquals(1, Tile.valueOf('l').getPointValue());
		assertEquals(3, Tile.valueOf('m').getPointValue());
		assertEquals(1, Tile.valueOf('n').getPointValue());
		assertEquals(1, Tile.valueOf('o').getPointValue());
		assertEquals(3, Tile.valueOf('p').getPointValue());
		assertEquals(10, Tile.valueOf('q').getPointValue());
		assertEquals(1, Tile.valueOf('r').getPointValue());
		assertEquals(1, Tile.valueOf('s').getPointValue());
		assertEquals(1, Tile.valueOf('t').getPointValue());
		assertEquals(1, Tile.valueOf('u').getPointValue());
		assertEquals(4, Tile.valueOf('v').getPointValue());
		assertEquals(4, Tile.valueOf('w').getPointValue());
		assertEquals(8, Tile.valueOf('x').getPointValue());
		assertEquals(4, Tile.valueOf('y').getPointValue());
		assertEquals(10, Tile.valueOf('z').getPointValue());
	}
	
	@Test
	public void testValueOf() {
		
		assertTrue(Tile.valueOf('a') == Tile.valueOf('a'));
		assertTrue(Tile.valueOf('A') == Tile.valueOf('a'));
		assertTrue(Tile.valueOf('a') != new Tile('a'));
	}
	
	@Test
	public void testClone() throws Exception {
		
		Tile t1 = new Tile('a');
		Tile t2 = (Tile) t1.clone();
		assertEquals(t1, t2);
		assertTrue(t1 != t2);
		
		t1 = Tile.valueOf('a');
		t2 = (Tile) t1.clone();
		assertEquals(t1, t2);
		assertTrue(t1 != t2);
	}
	
	@Test
	public void testToChar() {
		
		Tile t = new Tile('a');
		assertEquals('A', t.toChar());
		
		t = new Tile('A');
		assertEquals('A', t.toChar());
		
		t = Tile.valueOf('a');
		assertEquals('A', t.toChar());
		
		t = Tile.valueOf('A');
		assertEquals('A', t.toChar());
	}
	
	@Test
	public void testToString() {
		
		Tile t = Tile.valueOf('a');
		assertEquals("[A:1]", t.toString());
		
		t = Tile.valueOf('A');
		assertEquals("[A:1]", t.toString());
		
		t = Tile.valueOf('q');
		assertEquals("[Q:10]", t.toString());
	}
	
	@Test
	public void testEquals() {
		
		Tile t1 = new Tile('a');
		Tile t2 = new Tile('a');
		assertTrue(t1.equals(t2));
		
		t1 = Tile.valueOf('a');
		t2 = Tile.valueOf('a');
		assertTrue(t1.equals(t2));
		
		t1 = new Tile('a');
		t2 = Tile.valueOf('a');
		assertTrue(t1.equals(t2));
		
		t1 = Tile.valueOf('a');
		t2 = null;
		assertFalse(t1.equals(t2));
		
		t1 = Tile.valueOf('a');
		t2 = Tile.valueOf('b');
		assertFalse(t1.equals(t2));
	}
}