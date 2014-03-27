package com.wordswithcheats.algorithm.gaddag.exception;

import org.apache.commons.lang.Validate;

import com.wordswithcheats.algorithm.gaddag.Trie;
import com.wordswithcheats.algorithm.gaddag.TrieNode;

/**
 * Thrown when a {@link TrieNode} being added to a {@link Trie} already exists.
 * 
 * @author Matt Sidesinger
 */
public class TrieNodeExistsException extends TrieException {

	private TrieNode node;
	
	private static final long serialVersionUID = -6465747029918841895L;
	
	/**
	 * Creates a TrieNodeExistsException with the given message and the node that already exists in the {@link Trie},
	 * not the one trying to be added.
	 * 
	 * @param message	An error message.
	 * @param node		The already existing node.
	 * 
	 * @throws IllegalArgumentException		If the node is null.
	 */
	public TrieNodeExistsException(String message, TrieNode node) {
		super(message);
		Validate.notNull(node, "TrieNode cannot be null");
		setNode(node);
	}
	
	/**
	 * @return the {@link TrieNode}
	 */
	public TrieNode getNode() {	
		return node;
	}

	/**
	 * @param node the {@link TrieNode} to set
	 */
	protected void setNode(TrieNode node) {
		this.node = node;
	}
}
