package com.wordswithcheats.algorithm.gaddag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.wordswithcheats.algorithm.gaddag.exception.IllegalLetterException;

/**
 * Test case for the {@link TrieImpl} class.
 * 
 * @author Matt Sidesinger
 */
public class TrieImplTest {
	
	@Test
	public void testTrieImpl() {
		
		Trie t = new TrieImpl();
		
		assertNotNull(t.getRoot());
		assertTrue(t.getRoot().isRoot());
	}
	
	@Test
	public void testAddWord() throws Exception {
		
		Trie t = new TrieImpl();
		
		// test one word
		t.addWord("be");
		
		// verify
		TrieTestUtils.assertPathExists(t, "be");
		// verify rotations were added
		TrieTestUtils.assertPathExists(t, "e#b");
		
		// test one word
		t.addWord("test");

		// verify
		TrieTestUtils.assertPathExists(t, "test");
		// verify rotations were added
		TrieTestUtils.assertPathExists(t, "est#t");
		TrieTestUtils.assertPathExists(t, "st#et");
		TrieTestUtils.assertPathExists(t, "t#set");
		
		// test another independent word
		t.addWord("quiz");
		
		// verify both exist
		TrieTestUtils.assertPathExists(t, "test");
		TrieTestUtils.assertPathExists(t, "quiz");
		
		// add a word that shares a path
		t.addWord("tested");
		
		// add a second word that shares a path
		t.addWord("quizes");
		
		// verify that the path is shared by looking at node count
		assertEquals(98, ((TrieImpl) t).getNodeCount());
		// verify rotations were added
		TrieTestUtils.assertPathExists(t, "uizes#q");
		TrieTestUtils.assertPathExists(t, "izes#uq");
		TrieTestUtils.assertPathExists(t, "zes#iuq");
		TrieTestUtils.assertPathExists(t, "es#ziuq");
		TrieTestUtils.assertPathExists(t, "s#eziuq");
		
		// verify that all words exist
		assertEquals(5, ((TrieImpl) t).getWordCount());
		
		// add a word that already exists
		t.addWord("test");
		
		// verify word count again
		assertEquals(5, ((TrieImpl) t).getWordCount());

		try {
			t.addWord(null);
			fail("Cannot add <null> word");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			t.addWord("   ");
			fail("Cannot add empty word");
		} catch (IllegalLetterException e) {
			// success
		}
		
		try {
			t.addWord("123");
			fail("Cannot add numbers as a word");
		} catch (IllegalLetterException e) {
			// success
		}
		
		try {
			t.addWord("!@#$%^&*()");
			fail("Cannot add special characters as a word.");			
		} catch (IllegalLetterException e) {
			// success
		}
	}
}
