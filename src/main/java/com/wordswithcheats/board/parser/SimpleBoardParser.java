package com.wordswithcheats.board.parser;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wordswithcheats.board.BlankTile;
import com.wordswithcheats.board.Tile;
import com.wordswithcheats.board.parser.exception.BoardParseException;

/**
 * Parses boards in the following formats: 
 * <pre>
 * # 1: V                     S W A B
 * # 2: O D                 M A Y B E
 * # 3: T I     Q       J O U L E    
 * # 4: E S     U       I   R     U  
 * # 5:   H A T E       A R K     N  
 * # 6:         E R     O E       Z  
 * # 7:         N U   P   A       I  
 * # 8:           F R O   G A N O P H
 * # 9:           F   N   I          
 * #10:   I           D   N   G      
 * #11:   L E T   T E S T I E R      
 * #12:   I   O X I M     C   O     D
 * #13:   A                   W     I
 * #14: A L G A E   c O V E R L E T S
 * #15:                       Y     C
 * </pre>
 * <pre>
 *    a b c d e f g h i j k l m n o 
 *    ----------------------------- 
 * 01|=     A       J       '     A|
 * 02|  -   F   "   A   "       - U|
 * 03|    - O     O W E       -   T|
 * 04|i N D U C T S ' L A V E   B O|
 * 05|      L -         M I L K O S|
 * 06|  "       D O M A I N     N  |
 * 07|    '       B A N D     ' I  |
 * 08|=     P R I O R Y     E X E C|
 * 09|  H E H     '   '       I S  |
 * 10|  E W E   "       Q I     T  |
 * 11|  P E E -         A D        |
 * 12|'     S       '   T   -     F|
 * 13|    - I     ' R E s O L V E R|
 * 14|  -   N   A Y U   "       - O|
 * 15|Z I G G U R A T       '     G|
 * 16 ----------------------------- 
 * </pre>
 * <pre>
 *    a b c d e f g h i j k l m n o 
 *    ----------------------------- 
 * 01|# . L I S T E N E R . + . . #|
 * 02|W A I F . * . . . E . . W = .|
 * 03|A . = . . . + . + T . . O . .|
 * 04|L . T = . . . + J I V E R . +|
 * 05|T O E . = . . . . C = . N . .|
 * 06|I F S . . * . . . L . . . * .|
 * 07|E . S . . . + . E E . . + . B|
 * 08|R . E + . . P A X . . + . . L|
 * 09|. . R . . . + . A . . . + . O|
 * 10|. P A . . * . . M * . B E G O|
 * 11|. U . V A T I C I D e . . . D|
 * 12|+ N . A . . . U N . H U N K Y|
 * 13|. . Q I . . + R E . . . = . .|
 * 14|. = . R . * . I D * . . . = .|
 * 15|H U G Y . . . O . . . + . . #|
 *    ----------------------------- 
 * </pre>
 * 
 * @author Matt Sidesinger
 */
public class SimpleBoardParser implements BoardParser {
	
	private boolean lenient = false;
	
	// the '+' ensures that empty lines are allowed/expected and removed
	protected static final Pattern LINE_SPLITTER_LENIENT = Pattern.compile("(\r?\n|\u0085|\u2028|\u2029)+");
	protected static final Pattern LINE_SPLITTER_STRICT = Pattern.compile("\r?\n|\u0085|\u2028|\u2029");
	protected static final String VALID_NON_LETTER_CHARS = " .+-*=#'\"";
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleBoardParser.class);
	
	/**
	 * Creates a {@link SimpleBoardParser} that is not lenient.
	 */
	public SimpleBoardParser() {
	}

	/**
	 * Creates a {@link SimpleBoardParser}.
	 * 
	 * @param lenient	Whether the parser should be set to lenient mode.
	 */
	public SimpleBoardParser(final boolean lenient) {
		this.lenient = lenient;
	}
	
	@Override
	public Tile[][] parse(final CharSequence input) throws BoardParseException {
		
		// parse the input into lines
		final Pattern lineSplitter = lenient ? LINE_SPLITTER_LENIENT : LINE_SPLITTER_STRICT;
		final String[] lines = lineSplitter.split(input);
		if (lines.length == 0) {
			// input is empty
			throw new BoardParseException("Unexpected end of input", 1);
		}
		logger.info("{} lines found", Integer.valueOf(lines.length));
		
		Tile[][] board = parse(lines, !lenient);
		
		return board;
	}
	
	/**
	 * Parses an array of String objects and creates a multidimensional {@link Tile} array, where the first index is the
	 * y-axis and the second index is the x-axis.  The coordinates 0,0 represent the top left corner of the board.
	 * 
	 * @param lines		An array of String objects which will be processed in order		
	 * @param strict	Whether the parser should perform strict parsing or not
	 * 
	 * @return A multidimensional {@link Tile} array
	 * 
	 * @throws BoardParseException
	 */
	protected Tile[][] parse(final String[] lines, final boolean strict) throws BoardParseException {
		
		Tile[][] board = null;
		
		int headerLineCount = 0;
		try {
			// is there a header?
			if (strict) {
    			if (Character.isWhitespace(lines[0].charAt(0))) {
    				headerLineCount++;
    				if (Character.isWhitespace(lines[1].charAt(0))) {
    					headerLineCount++;
    				}
    				logger.info("{} header lines found", Integer.valueOf(headerLineCount));
    			}
			} else {
				if (lines[0].contains("---")) {
					headerLineCount = 1;
				} else if (lines[1].contains("---")) {
					headerLineCount = 2;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new BoardParseException("Unexpected end of line", headerLineCount + 1);
		}
		
		int lineIndex = headerLineCount;
		int charIndex = 0;
		
		// set variables
		boolean hasHash = lines[lineIndex].charAt(0) == '#';
		if (hasHash) charIndex++;			
		char lineNumberPadChar = lines[lineIndex].charAt(charIndex);
		charIndex++;
		
		int lineLength = 0;
		String delim = ""; // not null
		int delimIndex = getDelimIndex(lines[lineIndex]);
		if (delimIndex >= 0) {
			delim = Character.toString(lines[lineIndex].charAt(delimIndex));
		}
		
		if (strict && StringUtils.isEmpty(delim)) {
			throw new BoardParseException("Could not find line number delimiter - expecting ':' or '|'", lineIndex + 1);
		}
		
		if (strict) {
			lineLength = lines[lineIndex].length();
    		logger.info(
    				"Ensuring all lines are the same size, ignoring header. Expected line size is {}",
    				Integer.valueOf(lineLength));
		}
		
		int lenientModeLineLengthIndex = 0;
		boolean hasFooter = false;
		for (int i = headerLineCount + 1; i < lines.length; i++) {
			// footer?
			if (i == lines.length - 1) {
				hasFooter = lines[i].contains("---");
				if (strict && hasFooter) {
					if (headerLineCount == 0) {
						throw new BoardParseException("Not expecting footer since there was no header.", i + 1);
					}
					if (lines[i].charAt(0) != ' ' && lines[i].charAt(delimIndex) != ' ') {
    					throw new BoardParseException("Footer line is corrupt.", i + 1);
					}
				}
			}
			
			if (strict) {
    			if (lines[i].length() != lineLength || (hasFooter && lines[i].length() < lineLength -1)) {
    				String preMessage ="Inconsistent line length. Line %d: %d, Line %d: %d";
    				String message = String.format(preMessage,
    											   Integer.valueOf(headerLineCount + 1),
                    							   Integer.valueOf(lineLength),
                    							   Integer.valueOf(i + 1),
                    							   Integer.valueOf(lines[i].length()));
    				throw new BoardParseException(message);
    			}
			} else {
				// in lenient mode, the width is the longest line
				if (lines[i].length() > lineLength) {
					lineLength = lines[i].length();
					lenientModeLineLengthIndex = i;
				}
			}
		} // ~for
		
		// define the board size
		int evalLength = 0;
		if (strict) {
			boolean hasLineTerminator = delim.length() == 1 ? lines[lineIndex].endsWith(delim) : false;
			evalLength = hasLineTerminator ? lineLength - delimIndex : lineLength - delimIndex + delim.length();
		} else {
			// in lenient mode, the line number delimiter can be found at a different index
			delimIndex = getDelimIndex(lines[lenientModeLineLengthIndex]);
			if (delim != null && delim.length() > 0) {
				boolean hasLineTerminator = delim.length() == 1 ? lines[lenientModeLineLengthIndex].endsWith(delim) : false;
				evalLength = hasLineTerminator ? lineLength - delimIndex : lineLength - delimIndex + delim.length();
			} else {
				evalLength = lineLength;
			}
		}
		
		if (delim != null && delim.length() > 0) {
    		// evalLength should always be odd
    		if (evalLength % 2 == 0) {
    			evalLength--;
    			delim = delim + " ";
    		}
		} else {
			evalLength++;
		}
		
		int width = evalLength / 2;
		int height = hasFooter ? lines.length - headerLineCount - 1 : lines.length - headerLineCount;
		
		if (width <= 0 || height <= 0) {
			String message = String.format(
					"Invalid input format. Cannot create a %dx%d board",
					Integer.valueOf(width),
					Integer.valueOf(height));
			throw new BoardParseException(message);
		}
		
		board = new Tile[height][width];
		logger.info("Creating a board with dimensions wxh: {}x{}", Integer.valueOf(width), Integer.valueOf(height));
		
		for (int y = 0; lineIndex < lines.length; lineIndex++, y++) {
		
			logger.info("Parsing line {}", Integer.valueOf(lineIndex + 1));
			
			String line = lines[lineIndex];
			
			// footer?
			if (headerLineCount > 0 && lineIndex == lines.length - 1) {
				if (lines[lineIndex].contains("---")) {
					logger.info("Detected footer. Not parsing footer.");
					break;
				}
			}
			
			boolean hasLineTerminator = delim.length() == 1 ? line.endsWith(delim) : false;
			int length = hasLineTerminator ? line.length() - 1 : line.length();
			
			if (!strict) {
				// in lenient mode, the line number delimiter can be found at a different index
				delimIndex = getDelimIndex(line);
			}
			
			int lineNumber = lineIndex - headerLineCount + 1;
			
			if (strict) {
				String lineString = line.substring(0, delimIndex);
    			String expectedLineString = StringUtils.leftPad(
    					Integer.toString(lineNumber), hasHash ? delimIndex - 1 : delimIndex, lineNumberPadChar);
    			if (hasHash) {
    				expectedLineString = '#' + expectedLineString;
    			}
    			
    			if (!expectedLineString.equals(lineString)) {
    				String message = String.format(
    						"Unexpected line number or invalid format. Expecting '%s'", expectedLineString);
    				throw new BoardParseException(message, lineIndex + 1);
    			}
			}
			
			// don't validate delim character
			
			// footer?
			if (headerLineCount > 0 && lineIndex == lines.length - 1) {
				if (line.contains("---")) {
					logger.info("Detected footer. Not parsing footer.");
					break;
				}
			}
			
			int i = 0;
			int x = 0;
			for (charIndex = Math.max(delimIndex + delim.length(), 0); charIndex < length; charIndex++, i++) {
				
				char c = line.charAt(charIndex);
				logger.debug(
						"Evaluating character at position {}: '{}'", Integer.valueOf(charIndex), Character.valueOf(c));
				
				if (i % 2 == 1) {
					// expecting space
					if (strict && c != ' ') {
						String message = String.format(
								"Expecting ' ' at character number %d, not '%s'",
								Integer.valueOf(charIndex),
								Character.toString(c));
						throw new BoardParseException(message, lineIndex + 1);
					}
					
					x++;
					
				} else {
					
    				if (Character.isLetter(c)) {
    					Tile tile = null;
    					if (Character.isLowerCase(c)) {
    						tile = new BlankTile(c);
    						logger.info(
    								"Creating _BLANK_ tile at index ({},{}) with letter '{}'",
    								new Object[] {
    									Integer.valueOf(y),
    									Integer.valueOf(x),
    									Character.valueOf(Character.toUpperCase(c))
    								}
    							);
    					} else {
    						tile = Tile.valueOf(c);
    						logger.info(
    								"Creating tile at index ({},{}) with letter '{}'",
    								new Object[] {
    									Integer.valueOf(y),
    									Integer.valueOf(x),
    									Character.valueOf(c)
    								}
    							);
    					}
    					
    					try {
    						board[y][x] = tile;
    					} catch (IndexOutOfBoundsException e) {
    						throw e;
    					}
    					
    				} else if (strict && VALID_NON_LETTER_CHARS.indexOf(c) < 0) {
    					
    					String message = String.format(
    							"Illegal character, '%s', at character number %d",
    							Character.toString(c),
    							Integer.valueOf(charIndex));
    					throw new BoardParseException(message, lineIndex + 1);							
    				}
				}
			} // ~for each character
			
			if (strict && hasLineTerminator) {
				// validate that the line ends with the correct character
				if (line.charAt(lineLength - 1) != delim.charAt(0)) {
					String message = String.format(
							"Expecting ' ' at character number %d, not '%s'",
							Integer.valueOf(lineLength - 1),
							Character.toString(delim.charAt(0)));
					throw new BoardParseException(message, lineIndex + 1);
				}
			}
			
		} // ~for each line
		
		return board;
	}
	
	/**
	 * Obtains the index of the delimiter within the line.  If a delimiter was not found,
	 * then a negative value is returned, such as -1.
	 * <p>
	 * Assumes line is not <code>null</code>.
	 * 
	 * @param line	The String to search for the first delimiter.
	 * 
	 * @return	The index of where the delimiter was found.
	 */
	protected int getDelimIndex(final String line) {
		
		assert (line != null);
		
		int delimIndex = -1;
		int length = line.length();
		for (int charIndex = 0; charIndex < length; charIndex++) {
			
			char c = line.charAt(charIndex);
			
			if (c == ':' || c == '|') {
				delimIndex = charIndex;
				break;
			} else if (Character.isDigit(c)) {
				// the last numeric value before a non-number can be a delimiter
				delimIndex = charIndex;
			} else if (delimIndex >= 0) {
				// delimiter was found
				break;
			}
		}
		
		return delimIndex;
	}
	
	public boolean isLenient() {	
		return lenient;
	}

	public void setLenient(final boolean lenient) {
		this.lenient = lenient;
	}	
}
