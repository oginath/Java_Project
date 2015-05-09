package presenter;

import model.Model;
import observe.Observable;
import observe.Observer;
import view.View;
import algorithms.mazeGenerators.Maze;

public class Presenter implements Observer{
	
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
		if(o == m){
			Maze maze = m.getMaze();
			v.displayMaze(maze);
		}
		
		else if(o == v){
			Command c = v.getUserCommand();
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
