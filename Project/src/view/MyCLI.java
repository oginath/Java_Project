package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import commands.CLI;

/**
 * The Class MyCLI.
 */
public class MyCLI extends CLI {

	/** The Command. */
	String cmd;

	/** The argument. */
	String arg;

	/**
	 * Instantiates a new CLI.
	 *
	 * @param in
	 *            The input from which the commands will come.
	 * @param out
	 *            the output to feed the commands to.
	 */
	public MyCLI(BufferedReader in, PrintWriter out) {
		super(in, out);
	}

	/**
	 * Sets the command and argument according to the user's input.
	 */
	public void getCommand() {

		this.cmd = null;
		this.arg = null;

		System.out.print("Enter command: ");
		try {
			String line = this.in.readLine();

			if (!line.equals("exit")) {
				String[] sp = line.split(" ");
				if (sp.length > 1) {
					cmd = sp[0] + " " + sp[1];
					if (sp.length > 2) {
						arg = "";
						for (int i = 2; i < sp.length; i++) {
							arg += sp[i] + " ";
						}
					}
				} else {
					this.cmd = sp[0];
				}
			} else
				this.cmd = "exit";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the latest command issued by the user.
	 *
	 * @return The command.
	 */
	public String getCmd() {
		return this.cmd;
	}

	/**
	 * Returns the argument issued by the user.
	 *
	 * @return The arguments.
	 */
	public String getArg() {
		return this.arg;
	}

}
