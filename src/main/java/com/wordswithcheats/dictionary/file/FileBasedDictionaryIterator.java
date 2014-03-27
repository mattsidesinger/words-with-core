package com.wordswithcheats.dictionary.file;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to iterate through a file based dictionary, where each iteration returns a new word.
 * <p>
 * The {@link #remove()} method is not supported.
 * 
 * @author Matt Sidesinger
 */
public final class FileBasedDictionaryIterator implements Iterator<String> {
	
	private BufferedReader reader;
	private String nextWord;

	private static final Logger logger = LoggerFactory.getLogger(FileBasedDictionaryIterator.class);
	
	protected FileBasedDictionaryIterator(final Reader reader) throws FileNotFoundException {
		this.reader = new BufferedReader(reader);
		next();
	}

	@Override
	public boolean hasNext() {
		return (nextWord != null);
	}

	@Override
	public String next() {

		String currentWord = this.nextWord;
		while (true) {
			this.nextWord = null;
			try {
				this.nextWord = reader.readLine();
			} catch (IOException e) {
				// log and exit
				logger.error("Unable to read from dictionary file.", e);
			}
			if (nextWord == null) {
				// done reading from the file
				break;
			}
			this.nextWord = this.nextWord.trim();
			if (StringUtils.isNotBlank(this.nextWord)) {
				// found the next word
				break;
			}
		}
		return currentWord;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Closes the underlying {@link Reader}. This method is useful if you only want to process the first few lines
	 * of a larger file. If you do not close the iterator then the Reader remains open.
	 */
	public void close() throws IOException {
		if (this.reader != null) {
			this.reader.close();
		}
	}

	/**
	 * Closes the underlying {@link Reader} quietly. This method is useful if you only want to process the first few
	 * lines of a larger file. If you do not close the iterator then the Reader remains open.
	 * <p>
	 * This method can safely be called multiple times. 
	 */
	public void closeQuietly() {
		IOUtils.closeQuietly(this.reader);
	}
	
	@Override
	protected void finalize() throws Throwable {
		closeQuietly();
	}
}
