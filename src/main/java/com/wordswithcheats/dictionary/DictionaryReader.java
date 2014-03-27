package com.wordswithcheats.dictionary;

import java.io.IOException;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wordswithcheats.dictionary.file.FileBasedDictionaryReader;

/**
 * All DictionaryReader objects will extend from this class.
 * <p>
 * Use the {@link #defaultInstance()} method to obtain the file-backed
 * implementation, {@link FileBasedDictionaryReader}.
 * <p>
 * Ensure to call the "close" methods to ensure any resources are cleaned up.
 * <p>
 * This class is not a {@link Reader} implementation.
 * 
 * @author Matt Sidesinger
 */
public abstract class DictionaryReader implements Iterable<String> {
	
	private static final Logger logger = LoggerFactory.getLogger(DictionaryReader.class);
	
	/**
	 * @return	the default DictionaryReader implementation.
	 */
	public static DictionaryReader defaultInstance() {
		try {
			return new FileBasedDictionaryReader();
		} catch (IOException e) {
			logger.error("Unable to instantiate the default DictionaryReader", e);
			throw new RuntimeException("Unable to instantiate the default DictionaryReader", e);
		}
	}
	
	/**
	 * @return	The number of words in this dictionary.
	 */
	public abstract int getWordCount();

	/**
	 * Closes any underlying resources quietly.
	 * <p>
	 * This method can safely be called multiple times. 
	 */
	public abstract void closeQuietly();
	
	@Override
	protected void finalize() throws Throwable {
		closeQuietly();
	}
}
