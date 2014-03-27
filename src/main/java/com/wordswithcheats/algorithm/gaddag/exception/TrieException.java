package com.wordswithcheats.algorithm.gaddag.exception;

import org.apache.commons.lang.Validate;

import com.wordswithcheats.algorithm.gaddag.Trie;
import com.wordswithcheats.algorithm.gaddag.TrieNode;

/**
 * The base class for Exceptions thrown by {@link Trie} and {@link TrieNode} classes.
 * 
 * @author Matt Sidesinger
 */
public abstract class TrieException extends Exception {

	private static final long serialVersionUID = -2957481629627476325L;
	
	public TrieException(String message) {
		super(message);
		Validate.notEmpty(message, "a message must be provided");
	}
}
