package com.wordswithcheats.board.format;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wordswithcheats.board.Board;
import com.wordswithcheats.board.Tile;
import com.wordswithcheats.board.multiplier.Multiplier;

/**
 * Outputs the {@link Tile} objects from the Board separated by spaces.  Line numbers may also be printed
 * by setting <code>printLineNumbers</code> to <code>true</code>.
 * <p>
 * Line numbers are composed of the following properties:
 * lineNumberPrefix|line number|lineNumberSuffix
 * <p>
 * The default line number pattern when <code>printLineNumbers</code> is enabled is <code>"# 1:"</code>.
 * <p>
 * Without line numbers:
 * <pre>
 *     L I S T E N E R          
 * W A I F           E     W    
 * A                 T     O    
 * L   T           J I V E R    
 * T O E             C     N    
 * I F S             L          
 * E   S           E E         B
 * R   E       P A X           L
 *     R           A           O
 *   P A           M     B E G O
 *   U   V A T I C I D e       D
 *   N   A       U N   H U N K Y
 *     Q I       R E            
 *       R       I D            
 * H U G Y       O
 * </pre>
 * 
 * With line numbers:
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
</pre>
 * 
 * @author Matt Sidesinger
 */
public class SimpleBoardFormat extends BoardFormat {
		
	private boolean printLineNumbers = false;
	private boolean printMultipliers = true;
	private boolean printSpaceAfterTile = true;
	private char lineNumberPadChar = DEFAULT_LINE_NUMBER_PAD_CHAR; 
	private String lineNumberPrefix = null;
	private String lineNumberSuffix = null;
	private boolean printNewlines = true;
	
	protected static final char DEFAULT_LINE_NUMBER_PAD_CHAR = ' ';
	protected static final String DEFAULT_LINE_NUMBER_PREFIX = "#";
	protected static final String DEFAULT_LINE_NUMBER_SUFFIX = ":";
	protected static final String NEWLINE = "\n";
	
	private static final long serialVersionUID = -4373130663646243948L;
	private static final Logger logger = LoggerFactory.getLogger(SimpleBoardFormat.class);
	
	public SimpleBoardFormat() {
		setLineNumberPrefix(DEFAULT_LINE_NUMBER_PREFIX);
		setLineNumberSuffix(DEFAULT_LINE_NUMBER_SUFFIX);
		logger.debug("Line number prefix: {}", getLineNumberPrefix());
		logger.debug("Line number suffix: {}", getLineNumberSuffix());
	}
	
	@Override
	public void format(final Board board, final StringBuffer sb) {

		Validate.notNull(board, "Board cannot be null");
		Validate.notNull(sb, "StringBuffer cannot be null");
		
		int height = board.getHeight();
		for (int y = 1; y <= height; y++) {
			String row = formatRow(y, board);
			if (logger.isDebugEnabled()) {
				logger.debug("{}", row);
			}
			sb.append(row);
			if (this.printNewlines) {
				if ((y + 1) <= height) {
					sb.append(NEWLINE);
				}
			}
		}
	}
	
	/**
	 * Formats the a row given a row number.
	 * 
	 * @param rowNumber	The row number being printed; 1-based
	 * @param board		The originating Board
	 */
	protected String formatRow(final int rowNumber, final Board board) {
		
		StringBuilder sb = new StringBuilder();
	
		if (this.printLineNumbers) {
    		if (this.lineNumberPrefix != null) {
    			sb.append(this.lineNumberPrefix);
    		}
    		// generate a line number format based on the number of rows
    		int digits = Integer.toString(board.getHeight()).getBytes().length;
    		// pad left
    		sb.append(StringUtils.leftPad(Integer.toString(rowNumber), digits, this.lineNumberPadChar));
    		if (this.lineNumberSuffix != null) {
    			sb.append(this.lineNumberSuffix);
    		}
		}
		
		Tile[] row = board.getRow(rowNumber);
		for (int x = 0; x < row.length; x++) {
			if (row[x] == null) {
				if (this.printMultipliers) {
    				Multiplier multipler = board.getMultipliers().get(x + 1, rowNumber);
    				if (multipler != null) {
    					sb.append('*');
    				} else {
    					sb.append(' ');
    				}
				} else {
					sb.append(' ');
				}
			} else {
				sb.append(row[x].toChar());
			}
			if (this.printSpaceAfterTile) {
				if ((x + 1) < row.length) {
					sb.append(' ');
				}
			}
		}
		
		return sb.toString();
	}

	public boolean getPrintLineNumbers() {
		return printLineNumbers;
	}

	public void setPrintLineNumbers(final boolean printLineNumbers) {
		this.printLineNumbers = printLineNumbers;
	}
	
	public boolean getPrintMultipliers() {
		return printMultipliers;
	}

	public void setPrintMultipliers(final boolean printMultipliers) {
		this.printMultipliers = printMultipliers;
	}
	
	public boolean getPrintSpaceAfterTile() {
		return printSpaceAfterTile;
	}
	
	public void setPrintSpaceAfterTile(boolean printSpaceAfterTile) {
		this.printSpaceAfterTile = printSpaceAfterTile;
	}

	public char getLineNumberPadChar() {
		return lineNumberPadChar;
	}
	
	public void setLineNumberPadChar(final char lineNumberPadChar) {
		this.lineNumberPadChar = lineNumberPadChar;
	}

	public String getLineNumberPrefix() {
		return lineNumberPrefix;
	}

	public void setLineNumberPrefix(final String lineNumberPrefix) {
		this.lineNumberPrefix = lineNumberPrefix;
	}

	public String getLineNumberSuffix() {
		return lineNumberSuffix;
	}

	public void setLineNumberSuffix(final String lineNumberSuffix) {
		this.lineNumberSuffix = lineNumberSuffix;
	}
	
	public boolean isPrintNewlines() {
		return printNewlines;
	}
	
	public void setPrintNewlines(boolean printNewlines) {
		this.printNewlines = printNewlines;
	}
}
