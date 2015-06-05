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
import algorithms.mazeGenerators.MazeGenerator;
import algorithms.search.Searchable;
import algorithms.search.SearchableMaze;
import algorithms.search.Searcher;
import algorithms.search.Solution;

/**
 * The Class MyModel.
 */
public class MyModel extends Observable implements Model {

	/** The Observers of this class */
	private ArrayList<Observer> Observers;

	/** The maze to array list of solutions map. */
	// Array of solutions because a maze can have more than one solution,
	// different starting points, different algorithms etc.
	private HashMap<Maze, ArrayList<Solution>> mTOs;

	/** The string (name) to maze map. */
	private HashMap<String, Maze> nTOm;

	/** The Thread Pool. */
	private ExecutorService tp;

	/** Queue of mazes */
	private Queue<Maze> mQueue;

	/**
	 * The DataManger, used to save objects to the DB
	 * 
	 * For more information, see @class DataManager
	 */
	private DataManager dm;

	/** The Maze Generator Algorithm. */
	private MazeGenerator mazeGen;

	/** The Solver Algorithm */
	private Searcher searcher;

	/**
	 * Instantiates a new model.
	 *
	 * @param s
	 *            The solver algorithm
	 * @param mg
	 *            the Maze generating algorithm
	 * @param nOfThreads
	 *            the number of threads in the thread pool
	 */
	public MyModel(Searcher s, MazeGenerator mg, int nOfThreads) {
		this.dm = new DataManager();
		this.mTOs = this.loadMap();
		this.nTOm = new HashMap<String, Maze>();
		if (this.mTOs == null)
			this.mTOs = new HashMap<Maze, ArrayList<Solution>>();
		else {
			for (Maze m : mTOs.keySet())
				nTOm.put(m.getName(), m);
		}
		this.Observers = new ArrayList<Observer>();
		this.tp = Executors.newFixedThreadPool(nOfThreads);
		this.mQueue = new LinkedList<Maze>();
		this.searcher = s;
		this.mazeGen = mg;
	}

	/**
	 * Generate Maze.
	 *
	 * Generate a new maze and add it to the queue of mazes. The generation is
	 * done in a separate thread. Notifies the observers when done.
	 *
	 * @param rows
	 *            the rows of the maze
	 * @param cols
	 *            the columns of the maze
	 */
	@Override
	public void generateMaze(int rows, int cols) {
		System.out.println("Generating Maze....");
		Future<Maze> fm = tp.submit(new Callable<Maze>() {
			@Override
			public Maze call() throws Exception {
				Maze m = mazeGen.generateMaze(rows, cols);
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

	/**
	 * Get maze.
	 *
	 * @return The latest maze from the queue.
	 */
	@Override
	public Maze getMaze() {
		return mQueue.poll();
	}

	/**
	 * Solve Maze.
	 *
	 * Solves the given maze it and puts it in the Maze to Solutions map.
	 * /*-----Right now it only saves one solution per maze, later in the
	 * project it will save more than one----*\ The Solving is done in a
	 * separate thread. Notifies the observers when done.
	 * 
	 * @param m
	 *            the maze to be solved.
	 */
	@Override
	public void solveMaze(SearchableMaze sm) {
		Maze m = sm.getMaze();
		Solution s = null;

		System.out.println("Solving Maze....");
		Future<Solution> fs = tp.submit(new Callable<Solution>() {
			@Override
			public Solution call() throws Exception {
				return searcher.search(sm);
			}
		});
		try {
			s = fs.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		if (s != null) {
			ArrayList<Solution> sols = mTOs.get(m);
			if(sols == null)
				sols = new ArrayList<Solution>();
			sols.add(s);
			this.mTOs.remove(m);
			this.mTOs.put(m, sols);
			for (Observer observer : Observers)
				observer.update(this, "solution");
		}
	}

	/**
	 * Get Solution.
	 * 
	 * @param mazeName
	 *            the name of the maze to retrieve the solution for
	 * @return the Solution to the given maze
	 */
	@Override
	public Solution getSolution(Maze mazeName) {
		ArrayList<Solution> array = mTOs.get(mazeName);
		return array.get(array.size()-1);
	}

	/**
	 * Stop.
	 * 
	 * Stops the thread pool in an orderly way. Stops the data manager.
	 */
	@Override
	public void stop() {
		tp.shutdown();
		this.saveMap();
		dm.shutdown();
		try {
			if (tp.awaitTermination(100, TimeUnit.MILLISECONDS))
				;
			// exit complete
			// else
			// System.out.println("Shutdown error?");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public HashMap<String, Maze> getNtoM() {
		return nTOm;
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
	public void notifyObservers() {
		for (Observer observer : Observers)
			observer.update(this, null);
	}

	/**
	 * Save map.
	 * 
	 * Save the maze to solution map in the DB.
	 */
	public void saveMap() {
		dm.saveMazeMap(mTOs);
	}

	/**
	 * Load map.
	 * 
	 * Loads the maze to solution map from the DB.
	 *
	 * @return the Maze to Solution map
	 */
	public HashMap<Maze, ArrayList<Solution>> loadMap() {
		return dm.loadMazeMap();
	}

	/**
	 * Delete all data in the DB.
	 */
	public void deleteAllData() {
		dm.deleteAll();
	}
}
