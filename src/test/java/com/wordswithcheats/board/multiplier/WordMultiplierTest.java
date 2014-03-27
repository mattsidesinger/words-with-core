package com.wordswithcheats.board.multiplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test case for the {@link WordMultiplier} class.
 * 
 * @author Matt Sidesinger
 */
public class WordMultiplierTest {
	
	@Test
	public void testInstance() {
		
		WordMultiplier m = WordMultiplier.instance(2);
		assertNotNull(m);
		assertEquals(2, m.getValue());
		
		try {
			m = WordMultiplier.instance(1);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			m = WordMultiplier.instance(0);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			m = WordMultiplier.instance(-1);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
	}
	
	@Test
	public void testToString() {
		
		assertEquals("DW", WordMultiplier.instance(2).toString());
		assertEquals("TW", WordMultiplier.instance(3).toString());
		assertEquals("4W", WordMultiplier.instance(4).toString());
	}
	
	@Test
	public void testCompareTo() {
		
		assertEquals(0, WordMultiplier.instance(3).compareTo(WordMultiplier.instance(3)));
		assertTrue(WordMultiplier.instance(3).compareTo(WordMultiplier.instance(2)) > 0);
		assertTrue(WordMultiplier.instance(3).compareTo(WordMultiplier.instance(4)) < 0);
		assertTrue(WordMultiplier.instance(3).compareTo(null) > 0);
	}
	
	@Test
	public void testEquals() {
		
		assertTrue(WordMultiplier.instance(2).equals(WordMultiplier.instance(2)));
		assertFalse(WordMultiplier.instance(2).equals(WordMultiplier.instance(3)));
		assertFalse(WordMultiplier.instance(2).equals(null));
	}
}
