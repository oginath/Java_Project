package view;

import presenter.Command;
import algorithms.mazeGenerators.Maze;
import algorithms.solution.Solution;

public interface View {

	
	void start();
	void setCommands(Command cmd);
	Command getUserCommand();
	void displayMaze(Maze m);
	void displaySolution(Solution s);
}
