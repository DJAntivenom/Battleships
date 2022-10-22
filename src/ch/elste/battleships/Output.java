package ch.elste.battleships;

public class Output {	
	public static void println(String s, Player p) {
		if(p.getPlayerNumber() == 0) {
			System.out.println(s);
		}
	}
}
