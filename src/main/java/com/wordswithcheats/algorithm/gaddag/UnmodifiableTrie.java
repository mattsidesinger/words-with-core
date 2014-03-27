package com.wordswithcheats.algorithm.gaddag;

import org.apache.commons.lang.Validate;

import com.wordswithcheats.algorithm.gaddag.exception.IllegalLetterException;

/**
 * A {@link Trie} implementation that does not allow any type of modifications; this includes
 * adding nodes.  Any nodes that are returned will be {@link UnmodifiableTrieNode}
 * nodes.
 * 
 * @author Matt Sidesinger
 */
public class UnmodifiableTrie implements Trie {
	
	private Trie trie;
	
	private static final long serialVersionUID = -1674041257486561520L;
	
	public UnmodifiableTrie(Trie trie) {
		Validate.notNull(trie);
		this.trie = trie;
	}
	
	@Override
	public TrieNode getRoot() {
		return new UnmodifiableTrieNode(trie.getRoot());
	}
	
	@Override
	public void addWord(String word) throws IllegalLetterException {
		trie.addWord(word);
	}
}
