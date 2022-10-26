package ch.elste.battleships;

import ch.elste.battleships.Block.BlockType;
import ch.elste.battleships.Exceptions.BoatCollisionException;
import ch.elste.battleships.Exceptions.IllegalBoatSpecException;

/**
 * Used to create GameStates with {@link #getGameState(int)}.
 * 
 * @author Dillon Elste
 *
 */
public class GameStateFactory {
	private Player[] players;

	/**
	 * The order of p1 and p2 does matter.
	 * 
	 * @param p1 a usr player
	 * @param p2 a cpu player
	 */
	public GameStateFactory(Player p1, Player p2) {
		this.players = new Player[] { p1, p2 };
	}

	/**
	 * Checks that the given coordinates are correct for a boat of type
	 * {@code type}. If the coordinates are invalid an AssertionError is thrown.
	 * 
	 * @param from the first coordinate of the boat
	 * @param to   the second coordinate of the boat
	 * @param type the type of boat to add
	 * @throws IllegalBoatSpecException if the boat is diagonal or of wrong size
	 */
	private void assertBoatSpecs(Coordinate from, Coordinate to, BlockType type) throws IllegalBoatSpecException {
		if (from.getX() != to.getX() && from.getY() != to.getY()// make sure not diagonal
				|| from.getDistance(to) + 1 != type.length) { // +1 to get distance, because getDistance is exclusive to
			throw new IllegalBoatSpecException();
		}
	}

	/**
	 * Adds all boats of a single type for a given player.
	 * 
	 * @param g The grid belonging to the player p.
	 * @param p the player to ask for boats.
	 * @param t the type of boats to ask.
	 */
	private void addBoatsOfType(Grid g, Player p, BlockType t) {
		Coordinate[] cs;
		for (int i = 0; i < t.amount; i++) { // each type of boat
			if (p.getPlayerNumber() == players[0].getPlayerNumber()) {
				// print current state
				Output.clearScreen();
				Output.printGrid(g, "Current Grid");
			}

			while (true) { // do as long as a valid boat was received
				cs = p.getBoat(t); // ask player for Boat
				try {
					assertBoatSpecs(cs[0], cs[1], t); // check size and orientation
					g.addBoat(cs[0], cs[1], t, p.getPlayerNumber());
				} catch (IllegalBoatSpecException e) {
					if (p.getPlayerNumber() == players[0].getPlayerNumber())
						Output.println("Incorrect size or orientation. Please try again:");
					continue; // invalid boat specs -> try again
				} catch (BoatCollisionException e) {
					if (p.getPlayerNumber() == players[0].getPlayerNumber())
						Output.println("There is already another boat obstructing this square. Try again:");
					continue; // collision with already placed boat -> try again
				}

				break; // boat could be placed
			}
		}
	}

	/**
	 * Let Player p place all his boats on the grid.
	 * 
	 * @param g The grid belonging to player p.
	 * @param p the Player to ask for boats.
	 */
	private void addBoats(Grid g, Player p) {
		for (int i = 0; i < 4; i++) {
			addBoatsOfType(g, p, BlockType.getTypeFromIndex(i)); // go through boat types and add to grid
		}
	}

	/**
	 * Returns a GameState object with initialized grids, ready for the first move.
	 * 
	 * @param gridsize the number of blocks per side of the square.
	 * @return GameState object ready for the first move.
	 */
	public GameState getGameState(int gridsize) {
		Grid[] grids = { new Grid(gridsize), new Grid(gridsize) }; // init grids

		for (int i = 0; i < players.length; i++) {
			addBoats(grids[i], players[i]); // populate grids
		}

		GameState g = new GameState(grids); // create GameState
		return g;
	}
}
