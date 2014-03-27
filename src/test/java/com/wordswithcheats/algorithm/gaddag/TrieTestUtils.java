package com.wordswithcheats.algorithm.gaddag;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;

import junit.framework.AssertionFailedError;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;

/**
 * Utility class to assist in Trie related test cases.  It provides functionality similar to JUnit's assert
 * methods.
 * 
 * @author Matt Sidesinger
 */
public class TrieTestUtils {
	
	private static final String INDENT_STRING = "  ";
	private static final byte[] INDENT_STRING_AS_BYTES = INDENT_STRING.getBytes();
	private static final char NEW_LINE_CHAR = '\n';

	/**
	 * Determine if all letters from this String are contained in the Trie as a path.  A successful path denotes a
	 * parent-child relationship for each letter in the String, where the first letter has a matching child node of
	 * the root, and the second letter is a child of that node, and so forth.   The last node in the path must be
	 * a terminal node for this path to be considered to exist. 
	 * 
	 * @param trie		The Trie to check for the paths existence.
	 * @param word		The String, or the path, to check for.
	 * 
	 * @throws AssertionFailedError	If the path does not exist.
	 */
	public static void assertPathExists(final Trie trie, final String word) {
		
		Validate.notNull(trie, "Trie cannot be null");
		Validate.notEmpty(word, "String cannot be empty");
		
		assertPathExists(trie, word.trim().toCharArray());
	}

	/**
	 * Determine if all letters from this array are contained in the Trie as a path.  A successful path denotes a
	 * parent-child relationship for each letter in the array, where the first letter has a matching child node of
	 * the root, and the second letter is a child of that node, and so forth.  The last node in the path must be
	 * a terminal node for this path to be considered to exist. 
	 * 
	 * @param trie		The Trie to check for the paths existence.
	 * @param path		The char array, or the path, to check for.
	 * 
	 * @throws AssertionFailedError	If the path does not exist.
	 */
	public static void assertPathExists(final Trie trie, final char[] path) {
		
		Validate.notNull(trie, "Trie cannot be null");
		Validate.isTrue(path.length > 0, "char[] must have length > 0");
		
		TrieNode node = trie.getRoot();
		for (int i = 0; i < path.length; i++) {
			
			node = node.getChildNode(path[i]);
			
			if (node == null) {
				// determine what was found
				if (i == 0) {
					throw new AssertionFailedError(
							"The path could not be found. A child node of root could not be found for " + path[i]);	
				}
				char[] found = Arrays.copyOf(path, i);
				String foundAsString = ArrayUtils.toString(found);
				throw new AssertionFailedError(
						"The full path could not be found. The path could only be found up to " + i + " nodes: " +
						foundAsString);
			}
		} // ~for
		
		// make sure the last node is terminal
		if (!node.isTerminal()) {
    		throw new AssertionFailedError(
    			"The full path was found, but the last node was not terminal, therefore this is not a valid path");
		}
	}
	
	/**
	 * Prints a tree in an indented format.
	 * <p>
	 * [root]
	 * 	a
	 * 		n
	 * 			t
	 * 		r
	 * 			e
	 * 			t
	 * 	b
	 * 		a
	 * 			r
	 * 			t
	 * 
	 * @param trie		The Trie to print.
	 * @param out		The output stream to print the Trie to.
	 * 
	 * @throws IOException	When there is an error writing to the output stream.
	 */
	public static void printTrie(final Trie trie, final OutputStream out) throws IOException {
		if (trie == null) {
			out.write("null".getBytes());
			out.write(NEW_LINE_CHAR);
		} else {
			TrieNode root = trie.getRoot();
			if (root == null) {
				out.write("[no root defined]".getBytes());
				out.write(NEW_LINE_CHAR);
			} else {
				printNode(trie.getRoot(), out);
			}
		}
	}
	
	/**
	 * Prints a node and its children in an indented format.
	 * <p>
	 * a
	 * 	n
	 * 		t
	 * 	r
	 * 		e
	 * 		t
	 * 
	 * @param node	The node to print.
	 * @param out		The output stream to print the Trie to.
	 * 
	 * @throws IOException	When there is an error writing to the output stream.
	 */
	public static void printNode(final TrieNode node, final OutputStream out) throws IOException {
		if (node == null) {
			out.write("null".getBytes());
			out.write(NEW_LINE_CHAR);
		} else {
    		if (node.isRoot()) {
    			out.write("[root]".getBytes());
    			out.write(NEW_LINE_CHAR);
    			for (TrieNode cn : node.getChildNodes()) {
    				printNode(cn, out, 1);	
    			}
    		} else {
    			printNode(node, out, 0);
    		}
		}
	}
	
	/**
	 * Prints a node and its children in an indented format.
	 * 
	 * @param node	The node to print.
	 * @param out		The output stream to print the Trie to.
	 * @param indentCount	The number of tabs to start printing at.
	 * 
	 * @throws IOException	When there is an error writing to the output stream.
	 */
	private static void printNode(final TrieNode node, final OutputStream out, final int indentCount)
		throws IOException {
		
		if (node != null) {
			for (int i = 0; i < indentCount; i++) {
				out.write(INDENT_STRING_AS_BYTES);	
			}
			out.write(node.getLetter());
			out.write(NEW_LINE_CHAR);
			for (TrieNode cn : node.getChildNodes()) {
    			printNode(cn, out, indentCount + 1);	
    		}
		}
	}
	
	/**
	 * Prints the path to the given node.  Example, of the node 'x' was printed in the word "tux" the following
	 * would be printed: *-t-u-x
	 * 
	 * @param node		The node to print the path for.
	 */
	public static String getPathToNode(final TrieNode node) {
	
		StringBuilder path = new StringBuilder();
		
		if (node != null) {
			
			LinkedList<TrieNode> nodePath = new LinkedList<TrieNode>();
			// store the path in a linked list
			TrieNode currentNode = node;
			while (currentNode != null) {
				nodePath.push(currentNode);
				currentNode = currentNode.getParentNode();
			}
			
			// convert to String
			while (nodePath.size() > 0) {
				currentNode = nodePath.pop();
				if (currentNode.isRoot()) {
					path.append('*');
				} else {
					path.append(currentNode.getLetter());
				}
				
				if (nodePath.size() > 0) {
					path.append('-');
				}
			}
		}
		
		return path.toString();
	}
}
