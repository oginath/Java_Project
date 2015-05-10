package model;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import observe.Observable;
import observe.Observer;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.RecursiveBacktrackerMazeGenerator;
import algorithms.search.BFS;
import algorithms.search.Searcher;
import algorithms.solution.Solution;

public class MyModel implements Model, Observable {

	private ArrayList<Observer> Observers;
	ExecutorService tp;

	public MyModel() {
		this.Observers = new ArrayList<Observer>();
		tp = Executors.newFixedThreadPool(3);
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
		Maze m = null;
		try {
			m = fm.get();
		} catch (InterruptedException | ExecutionException e) {e.printStackTrace();}
		if (m!= null)
			for (Observer observer : Observers)
				observer.update(this, "maze");
	}

	@Override
	public Maze getMaze() {
		System.out.println("Getting Maze....");
		return null;
	}

	@Override
	public void solveMaze(Maze m) {
		System.out.println("Solving Maze....");
		Future<Solution> fs = tp.submit(new Callable<Solution>() {

			@Override
			public Solution call() throws Exception {

				Searcher searcher = new BFS();
				//Searchable s = new SearchableMaze(m, false);
				//return searcher.search(s);
				return new Solution();
			}
		});
		Solution s = null;
		try {
			s = fs.get();
		} catch (InterruptedException | ExecutionException e) {	e.printStackTrace();}
		if (s!=null)
			for (Observer observer : Observers)
				observer.update(this, "solution");
	}

	@Override
	public Solution getSolution() {
		System.out.println("Getting Solution....");
		return null;
	}

	@Override
	public void stop() {
		System.out.println("Stoping....");
		tp.shutdown();
		try {
			if (tp.awaitTermination(100, TimeUnit.MILLISECONDS))
				System.out.println("Shutdown complete, termination peaceful...");
			else 
				System.out.println("Shutdown error?");
		} catch (InterruptedException e) {e.printStackTrace();}

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
