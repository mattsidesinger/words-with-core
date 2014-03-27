package com.wordswithcheats.game;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;

import com.wordswithcheats.algorithm.ScrabbleAlgorithm;
import com.wordswithcheats.algorithm.gaddag.GaddagAlgorithm;
import com.wordswithcheats.application.GaddagCommandLine;
import com.wordswithcheats.board.Board;
import com.wordswithcheats.board.Rack;
import com.wordswithcheats.board.Tile;
import com.wordswithcheats.board.TileBag;
import com.wordswithcheats.board.TilePlacement;
import com.wordswithcheats.board.exception.IllegalTilePlacementException;
import com.wordswithcheats.board.format.SimpleBoardFormat;

/**
 * Generates a given number of random games as files in JSON format to the given directory.
 * <pre>
 * usage: GaddagCommandLine
 *   -c,--count       the number of files to generated; 1 if not included
 *   -d,--dir &lt;arg&gt;   output dir/path of the generated files (required)
 * </pre>
 * <p>
 * JSON output format:
 * <pre>
 * {
 *   rack_1: "ABCDEFG",
 *   rack_2: "HIJKLMN",
 *   board: " ABC ..."
 * }
 * </pre>
 * 
 * @author Matt Sidesinger
 */
public class RandomGameGenerator {

	private static final int MIN_MOVE_COUNT = 3;
	private static final int MAX_MOVE_COUNT = 12;
	
	private static final SimpleBoardFormat boardFormat;
	static {
		// flat, no new lines
		boardFormat = new SimpleBoardFormat();
		boardFormat.setPrintLineNumbers(false);
		boardFormat.setPrintMultipliers(false);
		boardFormat.setPrintNewlines(false);
		boardFormat.setPrintSpaceAfterTile(false);
		
	}
	
	public static void main(String[] args) {
		
		Options options = new Options();
		options.addOption(
				OptionBuilder.withLongOpt("count")
							  .hasArg()
							  .withDescription("the number of files to generated; 1 if not included")
							  .create("c")
			);
		options.addOption(
				OptionBuilder.withLongOpt("dir")
							 .hasArg()
							 .withDescription("output dir/path of the generated files (required)")
							 .isRequired()
							 .create("d")
			);
		
		int count = 0;
		String dir = null;
		File dirFile = null;
		try {
			
    		CommandLineParser parser = new PosixParser();
    		CommandLine line = parser.parse(options, args);
    		
    		// -c, --count
    		if (line.hasOption('c')) {
    			
    			String countString = line.getOptionValue('c');
    			try {
    				count = Integer.parseInt(countString);
    			} catch (NumberFormatException e) {
    				throw new RuntimeException("count must be a number");
    			}
    			if (count <= 0) {
    				throw new RuntimeException("count must be a number greater than or equal to 1");
    			}
    		}
    		
    		// -d, --dir
			dir = line.getOptionValue('d');
			if (dir == null || dir.trim().length() == 0) {
				throw new RuntimeException("invalid output dir");
			}
			dirFile = new File(dir);
			if (!dirFile.exists()) {
				throw new RuntimeException("the directory does not exist: " + dir);
			}
			if (!dirFile.isDirectory()) {
				throw new RuntimeException("the path is not a directory: " + dir);
			}
			if (!dirFile.canWrite()) {
				throw new RuntimeException("cannot write to the directory: " + dir);
			}
    		
		} catch (MissingOptionException e) {
			// automatically generate the usage statement
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(GaddagCommandLine.class.getSimpleName(), options);
			System.exit(0);
		} catch (ParseException e) {
			throw new RuntimeException("Unexpected error", e);
		}
		
		ScrabbleAlgorithm algorithm = new GaddagAlgorithm();
		Rack r1 = new Rack();
		Rack r2 = new Rack();
		Board board = new Board(15, 15);
		int score1 = 0;
		int score2 = 0;
		
		Map<String, String> game = new LinkedHashMap<String, String>();
		int moves = MIN_MOVE_COUNT;
		int couldNotPlayCount = 0;
		int step = 1000;
		if (count >= 100000) {
			step = 10000;
		}
		
		for (int i = 0; i < count; i++) {
			
			TileBag tileBag = new TileBag(); // no reset()
			tileBag.fillRack(r1);
			tileBag.fillRack(r2);
			
			for (int j = 0; j < moves; j++) {
				
    			Rack rack = null;
    			if (j % 2 == 1) {
    				rack = r2;
    			} else {
    				rack = r1;
    			}
				
    			List<TilePlacement> placements = algorithm.longestPlacement(board, rack);
    			if (placements == null || placements.size() == 0) {
    				couldNotPlayCount++;
    				if (couldNotPlayCount == 2) {
    					break;
    				}
    			} else {
        			try {
            			if (i % 2 == 1) {
            				score2 += board.place(placements);
            			} else {
            				score1 += board.place(placements);
            			}
            			
            			for (TilePlacement tp : placements) {
            				Tile tile = rack.take(tp.getTile());
            				if (tile == null) {
            					throw new RuntimeException("Not able to remove the following tile: " + tp.getTile());
            				}
            			}
            			
            			tileBag.fillRack(rack);
            			
        			} catch (IllegalTilePlacementException e) {
        				System.out.print("Board.place() generated an illegal placment.");
        				System.out.println("Board: " + board);
        				System.out.println("Placements: " + placements);
        				break;
        			}
    			}
			}
			
			// write to file
			String boardAsString = boardFormat.format(board);
			game.put("rack_1", r1.toSimpleString());
			game.put("rack_2", r2.toSimpleString());
			game.put("board", boardAsString);
			
			String json = JSONObject.toJSONString(game);
			File outputFile = new File(dirFile, "game." + (i + 1) + ".json");
			if (i % step == 0 && i != 0) {
				System.out.println("Writing file #" + (i + 1) + " ...");
			}
			try {
				FileUtils.writeStringToFile(outputFile, json);
			} catch (IOException e) {
				System.out.println("Unable to create or write game to file ('" +
									outputFile.getAbsolutePath() + "'): " + e.getMessage());
			}
			
			moves++;
			if (moves > MAX_MOVE_COUNT) {
				moves = MIN_MOVE_COUNT;
			}
			
			// clear variables
			board.clear();
			r1.clear();
			r2.clear();
			game.clear();
			couldNotPlayCount = 0;
		}
		
		System.out.println("Done.");
	}
}
