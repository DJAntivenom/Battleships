package ch.elste.battleships;

import java.io.InputStream;

import ch.elste.battleships.Exceptions.IllegalBoatSpecException;

/**
 * The Game class ensures that the game plays out in the correct manner. It asks
 * the players for move, one after the other, and checks with the
 * {@linkplain GameState} if they are legal. The initialization of the grids is
 * also supervised here.
 * 
 * Before {@link #run()} can be called, {@link #init()} needs to be called.
 * After run finishes {@link #init()} can be called again.
 * 
 * @author Dillon Elste
 *
 */
public class Game implements Runnable {
	public static final int GRID_SIZE = 10;

	private GameStateFactory gsf;
	private GameState gs;
	private Player usr, cpu;
	private boolean initialized;

	public Game(InputStream is) {
		Input in = new Input(is);

		this.usr = new PlayerUser(in, 0);
		this.cpu = new PlayerCpu(1, GRID_SIZE);

		this.gsf = new GameStateFactory(usr, cpu);

		this.initialized = false;
	}

	public Game() {
		this(System.in);
	}

	public void init() {
		if (initialized)
			throw new IllegalStateException("Already initialized, but not run");

		Output.clearScreen();
		gs = gsf.getGameState(GRID_SIZE);
		initialized = true;
		Output.clearScreen();
	}

	/**
	 * Polls the player for shots, until a legal move was made.
	 * 
	 * @param p the player to move
	 * @return the coordinates of the fired shot.
	 */
	private Coordinate getNextShot(Player p) {
		Coordinate shot;
		while (true) {
			shot = p.getNextShot();
			try {
				if (!gs.isValid(shot, p.getPlayerNumber())) {
					// this square was already shot
					if (p.getPlayerNumber() == usr.getPlayerNumber())
						Output.println("This position was already shot.");
					continue; // try again
				}
			} catch (IllegalBoatSpecException e) {
				// the coordinates are outside the grid
				if (p.getPlayerNumber() == usr.getPlayerNumber())
					Output.println("Please enter coordinates inside the grid!");
				continue; // try again
			}

			break; // found a suitable block
		}

		return shot;
	}

	/**
	 * Gets the player who's turn it is next.
	 * 
	 * @param current the player who just moved
	 * @return the next player
	 */
	private Player getNextPlayer(Player current) {
		return current.getPlayerNumber() == usr.getPlayerNumber() ? cpu : usr;
	}

	private void showOutput(int currId) {
		String title = currId == usr.getPlayerNumber() ? "OCEAN " : "TARGET ";
		title += "GRID";
		gs.showGrid(currId, title);
	}

	@Override
	public void run() {
		if (!this.initialized) {
			throw new IllegalStateException("Game wasn't initialized. See init()");
		}

		Player curr = usr;
		Coordinate shot;
		showOutput(cpu.getPlayerNumber());
		while (gs.getWinner() == -1) {
			showOutput(curr.getPlayerNumber());
			shot = getNextShot(curr);
			gs.shoot(shot, curr.getPlayerNumber());
			curr = getNextPlayer(curr);
			if (curr.getPlayerNumber() == cpu.getPlayerNumber()) {
				Output.clearScreen();
			}
		}

		Output.clearScreen();
		if (gs.getWinner() == usr.getPlayerNumber()) {
			Output.println("Congratulations, you won!");
		} else {
			Output.println("Better luck next time.");
			gs.showEndGrid(cpu.getPlayerNumber(), "REMAINING GRID");
		}

		initialized = false;
	}

}
