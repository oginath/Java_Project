package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.RecursiveBacktrackerMazeGenerator;
import algorithms.search.BFS;
import algorithms.search.Searchable;
import algorithms.search.SearchableMaze;
import algorithms.search.Searcher;
import algorithms.search.Solution;

public class MyModel extends Observable implements Model {

	private ArrayList<Observer> Observers;
	private HashMap<Maze, ArrayList<Solution>> mTOs; // Array of solutions because a maze can have more than one solutions, different algorithms etc
	private ExecutorService tp;
	private Queue<Maze> mQueue;
	private DataManager dm;
	
	public MyModel() {
		this.Observers = new ArrayList<Observer>();
		this.mTOs = new HashMap<Maze, ArrayList<Solution>>();
		this.tp = Executors.newFixedThreadPool(3);
		this.mQueue = new LinkedList<Maze>();
		this.dm = new DataManager();
	}

	@Override
	public void generateMaze(int rows, int cols) {
		System.out.println("Generating Maze....");
		Future<Maze> fm = tp.submit(new Callable<Maze>() {
			@Override
			public Maze call() throws Exception {
				Maze m = new RecursiveBacktrackerMazeGenerator().generateMaze(
						rows, cols);
				return m;
			}
		});
		Maze maze = null;
		try {
			maze = fm.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		if (maze != null)
			mQueue.add(maze);
			for (Observer observer : Observers)
				observer.update(this, "maze");
	}

	@Override
	public Maze getMaze() {
		return mQueue.poll();
	}

	@Override
	public void solveMaze(Maze m) {
		Solution s = null;
		if (this.mTOs.containsKey(m)) { 
		//	v.display("Solution to this maze already exists!");
			//get another?
			
			s = this.mTOs.get(m).get(0);
		} 
		else {
			System.out.println("Solving Maze....");
			Future<Solution> fs = tp.submit(new Callable<Solution>() {
				@Override
				public Solution call() throws Exception {

					Searcher searcher = new BFS();
					Searchable s = new SearchableMaze(m, false);
					return searcher.search(s);
				}
			});
			try {
				s = fs.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		
		}
		if (s != null) {
			ArrayList<Solution> l = new ArrayList<Solution>();
			l.add(s);
			this.mTOs.put(m, l);
			for (Observer observer : Observers)
				observer.update(this, "solution");
		}
	}

	@Override
	public Solution getSolution(Maze mazeName) {
		return this.mTOs.get(mazeName).get(0);
	}

	@Override
	public void stop() {
		System.out.println("Stoping....");
		tp.shutdown();
		dm.shutdown();
		try {
			if (tp.awaitTermination(100, TimeUnit.MILLISECONDS))
				System.out
						.println("Shutdown complete, termination peaceful...");
			else
				System.out.println("Shutdown error?");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

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
	
	public void saveMap(){
		dm.saveMazeMap(mTOs);
	}
	

	public HashMap<Maze, ArrayList<Solution>> loadMap(){
		return dm.loadMazeMap();
	}

	public void deleteAllData(){
		dm.deleteAll();
	}
}
