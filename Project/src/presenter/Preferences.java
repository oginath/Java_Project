package presenter;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;

import algorithms.mazeGenerators.MazeGenerator;
import algorithms.search.Searcher;

// TODO: Auto-generated Javadoc
/**
 * The Class Preferences.
 */
@SuppressWarnings("serial")
public class Preferences implements Serializable {
	
	/** The num of threads. */
	private int numOfThreads;
	
	/** The solver alg. */
	private Searcher solverAlg;
	
	/** The generator alg. */
	private MazeGenerator generatorAlg;
	
	//public int rows, cols; // default maze size
	//public boolean isDiagonal; // default maze diagonal binary
	
	/**
	 * Instantiates a new preferences.
	 */
	public Preferences() {
		
	}
	
	/**
	 * Gets the num of threads.
	 *
	 * @return the num of threads
	 */
	public int getNumOfThreads() {
		return numOfThreads;
	}
	
	/**
	 * Sets the num of threads.
	 *
	 * @param numOfThreads the new num of threads
	 */
	public void setNumOfThreads(int numOfThreads) {
		this.numOfThreads = numOfThreads;
	}
	
	/**
	 * Gets the solver alg.
	 *
	 * @return the solver alg
	 */
	public Searcher getSolverAlg() {
		return solverAlg;
	}
	
	/**
	 * Sets the solver alg.
	 *
	 * @param solverAlg the new solver alg
	 */
	public void setSolverAlg(Searcher solverAlg) {
		this.solverAlg = solverAlg;
	}
	
	/**
	 * Gets the generator alg.
	 *
	 * @return the generator alg
	 */
	public MazeGenerator getGeneratorAlg() {
		return generatorAlg;
	}
	
	/**
	 * Sets the generator alg.
	 *
	 * @param generatorAlg the new generator alg
	 */
	public void setGeneratorAlg(MazeGenerator generatorAlg) {
		this.generatorAlg = generatorAlg;
	}
	
	/**
	 * Load preferences.
	 */
	public void loadPreferences(){
		try {
			XMLDecoder xmlDe = new XMLDecoder(new FileInputStream("resources/preferences.xml"));
			Preferences p  = (Preferences) xmlDe.readObject();
			xmlDe.close();
			
			this.generatorAlg = p.getGeneratorAlg();
			this.numOfThreads = p.getNumOfThreads();
			this.solverAlg = p.getSolverAlg();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Save preferences.
	 */
	public void savePreferences(){
		try {
			XMLEncoder xmlEn = new XMLEncoder(new FileOutputStream("resources/preferences.xml"));
			xmlEn.writeObject(this);
			xmlEn.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
