package ch.elste.battleships;

import ch.elste.battleships.Block.BlockType;

public class PlayerCpu extends AbstractPlayer {
	private int gridsize;

	public PlayerCpu(int playerNumber, int gridsize) {
		this.playerNumber = playerNumber;
		this.gridsize = gridsize;
	}

	@Override
	public Coordinate getNextShot() {
		int x = (int) Math.round(Math.random() * gridsize);
		int y = (int) Math.round(Math.random() * gridsize);
		return new Coordinate(x, y);
	}

	@Override
	public Coordinate[] getBoat(BlockType type) {
		int dir = (int) (Math.random() * 2); // 1 horizontal, 0 vertical
		int x, y;
		int length = type.length - 1; // the -1 is needed because to is exclusive

		if (dir == 1) {
			x = (int) (Math.random() * (gridsize - length));
			y = (int) (Math.random() * gridsize);
		} else {
			x = (int) (Math.random() * gridsize);
			y = (int) (Math.random() * (gridsize - length));
		}

		Coordinate from = new Coordinate(x, y);
		Coordinate to = new Coordinate(x + length * dir, y + length * (1 - dir));
		return new Coordinate[] { from, to };
	}

}
