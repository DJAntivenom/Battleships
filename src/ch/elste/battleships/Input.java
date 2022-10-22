package ch.elste.battleships;

import java.io.InputStream;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Input {

	private Scanner in;

	public Input(InputStream is) {
		in = new Scanner(is);
	}

	public Coordinate[] getCoordinatePair(String display) {
		System.out.println(display);

		

		Coordinate c1 = null, c2 = null;
		throw new Error("unimplemented");
	}

	public Coordinate getCoordinate(String display) {
		throw new Error("unimplemented");
	}
}
