package view.CLI;

import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 * The Class RunnableCLI.
 * 
 * An object adapter between a CLI and a runnable interface.
 */
public class RunnableCLI implements Runnable {

	/** The CLI. */
	MyCLI cli;

	/**
	 * Instantiates a new runnable CLI.
	 *
	 * @param br
	 *            The input stream to get commands from
	 * @param pw
	 *            The output stream to write commands to
	 */
	public RunnableCLI(BufferedReader br, PrintWriter pw) {
		this.cli = new MyCLI(br, pw);
	}

	/**
	 * Run.
	 * 
	 * Gets the command from the user (an I/O blocking method, thus it should
	 * run in a separate thread).
	 */
	@Override
	public void run() {
		this.cli.getCommand();
	}

	/**
	 * Gets the latest command.
	 *
	 * @return the Command
	 */
	public String getCmd() {

		return this.cli.getCmd();
	}

	/**
	 * Gets the latest argument.
	 *
	 * @return the argument.
	 */
	public String getArg() {

		return this.cli.getArg();
	}
}
