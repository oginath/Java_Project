package view;

import java.util.ArrayList;

import observe.Observable;
import observe.Observer;
import presenter.Command;
import algorithms.mazeGenerators.Maze;
import algorithms.solution.Solution;

public class MyView implements View, Observable {

	private ArrayList<Command> cmdList;
	private ArrayList<Observer> Observers;

	public MyView() {
		this.cmdList = new ArrayList<Command>();
		this.Observers = new ArrayList<Observer>();

	}
	
	@Override
	public void start() {
		System.out.println("Starting....");
	}


	@Override
	public void displayMaze(Maze m) {
		System.out.println("Displaying Maze....");

	}

	@Override
	public void displaySolution(Solution s) {
		System.out.println("Displaying Soultion....");

	}

	@Override
	public void setCommands(Command cmd) {
		this.cmdList.add(cmd);
	}

	@Override
	public Command getUserCommand() {
		System.out.println("Getting user command....");
		return null;
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
