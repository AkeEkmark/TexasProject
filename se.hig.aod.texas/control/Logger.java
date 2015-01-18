package control;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
/**
 * A class to log strings and write them to a file.
 * @author Åke Ekmark, Andreas Wieselqvist
 *
 */
public class Logger {

	private String contents;
	public Logger() {
		contents = "";
	}
	public void addString(String string) {
		contents += string;
		contents += "\n";
	}
	public void endString() throws IOException {
		writeToFile(contents);
	}
	public void writeToFile(String contents) throws IOException {
		Writer output = null;
		output = new BufferedWriter(new FileWriter("game.txt"));
		output.write(contents); // skriver ut till game.txt
		output.close();

	}
}