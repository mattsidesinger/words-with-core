package com.wordswithcheats.application;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FileUtils;

import com.wordswithcheats.algorithm.gaddag.GaddagAlgorithm;
import com.wordswithcheats.board.Board;
import com.wordswithcheats.board.Rack;
import com.wordswithcheats.board.Tile;
import com.wordswithcheats.board.TilePlacement;
import com.wordswithcheats.board.exception.IllegalTilePlacementException;
import com.wordswithcheats.board.multiplier.Multipliers;
import com.wordswithcheats.board.parser.RackParser;
import com.wordswithcheats.board.parser.SimpleBoardParser;
import com.wordswithcheats.board.parser.exception.BoardParseException;
import com.wordswithcheats.board.parser.exception.RackParseException;

/**
 * Given a path to a file that contains a board object and a rack, the highest score placement is determined.
 * <p>
 * <pre>USAGE: GaddagCommandLine [-b path-to-file] -r letters-in-rack</pre>
 * -b, --board: Path to a board file.  If not included, assumes an empty board<br/>
 * -r, --rack: Letters in the rack (upper or lower cased), where a * signifies a blank tile. A rack must be between 1 and 7
 * characters long.<br/>
 * <p>
 * <strong>Valid examples:</strong>
 * <ul>
 * 	<li><pre>GaddagCommandLine -b /usr/board1.txt --rack RSTLNE</pre></li>
 *  <li><pre>GaddagCommandLine --board /usr/board2.txt -r ABDF*UX</pre></li>
 * </ul>
 * <p>
 * The format of the board at the given file is parsed by the {@link SimpleBoardParser} is lenient mode.
 * 
 * @author Matt Sidesinger
 */
public class GaddagCommandLine {
	
	public static void main(String[] args) {
		
		Options options = new Options();
		options.addOption(
				OptionBuilder.withLongOpt("board")
							  .hasArg()
							  .withDescription("path to the board file")
							  .create("b")
			);
		options.addOption(
				OptionBuilder.withLongOpt("rack")
							 .hasArg()
							 .withDescription("letters in the rack where * signified a blank tile (required)")
							 .isRequired()
							 .create("r")
			);

		Board board = null;
		Rack rack = null;
		
		// TODO remove this logic and place into WordsWithFriendsCommandLine
		Tile.setPointValue('a', 1);
		Tile.setPointValue('b', 4);
		Tile.setPointValue('c', 4);
		Tile.setPointValue('d', 2);
		Tile.setPointValue('e', 1);
		Tile.setPointValue('f', 4);
		Tile.setPointValue('g', 3);
		Tile.setPointValue('h', 3);
		Tile.setPointValue('i', 1);
		Tile.setPointValue('j', 10);
		Tile.setPointValue('k', 5);
		Tile.setPointValue('l', 2);
		Tile.setPointValue('m', 4);
		Tile.setPointValue('n', 2);
		Tile.setPointValue('o', 1);
		Tile.setPointValue('p', 4);
		Tile.setPointValue('q', 10);
		Tile.setPointValue('r', 1);
		Tile.setPointValue('s', 1);
		Tile.setPointValue('t', 1);
		Tile.setPointValue('u', 2);
		Tile.setPointValue('v', 5);
		Tile.setPointValue('w', 4);
		Tile.setPointValue('x', 8);
		Tile.setPointValue('y', 3);
		Tile.setPointValue('z', 10);
		
		try {
			
    		CommandLineParser parser = new PosixParser();
    		CommandLine line = parser.parse(options, args);
    		
    		// -r, --rack
			String rackString = line.getOptionValue('r');
			if (rackString.length() > 7) {
				throw new RuntimeException("the rack contains too many letters/tiles");
			}
			try {
				rack = new RackParser().parse(rackString);
			} catch (RackParseException e) {
				throw new RuntimeException("Invalid rack: " + rackString, e);
			}
    		
    		// -b, --board
    		if (line.hasOption('b')) {
    			
    			String filePath = line.getOptionValue('b');
    			File file = new File(filePath);
    			if (!file.exists()) {
    				throw new RuntimeException("The board file does not exist: " + filePath);
    			}
    			if (!file.canRead()) {
    				throw new RuntimeException("The board file cannot be read: " + filePath);
    			}
    			
    			try {
        			String fileContents = FileUtils.readFileToString(file);
        			Tile[][] tiles = new SimpleBoardParser(true).parse(fileContents);
        			board = new Board(tiles);
    			} catch (IOException e) {
    				throw new RuntimeException("Unable to read board from file: " + filePath, e);
    			} catch (BoardParseException e) {
    				throw new RuntimeException("The board file contents cannot be parsed", e);
    			}
    		} else {
    			board = new Board(15, 15);
    		}
    		
		} catch (MissingOptionException e) {
			// automatically generate the usage statement
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(GaddagCommandLine.class.getSimpleName(), options);
			System.exit(0);
		} catch (ParseException e) {
			throw new RuntimeException("Unexpected error", e);
		}
		
		assert (board != null);
		assert (rack != null);
		
		// TODO subclass board and set default bingo score of 35
		board.setBingoScore(35);
		
		// TODO subclass board that uses a static version of these multipliers
		Multipliers multipliers = new Multipliers()
			// double letter
			.addDoubleLetterScore(3,  2)
			.addDoubleLetterScore(13, 2)
			.addDoubleLetterScore(2,  3)
			.addDoubleLetterScore(5,  3)
			.addDoubleLetterScore(11, 3)
			.addDoubleLetterScore(14, 3)
			.addDoubleLetterScore(3,  5)
			.addDoubleLetterScore(7,  5)
			.addDoubleLetterScore(9,  5)
			.addDoubleLetterScore(13, 5)
			.addDoubleLetterScore(5,  7)
			.addDoubleLetterScore(11, 7)
			.addDoubleLetterScore(5,  9)
			.addDoubleLetterScore(11, 9)
			.addDoubleLetterScore(3,  11)
			.addDoubleLetterScore(7,  11)
			.addDoubleLetterScore(9,  11)
			.addDoubleLetterScore(13, 11)
			.addDoubleLetterScore(2,  13)
			.addDoubleLetterScore(5,  13)
			.addDoubleLetterScore(11, 13)
			.addDoubleLetterScore(14, 13)
			.addDoubleLetterScore(3,  14)
			.addDoubleLetterScore(13, 14)
			// triple letter
			.addTripleLetterScore(7,  1)
			.addTripleLetterScore(9,  1)
			.addTripleLetterScore(4,  4)
			.addTripleLetterScore(12, 4)
			.addTripleLetterScore(6,  6)
			.addTripleLetterScore(10, 6)
			.addTripleLetterScore(1,  7)
			.addTripleLetterScore(15, 7)
			.addTripleLetterScore(1,  9)
			.addTripleLetterScore(15, 9)
			.addTripleLetterScore(6,  10)
			.addTripleLetterScore(10, 10)
			.addTripleLetterScore(4,  12)
			.addTripleLetterScore(12, 12)
			.addTripleLetterScore(7,  15)
			.addTripleLetterScore(9,  15)
			// double word
			.addDoubleWordScore(6,  2)
			.addDoubleWordScore(10, 2)
			.addDoubleWordScore(8,  4)
			.addDoubleWordScore(2,  6)
			.addDoubleWordScore(14, 6)
			.addDoubleWordScore(4,  8)
			.addDoubleWordScore(12, 8)
			.addDoubleWordScore(2,  10)
			.addDoubleWordScore(14, 10)
			.addDoubleWordScore(8,  12)
			.addDoubleWordScore(6,  14)
			.addDoubleWordScore(10, 14)
			// triple word			
			.addTripleWordScore(4,  1)
			.addTripleWordScore(12, 1)
			.addTripleWordScore(1,  4)
			.addTripleWordScore(15, 4)
			.addTripleWordScore(1,  12)
			.addTripleWordScore(15, 12)
			.addTripleWordScore(4,  15)
			.addTripleWordScore(12, 15);
		board.setMultipliers(multipliers);
		
		GaddagAlgorithm gaddag = new GaddagAlgorithm();
		List<TilePlacement> tp = gaddag.calculateHighestScorePlacement(board, rack);
		
		// print the board
		
		if (tp == null || tp.size() == 0) {
			System.out.println("Unable to play.");
			System.exit(0);
		}
		
		try {
			
			int score = board.place(tp);
			System.out.println(board);
			
        	// determine the word that was played
        	String word = board.getPlacedWord(tp, false); // already validated
        	// "word" was played for "n" points
        	System.out.println("\"" + word.toUpperCase() + "\" was played for " + score + " points.");		
        	// exit with the score as the exit value
        	System.exit(score);
        	
		} catch (IllegalTilePlacementException e) {
			throw new RuntimeException("Unexpected error", e);
		}
	}
}
