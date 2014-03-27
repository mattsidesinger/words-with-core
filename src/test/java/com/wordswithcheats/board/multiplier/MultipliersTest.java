package com.wordswithcheats.board.multiplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test case for the {@link Multipliers} class.
 * 
 * @author Matt Sidesinger
 */
public class MultipliersTest {
	
	@Test
	public void testAdd() {
	
		final Multiplier m = LetterMultiplier.instance(2);
		
		Multipliers ms = new Multipliers();
		ms.add(1, 2, m);
		ms.add(1000, 2000, m);
		
		try {
			ms.add(0, 1, m);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			ms.add(1, 0, m);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			ms.add(-1, 0, m);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			ms.add(1, -1, m);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			ms.add(1, 1, null);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
	}
	
	@Test
	public void testGet() {
		
		final Multiplier m = LetterMultiplier.instance(2);
		
		Multipliers ms = new Multipliers();
		ms.add(1, 2, m);
		Multiplier get = ms.get(1, 2);
		
		assertEquals(m, get);
		
		// obtain a multiplier that does not exist
		get = ms.get(2, 3);
		assertNull(get);
	}
	
	@Test
	public void testAddDoubleLetterScore() {
		
		Multipliers ms = new Multipliers();
		ms.addDoubleLetterScore(1, 2);
		Multiplier get = ms.get(1, 2);
		assertEquals(LetterMultiplier.instance(2), get);
	}
	
	@Test
	public void testAddTripleLetterScore() {
		
		Multipliers ms = new Multipliers();
		ms.addTripleLetterScore(1, 2);
		Multiplier get = ms.get(1, 2);
		assertEquals(LetterMultiplier.instance(3), get);
	}
	
	@Test
	public void testAddDoubleWordScore() {
		
		Multipliers ms = new Multipliers();
		ms.addDoubleWordScore(1, 2);
		Multiplier get = ms.get(1, 2);
		assertEquals(WordMultiplier.instance(2), get);
	}
	
	@Test
	public void testAddTripleWordScore() {
		
		Multipliers ms = new Multipliers();
		ms.addTripleWordScore(1, 2);
		Multiplier get = ms.get(1, 2);
		assertEquals(WordMultiplier.instance(3), get);
	}
	
	@Test
	public void testToString() {
		
		Multipliers ms = new Multipliers();
		assertEquals("{}", ms.toString());
		
		ms.addTripleLetterScore(3, 4);
		ms.addDoubleWordScore(2, 3);
		ms.addTripleWordScore(4, 5);
		ms.addDoubleLetterScore(1, 2);
		// ensure ordering of x,y coords
		assertEquals("{(1,2:DL),(2,3:DW),(3,4:TL),(4,5:TW)}", ms.toString());
	}
}
