package model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import algorithms.mazeGenerators.Cell;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

// TODO: Auto-generated Javadoc
/**
 * The Class DataManager.
 */
public class DataManager {

	/** The cell set. */
	HashMap<Cell, Integer> cellSet = null;
	
	/** The session. */
	private Session session = null;
	
	/** The sf. */
	private SessionFactory sf = null;

	/**
	 * Instantiates a new data manager.
	 */
	public DataManager() {
		Logger log = Logger.getLogger("org.hibernate");
		log.setLevel(Level.SEVERE);
		String filePath = "resources/hibernate.cfg.xml";
		File f = new File(filePath);
		Configuration config = new Configuration();
		config.configure(f);
		ServiceRegistry sr = new StandardServiceRegistryBuilder()
				.applySettings(config.getProperties()).build();
		sf = config.configure().buildSessionFactory(sr);
		session = sf.openSession();
	}

	/**
	 * Save maze map.
	 *
	 * @param map the map
	 */
	public void saveMazeMap(HashMap<Maze, ArrayList<Solution>> map) {

		session.beginTransaction();

		for (Maze maze : map.keySet()) {

			this.saveMaze(maze);

			Query query = session
					.createQuery("FROM Solution Order by solID desc");
			@SuppressWarnings("unchecked")
			List<Solution> soList = query.list();
			Iterator<Solution> soIt = soList.iterator();

			Solution sol = null;
			int i = 0;
			if (soIt.hasNext()) {
				sol = soIt.next();
				i = sol.getSolID();
			}

			Iterator<Solution> it = map.get(maze).iterator();
			sol = null;
			while (it.hasNext()) {
				sol = it.next();
				sol.setMazeID(maze.getID());
				sol.setSolID(++i);
				this.saveSolution(sol);
			}
		}
		session.getTransaction().commit();
	}

	/**
	 * Save maze.
	 *
	 * @param maze the maze
	 */
	private void saveMaze(Maze maze) {

		if (cellSet == null) {
			cellSet = new HashMap<Cell, Integer>();

			Query q = session.createQuery("From Cell");
			@SuppressWarnings("unchecked")
			List<Cell> cellList = q.list();
			Iterator<Cell> cellIt = cellList.iterator();

			Cell c = null;
			while (cellIt.hasNext()) {
				c = cellIt.next();
				cellSet.put(c, c.getID());
			}
		}

		Query query = session.createQuery("FROM Maze Order by ID desc");

		@SuppressWarnings("unchecked")
		List<Maze> idList = query.list();
		Iterator<Maze> idIt = idList.iterator();

		Integer x = null;
		Maze tempMaze = null;
		if (idIt.hasNext()) {
			tempMaze = idIt.next();
			x = tempMaze.getID() + 1;
			maze.setID(x);
		} else
			maze.setID(1);

		maze.setMatrixArray(new byte[maze.getRows() * maze.getCols()]);

		for (int i = 0, k = 0; i < maze.getRows(); i++)
			for (int j = 0; j < maze.getCols(); j++, k++) {
				if (cellSet.containsKey(maze.getCell(j, i))) {
					maze.getCell(j, i).setID(cellSet.get(maze.getCell(j, i)));
					maze.getMatrixArray()[k] = (byte) maze.getCell(j, i)
							.getID();
					continue;
				}
				maze.getCell(j, i).setID(cellSet.size());
				maze.getMatrixArray()[k] = (byte) maze.getCell(j, i).getID();
				cellSet.put(maze.getCell(j, i), maze.getCell(j, i).getID());
			}

		for (Cell cell : cellSet.keySet())
			session.save(cell);
		session.save(maze);
	}

	/**
	 * Save solution.
	 *
	 * @param s the s
	 */
	private void saveSolution(Solution s) {

		session.save(s);
	}

	/**
	 * Load maze map.
	 *
	 * @return the hash map
	 */
	public HashMap<Maze, ArrayList<Solution>> loadMazeMap() {

		HashMap<Maze, ArrayList<Solution>> map = new HashMap<Maze, ArrayList<Solution>>();

		Query query = session.createQuery("FROM Maze Order by ID desc");

		@SuppressWarnings("unchecked")
		List<Maze> idList = query.list();
		Iterator<Maze> idIt = idList.iterator();

		Integer x = null;
		Maze tempMaze = null;
		if (idIt.hasNext()) {
			tempMaze = idIt.next();
			x = tempMaze.getID();
		} else {
			// no mazes
			return null;
		}

		Maze m = null;
		for (int i = 0; i < x; i++) {
			m = this.loadMaze(i + 1);
			ArrayList<Solution> solList = this.loadSolutions(m.getID());
			map.put(m, solList);
		}

		return map;
	}

	/**
	 * Load maze.
	 *
	 * @param index the index
	 * @return the maze
	 */
	private Maze loadMaze(int index) {

		Query query = session.createQuery("FROM Maze where ID = " + index);

		@SuppressWarnings("unchecked")
		List<Maze> list = query.list();
		Iterator<Maze> it = list.iterator();

		Maze m = null;
		if (it.hasNext())
			m = it.next();
		else {
			System.out.println("No Saved Data!"); // v.display()
			return null;
		}
		Cell[][] matrix = new Cell[m.getRows()][m.getCols()];

		query = session.createQuery("FROM Cell");
		HashMap<Integer, Cell> cellList = new HashMap<Integer, Cell>();
		@SuppressWarnings("unchecked")
		List<Cell> clist = query.list();
		Iterator<Cell> cit = clist.iterator();

		while (cit.hasNext()) {
			Cell c = cit.next();
			cellList.put(c.getID(), c);
		}

		for (int i = 0, k = 0; i < m.getRows(); i++)
			for (int j = 0; j < m.getCols(); j++, k++) {
				matrix[i][j] = new Cell(
						cellList.get((int) m.getMatrixArray()[k]));
			}

		m.setMatrix(matrix);
		m.setMatrixArray(null);

		return m;
	}

	/**
	 * Load solutions.
	 *
	 * @param mazeID the maze id
	 * @return the array list
	 */
	private ArrayList<Solution> loadSolutions(int mazeID) {

		Query query = this.session.createQuery("FROM Solution WHERE MazeID = "
				+ mazeID);

		@SuppressWarnings("unchecked")
		List<Solution> solList = query.list();
		Iterator<Solution> solIt = solList.iterator();

		ArrayList<Solution> solutions = new ArrayList<Solution>();

		while (solIt.hasNext()) {
			solutions.add(solIt.next());
		}
		return solutions;
	}

	/**
	 * Delete all.
	 */
	public void deleteAll() {

		session.beginTransaction();
		HashMap<Maze, ArrayList<Solution>> map = this.loadMazeMap();
		for (Maze maze : map.keySet()) {
			@SuppressWarnings("rawtypes")
			Iterator it = map.get(maze).iterator();
			while (it.hasNext()) {
				session.delete(it.next());
			}

			session.delete(maze);
		}
		session.getTransaction().commit();
	}
	
	/**
	 * Shutdown.
	 */
	public void shutdown(){
		this.sf.close();
	}
	
	// TODO 1: delete maze (and subsequential solutions)
	// TODO 2: update maze (update solutions pretty much)
	// TODO: Possible problem with deleting after loading
}
