package model;

import algorithms.mazeGenerators.Maze;
import algorithms.solution.Solution;

public interface Model {

	
	void generateMaze();
	Maze getMaze();
	void solveMaze(Maze m);
	Solution getSolution();
	void stop();
}
