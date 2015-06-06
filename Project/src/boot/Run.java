package boot;

//
//	By: Or Ginath 205928161
//      


/// change 111
import java.util.Observable;

import model.MyModel;
import presenter.Preferences;
import presenter.Presenter;
import view.View;
import view.SWT.GUI;

/**
 * The Class Run.
 */
public class Run {

	public static void main(String[] args) {
		Preferences pref = new Preferences();
		pref.loadPreferences();
		MyModel m = new MyModel(pref.getSolverAlg(), pref.getGeneratorAlg(), pref.getNumOfThreads());
		View v = new GUI();
		Presenter p = new Presenter(v,m);
		m.addObserver(p);
		((Observable)(v)).addObserver(p);
	
		v.start();
		m.stop();
	}

}
