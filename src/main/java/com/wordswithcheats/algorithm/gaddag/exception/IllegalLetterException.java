package com.wordswithcheats.algorithm.gaddag.exception;

import com.wordswithcheats.algorithm.gaddag.TrieNode;

/**
 * Thrown to specify that a word or {@link TrieNode} contains a character that is not
 * a letter a-z, A-Z.
 * 
 * @author Matt Sidesinger
 */
public class IllegalLetterException extends TrieException {

	private static final long serialVersionUID = -6126423880477628237L;

	public IllegalLetterException(String message) {
		super(message);
	}
}
