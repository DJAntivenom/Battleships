package ch.elste.battleships;

import java.io.PrintStream;

public class Output {

	private static PrintStream out = System.out;

	/**
	 * Sets the stream to print to.
	 * 
	 * @param out the stream to print to.
	 */
	public static void setOut(PrintStream out) {
		Output.out = out;
	}

	private static void printGrid(Grid g, String name, boolean end) {
		int gridsize = g.getGridsize();
		String eqs = "=".repeat((gridsize*2+3-name.length()) / 2 - 1);
		out.printf("%s %s %s\n", eqs, name, eqs);
		printIndices(gridsize);
		printSeperator(gridsize);
		for (int r = 0; r < gridsize; r++) {
			char index = (char) (r + 'A');
			out.printf("%c", index);
			for (int c = 0; c < gridsize; c++) {
				Coordinate co = new Coordinate(r, c);
				char symbol = end ? g.getTypeAt(co).symbol : g.getSymbolAt(co);
				out.printf("|%c", symbol);
			}
			out.printf("|%c\n", index);
		}
		printSeperator(gridsize);
		printIndices(gridsize);

	}

	public static void printEndGrid(Grid g, String name) {
		printGrid(g, name, true);
	}

	public static void printGrid(Grid g, String name) {
		printGrid(g, name, false);
	}

	/**
	 * Prints the String s to the selected output stream.
	 * 
	 * @param s the String to print
	 */
	public static void println(String s) {
		out.println(s);
	}

	/**
	 * Prints a bunch of newlines.
	 */
	public static void clearScreen() {
		String n = "\n\n\n\n";
		String s = "";
		for (int i = 0; i < 25; i++) {
			s += n;
		}
		println(s);
	}

	///////// helpers

	private static void printSeperator(int gridsize) {
		String s = " +";
		for (int i = 0; i < gridsize; i++) {
			s += "-+";
		}
		out.println(s);
	}

	// prints the indices above/below the board
	private static void printIndices(int gridsize) {
		String s = "  "; // make space for row labels and seperator
		for (int i = 0; i < gridsize; i++) {
			s += i; // print letter
			s += " "; // print space for seperators
		}
		out.println(s);
	}
}
