package ch.elste.battleships;

import java.util.ArrayList;
import java.util.List;

import ch.elste.battleships.Block.BlockType;

/**
 * A grid represents a square of blocks. It provides functions to add a boat,
 * check if a block is shootable and shoot a block.
 * 
 * @author Dillon Elste
 *
 */
public class Grid {

	/**
	 * All blocks contained in this grid.
	 */
	private Block[][] data;

	/**
	 * Creates a new Grid of size {@code gridsize*gridsize}.
	 * 
	 * @param gridsize the side length of the grid.
	 */
	public Grid(int gridsize) {
		data = new Block[gridsize][gridsize];
	}

	/**
	 * Calculates the indices of all boats connected to the block at coordinate
	 * {@code (x, y)}.
	 * 
	 * <p>
	 * If the block at {@code (x, y)} is of type water, the method behaves
	 * undefined.
	 * 
	 * @param x first coordinate of the block to check
	 * @param y second coordinate of the block to check
	 * @return
	 */
	private Coordinate[] getBoat(int x, int y) {
		List<Coordinate> boat = new ArrayList<>(data[x][y].getType().length);
		boat.add(new Coordinate(x, y));
		int dir;

		// if the boat is horizontal dir is 1
		if (data[x + 1][y].getType() != BlockType.WATER || data[x - 1][y].getType() != BlockType.WATER)
			dir = 1;
		else
			dir = 0;

		// pos is for right/up and neg is for left/down
		int pos_ind = 1, neg_ind = 1;

		// check if there is more 'boat' on at least one side
		// pos_ind * dir = 0 iff direction is vertical
		while (data[x + pos_ind * dir][y + pos_ind * (1 - dir)].getType() != BlockType.WATER
				|| data[x - neg_ind * dir][y + neg_ind * (1 - dir)].getType() != BlockType.WATER) {

			if (data[x + pos_ind * dir][y + pos_ind * (1 - dir)].getType() != BlockType.WATER) {
				boat.add(new Coordinate(x + pos_ind * dir, y + pos_ind * (1 - dir)));
				pos_ind += 1;
			}

			if (data[x + neg_ind * dir][y + neg_ind * (1 - dir)].getType() != BlockType.WATER) {
				boat.add(new Coordinate(x + neg_ind * dir, y + neg_ind * (1 - dir)));
				neg_ind += 1;
			}
		}

		return (Coordinate[]) boat.toArray();
	}

	/**
	 * Checks if a connected set of same-typed blocks was sunk and updates symbols
	 * accordingly. If the block at {@code (x, y)} is not part of a boat, nothing
	 * happens.
	 * 
	 * @param x the x coordinate of the block to check.
	 * @param y the y coordinate of the block to check.
	 */
	private void checkSunk(int x, int y) {
		if (data[x][y].isType(BlockType.WATER))
			return;

		Coordinate[] boat = getBoat(x, y);
		for (Coordinate c : boat) {
			if (!data[c.getX()][c.getY()].isShot())
				return;
		}

		for (Coordinate c : boat) {
			data[c.getX()][c.getY()].setSymbol(data[c.getX()][c.getY()].getType().symbol);
		}
	}

	/**
	 * Shoots the block at coordinate c. If c can't be shot the method throws an
	 * assertion error.
	 * 
	 * @param c the coordinate to shoot.
	 */
	public void shoot(Coordinate c) {
		int x = c.getX();
		int y = c.getY();

		assert !data[x][y].isShot();

		data[x][y].shoot();
		data[x][y].setSymbol(data[x][y].getType() == BlockType.WATER ? 'X' : 'O');
		checkSunk(x, y);
	}

	/**
	 * Checks if Coordinate c was not shot already.
	 * 
	 * @param c the coordinate to check
	 * @return true if c wasn't shot before
	 */
	public boolean isShootable(Coordinate c) {
		return !data[c.getX()][c.getY()].isShot();
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
		return data[x + 1 * dir][y + 1 * (1 - dir)].isType(BlockType.WATER)
				&& data[x - 1 * dir][y - 1 * (1 - dir)].isType(BlockType.WATER);
	}

	// checks 3 blocks left and 3 blocks right of boat to insert
	private boolean checkLeftRight(int x_start, int y_start, int length) {
		return !data[x_start - 1][y_start].isType(BlockType.WATER) // check blocks left of 'leftest'
				|| !data[x_start - 1][y_start + 1].isType(BlockType.WATER)
				|| !data[x_start - 1][y_start - 1].isType(BlockType.WATER)
				|| !data[x_start + length][y_start].isType(BlockType.WATER) // check blocks right of 'rightest'
				|| !data[x_start + length][y_start + 1].isType(BlockType.WATER)
				|| !data[x_start + length][y_start - 1].isType(BlockType.WATER);
	}

	// checks 3 blocks below and above the boat to insert
	private boolean checkBelowAbove(int x_start, int y_start, int length) {
		return !data[x_start][y_start - 1].isType(BlockType.WATER) // check blocks below lowest
				|| !data[x_start - 1][y_start - 1].isType(BlockType.WATER)
				|| !data[x_start + 1][y_start - 1].isType(BlockType.WATER)
				|| !data[x_start][y_start + length].isType(BlockType.WATER) // check blocks above highest
				|| !data[x_start - 1][y_start + length].isType(BlockType.WATER)
				|| !data[x_start + 1][y_start + length].isType(BlockType.WATER);
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
	 */
	public boolean addBoat(Coordinate from, Coordinate to, BlockType type, int playerNumber) {
		int dir; // 0 if boat vertical, otherwise 1
		int length = from.getDistance(to);
		int x_start = Math.min(from.getX(), to.getX());
		int y_start = Math.min(from.getY(), to.getY());
		if (from.getX() - to.getX() == 0) {
			dir = 0;
			if (!checkBelowAbove(x_start, y_start, length)) {
				return false; // if direction is vertical, check blocks below and above boat
			}
		} else {
			dir = 1;
			if (!checkLeftRight(x_start, y_start, length)) {
				return false; // if direction is horizontal, check blocks right and left of boat
			}
		}

		for (int i = 0; i < length; i++) {
			if (!isFree(x_start + i * dir, y_start + i * (1 - dir), dir)) {
				return false; // check each block left/right (above/below respectively)
			}
		}

		for (int i = 0; i < length; i++) {
			data[x_start + i * dir][y_start + i * (1 - dir)].setType(type);
			data[x_start + i * dir][y_start + i * (1 - dir)].setSymbol(playerNumber == 0 ? type.symbol : ' ');
		}

		return true;
	}

}
