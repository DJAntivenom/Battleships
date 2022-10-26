package ch.elste.battleships;

import ch.elste.battleships.Exceptions.IllegalBoatSpecException;

public class GameState {

	private Grid[] grids;

	public GameState(Grid[] grids) {
		this.grids = grids;
	}

	/**
	 * Prints the grid of player with Id {@code playerNumber} revealing the
	 * remaining tiles.
	 * 
	 * @param playerNumber the number of the player whose grid should be shown
	 * @param name         the title to display above the grid
	 */
	public void showEndGrid(int playerNumber, String name) {
		Output.printEndGrid(grids[playerNumber], name);
	}

	/**
	 * Prints the grid of player with Id {@code playerNumber} without revealing the
	 * remaining blocks.
	 * 
	 * @param playerNumber the number of the player whose grid should be shown
	 * @param name         the title to display above the grid
	 */
	public void showGrid(int playerNumber, String name) {
		Output.printGrid(grids[playerNumber], name);
	}

	/**
	 * Returns true if the player with Id {@code playerNubmer} can shoot the block
	 * at c in the opponent's grid.
	 * 
	 * @param c            the coordinate to check
	 * @param playerNumber the player who wants to shoot.
	 * @return true if the coordinate can be shot, false otherwise
	 * @throws IllegalBoatSpecException if the given coordinates are outside the
	 *                                  grid.
	 */
	public boolean isValid(Coordinate c, int playerNumber) throws IllegalBoatSpecException {
		return grids[1 - playerNumber].isShootable(c);
	}

	/**
	 * Player with Id {@code playerNumber} shoots at coordinate c, i.e. the grid of
	 * the other player will be updated
	 * 
	 * @param c            the coordinate to shoot
	 * @param playerNumber the player who shoots
	 * @return true if there was a boat at c and it is now sunk, false otherwise.
	 */
	public boolean shoot(Coordinate c, int playerNumber) {
		return grids[1 - playerNumber].shoot(c);
	}

	/**
	 * Returns the number of the player who won or -1 if there is no winner.
	 * 
	 * @return the number of the player who won or -1 if there is no winner
	 */
	public int getWinner() {
		for (int i = 0; i < grids.length; i++) {
			if (grids[i].getHitCount() == Grid.BOAT_BLOCK_COUNT) {
				return i;
			}
		}
		return -1;
	}
}
