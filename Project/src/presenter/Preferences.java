package presenter;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;

import view.View;

/**
 * 
 * The Class Preferences.
 * 
 */
@SuppressWarnings("serial")
public class Preferences implements Serializable {
	
	private int port;
	
	private String address;
	
	private Class<? extends View> ui;
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public Class<? extends View> getUI() {
		return ui;
	}
	
	public void setUI(Class<? extends View> ui) {
		this.ui = ui;
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
			
			this.port = p.getPort();
			this.address = p.getAddress();
			this.setUI(p.getUI());
			
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
