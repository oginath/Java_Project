package view.SWT;

import algorithms.search.Solution;

public interface GameBoard {

	void start();
	void stop();
	void insertClue(Solution sol);
}
