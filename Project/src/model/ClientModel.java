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

/**
 * The Class ClientModel.
 */
public class ClientModel extends Observable implements Model {

	/** The Observers. */
	ArrayList<Observer> Observers;
	
	/** The Socket representing the server. */
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
	 * @param port The port
	 * @param address The address
	 */
	public ClientModel(int port, String address) {
		connected = false;
		Observers = new ArrayList<Observer>();
		
		this.port = port;
		this.address = address;
	}
	
	/**
	 * If not connected to the server, connect.
	 * 
	 * Asks the server to generate a maze with the parameters.
	 *
	 * If the server returns that the generation was successful, notifies the observers.
	 *
	 * @param name The name of the maze to be generated
	 * @param rows The number of rows in the maze to be generated
	 * @param cols The number of columns in the maze to be generated
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
	
	/**
	 * If not connected to the server, connect.
	 * 
	 * Asks the server to return a maze with the specified name.
	 * 
	 * @param name The name of the required maze
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
	
	/**
	 * If not connected to the server, connect.
	 * 
	 * Asks the server to return starting and end points for a maze.
	 * 
	 * @param name The name of the maze that the required parameters are for.
	 * 
	 * @return The parameters from the server.
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
	
	/**
	 * If not connected to the server, connect.
	 * 
	 * Asks the server to solve a maze with the specified name.
	 * 
	 * @param name The name of the maze to solve
	 */
	@Override
	public void solveMaze(String name) {
		if(!connected)
			connect();
		
		if(connected){
			outToServer.println("solmaze " + name);
			outToServer.flush();
			notifyObservers("solution");
		}
		else
			notifyObservers("not connected");
	}

	/**
	 * If not connected to the server, connect.
	 * 
	 * Asks the server to return a solution for the maze with the specified name.
	 * 
	 * @param name The name of the required maze which the solution is for
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
	 * 
	 * Tries to connect to a server with the set port and address.
	 */
	public void connect(){
		try {
			myServer = new Socket(this.address, this.port);
			
			ois = new ObjectInputStream(myServer.getInputStream());
			outToServer = new PrintWriter(new OutputStreamWriter(myServer.getOutputStream()));
						
			connected = true;
		} catch (IOException e) {connected = false;}
	}
	
	/**
	 *
	 * Tells the server that this client is going to disconnect, 
	 * and closes the socket and all the streams.
	 * 
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

	/**
	 * Add Observer.
	 * 
	 * @param o
	 *            adds the observer to this the observers list.
	 */
	@Override
	public synchronized void addObserver(Observer o) {
		this.Observers.add(o);
	}
	
	/**
	 * Notify Observers.
	 * 
	 * notify the observers.
	 *
	 * @param obj An object to pass to the observers.
	 */
	@Override
	public void notifyObservers(Object arg) {
		for (Observer observer : Observers) {
			observer.update(this, arg);
		}
	}
}
