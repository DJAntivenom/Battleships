package ch.elste.battleships;

import ch.elste.battleships.Block.BlockType;

public class PlayerUser extends AbstractPlayer {
	private Input in;

	public PlayerUser(Input in, int playerNo) {
		this.in = in;
		this.playerNumber = playerNo;
	}

	@Override
	public Coordinate getNextShot() {
		return in.getCoordinate("Please enter a coordinate to shoot:");
	}

	@Override
	public Coordinate[] getBoat(BlockType type) {
		return in.getCoordinatePair(String.format("Please enter two coordinates for a %s. The length should be %d:",
				type.name, type.length));
	}

}
