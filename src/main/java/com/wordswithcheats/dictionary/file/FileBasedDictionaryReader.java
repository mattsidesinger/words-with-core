package com.wordswithcheats.dictionary.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wordswithcheats.dictionary.DictionaryReader;

/**
 * Provides an interface for reading a file-based dictionary file.  It assumes that each word is on its own line.
 * Empty lines are skipped and any beginning or ending whitespace is removed from each word.
 * 
 * @author Matt Sidesinger
 */
public class FileBasedDictionaryReader extends DictionaryReader {

	private File file;
	private int wordCount = 0;
	private FileReader reader;
	
	public static final String DEFAULT_FILE_NAME = "dictionary.txt";
	private static DecimalFormat numberFormat = new DecimalFormat("###,###,###");
	private static final Logger logger = LoggerFactory.getLogger(FileBasedDictionaryReader.class);
	
	/**
	 * Loads the dictionary file with the {@link #DEFAULT_FILE_NAME} in the root of the classpath for this class.
	 * 
	 * @throws IOException	When the "{@value #DEFAULT_FILE_NAME}" file cannot be found or read.
	 */
	public FileBasedDictionaryReader() throws IOException {
		this(DEFAULT_FILE_NAME);
	}
	
	/**
	 * Loads the dictionary file with the file name given in the root of the classpath for this class.
	 * 
	 * @param dictionaryFileName	The name of the dictionary
	 * 
	 * @throws IOException	When the file with the given file name cannot be found or read.
	 */
	public FileBasedDictionaryReader(final String dictionaryFileName) throws IOException {
		
		Validate.notEmpty(dictionaryFileName);
		
		URL dictionaryFileURL = getClass().getClassLoader().getResource(dictionaryFileName);
		if (dictionaryFileURL == null) {
			final String message = "The following file could not be found in the root of the classpath: " +
				dictionaryFileName;
			throw new FileNotFoundException(message);
		}
		init(dictionaryFileURL);
	}

	/**
	 * Loads the dictionary file with the file at the given URL.
	 * 
	 * @param dictionaryFileURL		The URL location of the dictionary file.
	 * 
	 * @throws IOException	When the file with the given URL cannot be found or read.
	 */
	public FileBasedDictionaryReader(final URL dictionaryFileURL) throws IOException {
		init(dictionaryFileURL);
	}

	/**
	 * Loads the dictionary file given.
	 * 
	 * @param dictionaryFile		The dictionary file to load.
	 * 
	 * @throws IOException	When the file with the given path does not exist or cannot be read.
	 */
	public FileBasedDictionaryReader(final File dictionaryFile) throws IOException {
		init(dictionaryFile);
	}
	
	/**
	 * Loads the dictionary file with the file at the given URL.
	 * 
	 * @param dictionaryFileURL		The URL location of the dictionary file.
	 * 
	 * @throws IOException	When the file with the given URL cannot be found or read.
	 */
	protected void init(final URL dictionaryFileURL) throws IOException {
		
		Validate.notNull(dictionaryFileURL);
		
		File dictionaryFile = null;
		
		try {
			dictionaryFile = new File(dictionaryFileURL.toURI());
		} catch (URISyntaxException e) {
			dictionaryFile = new File(dictionaryFileURL.getPath());
		}
		
		init(dictionaryFile);
	}
	
	/**
	 * Loads the dictionary file given.
	 * 
	 * @param dictionaryFile		The dictionary file to load.
	 * 
	 * @throws IOException	When the file with the given path does not exist or cannot be read.
	 */
	protected void init(final File dictionaryFile) throws IOException {
		
		Validate.notNull(dictionaryFile);
		
		if (!dictionaryFile.exists()) {
			final String message = "The following file could not be found: " + dictionaryFile.getAbsolutePath();
			throw new FileNotFoundException(message);
		}
		
		if (!dictionaryFile.isFile()) {
			throw new IOException("The given path is a directory, not a file: " + dictionaryFile.getAbsolutePath());
		}
		
		if (!dictionaryFile.canRead()) {
			throw new IOException("The file cannot be read: " + dictionaryFile.getAbsolutePath());
		}
		
		setFile(dictionaryFile);
		
		// count the number if lines in the file
		int lineCount = 0;
		logger.info("Counting the number of lines in the file...");
		LineNumberReader r = new LineNumberReader(new FileReader(file));
		try {
			while (r.readLine() != null){
				lineCount++;
			}
		} finally {
			r.close();
		}
		logger.info("{} lines found in file. It is assumed that the file contains this many words.", numberFormat.format(lineCount));
		
		setWordCount(lineCount);
	}
	
	@Override
	public Iterator<String> iterator() {
		try {
			closeQuietly();
			this.reader = new FileReader(getFile());
			return new FileBasedDictionaryIterator(this.reader);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	/**
	 * Closes the underlying {@link Reader}. This method is useful if you only want to process the first few lines
	 * of a larger file. If you do not close the iterator then the Reader remains open.
	 */
	@Override
	public void closeQuietly() {
		IOUtils.closeQuietly(this.reader);
	}
	
	public File getFile() {
		return file;
	}

	protected void setFile(final File file) {
		this.file = file;
	}

	@Override
	public int getWordCount() {
		return wordCount;
	}

	protected void setWordCount(final int wordCount) {	
		this.wordCount = wordCount;
	}

	protected FileReader getReader() {	
		return reader;
	}

	protected void setReader(FileReader reader) {
		this.reader = reader;
	}
}
