package view;

import algorithms.search.Solution;

/**
 * The Interface GameBoard.
 */
public interface GameBoard {

	/**
	 * Start a game.
	 */
	void start();
	
	/**
	 * Stop a game.
	 */
	void stop();
	
	/**
	 * Insert a clue to the game.
	 *
	 * @param sol The solution which the clue will consist of.
	 */
	void insertClue(Solution sol);
}
