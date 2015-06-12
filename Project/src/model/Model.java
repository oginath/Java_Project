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
	 * @param rows
	 *            the rows of the maze that's to be generated
	 * @param cols
	 *            the columns of the maze that's to be generated
	 */
	void generateMaze(String name, int rows, int cols);

	/**
	 * Gets the maze.
	 *
	 * A method to be overridden.
	 *
	 * @return the maze
	 */
	Maze getMaze(String name);

	/**
	 * Solve maze.
	 * 
	 * A method to be overridden.
	 * 
	 * @param m
	 *            the maze
	 */
	void solveMaze(String arg);

	/**
	 * Gets the solution.
	 *
	 * A method to be overridden.
	 *
	 * @param maze
	 *            the maze
	 * @return the solution
	 */
	Solution getSolution(String name);
	
	void addObserver(Observer o);


	/**
	 * Stop.
	 * 
	 * A method to be overridden.
	 */
	void stop();

//	boolean isMazeExist(String name);
//
//	void insertMaze(Maze m, String name);
}
