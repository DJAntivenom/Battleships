package ch.elste.battleships;

import java.io.FileNotFoundException;

public class GameStarter {

	public static void main(String[] args) {
		Game g;
		try {
			if (args.length > 0)
				g = new Game(new java.io.FileInputStream(args[0]));
			else
				g = new Game();
		} catch (FileNotFoundException e) {
			System.err.println("File was not found using standard input");
			g = new Game();
		}
		g.init();
		g.run();
	}

}
