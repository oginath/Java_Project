package view;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import commands.Command;

public interface View {

	
	void start();
	void setCommands(String cmdName, Command cmd);
	Command getUserCommand();
	String getUserArg();
	void displayMaze(Maze m);
	void displaySolution(Solution s);
}
