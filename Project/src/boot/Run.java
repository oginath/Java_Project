package boot;

//
//	By: Or Ginath 205928161
//      

import model.MyModel;
import presenter.Preferences;
import presenter.Presenter;
import view.CLIView;

/**
 * The Class Run.
 */
public class Run {

	public static void main(String[] args) {
		Preferences pref = new Preferences();
		pref.loadPreferences();
		MyModel m = new MyModel(pref.getSolverAlg(), pref.getGeneratorAlg(), pref.getNumOfThreads());
		CLIView v = new CLIView();
		Presenter p = new Presenter(v,m);
		m.addObserver(p);
		v.addObserver(p);
	
		v.start();
		m.stop();
	}

}
