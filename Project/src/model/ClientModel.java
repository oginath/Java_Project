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

// TODO: Auto-generated Javadoc
/**
 * The Class ClientModel.
 */
public class ClientModel extends Observable implements Model {

	/** The Observers. */
	ArrayList<Observer> Observers;
	
	/** The my server. */
	Socket myServer;
	
	/** The ois. */
	ObjectInputStream ois;
	
	/** The out to server. */
	PrintWriter outToServer;
	
	/** The connected. */
	boolean connected;
	
	/** The port. */
	int port;
	
	/** The address. */
	String address;
	
	/**
	 * Instantiates a new client model.
	 *
	 * @param port the port
	 * @param address the address
	 */
	public ClientModel(int port, String address) {
		connected = false;
		Observers = new ArrayList<Observer>();
		
		this.port = port;
		this.address = address;
	}
	
	/* (non-Javadoc)
	 * @see model.Model#generateMaze(java.lang.String, int, int)
	 */
	@Override
	public void generateMaze(String name, int rows, int cols) {
		if(!connected)
			connect();
		
		if(connected){
			outToServer.println("genmaze " + name + " " + rows + " " + cols);
			outToServer.flush();
			boolean flag = false;
			try {
				flag = ois.readBoolean();
			} catch (IOException e) {e.printStackTrace();}
			if(flag)
				notifyObservers("maze");
			else 
				notifyObservers("already exists");
		}
		else
			notifyObservers("not connected");
	}

	/* (non-Javadoc)
	 * @see model.Model#getMaze(java.lang.String)
	 */
	@Override
	public Maze getMaze(String name) {
		if(!connected)
			connect();
		
		Maze m = null;
		if(connected){
			try {
			outToServer.println("getmaze " + name);
			outToServer.flush();
			
			boolean flag = false;
			flag = ois.readBoolean();
			if(flag){
				Compressor c = new HuffmanAlg();
				ByteArrayInputStream bais = new ByteArrayInputStream(c.decompress(Base64.getDecoder().decode
						(((String) ois.readObject()).getBytes())));
				ObjectInputStream objIn = new ObjectInputStream(bais);
				objIn.close();
				
				m = (Maze) objIn.readObject();
			}
			else
				notifyObservers("doesn't exists");
			} catch (ClassNotFoundException | IOException e) {e.printStackTrace();}
		}
		else
			notifyObservers("not connected");
		return m;
	}
	
	/* (non-Javadoc)
	 * @see model.Model#getPositions(java.lang.String)
	 */
	@Override
	public String getPositions(String name){
		if(!connected)
			connect();
		
		String pos = null;
		if(connected){
			try {
			outToServer.println("getpos " + name);
			outToServer.flush();
			
			pos = (String) ois.readObject();
			
			} catch (IOException | ClassNotFoundException e) {e.printStackTrace();}
		}
		else
			notifyObservers("not connected");
		return pos;
	}
	
	/* (non-Javadoc)
	 * @see model.Model#solveMaze(java.lang.String)
	 */
	@Override
	public void solveMaze(String arg) {
		if(!connected)
			connect();
		
		if(connected){
			outToServer.println("solmaze " + arg);
			outToServer.flush();
			notifyObservers("solution");
		}
		else
			notifyObservers("not connected");
	}

	/* (non-Javadoc)
	 * @see model.Model#getSolution(java.lang.String)
	 */
	@Override
	public Solution getSolution(String name) {
		if(!connected)
			connect();
		
		Solution sol = null;
		if(connected){
			try {
			outToServer.println("getsol " + name);
			outToServer.flush();			
			
			sol = (Solution) ois.readObject();
			} catch (ClassNotFoundException | IOException e) {e.printStackTrace();}
		}
		else
			notifyObservers("not connected");
		return sol;
	}

	/**
	 * Connect.
	 */
	public void connect(){
		try {
			myServer = new Socket(this.address, this.port);
			
			ois = new ObjectInputStream(myServer.getInputStream());
			outToServer = new PrintWriter(new OutputStreamWriter(myServer.getOutputStream()));
						
			connected = true;
		} catch (IOException e) {connected = false;}
	}
	
	/* (non-Javadoc)
	 * @see model.Model#stop()
	 */
	@Override
	public void stop() {
		if(myServer != null){
			try {
				outToServer.println("stop");
				outToServer.flush();
				
				outToServer.close();
				ois.close();
				myServer.close();
			} catch (IOException e) {e.printStackTrace();}
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Observable#notifyObservers(java.lang.Object)
	 */
	@Override
	public void notifyObservers(Object arg) {
		for (Observer observer : Observers) {
			observer.update(this, arg);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observable#addObserver(java.util.Observer)
	 */
	@Override
	public synchronized void addObserver(Observer o) {
		this.Observers.add(o);
	}

}
