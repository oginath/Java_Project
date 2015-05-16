package view;

import commands.Command;
import commands.UserCommands;

// TODO: Auto-generated Javadoc
/**
 * The Class MyCommands.
 */
public class MyCommands extends UserCommands {

	/**
	 * Sets the commands.
	 *
	 * @param cmdName the cmd name
	 * @param cmd the cmd
	 */
	public void setCommands(String cmdName, Command cmd){
		this.commands.put(cmdName, cmd);
	}
}
