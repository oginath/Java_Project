package presenter;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;

import algorithms.mazeGenerators.MazeGenerator;
import algorithms.search.Searcher;

@SuppressWarnings("serial")
public class Preferences implements Serializable {
	
	private int numOfThreads;
	private Searcher solverAlg;
	private MazeGenerator generatorAlg;
	
	//public int rows, cols; // default maze size
	//public boolean isDiagonal; // default maze diagonal binary
	//public Heuristic hue; // default heuristic in case of A*
	
	public Preferences() {
		
	}
	
	public int getNumOfThreads() {
		return numOfThreads;
	}
	public void setNumOfThreads(int numOfThreads) {
		this.numOfThreads = numOfThreads;
	}
	public Searcher getSolverAlg() {
		return solverAlg;
	}
	public void setSolverAlg(Searcher solverAlg) {
		this.solverAlg = solverAlg;
	}
	public MazeGenerator getGeneratorAlg() {
		return generatorAlg;
	}
	public void setGeneratorAlg(MazeGenerator generatorAlg) {
		this.generatorAlg = generatorAlg;
	}
	
	public void loadPreferences(){
		try {
			XMLDecoder xmlDe = new XMLDecoder(new FileInputStream("preferences.xml"));
			Preferences p  = (Preferences) xmlDe.readObject();
			xmlDe.close();
			
			this.generatorAlg = p.getGeneratorAlg();
			this.numOfThreads = p.getNumOfThreads();
			this.solverAlg = p.getSolverAlg();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void savePreferences(){
		try {
			XMLEncoder xmlEn = new XMLEncoder(new FileOutputStream("preferences.xml"));
			xmlEn.writeObject(this);
			xmlEn.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
