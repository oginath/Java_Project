package view;

import java.util.Observer;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import commands.Command;

/**
 * The Interface View.
 */
public interface View {

	/**
	 * Start.
	 * 
	 * a method to overridden.
	 */
	void start();
	
	/**
	 * Sets the commands.
	 *
	 * a method to overridden.
	 *
	 * @param cmdName The name of the command
	 * @param cmd The command
	 */
	void setCommands(String cmdName, Command cmd);
	
	/**
	 * Gets the user command.
	 *
	 * a method to overridden.
	 *
	 * @return The user command
	 */
	Command getUserCommand(String command);
	
	/**
	 * Gets the user argument.
	 * 
	 * a method to overridden.
	 *
	 * @return The user argument
	 */
	String getUserArg(String arg);
	
	/**
	 * Display maze.
	 *
	 * a method to overridden.
	 *
	 * @param m The maze to be displayed.
	 */
	void displayMaze(Maze m);
	
	/**
	 * Display solution.
	 *
	 * @param s The solution to be displayed.
	 */
	void displaySolution(Solution s);
	
	void displayError(String err);
		
	void addObserver(Observer o);
	
}
