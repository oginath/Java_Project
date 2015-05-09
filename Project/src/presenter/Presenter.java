package presenter;

import observe.Observable;
import observe.Observer;
import model.Model;
import view.View;

public class Presenter implements Observer{
	
	Model m;
	View v;
	
	public Presenter(View v, Model m) {
		this.v = v;
		this.m = m;
		v.setCommands(new TestMVPCommand());
	}

	@Override
	public void update(Observable o, Object obj) {
		// TODO Auto-generated method stub
		
	}
	
}
