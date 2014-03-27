package com.wordswithcheats.algorithm.gaddag;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wordswithcheats.algorithm.gaddag.exception.IllegalLetterException;
import com.wordswithcheats.algorithm.gaddag.exception.TrieNodeExistsException;

/**
 * A {@link Trie} implementation that uses {@link TrieNodeImpl} as the TrieNode implementation.
 * 
 * @author Matt Sidesinger
 */
public class TrieImpl implements Trie {
	
	private TrieNodeImpl root;
	
	// not required by the Interface
	private int nodeCount;
	private int wordCount;
	
	private static final long serialVersionUID = -5250179657952349013L;
	private static final Logger logger = LoggerFactory.getLogger(TrieNodeImpl.class);
	
	public TrieImpl() {
		this.root = TrieNodeImpl.createRootNode();
	}
	
	protected void setRoot(final TrieNodeImpl node) {
		this.root = node;
	}
	
	@Override
	public TrieNode getRoot() {
		return root;
	}
	
	@Override
	public void addWord(final String word) throws IllegalLetterException {
		
		Validate.notEmpty(word, "An empty word cannot be added");
		if (!StringUtils.isAlpha(word)) {
			throw new IllegalLetterException("Words can only contain letters A-Z, a-z: " + word);
		}
		
		// add this word
		char[] letters = word.trim().toCharArray();
		if (addNodes(letters)) {
			// if any new nodes were added, then this signifies that this word did not already exist
			this.wordCount++;
		}
		
		// add variations of this word that contain the cross anchor character
		/*
		 * Example for the word "language":
			ANGUAGE#L
			NGUAGE#AL
			GUAGE#NAL
			UAGE#GNAL
			AGE#UGNAL
			GE#AUGNAL
			E#GAUGNAL
		*/
		if (letters.length == 1) {
			return;
		}
		
		// create a working array and add the "cross anchor character" to the end
		char[] variations = new char[letters.length + 1];
		System.arraycopy(letters, 1, variations, 0, letters.length - 1);
		variations[variations.length - 2] = TrieNodeImpl.CROSS_ANCHOR_CHAR;
		variations[variations.length - 1] = letters[0];
		logger.debug("Adding rotations for \"{}\"", word.trim());
		
		char first = 0;
		int crossAnchorCharIndex = letters.length - 1;
		int i = 0;
		while (true) {
			
			if (logger.isDebugEnabled()) {
				logger.debug("Adding rotation: {}", String.valueOf(variations));
			}
			
			addNodes(variations);
			
			if (crossAnchorCharIndex <= 1) {
				break;
			}
			
			// rotate
			first = variations[0];
			// replace the cross anchor character
			variations[crossAnchorCharIndex] = first;
			crossAnchorCharIndex--;
			
			for (i = 0; i < crossAnchorCharIndex; i++) {
				// move characters backwards
				variations[i] = variations[i + 1]; 
			}
			variations[crossAnchorCharIndex] = TrieNodeImpl.CROSS_ANCHOR_CHAR;
		}
	}
	
	/**
	 * Adds nodes to the tree that don't already exist for the characters in the character array.
	 * <p>
	 * Assumes that the char[] is not <code>null<code>.
	 * 
	 * @param letters		The character array to add.
	 * 
	 * @return	Whether or not this action resulted in the addition of a new terminal node.
	 */
	protected boolean addNodes(final char[] letters) throws IllegalLetterException {
		
		assert (letters != null);
		
		TrieNode node = this.root;
		
		boolean terminal = false;
		TrieNode childNode = null;
		for (int offset = 0; offset < letters.length; offset++) {
			
			terminal = (offset + 1 == letters.length);
			childNode = node.getChildNode(letters[offset]);
			
			if (childNode == null) {
				try {
					node = node.addChildNode(letters[offset], terminal);
					this.nodeCount++;
				} catch (TrieNodeExistsException e) {
					// this should not happen since getChildNode was called prior
				}
			} else {
				// only update if not already terminal
				terminal = terminal && !childNode.isTerminal();
				if (terminal) {
					childNode.setTerminal(true);
				}
				node = childNode;
			}
		} // ~for
		
		return terminal;
	}

	public int getNodeCount() {
		return nodeCount;
	}

	public int getWordCount() {	
		return wordCount;
	}
}
