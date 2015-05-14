package view;

import presenter.Presenter.Command;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

public interface View {

	
	void start();
	void setCommands(Command cmd);
	Command getUserCommand();
	void displayMaze(Maze m);
	void displaySolution(Solution s);
}
