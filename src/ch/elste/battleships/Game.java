package ch.elste.battleships;

import java.io.InputStream;

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

		gs = gsf.getGameState(GRID_SIZE);
		initialized = true;
	}

	private void showOutput(Player p) {
		throw new Error("unimplemented");
	}

	/**
	 * Polls the player for shots, until a legal move was made.
	 * 
	 * @param p the player to move
	 * @return the coordinates of the fired shot.
	 */
	private Coordinate getNextShot(Player p) {
		Coordinate shot = p.getNextShot();
		while (!gs.isValid(shot, p.getPlayerNumber())) {
			Output.println("This position was already shot", p);
			shot = p.getNextShot();
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

	@Override
	public void run() {
		if (!this.initialized) {
			throw new IllegalStateException("Game wasn't initialized. See init()");
		}

		Player curr = usr;
		Coordinate shot;
		while (!gs.hasWinner()) {
			showOutput(curr);
			shot = getNextShot(curr);
			gs.shoot(shot, curr.getPlayerNumber());
			curr = getNextPlayer(curr);
		}

		showOutput(usr);
		if (gs.getWinner() == usr.getPlayerNumber()) {
			// TODO output
			System.out.println("Congratulations, you won!");
		} else {
			System.out.println("Better luck next time");
		}

		initialized = false;
	}

}
