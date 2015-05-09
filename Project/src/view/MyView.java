package view;

import algorithms.mazeGenerators.Maze;
import algorithms.solution.Solution;

public class MyView implements View {

	@Override
	public void start() {
		System.out.println("Starting....");
	}

	@Override
	public void setCommands() {
		System.out.println("Setting Commands....");
	}

	@Override
	public void displayMaze(Maze m) {
		System.out.println("Displaying Maze....");

	}

	@Override
	public void displaySolution(Solution s) {
		System.out.println("Displaying Soultion....");

	}

}
