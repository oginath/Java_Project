package view;

import algorithms.mazeGenerators.Maze;
import algorithms.solution.Solution;

public interface View {

	
	void start();
	void setCommands();
	//Command getUserCommand();
	void displayMaze(Maze m);
	void displaySolution(Solution s);
}
