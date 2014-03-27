package com.wordswithcheats.algorithm.gaddag;

import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wordswithcheats.algorithm.gaddag.exception.IllegalLetterException;
import com.wordswithcheats.dictionary.DictionaryReader;

/**
 * A factory used to generate a {@link Trie}. This abstract class does not provide an implementation but does provide
 * support for creating a {@link Trie}.
 * 
 * @author Matt Sidesinger
 */
public abstract class TrieFactory {
	
	private DictionaryReader dictionary;
	
	private static DecimalFormat numberFormat = new DecimalFormat("###,###,###");
	private static final Logger logger = LoggerFactory.getLogger(TrieFactory.class);
	
	public abstract Trie createTrie();
	
	/**
	 * Creates a {@link Trie} object by reading each word found in the {@link DictionaryReader}.
	 * 
	 * @param dictionary	The {@link DictionaryReader} to create the {@link Trie} from
	 * 
	 * @return	A {@link Trie} using the words from the {@link DictionaryReader}
	 */
	protected Trie createTrie(final DictionaryReader dictionary) {
		
		Trie t = createTrieImpl();
		
		try {
    		long startTime = System.currentTimeMillis();
    		
    		int wordCount = 0;
    		for (String word : dictionary) {
    			try {
        			t.addWord(word);
        			wordCount++;
    			} catch (IllegalLetterException e) {
    				logger.error("Illegal word: {}", word);
    			}
    		}
    		
    		long endTime = System.currentTimeMillis();
    		long duration = endTime - startTime;
    		logger.info("Loading Trie complete. Duration: {} seconds",
    					new DecimalFormat("####.##").format((double) duration / 1000));
    		
    		logger.info("Word count: {}", numberFormat.format(wordCount));
    		
		} catch (OutOfMemoryError e) {
			// Average node count per word is: 35.79
			int estimatedNodeCount = (int) (getDictionary().getWordCount() * 35.79);
			// Memory usage calculation:
    		// The Trie is estimated to take up 24 bytes.
    		// Each node, including the root, is estimated to take up to 40 bytes.
			long memory = 24 + 40 + (estimatedNodeCount * 40);
			logger.info("OutOfMemoryError.  Estimated memory needed: {}k", numberFormat.format((int) (memory / 1024)));
			
			throw e;
			
		} finally {
			if (dictionary != null) {
				dictionary.closeQuietly();
			}
		}
		
		return t;
	}
	
	protected Trie createTrieImpl() {
		return new TrieImpl();
	}
	
	protected DictionaryReader getDictionary() {
		return dictionary;
	}
	
	protected void setDictionary(final DictionaryReader dictionary) {
		this.dictionary = dictionary;
	}
}
