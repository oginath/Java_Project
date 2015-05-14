package view;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import presenter.Presenter.Command;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

public class MyView extends Observable implements View{

	private ArrayList<Command> cmdList;
	private ArrayList<Observer> Observers;

	public MyView() {
		this.cmdList = new ArrayList<Command>();
		this.Observers = new ArrayList<Observer>();

	}
	
	@Override
	public void start() {
		System.out.println("Starting....");
		this.notifyObservers();
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
		return this.cmdList.get(0);
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
			observer.update(this, null);
	}

}
