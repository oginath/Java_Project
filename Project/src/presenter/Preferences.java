package presenter;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;

import view.View;

// TODO: Auto-generated Javadoc
/**
 * 
 * The Class Preferences.
 * 
 */
@SuppressWarnings("serial")
public class Preferences implements Serializable {
	
	/** The port. */
	private int port;
	
	/** The address. */
	private String address;
	
	/** The ui. */
	private Class<? extends View> ui;
	
	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Sets the port.
	 *
	 * @param port the new port
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * Gets the ui.
	 *
	 * @return the ui
	 */
	public Class<? extends View> getUI() {
		return ui;
	}
	
	/**
	 * Sets the ui.
	 *
	 * @param ui the new ui
	 */
	public void setUI(Class<? extends View> ui) {
		this.ui = ui;
	}
	
	/**
	 * Gets the view.
	 *
	 * @return the view
	 */
	public View getView(){
		try {
			return this.ui.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
		return null;
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
