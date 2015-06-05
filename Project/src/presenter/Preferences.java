package presenter;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;

import algorithms.mazeGenerators.MazeGenerator;
import algorithms.search.Searcher;

/**
 * The Class Preferences.
 * 
 * This class is responsible for saving the settings of 
 * some choices that in later parts of the project, the user will set.
 */
@SuppressWarnings("serial")
public class Preferences implements Serializable {
	
	/** The number of threads in the thread pool (Model). */
	private int numOfThreads;
	
	/** The solver algorithm to be used (Model). */
	private Searcher solverAlg;
	
	/** The generator algorithm to be used (Model) */
	private MazeGenerator generatorAlg;
	
	//public int rows, cols; // default maze size
	//public boolean isDiagonal; // default maze diagonal t/f
	
	/**
	 * Instantiates a new preferences.
	 */
	public Preferences() {
		
	}
	
	/**
	 * Gets the number of threads.
	 *
	 * @return the number of threads
	 */
	public int getNumOfThreads() {
		return numOfThreads;
	}
	
	/**
	 * Sets the number of threads.
	 *
	 * @param numOfThreads the new number of threads
	 */
	public void setNumOfThreads(int numOfThreads) {
		this.numOfThreads = numOfThreads;
	}
	
	/**
	 * Gets the solver algorithm.
	 *
	 * @return the solver algorithm
	 */
	public Searcher getSolverAlg() {
		return solverAlg;
	}
	
	/**
	 * Sets the solver algorithm.
	 *
	 * @param solverAlg the new solver algorithm
	 */
	public void setSolverAlg(Searcher solverAlg) {
		this.solverAlg = solverAlg;
	}
	
	/**
	 * Gets the generator algorithm.
	 *
	 * @return the generator algorithm
	 */
	public MazeGenerator getGeneratorAlg() {
		return generatorAlg;
	}
	
	/**
	 * Sets the generator algorithm.
	 *
	 * @param generatorAlg the new generator algorithm
	 */
	public void setGeneratorAlg(MazeGenerator generatorAlg) {
		this.generatorAlg = generatorAlg;
	}
	
	/**
	 * Load preferences.
	 * 
	 * Loads the preferences that were set in a previous run of the program,
	 * which are encoded in XML format.
	 * 
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
	 * 
	 * Saves the preferences in XML format.
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
