package view.CLI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import view.View;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import commands.Command;

/**
 * The Class MyView.
 */
public class CLIView extends Observable implements View {

	/** The Observers of this class */
	private ArrayList<Observer> Observers;

	/** The Commands set. */
	private MyCommands cmds;

	/** The Command Line Interface. */
	private RunnableCLI cli;

	/** a single Thread. */
	Thread t;

	/**
	 * Instantiates a new CLI view.
	 */
	public CLIView() {
		this.Observers = new ArrayList<Observer>();
		this.cmds = new MyCommands();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
		this.cli = new RunnableCLI(br, pw);
	}

	/**
	 * Starts this view, by getting a command from the user, which is done in a
	 * separate thread. if the user input is "exit" than this class stops.
	 */
	@Override
	public void start() {
		System.out.println("Type \"list\" for cmd list");

		do {
			t = new Thread(cli);
			t.start();
			while (t.isAlive())
				continue;

			notifyObservers(null);

		} while (this.cli.getCmd() != "exit");
	}

	/**
	 * Displays the maze to the screen using the class maze' print method.
	 */
	@Override
	public void displayMaze(Maze m, String s) {
		m.print();
	}

	/**
	 * Displays the solution to the screen using the class solution' print
	 * method.
	 */
	@Override
	public void displaySolution(Solution s) {
		s.print();
	}

	@Override
	public void displayError(String err) {
		System.out.println(err);
		
	}
	/**
	 * Sets a new Command.
	 */
	@Override
	public void setCommands(String cmdName, Command cmd) {
		this.cmds.setCommands(cmdName, cmd);
	}

	/**
	 * Returns the latest command issued by the user.
	 * 
	 * @return The command
	 */
	@Override
	public Command getUserCommand(String cmd) {
		return this.cmds.selectCommand(this.cli.getCmd());
	}

	/**
	 * Returns the latest argument set by the user.
	 * 
	 * @return the argument.
	 */
	public String getUserArg(String arg) {
		return this.cli.getArg();
	}

	/**
	 * Add Observer.
	 * 
	 * @param o
	 *            adds an observer to this the observers list.
	 */
	@Override
	public void addObserver(Observer o) {
		this.Observers.add(o);
	}

	/**
	 * Delete Observer.
	 * 
	 * @param o
	 *            Removes an observer from the observers list.
	 */
	@Override
	public void deleteObserver(Observer o) {
		this.Observers.remove(o);
	}

	/**
	 * Notify Observers.
	 *
	 * notify the observers.
	 */
	@Override
	public void notifyObservers(Object obj) {
		for (Observer observer : Observers)
			observer.update(this, obj);
	}


}
