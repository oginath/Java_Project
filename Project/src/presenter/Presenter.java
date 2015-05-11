package presenter;


import java.util.Observable;
import java.util.Observer;

import model.Model;
import view.View;
import algorithms.mazeGenerators.Maze;
import algorithms.solution.Solution;

public class Presenter implements Observer {

	Model m;
	View v;

	public Presenter(View v, Model m) {
		this.v = v;
		this.m = m;
		Command c = new TestMVPCommand();
		v.setCommands(c);
	}

	@Override
	public void update(Observable o, Object obj) {
		if (o == m) {
			if (((String)(obj)).equals("maze")) {
				Maze maze = m.getMaze();
				v.displayMaze(maze);
				m.solveMaze(maze);
			}
			else if (((String)(obj)).equals("solution")){
				Solution sol = m.getSolution();
				v.displaySolution(sol);
			}
		} 
		else if (o == v) {
			Command c = v.getUserCommand();
			c.doCommand("");
			c.doCommand("");
			c.doCommand("");
		}

	}

	// ##################### Commands:

	public interface Command {

		void doCommand(String cmd);
	}

	public class TestMVPCommand implements Command {

		@Override
		public void doCommand(String cmd) {
			System.out.println("Executing MVP Command....");
			m.generateMaze();
		}

	}

}
