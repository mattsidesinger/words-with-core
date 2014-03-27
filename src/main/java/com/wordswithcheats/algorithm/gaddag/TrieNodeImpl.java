package com.wordswithcheats.algorithm.gaddag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wordswithcheats.algorithm.gaddag.exception.IllegalLetterException;
import com.wordswithcheats.algorithm.gaddag.exception.TrieNodeExistsException;

/**
 * An implementation of the {@link TrieNode} backed by a {@link HashMap} and makes us of the
 * GADDAG algorithm.  The GADDAG algorithm uses a character, '{@value #CROSS_ANCHOR_CHARACTER}',
 * when determining what words can be placed and crossing over existing words on the board.
 * 
 * @author Matt Sidesinger
 */
public class TrieNodeImpl implements TrieNode, Serializable {
	
	private boolean root = false;
	private char letter = 0;
	private boolean terminal = false;
	private TrieNode parentNode;
	private Map<Character, TrieNode> childNodes;
	
	/**
	 * A special character that represents a crossing of the anchor during processing; if that makes much sense.
	 */
	public final static char CROSS_ANCHOR_CHAR = '#';
	private final static Character CROSS_ANCHOR_CHARACTER = Character.valueOf(CROSS_ANCHOR_CHAR);
	
	private static final long serialVersionUID = 8114686936121270932L;
	
	/**
	 * Private constructor creates a node with root as <code>true</code>,
	 * terminal as <code>false</code>, letter as 0, and parentNode as <code>null</code>.  In other words,
	 * a root node.
	 */
	protected TrieNodeImpl() {
		this.root = true;
		this.childNodes = new HashMap<Character, TrieNode>(26);
	}
	
	/**
	 * The constructor to be used to create a non-root node.
	 * 
	 * @param letter		The letter for this node to represent.
	 * @param parentNode	The required parent node.
	 * 
	 * @throws IllegalLetterException	When the word contains a character that is not a letter a-z, A-Z, or
	 * 									{@value #CROSS_ANCHOR_CHAR}
	 */
	protected TrieNodeImpl(final char letter, final boolean terminal, final TrieNode parentNode)
		throws IllegalLetterException {
		
		if (!Character.isLetter(letter) && letter != CROSS_ANCHOR_CHAR) {
			throw new IllegalLetterException(
					"letter must be a valud letter A-Z, a-z or the special character " + CROSS_ANCHOR_CHAR);
		}
		
		this.letter = Character.toLowerCase(letter);
		this.parentNode = parentNode;
		this.terminal = terminal;
	}
	
	/**
	 * Creates and returns a root node.
	 * 
	 * @return	A node that return <code>true</code> when {@link #isRoot()} is invoked.
	 */
	public static TrieNodeImpl createRootNode() {
		return new TrieNodeImpl();
	}
	
	protected void setRoot(final boolean root) {
		this.root = root;;
	}	
	
	@Override
	public boolean isRoot() {
		return root;
	}
	
	/**
	 * Determines whether this node's letter is the {@link #CROSS_ANCHOR_CHARACTER}.
	 * 
	 * @return	<code>true</code> if this node's letter is {@value #CROSS_ANCHOR_CHARACTER}
	 * 
	 * @see TrieNode#isCrossAnchorNode()
	 */
	@Override
	public boolean isCrossAnchorNode() {
		return (this.letter == CROSS_ANCHOR_CHAR);
	}
	
	protected void setLetter(final char letter) {
		this.letter = letter;
	}
	
	@Override
	public char getLetter() {
		return letter;
	}
	
	@Override
	public void setTerminal(final boolean terminal) {
		this.terminal = terminal;
	}
	
	@Override
	public boolean isTerminal() {
		return terminal;
	}
	
	protected void setParentNode(final TrieNode node) { 
		this.parentNode = node;
	}
	
	@Override
	public TrieNode getParentNode() {
		return parentNode;
	}
	
	@Override
	public TrieNode addChildNode(final char letter, final boolean terminal)
		throws IllegalLetterException, TrieNodeExistsException {
		
		char lowerLetter = Character.toLowerCase(letter);
		
		// see if this child node already exists
		TrieNode existingChildNode = getChildNode(lowerLetter);
		if (existingChildNode != null) {
			throw new TrieNodeExistsException(
					"a childe node for '" + lowerLetter + "' already exists", existingChildNode);
		}
		
		TrieNodeImpl childNode = new TrieNodeImpl(lowerLetter, terminal, this);
		if (childNodes == null) {
			childNodes = new HashMap<Character, TrieNode>(16);
		}
		childNodes.put(Character.valueOf(lowerLetter), childNode);
		
		return childNode;
	}
	
	@Override
	public TrieNode getOrAddChildNode(final char letter, final boolean terminal) throws IllegalLetterException {
		
		TrieNodeImpl node = null;
		
		char lowerLetter = Character.toLowerCase(letter);
		
		// see if this child node already exists
		node = (TrieNodeImpl) getChildNode(lowerLetter);
		if (node == null) {
			node = new TrieNodeImpl(lowerLetter, terminal, this);
    		if (childNodes == null) {
    			childNodes = new HashMap<Character, TrieNode>(16);
    		}
			childNodes.put(Character.valueOf(lowerLetter), node);			
		} else {
			if (terminal) {
				// update this node to be terminal
				node.setTerminal(true);
			}
		}
		
		return node;
	}
	
	/**
	 * Adds a child node with a letter equal to {@value #CROSS_ANCHOR_CHARACTER}.
	 * <p>
	 * A cross anchor node cannot be a terminal node.
	 * 
	 * @see #addChildNode(char, boolean)
	 */
	public TrieNode addChildCrossAnchorNode() throws IllegalLetterException, TrieNodeExistsException {
		return addChildNode(CROSS_ANCHOR_CHAR, false);
	}
	
	@Override
	public TrieNode getChildNode(final char letter) {
		
		TrieNode node = null;
		
		if (childNodes != null) {
			node = childNodes.get(Character.valueOf(Character.toLowerCase(letter)));
		}
		
		return node;
	}
	
	@Override
	public TrieNode getCrossAnchorNode() {
		
		TrieNode node = null;
		
		if (childNodes != null) {
			node = childNodes.get(CROSS_ANCHOR_CHARACTER);
		}
		
		return node;
	}
	
	@Override
	public Collection<TrieNode> getChildNodes() {
		if (childNodes == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableCollection(childNodes.values());
	}

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("root=");
		sb.append(root);
		
		sb.append(", letter='");
		if (letter != 0) {
			sb.append(letter);
		}
		
		sb.append("', terminal=");
		sb.append(this.terminal);
		
		sb.append(", parent=[");
		if (parentNode != null) {
			if (parentNode.isRoot()) {
				sb.append("root");
			} else {
				sb.append("'");
				sb.append(parentNode.getLetter());
				sb.append("'");
			}
		}
		
		sb.append("], children={");
		if (childNodes != null) {
			int i = 0;
			int length = childNodes.size();
			
			List<TrieNode> sortedChildNodes = new ArrayList<TrieNode>(childNodes.values());
			Collections.sort(sortedChildNodes, new Comparator<TrieNode>() {
				@Override
				public int compare(TrieNode n1, TrieNode n2) {
					int result = 0;
					if (n1 != null || n2 != null) {
						if (n1 == null) {
							result = -1;
						} else if (n2 == null) {
							result = 1;
						} else {
							result = n1.getLetter() - n2.getLetter();
						}
					}
					return result;
				}
			});
			
			for (TrieNode childNode : sortedChildNodes) {
				sb.append("'");
				sb.append(childNode.getLetter());
				sb.append("'");
				if ((i + 1) != length) {
					sb.append(",");
				}
				i++;
			}
		}
		sb.append("}");
		
		return sb.toString();
	}
}
