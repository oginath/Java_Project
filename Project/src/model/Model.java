package model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

// TODO: Auto-generated Javadoc
/**
 * The Interface Model.
 */
public interface Model {

	
	/**
	 * Generate maze.
	 *
	 * @param rows the rows
	 * @param cols the cols
	 */
	void generateMaze(int rows, int cols);
	
	/**
	 * Gets the maze.
	 *
	 * @return the maze
	 */
	Maze getMaze();
	
	/**
	 * Solve maze.
	 *
	 * @param m the m
	 */
	void solveMaze(Maze m);
	
	/**
	 * Gets the solution.
	 *
	 * @param maze the maze
	 * @return the solution
	 */
	Solution getSolution(Maze maze);
	
	/**
	 * Stop.
	 */
	void stop();
}
