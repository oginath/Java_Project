package boot;

import model.Model;
import model.MyModel;
import presenter.Presenter;
import view.MyView;
import view.View;

public class Run {

	public static void main(String[] args) {
		Model m = new MyModel();
		View v = new MyView();
		@SuppressWarnings("unused")
		Presenter p = new Presenter(v,m);

		v.start();
		
	}

}
