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
		Preferences pref = new Preferences();
		pref.loadPreferences();
		ClientModel m = new ClientModel(pref.getPort(), pref.getAddress());
		View v = pref.getView();
		Presenter p = new Presenter(v,m);
		m.addObserver(p);
		v.addObserver(p);
	
		v.start();
		m.stop();
		
	}
}
