package ch.elste.battleships;

public abstract class AbstractPlayer implements Player {
	protected int playerNumber;

	@Override
	public int getPlayerNumber() {
		return playerNumber;
	}

}
