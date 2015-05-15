package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import commands.CLI;

public class MyCLI extends CLI {

	
	String cmd;
	String arg;
	
	public MyCLI(BufferedReader in, PrintWriter out) {
		super(in, out);
	}

	
	public void getCommand(){
	
		this.cmd = null;
		this.arg = null;
		
		System.out.print("Enter command: ");
		try {
			String line = this.in.readLine();
			
			if(!line.equals("exit")){
				String[] sp = line.split(" ");

				cmd = sp[0] + " " +sp[1];
				if (sp.length > 2){
					arg = "";
					for(int i = 2; i < sp.length; i++){
						arg += sp[i] + " "; 
					}
				}
			}
			else 
				this.cmd = "exit";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getCmd(){
		return this.cmd;
	}
	public String getArg(){
		return this.arg;
	}
	
}
