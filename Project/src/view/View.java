package view;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import commands.Command;

// TODO: Auto-generated Javadoc
/**
 * The Interface View.
 */
public interface View {

	
	/**
	 * Start.
	 */
	void start();
	
	/**
	 * Sets the commands.
	 *
	 * @param cmdName the cmd name
	 * @param cmd the cmd
	 */
	void setCommands(String cmdName, Command cmd);
	
	/**
	 * Gets the user command.
	 *
	 * @return the user command
	 */
	Command getUserCommand();
	
	/**
	 * Gets the user arg.
	 *
	 * @return the user arg
	 */
	String getUserArg();
	
	/**
	 * Display maze.
	 *
	 * @param m the m
	 */
	void displayMaze(Maze m);
	
	/**
	 * Display solution.
	 *
	 * @param s the s
	 */
	void displaySolution(Solution s);
}
