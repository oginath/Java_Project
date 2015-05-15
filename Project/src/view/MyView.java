package view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import commands.Command;

public class MyView extends Observable implements View {

	private ArrayList<Observer> Observers;
	private MyCommands cmds;
	private RunnableCLI cli;
	Thread t;

	public MyView() {
		this.Observers = new ArrayList<Observer>();
		this.cmds = new MyCommands();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
		this.cli = new RunnableCLI(br, pw);
	}

	@Override
	public void start() {
		System.out.println("Starting....");
		
		do {
			t = new Thread(cli);
			t.start();
			while(t.isAlive())
				continue;
			
			notifyObservers();
			
		} while (this.cli.getCmd() != "exit");
	}

	@Override
	public void displayMaze(Maze m) {
		m.print();
	}

	@Override
	public void displaySolution(Solution s) {
		s.print();
	}

	@Override
	public void setCommands(String cmdName, Command cmd) {
		this.cmds.setCommands(cmdName, cmd);
	}

	@Override
	public Command getUserCommand() {
		return this.cmds.selectCommand(this.cli.getCmd());
	}

	public String getUserArg() {
		return this.cli.getArg();
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
