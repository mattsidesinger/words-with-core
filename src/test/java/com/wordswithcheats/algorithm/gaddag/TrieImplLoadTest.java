package com.wordswithcheats.algorithm.gaddag;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;

import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wordswithcheats.algorithm.gaddag.exception.IllegalLetterException;
import com.wordswithcheats.dictionary.file.FileBasedDictionaryReader;

/**
 * This class does not contain any unit tests, but can be used to determine
 * how long it takes to load the default dictionary file.
 * 
 * @author Matt Sidesinger
 */
@Ignore
public class TrieImplLoadTest {
	
	private static DecimalFormat numberFormat = new DecimalFormat("###,###,###");
	private static final Logger logger = LoggerFactory.getLogger(TrieImplLoadTest.class);
	
	public static void main(String[] args) {
		
		FileBasedDictionaryReader dictionary = null;
		int lineCount = 0;
		TrieImpl t = new TrieImpl();
		
		try {
			
			URL url = TrieImplLoadTest.class.getClassLoader().getResource(FileBasedDictionaryReader.DEFAULT_FILE_NAME);
			File file = null;
			long fileLength = 0;
			try {
				file = new File(url.toURI());
				fileLength = file.length();
				logger.info("Loading file: {}.  File size: {}k", file.getAbsolutePath(), numberFormat.format((int) (fileLength / 1024)));
			} catch (URISyntaxException e) {
				// ignore
			}
    		dictionary = new FileBasedDictionaryReader(file);
    		
    		// count the number if lines in the file for estimations
    		logger.info("Counting the number of lines in the file...");
    		LineNumberReader r = new LineNumberReader(new FileReader(file));
    		try {
	    		while (r.readLine() != null){
	    			lineCount++;
	    		}
    		} finally {
    			r.close();
    		}
    		logger.info("{} lines found in file. It is assumed that the file contains this many words.",
    					numberFormat.format(lineCount));
    		
    		long startTime = System.currentTimeMillis();
    		
    		int i = 0;
    		int percent = 0;
    		for (String word : dictionary) {
    			try {
        			t.addWord(word);
        			i++;
        			if (i % 100 == 0) {
        				double tempPercent = ((double) t.getWordCount() / (double) lineCount) * 100;
            			if (tempPercent > percent + 10) {
            				percent = (int) tempPercent;
            				logger.info("{}% file loaded: {} words.",
            							Integer.toString(percent), numberFormat.format(t.getWordCount()));
            			}
        			}
    			} catch (IllegalLetterException e) {
    				logger.error("Illegal word: {}", word);
    			}
    		}
    		
    		long endTime = System.currentTimeMillis();
    		long duration = endTime - startTime;
    		logger.info("Loading Trie complete. Duration: {} seconds",
    					new DecimalFormat("####.##").format((double) duration / 1000));
    		
    		logger.info("Word count: {}", numberFormat.format(t.getWordCount()));
    		logger.info("Node count: {}", numberFormat.format(t.getNodeCount()));
    		// Memory usage calculation:
    		// The Trie is estimated to take up 24 bytes.
    		// Each node, including the root, is estimated to take up to 40 bytes.
    		long memory = 24 + 40 + (t.getNodeCount() * 40);
    		logger.info("Estimated memory usage of Trie: {}k", numberFormat.format((int) (memory / 1024)));
    		
		} catch (IOException e) {
			logger.error("FileBasedDictionaryReader error. Could not read file.", e);
		} catch (OutOfMemoryError e) {
			// Average node count per word is: 35.79
			int estimatedNodeCount = (int) (lineCount * 35.79);
			// Memory usage calculation:
    		// The Trie is estimated to take up 24 bytes.
    		// Each node, including the root, is estimated to take up to 40 bytes.
			long memory = 24 + 40 + (estimatedNodeCount * 40);
			logger.info("OutOfMemoryError.  Estimated memory needed: {}k", numberFormat.format((int) (memory / 1024)));
		} finally {
			if (dictionary != null) {
				dictionary.closeQuietly();
			}
		}
	}
}
