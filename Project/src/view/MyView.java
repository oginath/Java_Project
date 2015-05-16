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

// TODO: Auto-generated Javadoc
/**
 * The Class MyView.
 */
public class MyView extends Observable implements View {

	/** The Observers. */
	private ArrayList<Observer> Observers;
	
	/** The cmds. */
	private MyCommands cmds;
	
	/** The cli. */
	private RunnableCLI cli;
	
	/** The t. */
	Thread t;

	/**
	 * Instantiates a new my view.
	 */
	public MyView() {
		this.Observers = new ArrayList<Observer>();
		this.cmds = new MyCommands();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
		this.cli = new RunnableCLI(br, pw);
	}

	/* (non-Javadoc)
	 * @see view.View#start()
	 */
	@Override
	public void start() {
		System.out.println("Type \"list\" for cmd list");
		
		do {
			t = new Thread(cli);
			t.start();
			while(t.isAlive())
				continue;
			
			notifyObservers();
			
		} while (this.cli.getCmd() != "exit");
	}

	/* (non-Javadoc)
	 * @see view.View#displayMaze(algorithms.mazeGenerators.Maze)
	 */
	@Override
	public void displayMaze(Maze m) {
		m.print();
	}

	/* (non-Javadoc)
	 * @see view.View#displaySolution(algorithms.search.Solution)
	 */
	@Override
	public void displaySolution(Solution s) {
		s.print();
	}

	/* (non-Javadoc)
	 * @see view.View#setCommands(java.lang.String, commands.Command)
	 */
	@Override
	public void setCommands(String cmdName, Command cmd) {
		this.cmds.setCommands(cmdName, cmd);
	}

	/* (non-Javadoc)
	 * @see view.View#getUserCommand()
	 */
	@Override
	public Command getUserCommand() {
		return this.cmds.selectCommand(this.cli.getCmd());
	}

	/* (non-Javadoc)
	 * @see view.View#getUserArg()
	 */
	public String getUserArg() {
		return this.cli.getArg();
	}

	/* (non-Javadoc)
	 * @see java.util.Observable#addObserver(java.util.Observer)
	 */
	@Override
	public void addObserver(Observer o) {
		this.Observers.add(o);
	}

	/* (non-Javadoc)
	 * @see java.util.Observable#deleteObserver(java.util.Observer)
	 */
	@Override
	public void deleteObserver(Observer o) {
		this.Observers.remove(o);
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observable#notifyObservers()
	 */
	@Override
	public void notifyObservers() {
		for (Observer observer : Observers)
			observer.update(this, null);
	}

}
