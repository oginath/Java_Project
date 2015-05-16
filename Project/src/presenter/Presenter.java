package presenter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import model.Model;
import view.View;
import algorithms.mazeGenerators.Maze;
import commands.Command;

// TODO: Auto-generated Javadoc
/**
 * The Class Presenter.
 */
public class Presenter implements Observer {

	/** The m. */
	Model m;
	
	/** The v. */
	View v;

	/** The n t om. */
	HashMap<String, Maze> nTOm;
	
	/** The nlist. */
	LinkedList<String> nlist;

	/**
	 * Instantiates a new presenter.
	 *
	 * @param v the v
	 * @param m the m
	 */
	public Presenter(View v, Model m) {
		this.v = v;
		this.m = m;
		this.nTOm = new HashMap<String, Maze>();
		this.nlist = new LinkedList<String>();

		v.setCommands("generate maze", new generateMazeCommand());
		v.setCommands("display maze", new displayMazeCommand());
		v.setCommands("solve maze", new solveMazeCommand());
		v.setCommands("display solution", new displaySolutionCommand());
		v.setCommands("exit", new Command(){

			@Override
			public void doCommand(String cmd) {
				System.out.println("exiting...");
			}
			
		});
		v.setCommands("list", new Command(){

			@Override
			public void doCommand(String cmd) {
				System.out.println("\nList of commands:");
				System.out.println("generate maze    <name> <x> <y> ---- generate maze with the specified name, x rows y columns");
				System.out.println("display maze     <name>         ---- display the maze with the specified name");
				System.out.println("solve maze       <name>         ---- solve the maze with the specified name");
				System.out.println("display solution <name>         ---- display the solution for the maze with the specified name");
				System.out.println("exit");
				
			}
			
		});
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object obj) {
		if (o == m) {
			if (((String) (obj)).equals("maze")) {
				Maze maze = m.getMaze();
				this.nTOm.put(this.nlist.poll(), maze);
				System.out.println("Maze is ready!");
			} else if (((String) (obj)).equals("solution")) {
				System.out.println("Solution is ready!");
			}
		} else if (o == v) {
			Command cmd = v.getUserCommand();
			if (cmd != null) {
				String arg = v.getUserArg();
				cmd.doCommand(arg);
			}
		}

	}

	// ##################### Commands:

	/**
	 * The Class generateMazeCommand.
	 */
	public class generateMazeCommand implements Command {

		/* (non-Javadoc)
		 * @see commands.Command#doCommand(java.lang.String)
		 */
		@Override
		public void doCommand(String arg) {
			String[] args = arg.split(" ");
			if (args.length == 3) {
				if(nTOm.containsKey(args[0])){
					System.out.println("Maze with this name already exists!");
					return;
				}
				nlist.add(args[0]);
				
				int rows = Integer.parseInt(args[1]);
				int cols = Integer.parseInt(args[2]);

				m.generateMaze(rows, cols);
			} else
				System.out.println("args error");//
		}
	}

	/**
	 * The Class displayMazeCommand.
	 */
	public class displayMazeCommand implements Command {

		/* (non-Javadoc)
		 * @see commands.Command#doCommand(java.lang.String)
		 */
		@Override
		public void doCommand(String mName) {
			String[] s = mName.split(" ");
			Maze maze = nTOm.get(s[0]);
			v.displayMaze(maze);
		}
	}

	/**
	 * The Class solveMazeCommand.
	 */
	public class solveMazeCommand implements Command {

		/* (non-Javadoc)
		 * @see commands.Command#doCommand(java.lang.String)
		 */
		@Override
		public void doCommand(String mName) {
			String[] s = mName.split(" ");
			m.solveMaze(nTOm.get(s[0]));
		}
	}

	/**
	 * The Class displaySolutionCommand.
	 */
	public class displaySolutionCommand implements Command {

		/* (non-Javadoc)
		 * @see commands.Command#doCommand(java.lang.String)
		 */
		@Override
		public void doCommand(String mName) {
			String[] s = mName.split(" ");
			v.displaySolution(m.getSolution(nTOm.get(s[0])));
		}
	}

}
