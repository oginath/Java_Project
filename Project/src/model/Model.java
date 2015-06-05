package model;

import java.util.HashMap;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

/**
 * The Interface Model.
 */
public interface Model {

	/**
	 * Generate maze.
	 *
	 * A method to be overridden.
	 *
	 * @param rows
	 *            the rows of the maze that's to be generated
	 * @param cols
	 *            the columns of the maze that's to be generated
	 */
	void generateMaze(int rows, int cols);

	/**
	 * Gets the maze.
	 *
	 * A method to be overridden.
	 *
	 * @return the maze
	 */
	Maze getMaze();

	/**
	 * Get name to maze Map.
	 * 
	 * A method to be overridden.
	 * 
	 * @return a HashMap between a string to a maze object
	 */
	HashMap<String, Maze> getNtoM();

	/**
	 * Solve maze.
	 * 
	 * A method to be overridden.
	 * 
	 * @param m
	 *            the maze
	 */
	void solveMaze(Maze m);

	/**
	 * Gets the solution.
	 *
	 * A method to be overridden.
	 *
	 * @param maze
	 *            the maze
	 * @return the solution
	 */
	Solution getSolution(Maze maze);

	/**
	 * Stop.
	 * 
	 * A method to be overridden.
	 */
	void stop();
}
