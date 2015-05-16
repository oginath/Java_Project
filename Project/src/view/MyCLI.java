package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import commands.CLI;

// TODO: Auto-generated Javadoc
/**
 * The Class MyCLI.
 */
public class MyCLI extends CLI {

	/** The cmd. */
	String cmd;
	
	/** The arg. */
	String arg;

	/**
	 * Instantiates a new my cli.
	 *
	 * @param in the in
	 * @param out the out
	 */
	public MyCLI(BufferedReader in, PrintWriter out) {
		super(in, out);
	}

	/**
	 * Gets the command.
	 *
	 * @return the command
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
				}
				else{
					this.cmd = sp[0];
				}
			} else
				this.cmd = "exit";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the cmd.
	 *
	 * @return the cmd
	 */
	public String getCmd() {
		return this.cmd;
	}

	/**
	 * Gets the arg.
	 *
	 * @return the arg
	 */
	public String getArg() {
		return this.arg;
	}

}
