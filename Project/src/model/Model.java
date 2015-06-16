package model;

import java.util.Observer;

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
	 * @param name the name
	 * @param rows            the rows of the maze that's to be generated
	 * @param cols            the columns of the maze that's to be generated
	 */
	void generateMaze(String name, int rows, int cols);

	/**
	 * Gets the maze.
	 * 
	 * A method to be overridden.
	 *
	 * @param name the name
	 * @return the maze
	 */
	Maze getMaze(String name);

	/**
	 * Gets the positions.
	 *
	 * @param name the name
	 * @return the positions
	 */
	String getPositions(String name);
	
	/**
	 * Solve maze.
	 * 
	 * A method to be overridden.
	 *
	 * @param arg the arg
	 */
	void solveMaze(String arg);

	/**
	 * Gets the solution.
	 * 
	 * A method to be overridden.
	 *
	 * @param name the name
	 * @return the solution
	 */
	Solution getSolution(String name);
	
	/**
	 * Adds the observer.
	 *
	 * @param o the o
	 */
	void addObserver(Observer o);


	/**
	 * Stop.
	 * 
	 * A method to be overridden.
	 */
	void stop();
}
