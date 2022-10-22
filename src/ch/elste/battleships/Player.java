package ch.elste.battleships;

import ch.elste.battleships.Block.BlockType;

public interface Player {

	/**
	 * Returns the starting and end coordinate for the given type of boat.
	 * 
	 * @param type of the boat
	 * @return An array of coordinates with 2 elements. The first element is the
	 *         starting position, while the second element is the end position
	 */
	public Coordinate[] getBoat(BlockType type);

	/**
	 * Each player has to define a number, functioning as the index of this player.
	 * 
	 * @return the player number
	 */
	public int getPlayerNumber();

	/**
	 * Returns the preferred location of the next shot as a {@linkplain Coordinate}.
	 * 
	 * @return the coordinate of the next shot
	 */
	public Coordinate getNextShot();
}
