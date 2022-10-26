package ch.elste.battleships;

public class Coordinate {

	private int x, y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Coordinate(char x, int y) {
		this.x = chrToNum(x);
		this.y = y;
	}

	/**
	 * Returns the Manhattan distance between coordinate c1 and c2.
	 * 
	 * @param c1 the first coordinate
	 * @param c2 the second coordinate
	 * @return the Manhattan distance of the given coordinates.
	 */
	public static int getDistance(Coordinate c1, Coordinate c2) {
		return Math.abs(c1.x - c2.x) + Math.abs(c1.y - c2.y);
	}

	/**
	 * Returns the Manhattan distance to the coordinate c. This is the mathematic
	 * distance, i.e. exclusive {@code this coordinate}.
	 * 
	 * @param c the other coordinate
	 * @return the Manhattan distance from this coordinate to the other.
	 */
	public int getDistance(Coordinate c) {
		return getDistance(this, c);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	private int chrToNum(char c) {
		return (int) (Character.toUpperCase(c) - 'A');
	}

	@Override
	public String toString() {
		return String.format("Coordinate(%d, %d)", x, y);
	}
}
