package boot;

//
//	By: Or Ginath 205928161
//      
import model.ClientModel;
import presenter.Preferences;
import presenter.Presenter;
import view.View;

public class Run {

	public static void main(String[] args) {
		try {
		Preferences pref = new Preferences();
		pref.loadPreferences();
		ClientModel m = new ClientModel(pref.getPort(), pref.getAddress());
		View v = pref.getUI().newInstance();
		Presenter p = new Presenter(v,m);
		m.addObserver(p);
		v.addObserver(p);
	
		v.start();
		m.stop();
		
		} catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
	}

}

//TODO: preferences for both sides, load maze (sort out name situation), congrats screen