package com.wordswithcheats.algorithm.gaddag;

import java.io.Serializable;

import com.wordswithcheats.algorithm.gaddag.exception.IllegalLetterException;

/**
 * A Trie is a container for {@link TrieNode} objects. A Trie also has methods to create and add nodes,
 * like when adding a word to the Trie.
 * 
 * @author Matt Sidesinger
 */
public interface Trie extends Serializable {

	/**
	 * Returns the root node for this Trie, unless a root has not yet been created, in which <code>null</code> will be
	 * returned.
	 * 
	 * @return		A {@link TrieNode} that returns <code>true</code for {@link TrieNode#isRoot()}
	 */
	public TrieNode getRoot();
	
	/**
	 * Breaks the word into characters and adds each one as a {@link TrieNode}.
	 * 
	 * @param word	The group of letters to add as nodes.
	 * 
	 * @throws IllegalLetterException	When the word contains a character that is not a letter a-z, A-Z.
	 * @throws IllegalArgumentException	If the word is null or blank.
	 */
	public void addWord(final String word) throws IllegalLetterException;
}
