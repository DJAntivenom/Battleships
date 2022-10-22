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
		int x = (int) (Math.random() * (gridsize-type.length));
		int y = (int) (Math.random() * gridsize-type.length);
		Coordinate from = new Coordinate(x, y);
		int dir = (int) (Math.random() * 2);
		return new Coordinate[] {from, new Coordinate(x+type.length*dir, y+type.length*(1-dir))};
	}

}
