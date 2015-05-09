package model;

import java.util.ArrayList;

import observe.Observable;
import observe.Observer;
import algorithms.mazeGenerators.Maze;
import algorithms.solution.Solution;

public class MyModel implements Model, Observable {

	private ArrayList<Observer> Observers;

	public MyModel() {
		this.Observers = new ArrayList<Observer>();
	}

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

	@Override
	public void addObserver(Observer o) {
		this.Observers.add(o);
	}

	@Override
	public void deleteObserver(Observer o) {
		this.Observers.remove(o);
	}

	@Override
	public void notifyObservers() {
		for (Observer observer : Observers)
			observer.update(null, null);
	}

}
