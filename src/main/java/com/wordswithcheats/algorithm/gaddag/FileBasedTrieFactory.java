package com.wordswithcheats.algorithm.gaddag;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wordswithcheats.dictionary.file.FileBasedDictionaryReader;

/**
 * A factory used to generate a {@link Trie} from a file.  It is assumed that the file contains a dictionary of words,
 * each on their own line.  Blank lines are ignored.
 * 
 * @author Matt Sidesinger
 */
public class FileBasedTrieFactory extends TrieFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(FileBasedTrieFactory.class);
	
	/**
	 * Creates a {@link FileBasedTrieFactory} that will uses the default dictionary file to generate the {@link Trie}
	 * when {@link #createTrie()} is called.
	 * <p>
	 * The default file is located at /{@value FileBasedDictionaryReader#DEFAULT_FILE_NAME}
	 */
	public FileBasedTrieFactory() throws IOException {
		this(FileBasedDictionaryReader.DEFAULT_FILE_NAME);
	}

	/**
	 * Creates a {@link FileBasedTrieFactory} that will uses the dictionary file at the given file path to
	 * generate the {@link Trie} when {@link #createTrie()} is called.
	 */
	public FileBasedTrieFactory(final String dictionaryFilePath) throws IOException {
		
		URL url = FileBasedTrieFactory.class.getClassLoader().getResource(dictionaryFilePath);
		File file = null;
		try {
			URI uri = url.toURI();
			logger.info("Dictionary URI: " + uri);
			file = new File(uri);
		} catch (URISyntaxException e) {
			// ignore
		}
		setDictionary(new FileBasedDictionaryReader(file));
	}
	
	/**
	 * Creates a {@link FileBasedTrieFactory} that will uses the dictionary file at the given file path to
	 * generate the {@link Trie} when {@link #createTrie()} is called.
	 */
	public FileBasedTrieFactory(final File dictionaryFile) throws IOException {
		setDictionary(new FileBasedDictionaryReader(dictionaryFile));
	}
	
	@Override
	public Trie createTrie() {
		return createTrie(getDictionary());
	}
}