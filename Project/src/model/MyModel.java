package model;

import algorithms.mazeGenerators.Maze;
import algorithms.solution.Solution;

public class MyModel implements Model {

	@Override
	public void generateMaze() {
		System.out.println("Generating Maze....");
	}

	@Override
	public Maze getMaze() {
		System.out.println("Getting Maze....");
		return null;
	}

	@Override
	public void solveMaze(Maze m) {
		System.out.println("Solving Maze....");
	}

	@Override
	public Solution getSolution() {
		System.out.println("Getting Solution....");
		return null;
	}

	@Override
	public void stop() {
		System.out.println("Stoping....");

	}

}
