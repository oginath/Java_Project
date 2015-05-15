package model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

public interface Model {

	
	void generateMaze(int rows, int cols);
	Maze getMaze();
	void solveMaze(Maze m);
	Solution getSolution(Maze maze);
	void stop();
}
