package model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Observable;
import java.util.Observer;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import compression_algorithms.Compressor;
import compression_algorithms.HuffmanAlg;

public class ClientModel extends Observable implements Model {

	ArrayList<Observer> Observers;
	Socket myServer;
	ObjectInputStream ois;
	PrintWriter outToServer;
	
	public ClientModel() {
		try {
			myServer = new Socket("localhost", 5400);
			
			ois = new ObjectInputStream(myServer.getInputStream());
			outToServer = new PrintWriter(new OutputStreamWriter(myServer.getOutputStream()));
						
			Observers = new ArrayList<Observer>();
			
		} catch (IOException e) {e.printStackTrace();}
	}
	
	@Override
	public void generateMaze(String name, int rows, int cols) {
		
		outToServer.println("genmaze " + name + " " + rows + " " + cols);
		outToServer.flush();
		
		notifyObservers("maze");
	}

	@Override
	public Maze getMaze(String name) {
		Maze m = null;
		try {
		outToServer.println("getmaze " + name);
		outToServer.flush();
				
		Compressor c = new HuffmanAlg();
		ByteArrayInputStream bais = new ByteArrayInputStream(c.decompress(Base64.getDecoder().decode
				(((String) ois.readObject()).getBytes())));
		ObjectInputStream objIn = new ObjectInputStream(bais);
		objIn.close();
		
		m = (Maze) objIn.readObject();
		} catch (ClassNotFoundException | IOException e) {e.printStackTrace();}
		return m;
	}

	@Override
	public void solveMaze(String arg) {
		outToServer.println("solmaze " + arg);
		outToServer.flush();
		
		notifyObservers("solution");
	}

	@Override
	public Solution getSolution(String name) {
		Solution sol = null;
		try {
		outToServer.println("getsol " + name);
		outToServer.flush();			
		
		sol = (Solution) ois.readObject();
		} catch (ClassNotFoundException | IOException e) {e.printStackTrace();}
		return sol;
	}

	@Override
	public void stop() {
		try {
			outToServer.println("stop");
			outToServer.flush();
			
			outToServer.close();
			ois.close();
			myServer.close();
		} catch (IOException e) {e.printStackTrace();}
	}

	@Override
	public void notifyObservers(Object arg) {
		for (Observer observer : Observers) {
			observer.update(this, arg);
		}
	}
	
	@Override
	public synchronized void addObserver(Observer o) {
		this.Observers.add(o);
	}

}
