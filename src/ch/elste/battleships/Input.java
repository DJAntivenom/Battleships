package ch.elste.battleships;

import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Input {

	private Scanner in;

	private static final String wrongFormat = "Please use the following format for a coordinate: A1";

	public Input(InputStream is) {
		in = new Scanner(is);
	}

	// advances the scanner and if no more inputs could be read, start reading from
	// standard input.
	private String advanceScanner() {
		String n;
		try {
			n = in.nextLine();
		} catch (NoSuchElementException e) {
			in = new Scanner(System.in);
			n = in.nextLine();
		}

		return n;
	}

	/**
	 * Finds the (index+1)th coordinate in the String {@code line}. If it contains
	 * no coordinates, the empty string is returned.
	 * 
	 * @param line  the String to scan
	 * @param index specifies which coordinate in the string should be returned
	 * @return a string with a char at index 0 followed by an int, or the empty
	 *         string
	 */
	private String readCoord(String line, int index) {
		String coordRegex = "[a-zA-Z][0-9]+"; // regular expr for char followed by int
		Pattern p = Pattern.compile(coordRegex);
		Matcher m = p.matcher(line);

		for (int i = 0; i < index; i++) {
			m.find(); // skip the coordinates before index
		}

		if (m.find()) {
			return m.group();
		} else {
			return "";
		}
	}

	/**
	 * Returns two coordinates from user.
	 * 
	 * @param display The text to display to user before scanning for input.
	 * @return an array of coordinates with exactly two elements
	 */
	public Coordinate[] getCoordinatePair(String display) {
		Output.println(display); // show display

		String line = advanceScanner();
		String first = readCoord(line, 0);
		String second = readCoord(line, 1);

		while (first == "" || second == "") { // do until two correct coords are given
			Output.println(wrongFormat);
			line = advanceScanner();
			first = readCoord(line, 0);
			second = readCoord(line, 1);
		}

		Coordinate c1 = new Coordinate(first.charAt(0), Integer.parseInt(first.substring(1)));
		Coordinate c2 = new Coordinate(second.charAt(0), Integer.parseInt(second.substring(1)));
		return new Coordinate[] { c1, c2 };
	}

	/**
	 * Reads one Coordinate from the set InputStream.
	 * 
	 * @param display the text to display to user.
	 * @return a new Coordinate given by the user.
	 */
	public Coordinate getCoordinate(String display) {
		Output.println(display);
		String line = advanceScanner();
		String s = readCoord(line, 0);

		while (s == "") {
			Output.println(wrongFormat);
			line = advanceScanner();
			s = readCoord(line, 0);
		}

		return new Coordinate(s.charAt(0), Integer.parseInt(s.substring(1)));
	}
}
