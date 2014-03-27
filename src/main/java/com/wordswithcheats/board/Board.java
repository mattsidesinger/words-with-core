package com.wordswithcheats.board;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wordswithcheats.board.exception.IllegalTilePlacementException;
import com.wordswithcheats.board.format.BoardFormat;
import com.wordswithcheats.board.format.SimpleBoardFormat;
import com.wordswithcheats.board.multiplier.LetterMultiplier;
import com.wordswithcheats.board.multiplier.Multiplier;
import com.wordswithcheats.board.multiplier.Multipliers;
import com.wordswithcheats.board.multiplier.WordMultiplier;

// TODO javadoc
/**
 * <p>
 * A Board is not thread-safe.
 * 
 * @author Matt Sidesinger
 */
public class Board implements Serializable {

	private int width;
	private int height;
	protected Tile[][] tiles;
	protected int tileCount = 0;
	private Multipliers multipliers = DEFAULT_MULTIPLIERS;
	private int bingoTileCount = DEFAULT_BINGO_TILE_COUNT;
	private int bingoScore = DEFAULT_BINGO_SCORE;
	private BoardFormat boardFormat;
	
	private static final long serialVersionUID = -8988534486744774251L;
	
	private static final Multipliers DEFAULT_MULTIPLIERS;
	static {
		DEFAULT_MULTIPLIERS = new Multipliers()
			// double letter
			.addDoubleLetterScore(4,  1)
			.addDoubleLetterScore(12, 1)
			.addDoubleLetterScore(7,  3)
			.addDoubleLetterScore(9,  3)
			.addDoubleLetterScore(1,  4)
			.addDoubleLetterScore(8,  4)
			.addDoubleLetterScore(15, 4)
			.addDoubleLetterScore(3,  7)
			.addDoubleLetterScore(7,  7)
			.addDoubleLetterScore(9,  7)
			.addDoubleLetterScore(13, 7)
			.addDoubleLetterScore(4,  8)
			.addDoubleLetterScore(12, 8)
			.addDoubleLetterScore(3,  9)
			.addDoubleLetterScore(7,  9)
			.addDoubleLetterScore(9,  9)
			.addDoubleLetterScore(13, 9)
			.addDoubleLetterScore(1,  12)
			.addDoubleLetterScore(8,  12)
			.addDoubleLetterScore(15, 12)
			.addDoubleLetterScore(7,  13)
			.addDoubleLetterScore(9,  13)
			.addDoubleLetterScore(4,  15)
			.addDoubleLetterScore(12, 15)
			// triple letter
			.addTripleLetterScore(6,  2)
			.addTripleLetterScore(10, 2)
			.addTripleLetterScore(2,  6)
			.addTripleLetterScore(6,  6)
			.addTripleLetterScore(10, 6)
			.addTripleLetterScore(14, 6)
			.addTripleLetterScore(2,  10)
			.addTripleLetterScore(6,  10)
			.addTripleLetterScore(10, 10)
			.addTripleLetterScore(14, 10)
			.addTripleLetterScore(6,  14)
			.addTripleLetterScore(10, 14)
			// double word
			.addDoubleWordScore(8,  8)
			.addDoubleWordScore(2,  2)
			.addDoubleWordScore(3,  3)
			.addDoubleWordScore(4,  4)
			.addDoubleWordScore(5,  5)
			.addDoubleWordScore(11, 5)
			.addDoubleWordScore(12, 4)
			.addDoubleWordScore(13, 3)
			.addDoubleWordScore(14, 2)
			.addDoubleWordScore(2,  14)
			.addDoubleWordScore(3,  13)
			.addDoubleWordScore(4,  12)
			.addDoubleWordScore(5,  11)
			.addDoubleWordScore(11, 11)
			.addDoubleWordScore(12, 12)
			.addDoubleWordScore(13, 13)
			.addDoubleWordScore(14, 14)
			// triple word			
			.addTripleWordScore(1,  1)
			.addTripleWordScore(8,  1)
			.addTripleWordScore(15, 1)
			.addTripleWordScore(1,  8)
			.addTripleWordScore(15, 8)
			.addTripleWordScore(1,  15)
			.addTripleWordScore(8,  15)
			.addTripleWordScore(15, 15);
	}
	
	/**
	 * The default number of tiles that need to be played in one turn to get a "bingo."
	 */
	public static final int DEFAULT_BINGO_TILE_COUNT = 7;
	/**
	 * The default number of points awarded for a "bingo."  
	 */
	public static final int DEFAULT_BINGO_SCORE = 50;
	
	private static final Logger logger = LoggerFactory.getLogger(Board.class);
	
	public Board(final int width, final int height) {
		Validate.isTrue(width > 1 && height > 1, "width and height must both be greater than 1");
		this.width = width;
		this.height = height;
		this.tiles = new Tile[height][width];
	}
	
	public Board(final int width, final int height, final Multipliers multipliers) {
		this(width, height);
		this.multipliers = multipliers;
	}
	
	public Board(final Tile[][] tiles) {
		
		Validate.notNull(tiles, "the Tile array cannot be null");
		
		int width = 0;
		int height = tiles.length;
		if (height > 0) {
			width = tiles[0].length;
		}
		
		Validate.isTrue(width > 1 && height > 1, "width and height must both be greater than 1");
		logger.info("Creating board with WxH: {}x{}", Integer.toString(width), Integer.toString(height));
		this.width = width;
		this.height = height;
		this.tiles = tiles;
		
		// determine tile count
		this.tileCount = 0;
		for (Tile[] row : tiles) {
			if (row != null) {
    			for (Tile tile : row) {
    				if (tile != null) {
    					this.tileCount++;
    				}
    			}
			}
		}
		logger.info("{} tiles found", Integer.valueOf(this.tileCount));
	}
	
	public Board(final Tile[][] tiles, final Multipliers multipliers) {
		this(tiles);
		this.multipliers = multipliers;
	}
	
	public void clear() {
		for (int i = 0; i < this.tiles.length; i++) {
			for (int j = 0; j < this.tiles[i].length; j++) {
				// clear the Tile at this position
				this.tiles[i][j] = null;
			}
		}
		this.tileCount = 0;
	}
	
	public int getStartX() {
		if (width % 2 == 1) {
			return (width + 1) / 2;
		}
		return width / 2;
	}
	
	public int getStartY() {
		if (height % 2 == 1) {
			return (height + 1) / 2;
		}
		return height / 2;
	}
	
	public int place(final TilePlacement... placements) throws IllegalTilePlacementException {
		return place(Arrays.asList(placements));
	}
	
	public int place(final List<TilePlacement> placements) throws IllegalTilePlacementException {
		validate(placements);
		// score first before placing the tiles
		int score = score(placements, false);
		for (TilePlacement p : placements) {
			// TilePlacement guarantees that x, y coordinates are > 0
			this.tiles[p.getY() - 1][p.getX() - 1] = p.getTile();
			this.tileCount++;
		}
		logger.info("{} tiles placed", Integer.valueOf(this.tileCount));
		return score;
	}
	
	/**
	 * @see #validate(List)	
	 */
	public void validate(final TilePlacement... placements) throws IllegalTilePlacementException {
		validate(Arrays.asList(placements));
	}
	
	/**
	 * Validate that all {@link TilePlacement} objects have the same x or same y
	 * coordinate.  This ensures that the tiles were placed in a straight line.
	 * <p>
	 * This method also ensure that a tile exists or is being placed within the min and max tiles
	 * being placed.
	 * 
	 * @param placements	The {@link Tile} objects and their x, y coordinates being placed on the board.
	 * 
	 * @throws IllegalTilePlacementException	
	 */
	public void validate(final List<TilePlacement> placements) throws IllegalTilePlacementException {
		
		Validate.notNull(placements, "at least one TilePlacement is required");
		Validate.isTrue(placements.size() > 0, "at least one TilePlacement is required");
		
		if (placements.size() == 1) {
			TilePlacement p = placements.get(0);
			validate(p);
			if (isEmpty()) {
				// tile must have same coordinates as the start position
				if (p.getX() != getStartX() || p.getY() != getStartY()) {
					throw new IllegalTilePlacementException("the tile placed must be placed on the start position", p);
				}
			} else {
    			// ensure at least one tile exists, NESW
        		if (!hasAdjacentTile(p.getX(), p.getY())) {
        			throw new IllegalTilePlacementException(
        					"the tile placed must be adjacent to an already existing tile on the board", p);
        		}
			}
			logger.debug("{} : valid!", p);
		} else {
			
			int xSum = 0;
			int ySum = 0;
			int maxX = 0;
			int maxY = 0;
    		int minX = 0;
    		int minY = 0;
    		Map<Integer, TilePlacement> placementsByX = new HashMap<Integer, TilePlacement>(4);
    		Map<Integer, TilePlacement> placementsByY = new HashMap<Integer, TilePlacement>(4);
    		
    		for (TilePlacement p : placements) {
    			validate(p);
    			logger.debug("{} : valid!", p.toString());
    			xSum += p.getX();
    			ySum += p.getY();
    			maxX = p.getX() > maxX ? p.getX() : maxX;
    			maxY = p.getY() > maxY ? p.getY() : maxY;
    			minX = p.getX() < minX || minX <= 0 ? p.getX() : minX;
    			minY = p.getY() < minY || minY <= 0 ? p.getY() : minY;
    			placementsByX.put(Integer.valueOf(p.getX()), p);
    			placementsByY.put(Integer.valueOf(p.getY()), p);
    		}
    		
    		float xAvg = xSum / placements.size();
    		float yAvg = ySum / placements.size();
    		boolean xAligned = (xAvg == minX && xAvg == maxX);
    		boolean yAligned = (yAvg == minY && yAvg == maxY);
    		if (xAligned || yAligned) {
    			
    			boolean legal = false;
    			
    			if (xAligned) { // vertically aligned
    				
    				int x = minX; // minX and maxX are the same
    				for (int y = minY; y <= maxY; y++) {
    					TilePlacement p = placementsByY.get(Integer.valueOf(y));
    					if (p == null) {
    						// a tile must already exist at this position on the board to be legal
    						Tile t = get(x, y);
    						if (t == null) {
    							String message = 
    								String.format("a tile must be placed at (%d,%d) for this to be a valid placement",
    											  Integer.valueOf(x), Integer.valueOf(y));
    							throw new IllegalTilePlacementException(message);
    						}
    					}
    					if (!legal) {
    						if (isEmpty()) {
    							// must pass through the start position
    							legal = (x == getStartX() && y == getStartY());
    						} else {
    							// at least one tile placed must be vertically adjacent to an already placed tile
    							legal = hasAdjacentTile(x, y);
    						}
    					}
    				} // ~for y: minY => maxY
    				
    			} else { // horizontally aligned
    				
    				int y = minY; // minY and maxY are the same
    				for (int x = minX; x <= maxX; x++) {
    					TilePlacement p = placementsByX.get(Integer.valueOf(x));
    					if (p == null) {
    						// a tile must already exist at this position on the board to be legal
    						Tile t = get(x, y);
    						if (t == null) {
    							String message = 
    								String.format("a tile must be placed at (%d,%d) for this to be a valid placement",
    											  Integer.valueOf(x), Integer.valueOf(y));
    							throw new IllegalTilePlacementException(message);
    						}
    					}
    					if (!legal) {
    						if (isEmpty()) {
    							// must pass through the start position
    							legal = (x == getStartX() && y == getStartY());
    						} else {
    							// at least one tile placed must be vertically adjacent to an already placed tile
    							legal = hasAdjacentTile(x, y);
    						}
    					}
    				} // ~for x: minX => maxX 
    			}
    			
    			if (!legal) {
    				if (isEmpty()) {
    					throw new IllegalTilePlacementException(
    							"at least one tile placed must be placed on the start position");
    				}
					throw new IllegalTilePlacementException(
							"at least one tile placed must be adjacent to an already existing tile on the board");
    			}
    			
    		} else {
    			logger.error("tiles must be placed in a line: " + placements);
    			throw new IllegalTilePlacementException("tiles must be placed in a line");
    		}
		}
		
	}
	
	protected void validate(final TilePlacement placement) throws IllegalTilePlacementException {
		
		Validate.notNull(placement, "a null TilePlacement cannot be placed");
		
		if (placement.getX() > width) {
			String message = String.format("the position (%d,%d) does not exist on the board",
										   Integer.valueOf(placement.getX()),
										   Integer.valueOf(placement.getY()));
			throw new IllegalTilePlacementException(message, placement);
		}
		if (placement.getY() > height) {
			String message = String.format("the position (%d,%d) does not exist on the board",
										   Integer.valueOf(placement.getX()),
										   Integer.valueOf(placement.getY()));
			throw new IllegalTilePlacementException(message, placement);
		}
		
		// ensure a Tile does not already exist at this position on the board
		if (get(placement.getX(), placement.getY()) != null) {
			String message = String.format("a Tile already exists at position (%d,%d) on the board",
										   Integer.valueOf(placement.getX()),
										   Integer.valueOf(placement.getY()));
			throw new IllegalTilePlacementException(message, placement);
		}
	}
	
	public boolean hasAdjacentTile(final int x, final int y) throws IllegalTilePlacementException {
		boolean hasAdjacentTile = hasAdjacentHorizontalTile(x, y);
		hasAdjacentTile = hasAdjacentTile || hasAdjacentVerticalTile(x, y);
		return hasAdjacentTile;
	}
	
	protected boolean hasAdjacentHorizontalTile(final int x, final int y) throws IllegalTilePlacementException {
		Tile tile = getEast(x, y);
		if (tile == null) {
			tile = getWest(x, y);
		}
		return (tile != null);
	}
	
	protected boolean hasAdjacentVerticalTile(final int x, final int y) throws IllegalTilePlacementException {
		Tile tile = getNorth(x, y);
		if (tile == null) {
			tile = getSouth(x, y);
		}
		return (tile != null);
	}
	
	public Tile get(final int x, final int y) {
		
		Validate.isTrue(x > 0, "invalid x value: must be greater than 0");
		Validate.isTrue(y > 0, "invalid y value: must be greater than 0");
		Validate.isTrue(x <= width, "invalid x value: cannot be greater than width");
		Validate.isTrue(y <= height, "invalid y value: cannot be greater than height");
		
		Tile tile = this.tiles[y - 1][x - 1];
		return tile;
	}
	
	public Tile[] getRow(final int y) {

		Validate.isTrue(y > 0, "invalid y value: must be greater than 0");
		Validate.isTrue(y <= height, "invalid y value: cannot be greater than height");
		
		Tile[] row = this.tiles[y - 1];
		return row;
	}
	
	protected Tile getNorth(final int x, final int y) throws IllegalTilePlacementException {
		Tile tile = null;
		if (y > 1) {
			try {
				tile = get(x, y - 1);
			} catch (IllegalArgumentException e) {
				throw new IllegalTilePlacementException(e.getMessage());
			}
		}
		return tile;
	}
	
	protected Tile getEast(final int x, final int y) throws IllegalTilePlacementException{
		Tile tile = null;
		if (x < width) {
			try {
				tile = get(x + 1, y);
			} catch (IllegalArgumentException e) {
				throw new IllegalTilePlacementException(e.getMessage());
			}
		}
		return tile;	
	}
	
	protected Tile getSouth(final int x, final int y) throws IllegalTilePlacementException{
		Tile tile = null;
		if (y < height) {
			try {
				tile = get(x, y + 1);
			} catch (IllegalArgumentException e) {
				throw new IllegalTilePlacementException(e.getMessage());
			}
		}
		return tile;
	}
	
	protected Tile getWest(final int x, final int y) throws IllegalTilePlacementException{
		Tile tile = null;
		if (x > 1) {
			try {
				tile = get(x - 1, y);
			} catch (IllegalArgumentException e) {
				throw new IllegalTilePlacementException(e.getMessage());
			}
		}
		return tile;
	}
	
	public String getPlacedWord(final TilePlacement[] placements) throws IllegalTilePlacementException {
		return getPlacedWord(Arrays.asList(placements), true);
	}
	
	public String getPlacedWord(final TilePlacement[] placements, final boolean validate)
		throws IllegalTilePlacementException {
		
		return getPlacedWord(Arrays.asList(placements), validate);
	}
	
	public String getPlacedWord(final List<TilePlacement> placements) throws IllegalTilePlacementException {
		return getPlacedWord(placements);
	}
	
	public String getPlacedWord(final List<TilePlacement> placements, final boolean validate)
		throws IllegalTilePlacementException {
	
		if (validate) {
			validate(placements);
		}
		
		StringBuilder word = new StringBuilder();
		int xSum = 0;
		int ySum = 0;
		int maxX = 0;
		int maxY = 0;
		int minX = 0;
		int minY = 0;
		Map<Integer, TilePlacement> placementsByX = new HashMap<Integer, TilePlacement>(4);
		Map<Integer, TilePlacement> placementsByY = new HashMap<Integer, TilePlacement>(4);
		
		int count = 0;
		for (TilePlacement p : placements) {
			if (p == null) {
				continue;
			}
			count++;
			xSum += p.getX();
			ySum += p.getY();
			maxX = p.getX() > maxX ? p.getX() : maxX;
			maxY = p.getY() > maxY ? p.getY() : maxY;
			minX = p.getX() < minX || minX <= 0 ? p.getX() : minX;
			minY = p.getY() < minY || minY <= 0 ? p.getY() : minY;
			placementsByX.put(Integer.valueOf(p.getX()), p);
			placementsByY.put(Integer.valueOf(p.getY()), p);
		}
		
		float xAvg = xSum / placements.size();
		float yAvg = ySum / placements.size();
		boolean xAligned = (xAvg == minX && xAvg == maxX);
		boolean yAligned = (yAvg == minY && yAvg == maxY);
		
		if (count == 0) {
			return "";
		} else if (count == 1) {
			if (hasAdjacentVerticalTile(minX, minY)) {
				xAligned = true;
				yAligned = false;
			} else {
				xAligned = false;
				yAligned = true;
			}
		}
		
		if (xAligned) { // vertically aligned
			
			int x = minX;
			// find minY
			Tile tile = null;
			while ((tile = getNorth(x, minY)) != null) {
				minY--;
			}
			// find maxY
			while ((tile = getSouth(x, maxY)) != null) {
				maxY++;
			}
			
			TilePlacement placement = null;
			for (int y = minY; y <= maxY; y++) {
				
				tile = get(x, y);
				placement = null;
				
				if (tile == null) {
					placement = placementsByY.get(Integer.valueOf(y));
					if (placement == null) {
						break;
					}
					tile = placement.getTile();
				}
				
				word.append(tile.toChar());
			}
			
		} else if (yAligned) { // horizontally aligned
		
			int y = minY;
			// find minX
			Tile tile = null;
			while ((tile = getWest(minX, y)) != null) {
				minX--;
			}
			// find maxX
			while ((tile = getEast(maxX, y)) != null) {
				maxX++;
			}
			
			TilePlacement placement = null;
			for (int x = minX; x <= maxX; x++) {
				
				tile = get(x, y);
				placement = null;
				
				if (tile == null) {
					placement = placementsByX.get(Integer.valueOf(x));
					if (placement == null) {
						break;
					}
					tile = placement.getTile();
				}
				
				word.append(tile.toChar());
			}
		}
		
		return word.toString();
	}
	
	/**
	 * Obtains a {@link List} of all Tile placed on the board.
	 * 
	 * @return A {@link List} of {@link Tile} objects.
	 */
	public List<Tile> getAllPlacedTiles() {
		
		List<Tile> tileList = null;
		
		if (isEmpty()) {
			tileList = Collections.emptyList();
		} else {
			
			tileList = new ArrayList<Tile>();
			
			for (Tile[] row : this.tiles) {
				if (row != null) {
	    			for (Tile tile : row) {
	    				if (tile != null) {
	    					tileList.add(tile);
	    				}
	    			}
				}
			}
		}
		
		return tileList;
	}
	
	/**
	 * Method returns all of the current tile placements as words (String objects). These are not the words that were
	 * placed historically, rather these are the words that are currently represented on the board.
	 * <p>
	 * The words are not returned in any guaranteed order.
	 * 
	 * @return	A {@link List} of words currently represented on the board.
	 */
	public List<String> getAllWords() {
		
		List<String> words = null;
		
		if (isEmpty()) {
			words = Collections.emptyList();
		} else {
			
			words = new ArrayList<String>();
			
			StringBuilder word = new StringBuilder(Math.max(height, width));
			int x = 0;
			int y = 0;
			
			// search for horizontal words
			for (y = 0; y < height; y++) {
				for (x = 0; x < width; x++) {
					
					Tile t = tiles[y][x];
					if (t != null) {
						word.append(t.getLetter());
					}
					
					if (t == null || x == width - 1) {
						if (word.length() > 0) {
							if (word.length() > 1) {
								words.add(word.toString());
							}
							word.setLength(0); // clear
						}
					}
				}
			}
			
			// search for vertical words
			for (x = 0; x < width; x++) {
				for (y = 0; y < height; y++) {
					
					Tile t = tiles[y][x];
					if (t != null) {
						word.append(t.getLetter());
					}
					
					if (t == null || y == height - 1) {
						if (word.length() > 0) {
							if (word.length() > 1) {
								words.add(word.toString());
							}
							word.setLength(0); // clear
						}
					}
				}
			}
		}
		
		return words;
	}
	
	public int score(final TilePlacement[] placements) throws IllegalTilePlacementException {
		return score(Arrays.asList(placements), true);
	}
	
	public int score(final List<TilePlacement> placements) throws IllegalTilePlacementException {
		return score(placements, true);
	}
	
	public int score(final TilePlacement[] placements, final boolean validate) throws IllegalTilePlacementException {
		return score(Arrays.asList(placements), validate);
	}
	
	public int score(final List<TilePlacement> placements, final boolean validate) throws IllegalTilePlacementException {
		
		int score = 0;
		
		if (validate) {
			validate(placements);
		}
		
		if (placements == null || placements.size() == 0) {
			return 0;
		}
		
		int xSum = 0;
		int ySum = 0;
		int maxX = 0;
		int maxY = 0;
		int minX = 0;
		int minY = 0;
		Map<Integer, TilePlacement> placementsByX = new HashMap<Integer, TilePlacement>(4);
		Map<Integer, TilePlacement> placementsByY = new HashMap<Integer, TilePlacement>(4);
		
		int count = 0;
		for (TilePlacement p : placements) {
			if (p == null) {
				continue;
			}
			count++;
			xSum += p.getX();
			ySum += p.getY();
			maxX = p.getX() > maxX ? p.getX() : maxX;
			maxY = p.getY() > maxY ? p.getY() : maxY;
			minX = p.getX() < minX || minX <= 0 ? p.getX() : minX;
			minY = p.getY() < minY || minY <= 0 ? p.getY() : minY;
			placementsByX.put(Integer.valueOf(p.getX()), p);
			placementsByY.put(Integer.valueOf(p.getY()), p);
		}
		
		if (count == 0) {
			return 0;
		} else if (count == 1) {
			TilePlacement p = placements.get(0);
			score += scoreHorizontally(p);
			if (score == 0) {
				score += scoreVertically(p);
			}
			return score;
		}
		
		float xAvg = xSum / placements.size();
		float yAvg = ySum / placements.size();
		boolean xAligned = (xAvg == minX && xAvg == maxX);
		boolean yAligned = (yAvg == minY && yAvg == maxY);
		
		if (xAligned) { // vertically aligned
			
			int x = minX;
			// find minY
			Tile tile = null;
			while ((tile = getNorth(x, minY)) != null) {
				minY--;
			}
			// find maxY
			while ((tile = getSouth(x, maxY)) != null) {
				maxY++;
			}
			
			int wordMultiplier = 1;
			Multiplier multiplier = null;
			TilePlacement placement = null;
			
			// variables used for debugging
			StringBuilder sb = null;
			if (logger.isDebugEnabled()) {
				sb = new StringBuilder();
				sb.append('(');
			}
			
			int yScore = 0; // the vertical score
			for (int y = minY; y <= maxY; y++) {
				
				tile = get(x, y);
				placement = null;
				multiplier = null;
				
				if (tile == null) {
					placement = placementsByY.get(Integer.valueOf(y));
					if (placement == null) {
						break;
					}
					tile = placement.getTile();
					multiplier = this.multipliers.get(x, y);
				}
				
				int letterScore = tile.getPointValue();
				if (logger.isDebugEnabled()) {
					if (sb.length() > 1) {
						sb.append(' ');
						sb.append('+');
						sb.append(' ');
					}
					sb.append(tile.toString());
				}
				
				if (multiplier != null) {
					if (multiplier instanceof LetterMultiplier) {
						letterScore *= multiplier.getValue();
						if (logger.isDebugEnabled()) {
							sb.append('x');
							sb.append(multiplier.getValue());
						}
					} else if (multiplier instanceof WordMultiplier) {
						wordMultiplier *= ((WordMultiplier) multiplier).getValue();
					}
				}
				
				yScore += letterScore;
				if (placement != null) {
					score += scoreHorizontally(placement);
				}
			}
			
			// add the vertical score
			yScore *= wordMultiplier;
			score += yScore;
			
			if (logger.isDebugEnabled()) {
				sb.append(')');
				if (wordMultiplier > 1) {
					sb.append('x');
					sb.append(wordMultiplier);
				}
				sb.append(' ');
				sb.append('=');
				sb.append(' ');
				sb.append(yScore);
				logger.debug(sb.toString());
			}
			
		} else if (yAligned) { // horizontally aligned
		
			int y = minY;
			// find minX
			Tile tile = null;
			while ((tile = getWest(minX, y)) != null) {
				minX--;
			}
			// find maxX
			while ((tile = getEast(maxX, y)) != null) {
				maxX++;
			}
			
			int wordMultiplier = 1;
			Multiplier multiplier = null;
			TilePlacement placement = null;
			
			// variables used for debugging
			StringBuilder sb = null;
			if (logger.isDebugEnabled()) {
				sb = new StringBuilder();
				sb.append('(');
			}
			
			int xScore = 0; // the vertical score
			for (int x = minX; x <= maxX; x++) {
				
				tile = get(x, y);
				placement = null;
				multiplier = null;
				
				if (tile == null) {
					placement = placementsByX.get(Integer.valueOf(x));
					if (placement == null) {
						break;
					}
					tile = placement.getTile();
					multiplier = this.multipliers.get(x, y);
				}
				
				int letterScore = tile.getPointValue(); 
				if (logger.isDebugEnabled()) {
					if (sb.length() > 1) {
						sb.append(' ');
						sb.append('+');
						sb.append(' ');
					}
					sb.append(tile.toString());
				}
				
				if (multiplier != null) {
					if (multiplier instanceof LetterMultiplier) {
						letterScore *= multiplier.getValue();
						if (logger.isDebugEnabled()) {
							sb.append('x');
							sb.append(multiplier.getValue());
						}
					} else if (multiplier instanceof WordMultiplier) {
						wordMultiplier *= ((WordMultiplier) multiplier).getValue();
					}
				}
				
				xScore += letterScore;
				score += scoreVertically(placement);
			}
			
			// add the vertical score
			xScore *= wordMultiplier;
			score += xScore;
			
			if (logger.isDebugEnabled()) {
				sb.append(')');
				if (wordMultiplier > 1) {
					sb.append('x');
					sb.append(wordMultiplier);
				}
				sb.append(' ');
				sb.append('=');
				sb.append(' ');
				sb.append(xScore);
				logger.debug(sb.toString());
			}
		}
		
		if (count >= getBingoTileCount()) {
			logger.debug("BINGO awarded. +{}", Integer.toString(getBingoScore()));
			score += getBingoScore();
		}
		
		logger.debug("TOTAL: {}", Integer.toString(score));
		return score;
	}
	
	protected int scoreHorizontally(final TilePlacement placement) throws IllegalTilePlacementException {
		
		int score = 0;
		
		if (placement != null) {
		
    		int x = placement.getX();
    		int y = placement.getY();
    		Tile tile = null;
    		int tileCount = 1;
    		
			// variables used for debugging
			StringBuilder sb = null;
			if (logger.isDebugEnabled()) {
				sb = new StringBuilder();
				sb.append('(');
			}
    		
    		// go west
    		while ((tile = getWest(x, y)) != null) {
				tileCount++;
				if (logger.isDebugEnabled()) {
					sb.insert(1, ' ');
					sb.insert(1, '+');
					sb.insert(1, ' ');
					sb.insert(1, tile.toString());
				}
				score += tile.getPointValue();
    			x--;
    		}
    		
    		tile = placement.getTile();
    		
			if (logger.isDebugEnabled()) {
				sb.append(tile.toString());
			}
			
			int letterScore = tile.getPointValue();
			Multiplier multiplier = this.multipliers.get(placement.getX(), placement.getY());
			int wordMultiplier = 1;
			
			if (multiplier != null) {
				if (multiplier instanceof LetterMultiplier) {
					letterScore *= multiplier.getValue();
					if (logger.isDebugEnabled()) {
						sb.append('x');
						sb.append(multiplier.getValue());
					}
				} else if (multiplier instanceof WordMultiplier) {
					wordMultiplier = ((WordMultiplier) multiplier).getValue();
				}
			}
			
			score += letterScore;
    		
    		// go east
			x = placement.getX();
    		while ((tile = getEast(x, y)) != null) {
				tileCount++;
				if (logger.isDebugEnabled()) {
					if (sb.length() > 1) {
						sb.append(' ');
						sb.append('+');
						sb.append(' ');
					}
					sb.append(tile.toString());
				}				
				score += tile.getPointValue();
    			x++;
    		}
    		
    		score *= wordMultiplier;
    	
    		if (tileCount <= 1) {
    			score = 0;
    		} else if (logger.isDebugEnabled()) {
				sb.append(')');
				if (wordMultiplier > 1) {
					sb.append('x');
					sb.append(wordMultiplier);
				}
				sb.append(' ');
				sb.append('=');
				sb.append(' ');
				sb.append(score);
				logger.debug(sb.toString());
			}
		}
		
		return score;
	}
	
	protected int scoreVertically(final TilePlacement placement) throws IllegalTilePlacementException {

		int score = 0;
		
		if (placement != null) {
		
    		int x = placement.getX();
    		int y = placement.getY();
    		Tile tile = null;
    		int tileCount = 1;
    		
			// variables used for debugging
			StringBuilder sb = null;
			if (logger.isDebugEnabled()) {
				sb = new StringBuilder();
				sb.append('(');
			}
    		
    		// go north
    		while ((tile = getNorth(x, y)) != null) {
    			tileCount++;
				if (logger.isDebugEnabled()) {
					sb.insert(1, ' ');
					sb.insert(1, '+');
					sb.insert(1, ' ');
					sb.insert(1, tile.toString());
				}
				score += tile.getPointValue();
    			y--;
    		}
    		
    		tile = placement.getTile();
    		
			if (logger.isDebugEnabled()) {
				sb.append(tile.toString());
			}
    		
    		int letterScore = tile.getPointValue();
			Multiplier multiplier = this.multipliers.get(placement.getX(), placement.getY());
			int wordMultiplier = 1;
			
			if (multiplier != null) {
				if (multiplier instanceof LetterMultiplier) {
					letterScore *= multiplier.getValue();
					if (logger.isDebugEnabled()) {
						sb.append('x');
						sb.append(multiplier.getValue());
					}
				} else if (multiplier instanceof WordMultiplier) {
					wordMultiplier = ((WordMultiplier) multiplier).getValue();
				}
			}
			
			score += letterScore;
    		
    		// go south
			y = placement.getY();
    		while ((tile = getSouth(x, y)) != null) {
    			tileCount++;
				if (logger.isDebugEnabled()) {
					if (sb.length() > 1) {
						sb.append(' ');
						sb.append('+');
						sb.append(' ');
					}
					sb.append(tile.toString());
				}
				score += tile.getPointValue();
    			y++;
    		}
    		
    		score *= wordMultiplier;
    		
    		if (tileCount <= 1) {
    			score = 0;
    		} else if (logger.isDebugEnabled()) {
				sb.append(')');
				if (wordMultiplier > 1) {
					sb.append('x');
					sb.append(wordMultiplier);
				}
				sb.append(' ');
				sb.append('=');
				sb.append(' ');
				sb.append(score);
				logger.debug(sb.toString());
			}
		}
		
		return score;
	}
	
	public Tile[][] toArray() {
		return (Tile[][]) ArrayUtils.clone(this.tiles);
	}
	
	/**
	 * @return the width
	 */
	public int getWidth() {	
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return the tiles
	 */
	protected Tile[][] getTiles() {	
		return tiles;
	}

	/**
	 * @return the empty
	 */
	public boolean isEmpty() {
		return tileCount == 0;
	}
	
	/**
	 * @return	Return the number of tiles placed 
	 */
	public int getTileCount() {
		return tileCount;
	}

	/**
	 * @return the multipliers
	 */
	public Multipliers getMultipliers() {	
		return multipliers;
	}

	/**
	 * @param multipliers the multipliers to set
	 */
	public void setMultipliers(final Multipliers multipliers) {
		this.multipliers = multipliers;
	}
	
	/**
	 * @return The number of tiles that need to be played in a single turn to receive the bingo score.
	 */
	public int getBingoTileCount() {
		return bingoTileCount;
	}
	
	/**
	 * @param bingoTileCount The number of tiles that need to be played in a single turn to receive the bingo score.
	 */
	public void setBingoTileCount(int bingoTileCount) {
		// ensure that the Bingo tile count is always greater than 1
		Validate.isTrue(bingoScore >= 1, "bingoTileCount must be greater than or equal to 1");
		this.bingoTileCount = bingoTileCount;
	}

	/**
	 * @return The number of points awarded when a user uses the number of tiles set by the bingoTileCount.
	 */
	public int getBingoScore() {
		return bingoScore;
	}

	/**
	 * @param bingoScore The number of points awarded when a user uses the number of tiles set by the bingoTileCount.
	 */
	public void setBingoScore(int bingoScore) {
		// ensure that the Bingo score never negative
		Validate.isTrue(bingoScore >= 0, "bingoScore must be greater than or equal to 0");
		this.bingoScore = bingoScore;
	}

	/**
	 * @return the boardFormat
	 */
	public BoardFormat getBoardFormat() {	
		return boardFormat;
	}

	
	/**
	 * @param boardFormat the boardFormat to set
	 */
	public void setBoardFormat(final BoardFormat boardFormat) {
		this.boardFormat = boardFormat;
	}

	/**
	 * Converts the {@link Board} to a {@link String} using the set {@link BoardFormat}.  If the
	 * {@link BoardFormat} is <code>null</code> then a new instance of {@link SimpleBoardFormat} is used.
	 */
	@Override
	public String toString() {
		String toString = null;
		if (this.boardFormat == null) {
			toString = new SimpleBoardFormat().format(this);
		} else {
			toString = boardFormat.format(this);
		}
		return toString;
	}
}
