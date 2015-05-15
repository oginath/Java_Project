package view;

import commands.Command;
import commands.UserCommands;

public class MyCommands extends UserCommands {

	public void setCommands(String cmdName, Command cmd){
		this.commands.put(cmdName, cmd);
	}
}
