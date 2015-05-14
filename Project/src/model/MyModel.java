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
	private HashMap<Maze, Solution> mTOs;
	private ExecutorService tp;
	private Queue<Maze> mQueue;
	private Queue<Solution> sQueue;
	
	public MyModel() {
		this.Observers = new ArrayList<Observer>();
		this.mTOs = new HashMap<Maze, Solution>();
		this.tp = Executors.newFixedThreadPool(3);
		this.mQueue = new LinkedList<Maze>();
		this.sQueue = new LinkedList<Solution>();
	}

	@Override
	public void generateMaze() {
		System.out.println("Generating Maze....");
		Future<Maze> fm = tp.submit(new Callable<Maze>() {
			@Override
			public Maze call() throws Exception {
				Maze m = new RecursiveBacktrackerMazeGenerator().generateMaze(
						10, 10);
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
		if (this.mTOs.containsKey(m)) { // TODO: maze HASHCODE & EQUALS
			System.out.println("Solution to this maze already exists!");
			s = this.mTOs.get(m);
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
			this.mTOs.put(m, s);
		}
		if (s != null) {
			sQueue.add(s);
			for (Observer observer : Observers)
				observer.update(this, "solution");
		}
	}

	@Override
	public Solution getSolution() {
		return sQueue.poll();
	}

	@Override
	public void stop() {
		System.out.println("Stoping....");
		tp.shutdown();
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

}
