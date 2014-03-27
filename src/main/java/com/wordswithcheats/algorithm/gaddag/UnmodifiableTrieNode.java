package com.wordswithcheats.algorithm.gaddag;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.lang.Validate;

/**
 * A {@link TrieNode} implementation that does not allow any type of modifications; this includes
 * adding nodes.
 * 
 * @author Matt Sidesinger
 */
public class UnmodifiableTrieNode implements TrieNode, Serializable {
	
	private TrieNode node;
	
	private static final long serialVersionUID = 6293488783867444031L;
	
	public UnmodifiableTrieNode(TrieNode node) {
		Validate.notNull(node);
		this.node = node;
	}

	@Override
	public boolean isRoot() {
		return node.isRoot();
	}
	
	/**
	 * Not supported.
	 * 
	 * @throws UnsupportedOperationException	Always.
	 */
	@Override
	public void setTerminal(boolean terminal) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isTerminal() {
		return node.isTerminal();
	}

	@Override
	public char getLetter() {
		return node.getLetter();
	}
	
	@Override
	public boolean isCrossAnchorNode() {
		return node.isCrossAnchorNode();
	}

	/**
	 * Not supported.
	 * 
	 * @throws UnsupportedOperationException	Always.
	 */
	@Override
	public TrieNode addChildNode(char letter, boolean terminal) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TrieNode getParentNode() {
		TrieNode parentNode = node.getParentNode();
		if (parentNode != null) {
			return new UnmodifiableTrieNode(parentNode); 
		}
		return parentNode;
	}

	@Override
	public TrieNode getChildNode(char letter) {
		TrieNode childNode = node.getChildNode(letter);
		if (childNode != null) {
			return new UnmodifiableTrieNode(childNode); 
		}
		return childNode;
	}
	
	/**
	 * Not supported.
	 * 
	 * @throws UnsupportedOperationException	Always.
	 */
	@Override
	public TrieNode getOrAddChildNode(char letter, boolean terminal) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public TrieNode getCrossAnchorNode() {
		return node.getCrossAnchorNode();
	}

	@Override
	public Collection<TrieNode> getChildNodes() {
		return node.getChildNodes();
	}

	@Override
	public String toString() {
		return node.toString();
	}

	@Override
	public int hashCode() {
		return node.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return node.equals(obj);
	}
}
