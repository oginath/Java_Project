package boot;

//
//	By: Or Ginath & Dor hivert
//      205928161    301680948

import model.MyModel;
import presenter.Preferences;
import presenter.Presenter;
import view.MyView;

// TODO: Auto-generated Javadoc
/**
 * The Class Run.
 */
public class Run {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Preferences pref = new Preferences();
		pref.loadPreferences();
		MyModel m = new MyModel(pref.getSolverAlg(), pref.getGeneratorAlg(), pref.getNumOfThreads());
		MyView v = new MyView();
		Presenter p = new Presenter(v,m);
		m.addObserver(p);
		v.addObserver(p);
	
		v.start();
		m.stop();
	}

}
