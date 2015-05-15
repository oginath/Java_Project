package view;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class RunnableCLI implements Runnable {

	MyCLI cli;
	
	public RunnableCLI(BufferedReader br, PrintWriter pw) {
		this.cli = new MyCLI(br, pw);
	}

	@Override
	public void run() {
		this.cli.getCommand();
	}
	
	public String getCmd(){
		
		return this.cli.getCmd();
	}
	
	public String getArg(){
		
		return this.cli.getArg();
	}
}
