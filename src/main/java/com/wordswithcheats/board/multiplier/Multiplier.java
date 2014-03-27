package com.wordswithcheats.board.multiplier;

import java.io.Serializable;

import org.apache.commons.lang.Validate;

/**
 * A multiplier is used during scoring.  Examples are a "double word score" and "triple letter score,"
 * 
 * @author Matt Sidesinger
 */
public abstract class Multiplier implements Comparable<Multiplier>, Serializable {

	private int value = 1;

	private static final long serialVersionUID = -4388976262805151707L;
	
	/**
	 * Creates a Multiplier with the given value.
	 * 
	 * @param value	The value the score should be multiplied by.
	 */
	public Multiplier(final int value) {
		Validate.isTrue(value > 1, "multiplier value must be greater than 1");
		this.value = value;
	}
	
	/**
	 * @return	The value the score should be multiplied by. 
	 */
	public int getValue() {
		return this.value;
	}

	@Override
	public int compareTo(Multiplier m) {
		int result = 1;
		if (m != null) {
			result = this.value - m.getValue();
		}
		return result;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Multiplier other = (Multiplier) obj;
		if (value != other.value)
			return false;
		return true;
	}
}
