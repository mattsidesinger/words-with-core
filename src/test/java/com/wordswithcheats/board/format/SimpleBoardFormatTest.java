package com.wordswithcheats.board.format;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.wordswithcheats.board.Board;
import com.wordswithcheats.board.Tile;
import com.wordswithcheats.board.parser.SimpleBoardParser;
import com.wordswithcheats.board.parser.exception.BoardParseException;


/**
 * Test case for the {@link SimpleBoardFormat} class.
 * 
 * @author Matt Sidesinger
 */
public class SimpleBoardFormatTest {
	
	private static final String BOARD_NO_LINE_NUMBERS_STRING =
        "V                     S W A B\n" +
        "O D                 M A Y B E\n" +
        "T I     Q       J O U L E    \n" +
        "E S     U       I   R     U  \n" +
        "  H A T E       A R K     N  \n" +
        "        E R     O E       Z  \n" +
        "        N U   P   A       I  \n" +
        "          F R O   G A N O P H\n" +
        "          F   N   I          \n" +
        "  I           D   N   G      \n" +
        "  L E T   T E S T I E R      \n" +
        "  I   O X I M     C   O     D\n" +
        "  A                   W     I\n" +
        "A L G A E   c O V E R L E T S\n" +
        "                      Y     C";
	
	private static final String BOARD_DEFAULT_LINE_NUMBER_FORMAT_STRING =
		"# 1:V                     S W A B\n" +
        "# 2:O D                 M A Y B E\n" +
        "# 3:T I     Q       J O U L E    \n" +
        "# 4:E S     U       I   R     U  \n" +
        "# 5:  H A T E       A R K     N  \n" +
        "# 6:        E R     O E       Z  \n" +
        "# 7:        N U   P   A       I  \n" +
        "# 8:          F R O   G A N O P H\n" +
        "# 9:          F   N   I          \n" +
        "#10:  I           D   N   G      \n" +
        "#11:  L E T   T E S T I E R      \n" +
        "#12:  I   O X I M     C   O     D\n" +
        "#13:  A                   W     I\n" +
        "#14:A L G A E   c O V E R L E T S\n" +
        "#15:                      Y     C";
	
	private static final String BOARD_CUSTOM_LINE_NUMBER_FORMAT_STRING =
        "=01| V                     S W A B\n" +
        "=02| O D                 M A Y B E\n" +
        "=03| T I     Q       J O U L E    \n" +
        "=04| E S     U       I   R     U  \n" +
        "=05|   H A T E       A R K     N  \n" +
        "=06|         E R     O E       Z  \n" +
        "=07|         N U   P   A       I  \n" +
        "=08|           F R O   G A N O P H\n" +
        "=09|           F   N   I          \n" +
        "=10|   I           D   N   G      \n" +
        "=11|   L E T   T E S T I E R      \n" +
        "=12|   I   O X I M     C   O     D\n" +
        "=13|   A                   W     I\n" +
        "=14| A L G A E   c O V E R L E T S\n" +
        "=15|                       Y     C";
	
	@Test
	public void testFormat() throws Exception {
	
		SimpleBoardFormat bf = new SimpleBoardFormat();
		bf.setPrintMultipliers(false);
		
		Board board = new Board(3, 3);
		assertEquals("     \n     \n     ", bf.format(board));
		
		board = loadBoard("SimpleBoardParserTest/lenient/lenient1.txt");
		assertEquals(BOARD_NO_LINE_NUMBERS_STRING, bf.format(board));
		
		bf.setPrintLineNumbers(true);
		assertEquals(BOARD_DEFAULT_LINE_NUMBER_FORMAT_STRING, bf.format(board));
		
		bf.setLineNumberPrefix("=");
		bf.setLineNumberPadChar('0');
		bf.setLineNumberSuffix("| ");
		assertEquals(BOARD_CUSTOM_LINE_NUMBER_FORMAT_STRING, bf.format(board));
	}
	
	protected Board loadBoard(String filePath) throws IOException, BoardParseException {
		
		URL boardURL = SimpleBoardFormatTest.class.getClassLoader().getResource(filePath);
		if (boardURL == null) {
			throw new IOException("Cannot find the file at " + filePath);
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
		
		SimpleBoardParser sbp = new SimpleBoardParser(true);
		Tile[][] tiles = sbp.parse(input);
		Board b = new Board(tiles);
		
		return b;
	}
}
