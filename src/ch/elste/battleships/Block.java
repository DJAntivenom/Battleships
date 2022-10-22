package ch.elste.battleships;

public class Block {
	private BlockType type;
	private char symbol;
	private boolean shot;
	
	public Block() {
		this.type = BlockType.WATER;
		this.symbol = ' ';
		this.shot = false;
	}
	
	public BlockType getType() {
		return type;
	}
	
	public void setType(BlockType type) {
		this.type = type;
	}
	
	public char getSymbol() {
		return symbol;
	}
	
	public void setSymbol(char symbol) {
		this.symbol = symbol;
	}
	
	public boolean isShot() {
		return shot;
	}
	
	public void shoot() {
		this.shot = true;
	}
	
	public boolean isType(BlockType type) {
		return this.type == type;
	}
	
	
	public static enum BlockType {
		WATER(' ', -1, -1, "water"),
		CRUISER('C', 6, 1, "cruiser"),
		BATTLESHIP('B', 4, 2, "battleship"),
		SUBMARINE('S', 3, 3, "submarine"),
		PATROL_BOAT('P', 2, 4, "patrol boat");
		
		public final char symbol;
		public final int length, amount;
		public final String name;
		
		private BlockType(char s, int len, int amnt, String n) {
			symbol = s;
			length = len;
			amount = amnt;
			name = n;
		}
		
		public static BlockType getTypeFromIndex(int i) {
			switch(i) {
			case 0:
				return BlockType.CRUISER;
			case 1:
				return BlockType.BATTLESHIP;
			case 2:
				return BlockType.SUBMARINE;
			case 3:
				return BlockType.PATROL_BOAT;
			default:
				throw new IllegalArgumentException("Unexpected value: " + i);
			}
		}
	}
}
