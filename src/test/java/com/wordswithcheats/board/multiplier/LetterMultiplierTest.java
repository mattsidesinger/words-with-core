package com.wordswithcheats.board.multiplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test case for the {@link LetterMultiplier} class.
 * 
 * @author Matt Sidesinger
 */
public class LetterMultiplierTest {
	
	@Test
	public void testInstance() {
		
		LetterMultiplier m = LetterMultiplier.instance(2);
		assertNotNull(m);
		assertEquals(2, m.getValue());
		
		try {
			m = LetterMultiplier.instance(1);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			m = LetterMultiplier.instance(0);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			m = LetterMultiplier.instance(-1);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
	}
	
	@Test
	public void testToString() {
		
		assertEquals("DL", LetterMultiplier.instance(2).toString());
		assertEquals("TL", LetterMultiplier.instance(3).toString());
		assertEquals("4L", LetterMultiplier.instance(4).toString());
	}
	
	@Test
	public void testCompareTo() {
		
		assertEquals(0, LetterMultiplier.instance(3).compareTo(LetterMultiplier.instance(3)));
		assertTrue(LetterMultiplier.instance(3).compareTo(LetterMultiplier.instance(2)) > 0);
		assertTrue(LetterMultiplier.instance(3).compareTo(LetterMultiplier.instance(4)) < 0);
		assertTrue(LetterMultiplier.instance(3).compareTo(null) > 0);
	}
	
	@Test
	public void testEquals() {
		
		assertTrue(LetterMultiplier.instance(2).equals(LetterMultiplier.instance(2)));
		assertFalse(LetterMultiplier.instance(2).equals(LetterMultiplier.instance(3)));
		assertFalse(LetterMultiplier.instance(2).equals(null));
	}
}
