package ch.elste.battleships;

import ch.elste.battleships.Block.BlockType;
import ch.elste.battleships.Exceptions.BoatCollisionException;
import ch.elste.battleships.Exceptions.IllegalBoatSpecException;

/**
 * A grid represents a square of blocks. It provides functions to add a boat,
 * check if a block is shootable and shoot a block.
 * 
 * @author Dillon Elste
 *
 */
public class Grid {

	public static final char SYMBOL_HIT = 'X';
	public static final char SYMBOL_MISS = 'O';

	public static final int BOAT_BLOCK_COUNT = 1 * 6 + 2 * 4 + 3 * 3 + 4 * 2;

	/**
	 * All blocks contained in this grid.
	 */
	private Block[][] data;

	/**
	 * Counts how many non-water blocks have been shot.
	 */
	private int hitCount;

	/**
	 * Creates a new Grid of size {@code gridsize*gridsize}.
	 * 
	 * @param gridsize the side length of the grid.
	 */
	public Grid(int gridsize) {
		hitCount = 0;
		data = new Block[gridsize][gridsize];
		for (int r = 0; r < gridsize; r++)
			for (int c = 0; c < gridsize; c++)
				data[r][c] = new Block();
	}

	/**
	 * Finds the start of the Boat containing Block (x, y). If the block at (x, y)
	 * is not part of a boat or {@code (x, y)} is outside the grid, this method's
	 * behavior is undefined.
	 * 
	 * @param dir the direction to check in. 1 means horizontal, 0 means vertical.
	 * @param x   the x coordinate of the block to check
	 * @param y   the y coordinate of the block to check
	 * @return the coordinate of the start block
	 */
	private Coordinate findStart(int dir, int x, int y) {
		if ((dir == 1 && x == 0) || (dir == 0 && y == 0)) // we are at border => block is start
			return new Coordinate(x, y);

		while (!data[x - 1 * dir][y - 1 * (1 - dir)].isType(BlockType.WATER)) {
			x -= 1 * dir;
			y -= 1 * (1 - dir);

			// bounds check.
			if ((dir == 1 && x == 0) || (dir == 0 && y == 0))
				break;
		}

		return new Coordinate(x, y);
	}

	/**
	 * Calculates the indices of all boats connected to the block at coordinate
	 * {@code (x, y)}.
	 * 
	 * <p>
	 * If the block at {@code (x, y)} is of type water, the method behaves
	 * undefined. If {@code (x, y)} is outside the grid, the method behavior is
	 * undefined.
	 * 
	 * @param x first coordinate of the block to check
	 * @param y second coordinate of the block to check
	 * @return an array of coordinates, each one corresponding to one block of the
	 *         boat.
	 */
	private Coordinate[] getBoat(int x, int y) {
		Coordinate[] res = new Coordinate[data[x][y].getType().length];
		int dir;
		// if the boat is horizontal dir is 1
		if ((x < data.length - 1 && !data[x + 1][y].isType(BlockType.WATER))
				|| (x > 0 && !data[x - 1][y].isType(BlockType.WATER)))
			dir = 1;
		else
			dir = 0;

		Coordinate start = findStart(dir, x, y); // find the highest/leftest block of the boat

		for (int i = 0; i < res.length; i++) { // create the coordinates
			res[i] = new Coordinate(start.getX() + i * dir, start.getY() + i * (1 - dir));
		}

		return res;
	}

	/**
	 * Checks if a connected set of same-typed blocks was sunk and updates symbols
	 * accordingly. If the block at {@code (x, y)} is not part of a boat, nothing
	 * happens.
	 * 
	 * @param x the x coordinate of the block to check.
	 * @param y the y coordinate of the block to check.
	 * @return true if the boat was sunk, false otherwise.
	 */
	private boolean checkSunk(int x, int y) {
		if (data[x][y].isType(BlockType.WATER))
			return false;

		Coordinate[] boat = getBoat(x, y);
		for (Coordinate c : boat) {
			if (!data[c.getX()][c.getY()].isShot())
				return false;
		}

		for (Coordinate c : boat) {
			data[c.getX()][c.getY()].setSymbol(data[c.getX()][c.getY()].getType().symbol);
		}

		return true;
	}

	/**
	 * Shoots the block at coordinate c. If c can't be shot the method throws an
	 * assertion error.
	 * 
	 * @param c the coordinate to shoot.
	 * @return true if the boat was sunk, false otherwise.
	 */
	public boolean shoot(Coordinate c) {
		int x = c.getX();
		int y = c.getY();

		assert !data[x][y].isShot();

		data[x][y].shoot();
		data[x][y].setSymbol(data[x][y].isType(BlockType.WATER) ? SYMBOL_MISS : SYMBOL_HIT);

		if (!data[x][y].isType(BlockType.WATER)) {
			hitCount++;
		}

		return checkSunk(x, y);
	}

	/**
	 * Checks if Coordinate c was not shot already.
	 * 
	 * @param c the coordinate to check
	 * @return true if c wasn't shot before
	 * @throws IllegalBoatSpecException if the coordinates are outside the grid.
	 */
	public boolean isShootable(Coordinate c) throws IllegalBoatSpecException {
		int x = c.getX();
		int y = c.getY();
		if (x < 0 || x >= data.length || y < 0 || y >= data[0].length)
			throw new IllegalBoatSpecException();
		return !data[x][y].isShot();
	}

	/**
	 * Checks if the blocks to the left and right (below and above if {@code dir=0})
	 * of the Block at x y are of type Water.
	 * 
	 * @param x   the x coordinate
	 * @param y   the y coordinate
	 * @param dir 0 if the boat is vertical 1 otherwise
	 * @return true if both blocks are water, false otherwise
	 */
	private boolean isFree(int x, int y, int dir) {
		boolean res = true;
		if (dir == 0) { // boat is vertical
			if (x > 0) // don't check left, if boat is at left edge
				res = res && data[x - 1][y].isType(BlockType.WATER);
			if (x < data.length - 1) // check right
				res = res && data[x + 1][y].isType(BlockType.WATER);
		} else { // boat is horizontal
			if (y > 0)
				res = res && data[x][y - 1].isType(BlockType.WATER); // check above
			if (y + 1 < data[0].length)
				res = res && data[x][y + 1].isType(BlockType.WATER); // check below
		}
		return res;
	}

	// checks 3 blocks left and right of boat to insert. returns true if they are
	// water
	private boolean checkLeftRight(int x_start, int y_start, int length) {
		boolean rightFree = true, leftFree = true;
		if (x_start > 0) { // check blocks left of 'leftest'
			leftFree = data[x_start - 1][y_start].isType(BlockType.WATER);
			if (y_start > 0)
				leftFree = leftFree && data[x_start - 1][y_start - 1].isType(BlockType.WATER);
			if (y_start + length < data[0].length - 1)
				leftFree = leftFree && data[x_start - 1][y_start + 1].isType(BlockType.WATER);
		}

		if (x_start + length < data.length - 1) { // check blocks right of 'rightest'
			rightFree = data[x_start + length][y_start].isType(BlockType.WATER);
			if (y_start > 0)
				rightFree = rightFree && data[x_start + length][y_start - 1].isType(BlockType.WATER);
			if (y_start + length < data[0].length - 1)
				rightFree = rightFree && data[x_start + length][y_start + 1].isType(BlockType.WATER);
		}

		return rightFree && leftFree;
	}

	// checks 3 blocks below and above the boat to insert. returns true if they are
	// water
	private boolean checkBelowAbove(int x_start, int y_start, int length) {
		boolean aboveFree = true, belowFree = true;
		if (y_start > 0) { // check blocks below lowest
			belowFree = data[x_start][y_start - 1].isType(BlockType.WATER);
			if (x_start > 0)
				belowFree = belowFree && data[x_start - 1][y_start - 1].isType(BlockType.WATER);
			if (x_start + length < data.length - 1)
				belowFree = belowFree && data[x_start + 1][y_start - 1].isType(BlockType.WATER);
		}

		if (y_start + length < data[0].length - 1) { // check blocks above highest
			aboveFree = data[x_start][y_start + length].isType(BlockType.WATER);
			if (x_start > 0)
				aboveFree = aboveFree && data[x_start - 1][y_start + length].isType(BlockType.WATER);
			if (x_start + length < data.length - 1)
				aboveFree = aboveFree && data[x_start + 1][y_start + length].isType(BlockType.WATER);
		}

		return aboveFree && belowFree;
	}

	/**
	 * Adds a boat of type {@code type} to the grid. It starts at position
	 * {@code from} and ends at position {@code to}. This method checks for the
	 * right amount of distance to other boats.
	 * 
	 * <p>
	 * If the two coordinates do not form a straight line, the function behaves
	 * undefined. If {@code from} or {@code to} are outside the grid, the function
	 * throws an {@link IndexOutOfBoundsException}.
	 * 
	 * @param from         the starting coordinate, has to form a straight line with
	 *                     {@code to}.
	 * @param to           the end coordinate, has to form a straight line with
	 *                     {@code from}.
	 * @param type         the type of boat to add. This method does not check if
	 *                     the length matches the type
	 * @param playerNumber the player this grid belongs to. Used to determine the
	 *                     boats symbol to display.
	 * @return true if the boat could be placed, false otherwise.
	 * @throws BoatCollisionException if the coordinates intersect or are too close
	 *                                to an existing boat.
	 */
	public void addBoat(Coordinate from, Coordinate to, BlockType type, int playerNumber)
			throws BoatCollisionException {
		int dir; // 0 if boat vertical, otherwise 1
		int length = from.getDistance(to) + 1;
		int x_start = Math.min(from.getX(), to.getX());
		int y_start = Math.min(from.getY(), to.getY());
		if (from.getX() - to.getX() == 0) {
			dir = 0;
			if (!checkBelowAbove(x_start, y_start, length)) {
				throw new BoatCollisionException(); // if direction is vertical, check blocks below and above boat
			}
		} else {
			dir = 1;
			if (!checkLeftRight(x_start, y_start, length)) {
				throw new BoatCollisionException(); // if direction is horizontal, check blocks right and left of boat
			}
		}

		for (int i = 0; i < length; i++) {
			if (!isFree(x_start + i * dir, y_start + i * (1 - dir), dir)) {
				throw new BoatCollisionException(); // check each block left/right (above/below respectively)
			}
		}

		for (int i = 0; i < length; i++) {
			data[x_start + i * dir][y_start + i * (1 - dir)].setType(type);
			data[x_start + i * dir][y_start + i * (1 - dir)].setSymbol(playerNumber == 0 ? type.symbol : ' ');
		}
	}

	/**
	 * Returns the type of the block at position c.
	 * 
	 * @param c the coordinate of the block to get the type of
	 * @return the type at coordinate c as a BlockType
	 */
	public BlockType getTypeAt(Coordinate c) {
		return data[c.getX()][c.getY()].getType();
	}

	/**
	 * Returns the symbol of the Block at position c.
	 * 
	 * @param c the coordinate of the block to get the symbol of
	 * @return the symbol at coordinate c as a char
	 */
	public char getSymbolAt(Coordinate c) {
		return data[c.getX()][c.getY()].getSymbol();
	}

	/**
	 * Returns the side-length of the grid.
	 * 
	 * @return the side-length of the grid
	 */
	public int getGridsize() {
		return data.length;
	}

	/**
	 * Returns the number of hit non-water blocks.
	 * 
	 * @return the number of hit non-water blocks
	 */
	public int getHitCount() {
		return hitCount;
	}

}
