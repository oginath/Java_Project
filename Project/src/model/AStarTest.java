package model;
import org.junit.Test;
import algorithms.heuristic.MazeEuclideanDistance;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.RecursiveBacktrackerMazeGenerator;
import algorithms.search.AStar;
import algorithms.search.Searchable;
import algorithms.search.SearchableMaze;
import algorithms.search.Searcher;


public class AStarTest {
	
	private Maze m;
	private Searchable s;
	private Searcher searcher;
	
	public AStarTest() {
		 m = new RecursiveBacktrackerMazeGenerator().generateMaze(60, 60);
		 s = new SearchableMaze(m, true);	
		 searcher = new AStar(new MazeEuclideanDistance());
	}
	
	@Test(timeout = 100)
	public void AStarSolvingTest(){
		// Testing A*'s speed 
		searcher.search(s);
	}
}
