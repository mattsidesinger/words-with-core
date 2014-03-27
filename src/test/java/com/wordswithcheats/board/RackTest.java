package com.wordswithcheats.board;

import static com.wordswithcheats.board.Tile.A;
import static com.wordswithcheats.board.Tile.B;
import static com.wordswithcheats.board.Tile.C;
import static com.wordswithcheats.board.Tile.D;
import static com.wordswithcheats.board.Tile.E;
import static com.wordswithcheats.board.Tile.F;
import static com.wordswithcheats.board.Tile.G;
import static com.wordswithcheats.board.Tile.J;
import static com.wordswithcheats.board.Tile.R;
import static com.wordswithcheats.board.Tile.Z;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.wordswithcheats.board.exception.RackFullException;

/**
 * Test case for the {@link Rack} class.
 * 
 * @author Matt Sidesinger
 */
public class RackTest {

	@Test
	public void testRack() {
	
		Rack r = new Rack();
		assertEquals(0, r.tileCount());
		assertEquals(7, r.size());
		assertTrue(Arrays.equals(new Tile[] { null, null, null, null, null, null, null }, r.toArray()));
		
		r = new Rack(6);
		assertEquals(0, r.tileCount());
		assertEquals(6, r.size());
		assertTrue(Arrays.equals(new Tile[] { null, null, null, null, null, null }, r.toArray()));
		
		try {
			r = new Rack(0);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			r = new Rack(-1);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		List<Tile> l = new ArrayList<Tile>();
		l.add(A);
		r = new Rack(l);
		assertEquals(1, r.tileCount());
		assertEquals(7, r.size());
		assertTrue(Arrays.equals(new Tile[] { A, null, null, null, null, null, null }, r.toArray()));
		
		l.add(B);
		l.add(C);
		r = new Rack(l);
		assertEquals(3, r.tileCount());
		assertEquals(7, r.size());
		assertTrue(Arrays.equals(new Tile[] { A, B, C, null, null, null, null }, r.toArray()));
		
		l = new ArrayList<Tile>();
		l.add(null);
		r = new Rack(l);
		assertEquals(0, r.tileCount());
		assertEquals(7, r.size());
		assertTrue(Arrays.equals(new Tile[] { null, null, null, null, null, null, null }, r.toArray()));
		
		l.add(A); // now contains null and 'a'
		r = new Rack(l);
		assertEquals(1, r.tileCount());
		assertEquals(7, r.size());
		assertTrue(Arrays.equals(new Tile[] { A, null, null, null, null, null, null }, r.toArray()));
		
		try {
			r = new Rack((List<Tile>) null);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		r = new Rack(Collections.<Tile>emptyList());
		assertEquals(0, r.tileCount());
		assertEquals(7, r.size());
		assertTrue(Arrays.equals(new Tile[] { null, null, null, null, null, null, null }, r.toArray()));
		
		Tile[] tiles = new Tile[] { A };
		r = new Rack(tiles);
		assertEquals(1, r.tileCount());
		assertEquals(7, r.size());
		assertTrue(Arrays.equals(new Tile[] { A, null, null, null, null, null, null }, r.toArray()));
		
		tiles = new Tile[] { A, B, C };
		r = new Rack(tiles);
		assertEquals(3, r.tileCount());
		assertEquals(7, r.size());
		assertTrue(Arrays.equals(new Tile[] { A, B, C, null, null, null, null }, r.toArray()));
		
		tiles = new Tile[] { null };
		r = new Rack(tiles);
		assertEquals(0, r.tileCount());
		assertEquals(7, r.size());
		assertTrue(Arrays.equals(new Tile[] { null, null, null, null, null, null, null }, r.toArray()));
		
		tiles = new Tile[] { null, A };
		r = new Rack(tiles);
		assertEquals(1, r.tileCount());
		assertEquals(7, r.size());
		assertTrue(Arrays.equals(new Tile[] { A, null, null, null, null, null, null }, r.toArray()));
		
		try {
			r = new Rack((Tile[]) null);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		r = new Rack(new Tile[0]);
		assertEquals(0, r.tileCount());
		assertEquals(7, r.size());
		assertTrue(Arrays.equals(new Tile[] { null, null, null, null, null, null, null }, r.toArray()));
	}
	
	@Test
	public void testSize() {
		
		 Rack r = new Rack();
		 assertEquals(Rack.DEFAULT_SIZE, r.size());
		 
		 r = new Rack(8);
		 assertEquals(8, r.size());
	}
	
	@Test
	public void testIsEmpty() throws Exception {
		
		Rack r = new Rack();
		assertTrue(r.isEmpty());
		
		r.add(A);
		assertFalse(r.isEmpty());
		
		r.clear();
		assertTrue(r.isEmpty());
	}
	
	@Test
	public void testTileCount() throws Exception {
		
		Rack r = new Rack();
		assertEquals(0, r.tileCount());
		
		r.add(A);
		assertEquals(1, r.tileCount());
		
		r.clear();
		assertEquals(0, r.tileCount());
	}
	
	@Test
	public void testEmptyCount() throws Exception {
		
		Rack r = new Rack();
		assertEquals(7, r.emptyCount());
		
		r.add(A);
		assertEquals(6, r.emptyCount());
		
		r.clear();
		assertEquals(7, r.emptyCount());
	}
	
	@Test
	public void testAdd() throws Exception {
		
		Rack r = new Rack(3);
		r.add(A);
		assertEquals(1, r.tileCount());
		
		try {
			r.add((Tile) null);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		r.add(B);
		r.add(C);
		try {
			r.add(D);
			fail("RackFullException should have been thrown");
		} catch (RackFullException e) {
			// success
		}
	}
	
	@Test
	public void testTake() throws Exception {
		
		Rack r = new Rack();
		Tile t = r.take(A);
		assertEquals(null, t);
		
		r.add(A);
		
		t = r.take(B);
		assertEquals(null, t);
		assertEquals(1, r.tileCount());
		
		t = r.take(A);
		assertEquals(A, t);
		assertEquals(0, r.tileCount());
		
		r.add(A);
		r.add(A);
		t = r.take(A);
		assertEquals(1, r.tileCount());
		
		r.clear();
		
		try {
			r.take((Tile) null);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		r.add(A);
		r.add(B);
		r.add(B);
		r.add(C);
		r.add(C);
		r.add(C);
		r.add(C);
		
		Tile[] expected = new Tile[] { B, C };
		Tile[] tiles = null;
		tiles = r.take(new Tile[] { B, C });
		assertTrue(Arrays.equals(expected, tiles));
		
		expected = new Tile[] { C, C };
		tiles = r.take(new Tile[] { C, C });
		assertTrue(Arrays.equals(expected, tiles));

		expected = new Tile[0];
		tiles = r.take(new Tile[] { Z });
		assertTrue(Arrays.equals(expected, tiles));
		
		expected = new Tile[] { A };
		tiles = r.take(new Tile[] { A, Z });
		assertTrue(Arrays.equals(expected, tiles));
		
		try {
			r.take((Tile[]) null);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		expected = new Tile[0];
		tiles = r.take(new Tile[0]);
		assertTrue(Arrays.equals(expected, tiles));
		
		try {
			r.take(new Tile[] { null });
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			r.take(new Tile[] { A, null });
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			// success
		}
		
		// ensure 'a' was not removed
		assertEquals(2, r.tileCount());
	}
	
	@Test
	public void testClear() throws Exception {
		
		final Tile[] expected = new Tile[] {
			null, null, null, null, null, null, null 
		};
		
		Rack r = new Rack();
		r.clear();
		assertEquals(0, r.tileCount());
		assertTrue(Arrays.equals(expected, r.toArray()));
		
		r.add(A);
		r.clear();
		assertEquals(0, r.tileCount());
		assertTrue(Arrays.equals(expected, r.toArray()));
		
		r.add(A);
		r.add(B);
		r.add(C);
		r.add(D);
		r.add(E);
		r.add(F);
		r.add(G);
		r.clear();
		assertEquals(0, r.tileCount());
		assertTrue(Arrays.equals(expected, r.toArray()));
	}
	
	@Test
	public void testSort() throws Exception {
		
		Rack r = null;
		Tile[] expected = null;
		
		r = new Rack();
		r.sort();
		
		expected = new Tile[] { A, null, null, null, null, null, null };
		r.add(A);
		r.sort();
		assertTrue(Arrays.equals(expected, r.toArray()));
		
		expected = new Tile[] { A, B , null, null, null, null, null };
		r = new Rack(new Tile[] { B, A });
		r.sort();
		assertTrue(Arrays.equals(expected, r.toArray()));
		
		expected = new Tile[] {
			new BlankTile(), new BlankTile('a'), new BlankTile('z'), A, Z, null, null
		};
		r = new Rack(new Tile[] { new BlankTile('z'), Z,  new BlankTile(), A, new BlankTile('a')});
		r.sort();
		assertTrue(Arrays.equals(expected, r.toArray()));
	}
	
	@Test
	public void testShuffle() throws Exception {
		
	}
	
	@Test
	public void rotateLeft() throws Exception {
		
	}
	
	@Test
	public void rotateRight() throws Exception {
		
	}
	
	@Test
	public void testToArray() throws Exception {

		Rack r = new Rack();
		Tile[] expected = new Tile[] {
			null, null, null, null, null, null, null 
		};
		assertTrue(Arrays.equals(expected, r.toArray()));
		
		
		expected = new Tile[] {
			A, null, null, null, null, null, null 
		};
		r.add(A);
		assertTrue(Arrays.equals(expected, r.toArray()));
		
		expected = new Tile[] { A, B, new BlankTile(), J, D, Z, R };
		r = new Rack(expected);
		assertTrue(Arrays.equals(expected, r.toArray()));
		
		r.take(B);
		expected = new Tile[] { A, new BlankTile(), J, D, Z, R, null };
		assertTrue(Arrays.equals(expected, r.toArray()));
		
		r.take(D);
		expected = new Tile[] { A, new BlankTile(), J, Z, R, null, null };
		assertTrue(Arrays.equals(expected, r.toArray()));
		
		r.take(R);
		expected = new Tile[] { A, new BlankTile(), J, Z, null, null, null };
		assertTrue(Arrays.equals(expected, r.toArray()));
		
		r.add(G);
		expected = new Tile[] { A, new BlankTile(), J, Z, G, null, null };
		assertTrue(Arrays.equals(expected, r.toArray()));
	}
	
	@Test
	public void testToString() throws Exception {
		
		Rack r = new Rack();
		assertEquals("[  | | | | | |  ]", r.toString());
		
		r = new Rack();
		r.add(A);
		assertEquals("[ [A:1]| | | | | |  ]", r.toString());
		
		Tile[] tiles = new Tile[] { A, B, new BlankTile(), J, D, Z, R };
		r = new Rack(tiles);
		assertEquals("[ [A:1]|[B:3]|[ :0]|[J:8]|[D:2]|[Z:10]|[R:1] ]", r.toString());
		
		r.take(B);
		assertEquals("[ [A:1]|[ :0]|[J:8]|[D:2]|[Z:10]|[R:1]|  ]", r.toString());
		
		r.take(D);
		assertEquals("[ [A:1]|[ :0]|[J:8]|[Z:10]|[R:1]| |  ]", r.toString());
		
		r.take(R);
		assertEquals("[ [A:1]|[ :0]|[J:8]|[Z:10]| | |  ]", r.toString());
		
		r.add(G);
		assertEquals("[ [A:1]|[ :0]|[J:8]|[Z:10]|[G:2]| |  ]", r.toString());
	}
}
