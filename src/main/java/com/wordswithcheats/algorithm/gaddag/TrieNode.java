package com.wordswithcheats.algorithm.gaddag;

import java.util.Collection;

import com.wordswithcheats.algorithm.gaddag.exception.IllegalLetterException;
import com.wordswithcheats.algorithm.gaddag.exception.TrieNodeExistsException;

/**
 * Defines the basic methods that all nodes in a {@link Trie} should have.
 * 
 * @author Matt Sidesinger
 */
public interface TrieNode {

	/**
	 * Indicates whether this node is the root node for the Trie.  There is only one root node per Trie.
	 * 
	 * @return		<code>true</code> if this is the root node, otherwise <code>false</code>.
	 */
	public boolean isRoot();
	
	/**
	 * Sets whether this node is the last letter in word.
	 * 
	 * @param terminal		<code>true</code> if this is a terminal node, otherwise <code>false</code>.
	 */
	public void setTerminal(final boolean terminal);
	
	/**
	 * Indicates whether this node is the last letter in word.
	 * 
	 * @return		<code>true</code> if this is a terminal node, otherwise <code>false</code>.
	 */	
	public boolean isTerminal();
	
	/**
	 * Indicates whether this node is used a place holder node needed to exist when processing the board
	 * for possible words.
	 * 
	 * @return		<code>true</code> if this is a cross anchor node, otherwise <code>false</code>.
	 */
	public boolean isCrossAnchorNode();
	
	/**
	 * @return		The letter that this node represents.  If this is a root node, a char of value 0 will be
	 * 				returned.
	 */
	public char getLetter();
	
	/**
	 * If this is a child node, a {@link TrieNode} will be returned that contain a child node which is equal to this
	 * node.  If this child does not have a parent node, like in the case of a root node, <code>null</code> will
	 * be returned. 
	 * 
	 * @return		This node's parent node, if it has one.
	 */
	public TrieNode getParentNode();
	
	/**
	 * Adds a child node to this node with the given letter.
	 * 
	 * @param letter	The letter to be added as a child node.
	 * @param terminal	Whether this node is the last letter in a word.
	 * 
	 * @return		The child node that was just added.
	 * 
	 * @throws IllegalLetterException	When the word contains a character that is not a letter a-z, A-Z, or
	 * 									a cross anchor character.
	 * @throws TrieNodeExistsException	If a child node with the given letter already exists.
	 */
	public TrieNode addChildNode(final char letter, final boolean terminal)
		throws IllegalLetterException, TrieNodeExistsException;
	
	/**
	 * Finds a child node whose letter is equal to that of the letter parameter that was given.
	 * If the child node has not been defined as terminal, but the terminal parameter has been given to be
	 * <code>true</code>, then the node is marked as terminal.
	 * <p>
	 * If a child node with the letter cannot be found, then a child node is added with the given letter.
	 * 
	 * @param letter	The letter to find or to be added as a child node.
	 * 
	 * @return		The child node that was found or just added.
	 * 
	 * @throws IllegalLetterException	When the word contains a character that is not a letter a-z, A-Z, or
	 * 									a cross anchor character.
	 * @throws TrieNodeExistsException	If a child node with the given letter already exists.
	 */
	public TrieNode getOrAddChildNode(final char letter, final boolean terminal)
		throws IllegalLetterException, TrieNodeExistsException;
	
	/**
	 * Finds a child node whose letter is equal to that of the letter parameter that was given.
	 * 
	 * @param letter	The letter to search for in a child node.
	 * 
	 * @return	A node whose letter is equal to the letter parameter that was given.
	 */
	public TrieNode getChildNode(final char letter);

	/**
	 * Finds the child node that is the cross anchor node.
	 * 
	 * @return	The child cross anchor node, if one exists
	 */
	public TrieNode getCrossAnchorNode();
	
	/**
	 * Returns all nodes that are children of this node.  If this is a terminal node,
	 * then an empty {@link Collection} is returned.
	 * <p>
	 * The collection returned cannot be modified.
	 * 
	 * @return		The child nodes of this node as a {@link Collection}.
	 */
	public Collection<TrieNode> getChildNodes();
}
