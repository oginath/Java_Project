package view;

import commands.Command;
import commands.UserCommands;

/**
 * The Class MyCommands.
 */
public class MyCommands extends UserCommands {

	/**
	 * Adds a new command to the string (name of the command) to command map.
	 *
	 * @param cmdName The name of the command
	 * @param cmd The command
	 */
	public void setCommands(String cmdName, Command cmd){
		this.commands.put(cmdName, cmd);
	}
}
