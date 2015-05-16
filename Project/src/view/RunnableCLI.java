package view;

import java.io.BufferedReader;
import java.io.PrintWriter;

// TODO: Auto-generated Javadoc
/**
 * The Class RunnableCLI.
 */
public class RunnableCLI implements Runnable {

	/** The cli. */
	MyCLI cli;
	
	/**
	 * Instantiates a new runnable cli.
	 *
	 * @param br the br
	 * @param pw the pw
	 */
	public RunnableCLI(BufferedReader br, PrintWriter pw) {
		this.cli = new MyCLI(br, pw);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		this.cli.getCommand();
	}
	
	/**
	 * Gets the cmd.
	 *
	 * @return the cmd
	 */
	public String getCmd(){
		
		return this.cli.getCmd();
	}
	
	/**
	 * Gets the arg.
	 *
	 * @return the arg
	 */
	public String getArg(){
		
		return this.cli.getArg();
	}
}
