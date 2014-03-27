package com.wordswithcheats.dictionary.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

/**
 * Test case for the {@link FileBasedDictionaryReader} class.
 * 
 * @author Matt Sidesinger
 */
public class FileBasedDictionaryReaderTest {
	
	@Test
	public void testIterator() throws IOException {
		
		FileBasedDictionaryReader dr = null;
		
		try {
			dr = new FileBasedDictionaryReader("dictionary-test.txt");
			Iterator<String> i = dr.iterator();
		
    		assertNotNull("iterator was null", i);
    		
    		assertTrue(i.hasNext());
    		assertEquals("one", i.next());
    		assertTrue(i.hasNext());
    		assertEquals("two", i.next());
    		assertTrue(i.hasNext());
    		assertEquals("three", i.next());
    		assertTrue(i.hasNext());
    		assertEquals("four", i.next());
    		assertTrue(i.hasNext());
    		assertEquals("five", i.next());
    		assertTrue(i.hasNext());
    		assertEquals("six", i.next());
    		assertTrue(i.hasNext());
    		assertEquals("seven", i.next());
    		assertTrue(i.hasNext());
    		assertEquals("eight", i.next());
    		assertTrue(i.hasNext());
    		assertEquals("nine", i.next());
    		assertTrue(i.hasNext());
    		assertEquals("ten", i.next());
    		assertFalse("The last word has been reached.  hasNext() should return false.", i.hasNext());
    		
		} finally {
			dr.closeQuietly();
		}
	}
}
