package model;
import org.junit.Test;
import algorithms.heuristic.MazeEuclideanDistance;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.RecursiveBacktrackerMazeGenerator;
import algorithms.search.AStar;
import algorithms.search.Searchable;
import algorithms.search.SearchableMaze;
import algorithms.search.Searcher;


// TODO: Auto-generated Javadoc
/**
 * The Class AStarTest.
 */
public class AStarTest {
	
	/** The m. */
	private Maze m;
	
	/** The s. */
	private Searchable s;
	
	/** The searcher. */
	private Searcher searcher;
	
	/**
	 * Instantiates a new a star test.
	 */
	public AStarTest() {
		 m = new RecursiveBacktrackerMazeGenerator().generateMaze(60, 60);
		 s = new SearchableMaze(m, true);	
		 searcher = new AStar(new MazeEuclideanDistance());
	}
	
	/**
	 * A star solving test.
	 */
	@Test(timeout = 100)
	public void AStarSolvingTest(){
		// Testing A*'s speed 
		searcher.search(s);
	}
}
