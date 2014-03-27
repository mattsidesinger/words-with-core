package com.wordswithcheats.board.parser;

import static org.junit.Assert.fail;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wordswithcheats.board.Tile;
import com.wordswithcheats.board.parser.exception.BoardParseException;
import com.wordswithcheats.board.util.TileUtils;

/**
 * Test case for the {@link SimpleBoardParser} class.
 * 
 * @author Matt Sidesinger
 */
public class SimpleBoardParserTest {
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleBoardParserTest.class);
	
	/**
	 * Used to validate the contents of:
	 * 	<ul>
	 *		<li>/SimpleBoardParserTest/strict1.txt</li>
	 *		<li>/SimpleBoardParserTest/lenient.txt</li>
	 *	</ul>
	 */
	public static final char[][] BOARD_1 = new char[][] /* 15x15 */ {
		{'V', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'S', 'W', 'A', 'B'}, // 1
		
		{'O', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'M', 'A', 'Y', 'B', 'E'}, // 2
		
		{'T', 'I', ' ', ' ', 'Q', ' ', ' ', ' ', 'J', 'O', 'U', 'L', 'E', ' ', ' '}, // 3
		
		{'E', 'S', ' ', ' ', 'U', ' ', ' ', ' ', 'I', ' ', 'R', ' ', ' ', 'U', ' '}, // 4
		
		{' ', 'H', 'A', 'T', 'E', ' ', ' ', ' ', 'A', 'R', 'K', ' ', ' ', 'N', ' '}, // 5
		
		{' ', ' ', ' ', ' ', 'E', 'R', ' ', ' ', 'O', 'E', ' ', ' ', ' ', 'Z', ' '}, // 6
		
		{' ', ' ', ' ', ' ', 'N', 'U', ' ', 'P', ' ', 'A', ' ', ' ', ' ', 'I', ' '}, // 7
		
		{' ', ' ', ' ', ' ', ' ', 'F', 'R', 'O', ' ', 'G', 'A', 'N', 'O', 'P', 'H'}, // 8
		
		{' ', ' ', ' ', ' ', ' ', 'F', ' ', 'N', ' ', 'I', ' ', ' ', ' ', ' ', ' '}, // 9
		
		{' ', 'I', ' ', ' ', ' ', ' ', ' ', 'D', ' ', 'N', ' ', 'G', ' ', ' ', ' '}, // 10
		
		{' ', 'L', 'E', 'T', ' ', 'T', 'E', 'S', 'T', 'I', 'E', 'R', ' ', ' ', ' '}, // 11
		
		{' ', 'I', ' ', 'O', 'X', 'I', 'M', ' ', ' ', 'C', ' ', 'O', ' ', ' ', 'D'}, // 12
		
		{' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'W', ' ', ' ', 'I'}, // 13
		
		{'A', 'L', 'G', 'A', 'E', ' ', 'c', 'O', 'V', 'E', 'R', 'L', 'E', 'T', 'S'}, // 14
		
		{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'Y', ' ', ' ', 'C'}  // 15
	};
	
	/**
	 * Used to validate the contents of:
	 * 	<ul>
	 *		<li>/SimpleBoardParserTest/strict2.txt</li>
	 *		<li>/SimpleBoardParserTest/lenient2.txt</li>
	 *	</ul>
	 */
	public static final char[][] BOARD_2 = new char[][] /* 15x15 */ {
		{'G', ' ', 'A', 'Y', 'E', ' ', ' ', 'W', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, // 1
		
		{'R', 'A', 'W', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, // 2
		
		{'U', ' ', ' ', 'O', 'P', 'T', 'I', 'N', 'G', ' ', ' ', ' ', ' ', ' ', ' '}, // 3
		
		{'B', 'E', 'E', 'F', 'Y', ' ', ' ', 'E', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, // 4
		
		{' ', 'N', ' ', ' ', 'I', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'T'}, // 5
		
		{' ', 'V', ' ', ' ', 'N', ' ', 'J', ' ', ' ', ' ', ' ', ' ', ' ', 'X', 'I'}, // 6
		
		{' ', 'y', ' ', ' ', 'S', 'C', 'U', 'D', ' ', ' ', ' ', ' ', 'A', 'I', 'T'}, // 7
		
		{' ', ' ', ' ', ' ', ' ', ' ', 'R', 'E', 'S', 'O', 'l', 'E', 'D', ' ', 'I'}, // 8
		
		{' ', ' ', 'H', ' ', ' ', ' ', 'A', ' ', 'K', 'A', ' ', ' ', 'Z', ' ', ' '}, // 9
		
		{' ', ' ', 'I', 'M', 'P', 'E', 'L', ' ', 'A', 'F', 'O', 'R', 'E', ' ', ' '}, // 10
		
		{' ', ' ', 'R', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'G', 'O', ' ', ' ', ' '}, // 11
		
		{' ', 'T', 'E', 'A', 'B', 'O', 'A', 'R', 'D', ' ', 'L', 'O', ' ', ' ', ' '}, // 12
		
		{' ', ' ', 'R', ' ', 'I', ' ', 'V', ' ', ' ', ' ', 'E', 'M', 'I', 'T', 'S'}, // 13
		
		{' ', ' ', ' ', 'E', 'N', 'S', 'O', 'U', 'L', 'E', 'D', ' ', ' ', ' ', 'U'}, // 14
		
		{'L', 'I', 'N', 'T', ' ', ' ', ' ', 'H', 'O', ' ', ' ', ' ', ' ', ' ', 'Q'}  // 15
	};

	/**
	 * Used to validate the contents of:
	 * 	<ul>
	 *		<li>/SimpleBoardParserTest/strict3.txt</li>
	 *		<li>/SimpleBoardParserTest/lenient3.txt</li>
	 *	</ul>
	 */
	public static final char[][] BOARD_3 = new char[][] /* 15x15 */ {
		{' ', ' ', ' ', 'A', ' ', ' ', ' ', 'J', ' ', ' ', ' ', ' ', ' ', ' ', 'A'}, // 1
		
		{' ', ' ', ' ', 'F', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', 'U'}, // 2
		
		{' ', ' ', ' ', 'O', ' ', ' ', 'O', 'W', 'E', ' ', ' ', ' ', ' ', ' ', 'T'}, // 3
		
		{'i', 'N', 'D', 'U', 'C', 'T', 'S', ' ', 'L', 'A', 'V', 'E', ' ', 'B', 'O'}, // 4
		
		{' ', ' ', ' ', 'L', ' ', ' ', ' ', ' ', ' ', 'M', 'I', 'L', 'K', 'O', 'S'}, // 5
		
		{' ', ' ', ' ', ' ', ' ', 'D', 'O', 'M', 'A', 'I', 'N', ' ', ' ', 'N', ' '}, // 6
		
		{' ', ' ', ' ', ' ', ' ', ' ', 'B', 'A', 'N', 'D', ' ', ' ', ' ', 'I', ' '}, // 7
		
		{' ', ' ', ' ', 'P', 'R', 'I', 'O', 'R', 'Y', ' ', ' ', 'E', 'X', 'E', 'C'}, // 8
		
		{' ', 'H', 'E', 'H', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'I', 'S', ' '}, // 9
		
		{' ', 'E', 'W', 'E', ' ', ' ', ' ', ' ', ' ', 'Q', 'I', ' ', ' ', 'T', ' '}, // 10
		
		{' ', 'P', 'E', 'E', ' ', ' ', ' ', ' ', ' ', 'A', 'D', ' ', ' ', ' ', ' '}, // 11
		
		{' ', ' ', ' ', 'S', ' ', ' ', ' ', ' ', ' ', 'T', ' ', ' ', ' ', ' ', 'F'}, // 12
		
		{' ', ' ', ' ', 'I', ' ', ' ', ' ', 'R', 'E', 's', 'O', 'L', 'V', 'E', 'R'}, // 13
		
		{' ', ' ', ' ', 'N', ' ', 'A', 'Y', 'U', ' ', ' ', ' ', ' ', ' ', ' ', 'O'}, // 14
		
		{'Z', 'I', 'G', 'G', 'U', 'R', 'A', 'T', ' ', ' ', ' ', ' ', ' ', ' ', 'G'}  // 15
	};
	
	/**
	 * Used to validate the contents of:
	 * 	<ul>
	 *		<li>/SimpleBoardParserTest/strict4.txt</li>
	 *		<li>/SimpleBoardParserTest/lenient4.txt</li>
	 *	</ul>
	 */
	public static final char[][] BOARD_4 = new char[][] /* 15x15 */ {
		{' ', ' ', 'L', 'I', 'S', 'T', 'E', 'N', 'E', 'R', ' ', ' ', ' ', ' ', ' '}, // 1
		
		{'W', 'A', 'I', 'F', ' ', ' ', ' ', ' ', ' ', 'E', ' ', ' ', 'W', ' ', ' '}, // 2
		
		{'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'T', ' ', ' ', 'O', ' ', ' '}, // 3
		
		{'L', ' ', 'T', ' ', ' ', ' ', ' ', ' ', 'J', 'I', 'V', 'E', 'R', ' ', ' '}, // 4
		
		{'T', 'O', 'E', ' ', ' ', ' ', ' ', ' ', ' ', 'C', ' ', ' ', 'N', ' ', ' '}, // 5
		
		{'I', 'F', 'S', ' ', ' ', ' ', ' ', ' ', ' ', 'L', ' ', ' ', ' ', ' ', ' '}, // 6
		
		{'E', ' ', 'S', ' ', ' ', ' ', ' ', ' ', 'E', 'E', ' ', ' ', ' ', ' ', 'B'}, // 7
		
		{'R', ' ', 'E', ' ', ' ', ' ', 'P', 'A', 'X', ' ', ' ', ' ', ' ', ' ', 'L'}, // 8
		
		{' ', ' ', 'R', ' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', 'O'}, // 9
		
		{' ', 'P', 'A', ' ', ' ', ' ', ' ', ' ', 'M', ' ', ' ', 'B', 'E', 'G', 'O'}, // 10
		
		{' ', 'U', ' ', 'V', 'A', 'T', 'I', 'C', 'I', 'D', 'e', ' ', ' ', ' ', 'D'}, // 11
		
		{' ', 'N', ' ', 'A', ' ', ' ', ' ', 'U', 'N', ' ', 'H', 'U', 'N', 'K', 'Y'}, // 12
		
		{' ', ' ', 'Q', 'I', ' ', ' ', ' ', 'R', 'E', ' ', ' ', ' ', ' ', ' ', ' '}, // 13
		
		{' ', ' ', ' ', 'R', ' ', ' ', ' ', 'I', 'D', ' ', ' ', ' ', ' ', ' ', ' '}, // 14
		
		{'H', 'U', 'G', 'Y', ' ', ' ', ' ', 'O', ' ', ' ', ' ', ' ', ' ', ' ', ' '}  // 15
	};
	
	@Test
	public void testParseStrict() throws IOException, BoardParseException {
		
		SimpleBoardParser sbp = new SimpleBoardParser(false);
		
		String input = null;
		Tile[][] board = null;
		
		input = loadBoardFileInput("SimpleBoardParserTest/strict/strict1.txt");
		board = sbp.parse(input);
		logger.info("Done parsing strict1.txt");
		if (!TileUtils.equals(board, BOARD_1)) {
			fail("Output of parse() is not what is expected.");
		}
		
		input = loadBoardFileInput("SimpleBoardParserTest/strict/strict2.txt");
		board = sbp.parse(input);
		logger.info("Done parsing strict2.txt");
		if (!TileUtils.equals(board, BOARD_2)) {
			fail("Output of parse() is not what is expected.");
		}
		
		input = loadBoardFileInput("SimpleBoardParserTest/strict/strict3.txt");
		board = sbp.parse(input);
		logger.info("Done parsing strict3.txt");
		if (!TileUtils.equals(board, BOARD_3)) {
			fail("Output of parse() is not what is expected.");
		}
		
		input = loadBoardFileInput("SimpleBoardParserTest/strict/strict4.txt");
		board = sbp.parse(input);
		logger.info("Done parsing strict4.txt");
		if (!TileUtils.equals(board, BOARD_4)) {
			fail("Output of parse() is not what is expected.");
		}
	}
	
	@Test
	public void testParseLenient() throws IOException, BoardParseException {
		
		SimpleBoardParser sbp = new SimpleBoardParser(true);
		
		String input = null;
		Tile[][] board = null;
		
		// tests variable length lines and extra empty lines
		input = loadBoardFileInput("SimpleBoardParserTest/lenient/lenient1.txt");
		logger.info("Parsing lenient1.txt");
		board = sbp.parse(input);
		logger.info("Done parsing lenient1.txt");
		if (!TileUtils.equals(board, BOARD_1)) {
			fail("Output of parse() is not what is expected.");
		}
		
		// tests variances in line number prefixes
		input = loadBoardFileInput("SimpleBoardParserTest/lenient/lenient2.txt");
		logger.info("Parsing lenient2.txt");
		board = sbp.parse(input);
		logger.info("Done parsing lenient2.txt");
		if (!TileUtils.equals(board, BOARD_2)) {
			fail("Output of parse() is not what is expected.");
		}
		
		// test invalid characters in white space and partial header/footer
		input = loadBoardFileInput("SimpleBoardParserTest/lenient/lenient3.txt");
		logger.info("Parsing lenient3.txt");
		board = sbp.parse(input);
		logger.info("Done parsing lenient3.txt");
		if (!TileUtils.equals(board, BOARD_3)) {
			fail("Output of parse() is not what is expected.");
		}
		
		// test
		input = loadBoardFileInput("SimpleBoardParserTest/lenient/lenient4.txt");
		logger.info("Parsing lenient4.txt");
		board = sbp.parse(input);
		logger.info("Done parsing lenient4.txt");
		if (!TileUtils.equals(board, BOARD_4)) {
			fail("Output of parse() is not what is expected.");
		}
	}
	
	protected String loadBoardFileInput(String path) throws IOException {
		
		URL boardURL = getClass().getClassLoader().getResource(path);
		if (boardURL == null) {
			throw new IOException("Cannot find the file at " + path);
		}
		
		File boardFile = null;
		try {
			boardFile = new File(boardURL.toURI());
		} catch (URISyntaxException e) {
			// ignore
		}
		if (boardFile == null || !boardFile.exists() || !boardFile.isFile() || !boardFile.canRead()) {
			throw new IOException("Cannot read the file at " + boardURL);
		}
		
		String input = null;
		InputStream is = new BufferedInputStream(new FileInputStream(boardFile));
		try {
    		input = IOUtils.toString(is);
		} finally {
			IOUtils.closeQuietly(is);
		}
		
		return input;
	}
}
