package ch.elste.battleships;

public class GameState {

	private Grid[] grids;

	public GameState(Grid[] grids) {
		this.grids = grids;
	}

	public boolean isValid(Coordinate c, int playerNumber) {
		return grids[playerNumber].isShootable(c);
	}

	public void shoot(Coordinate c, int playerNumber) {
		grids[playerNumber].shoot(c);
	}

	public boolean hasWinner() {
		throw new Error("unimplemented");
	}

	public int getWinner() {
		throw new Error("unimplemented");
	}
}
