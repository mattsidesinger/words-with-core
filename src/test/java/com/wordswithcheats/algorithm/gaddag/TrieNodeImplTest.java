package com.wordswithcheats.algorithm.gaddag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wordswithcheats.algorithm.gaddag.exception.IllegalLetterException;
import com.wordswithcheats.algorithm.gaddag.exception.TrieNodeExistsException;

/**
 * Test case for the {@link TrieNodeImpl} class.
 * 
 * @author Matt Sidesinger
 */
public class TrieNodeImplTest {
	
	private static final Logger logger = LoggerFactory.getLogger(TrieNodeImplTest.class);
	
	@Test
	public void testIsRoot() {
		
		TrieNode node = new TrieNodeImpl();
		logger.info("Testing root node: {}", node.toString());
		
		assertTrue(node.isRoot());
		assertFalse(node.isTerminal());
		assertFalse(node.isCrossAnchorNode());
		assertEquals(0, node.getLetter());
		assertNull(node.getParentNode());
		
		node = TrieNodeImpl.createRootNode();
		logger.info("Testing root node: {}", node.toString());
		
		assertTrue(node.isRoot());
		assertFalse(node.isTerminal());
		assertFalse(node.isCrossAnchorNode());
		assertEquals(0, node.getLetter());
		assertNull(node.getParentNode());
	}
	
	@Test
	public void testIsCrossAnchorNode() throws Exception {
	
		TrieNode node = new TrieNodeImpl(TrieNodeImpl.CROSS_ANCHOR_CHAR, false, null);
		logger.info("Testing \"Cross Anchor\" node: {}", node.toString());
		
		assertFalse(node.isRoot());
		assertFalse(node.isTerminal());
		assertTrue(node.isCrossAnchorNode());
		assertEquals(TrieNodeImpl.CROSS_ANCHOR_CHAR, node.getLetter());
		assertNull(node.getParentNode());
	}
	
	@Test
	public void testHierarchy() throws Exception {
		
		TrieNodeImpl root = new TrieNodeImpl();
		
		// assert no children
		assertNotNull(root.getChildNodes()); // requirement: a collection of size 0 is returned
		assertEquals(0, root.getChildNodes().size());
		
		// test addChildNode
		TrieNode a = root.addChildNode('a', true);
		TrieNode b = root.addChildNode('B', false);
		
		assertEquals(root, a.getParentNode());
		assertEquals('a', a.getLetter());
		assertEquals('b', b.getLetter());
		assertTrue(a.isTerminal());
		assertFalse(b.isTerminal());
		
		// ensure they were added added
		
		// assert children
		assertEquals(2, root.getChildNodes().size());
		
		// test getChildNode
		assertNotNull(root.getChildNode('a'));
		// try the capital version
		assertNotNull(root.getChildNode('A'));
		// try the non capitalized version
		assertNotNull(root.getChildNode('b'));
		
		// test adding it again
		try {
			root.addChildNode('a', false);
			fail("TrieNodeExistsException should have been thrown: 'a' has already been added");
		} catch (TrieNodeExistsException e) {
			// success
		}
		// capitalized version
		try {
			root.addChildNode('A', false);
			fail("TrieNodeExistsException should have been thrown: 'a' has already been added");
		} catch (TrieNodeExistsException e) {
			// success
		}
		
		// test adding a bad character
		try {
			root.addChildNode('%', false);
			fail("IllegalLetterException should have been thrown: '%' is a not a letter");
		} catch (IllegalLetterException e) {
			// success
		}
		
		// getOrAddChildNode
		TrieNode a2 = root.getOrAddChildNode('a', false);
		assertEquals(a, a2);
		// ensure still terminal
		assertTrue(a.isTerminal());
		root.getOrAddChildNode('b', true);
		// ensure b is now terminal
		assertTrue(root.getChildNode('b').isTerminal());
		
		// add a new node and perform same tests as before
		TrieNode c = root.getOrAddChildNode('c', true);
		assertEquals(3, root.getChildNodes().size());
		assertEquals(root, c.getParentNode());
		assertEquals('c', c.getLetter());
		assertTrue(c.isTerminal());
		
		// addChildCrossAnchorNode
		TrieNode can = ((TrieNodeImpl) a).addChildCrossAnchorNode();
		
		assertFalse(can.isRoot());
		assertFalse(can.isTerminal());
		assertTrue(can.isCrossAnchorNode());
		assertEquals(TrieNodeImpl.CROSS_ANCHOR_CHAR, can.getLetter());
		assertEquals(a, can.getParentNode());
	}
	
	@Test
	public void testToString() throws Exception {

		TrieNode node = null;
		
		node = TrieNodeImpl.createRootNode();
		assertEquals("root=true, letter='', terminal=false, parent=[], children={}", node.toString());
		
		node = node.addChildNode('a', false);
		assertEquals("root=false, letter='a', terminal=false, parent=[root], children={}", node.toString());
		
		node = node.addChildNode('r', false);
		node.addChildNode('c', true); // arc
		node.addChildNode('e', true); // are
		node.addChildNode('t', true); // art
		assertEquals("root=false, letter='r', terminal=false, parent=['a'], children={'c','e','t'}", node.toString());
		
		node = node.getChildNode('e');
		assertEquals("root=false, letter='e', terminal=true, parent=['r'], children={}", node.toString());
	}
}
