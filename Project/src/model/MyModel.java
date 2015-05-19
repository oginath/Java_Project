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

// TODO: Auto-generated Javadoc
/**
 * The Class MyModel.
 */
public class MyModel extends Observable implements Model {

	/** The Observers. */
	private ArrayList<Observer> Observers;
	
	/** The m t os. */
	private HashMap<Maze, ArrayList<Solution>> mTOs; // Array of solutions because a maze can have more than one solution, different algorithms etc
	
	private HashMap<String, Maze> nTOm;
	
	/** The tp. */
	private ExecutorService tp;
	
	/** The m queue. */
	private Queue<Maze> mQueue;
	
	/** The dm. */
	private DataManager dm;
	
	/** The searcher. */
	private Searcher searcher;
	
	/** The maze gen. */
	private MazeGenerator mazeGen;
	
	/**
	 * Instantiates a new my model.
	 *
	 * @param s the s
	 * @param mg the mg
	 * @param nOfThreads the n of threads
	 */
	public MyModel(Searcher s, MazeGenerator mg, int nOfThreads) {
		this.dm = new DataManager();
		this.mTOs = this.loadMap();
		this.nTOm = new HashMap<String, Maze>();
		if(this.mTOs == null)
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

	/* (non-Javadoc)
	 * @see model.Model#generateMaze(int, int)
	 */
	@Override
	public void generateMaze(int rows, int cols) {
		System.out.println("Generating Maze....");
		Future<Maze> fm = tp.submit(new Callable<Maze>() {
			@Override
			public Maze call() throws Exception {
				Maze m = mazeGen.generateMaze(
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

	/* (non-Javadoc)
	 * @see model.Model#getMaze()
	 */
	@Override
	public Maze getMaze() {
		return mQueue.poll();
	}

	/* (non-Javadoc)
	 * @see model.Model#solveMaze(algorithms.mazeGenerators.Maze)
	 */
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

	/* (non-Javadoc)
	 * @see model.Model#getSolution(algorithms.mazeGenerators.Maze)
	 */
	@Override
	public Solution getSolution(Maze mazeName) {
		return this.mTOs.get(mazeName).get(0);
	}

	/* (non-Javadoc)
	 * @see model.Model#stop()
	 */
	@Override
	public void stop() {
		tp.shutdown();
		this.saveMap();
		dm.shutdown();
		try {
			if (tp.awaitTermination(100, TimeUnit.MILLISECONDS))
				System.out
						.println("Exit complete");
			else
				System.out.println("Shutdown error?");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public HashMap<String, Maze> getNtoM() {
		return nTOm;
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
	
	/**
	 * Save map.
	 */
	public void saveMap(){
		dm.saveMazeMap(mTOs);
	}
	

	/**
	 * Load map.
	 *
	 * @return the hash map
	 */
	public HashMap<Maze, ArrayList<Solution>> loadMap(){
		return dm.loadMazeMap();
	}

	/**
	 * Delete all data.
	 */
	public void deleteAllData(){
		dm.deleteAll();
	}
}
