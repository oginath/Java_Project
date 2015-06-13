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
	boolean connected;
	int port;
	String address;
	
	public ClientModel(int port, String address) {
		connected = false;
		Observers = new ArrayList<Observer>();
		
		this.port = port;
		this.address = address;
	}
	
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
	@Override
	public String getPositions(String name){
		String pos = null;
		try {
		outToServer.println("getpos " + name);
		outToServer.flush();
		
		pos = (String) ois.readObject();
		
		} catch (IOException | ClassNotFoundException e) {e.printStackTrace();}
		
		return pos;
	}
	@Override
	public void solveMaze(String arg) {
		if(!connected)
			connect();
		
		if(connected){
			outToServer.println("solmaze " + arg);
			outToServer.flush();
		}
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

	public void connect(){
		try {
			myServer = new Socket(this.address, this.port);
			
			ois = new ObjectInputStream(myServer.getInputStream());
			outToServer = new PrintWriter(new OutputStreamWriter(myServer.getOutputStream()));
						
			connected = true;
		} catch (IOException e) {connected = false;}
	}
	
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
