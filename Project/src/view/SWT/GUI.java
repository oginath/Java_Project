package view.SWT;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import view.View;
import view.CLI.MyCommands;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import commands.Command;

public class GUI extends BasicWindow implements View {

	/** The Observers of this class */
	private ArrayList<Observer> Observers;
	MazeBoard md;
	Maze maze;
	Menu menuBar, fileMenu, helpMenu;
	MenuItem fileHeader, helpHeader;
	MenuItem openFileItem, exitItem, helpItem, creditsItem;
	Button buttonNewGame, buttonLoadGame, buttonStopGame, buttonGetClue;
	int clues;
	private MyCommands cmds;
	String mazeName;
	
	public GUI() {
		super();
		this.Observers = new ArrayList<Observer>();
		this.cmds = new MyCommands();
	}
	
	@Override
	public void initWidgets() {
	
		this.shell.setLayout(new GridLayout(2, false));
		shell.setBackground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
		shell.setText("the Maze Spelunker");
		
		menuBar = new Menu(shell, SWT.BAR);
		
		fileMenu = new Menu(shell, SWT.DROP_DOWN);
		
		fileHeader = new MenuItem(menuBar, SWT.CASCADE);
		fileHeader.setText("&File");
		fileHeader.setMenu(fileMenu);
		
		openFileItem = new MenuItem(fileMenu, SWT.PUSH);
		openFileItem.setText("&Open File");
		
		exitItem = new MenuItem(fileMenu, SWT.PUSH);
		exitItem.setText("&Exit");
		
		helpMenu = new Menu(shell, SWT.DROP_DOWN);
		
		helpHeader = new MenuItem(menuBar, SWT.CASCADE);
		helpHeader.setText("&Help");
		helpHeader.setMenu(helpMenu);
		
		helpItem = new MenuItem(helpMenu, SWT.PUSH);
		helpItem.setText("&Instructions");
		
		creditsItem = new MenuItem(helpMenu, SWT.PUSH);
		creditsItem.setText("&Credits");
		creditsItem.setEnabled(false);

		
		shell.setMenuBar(menuBar);

		
		buttonNewGame = new Button(this.shell, SWT.PUSH | SWT.BUTTON1);
		buttonNewGame.setText("New Game");
		buttonNewGame.setLayoutData(new GridData(GridData.FILL, SWT.CENTER, false, false, 1, 1));
		
		md = new MazeBoard(shell, SWT.BORDER | SWT.DOUBLE_BUFFERED, null);
		md.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 4));
		
		buttonLoadGame = new Button(this.shell, SWT.PUSH);
		buttonLoadGame.setText("Load Game");
		buttonLoadGame.setLayoutData(new GridData(GridData.FILL, SWT.CENTER, false, false, 1, 1));
		
		buttonStopGame = new Button(this.shell, SWT.PUSH);
		buttonStopGame.setText("Stop Game");
		buttonStopGame.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		buttonStopGame.setVisible(false);
		buttonStopGame.setEnabled(false);
		

		buttonGetClue = new Button(this.shell, SWT.PUSH);
		buttonGetClue.setText("Clue");
		buttonGetClue.setLayoutData(new GridData(SWT.FILL , SWT.TOP, false, false, 1,1));
		buttonGetClue.setVisible(false);
		buttonGetClue.setEnabled(false);
		
		buttonNewGame.addSelectionListener(new SelectionAdapter(){

			Combo c1, c2;
			
			public void widgetSelected(SelectionEvent event) {		
												
				Shell newGameShell = new Shell(shell);
				newGameShell.setSize(250, 120);
				GridLayout gd = new GridLayout();
				gd.numColumns = 4;
				gd.makeColumnsEqualWidth = false;
				newGameShell.setLayout(gd);

				Button b = new Button(newGameShell, SWT.CHECK);
				b.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 1 ,1 ));
				Label size = new Label(newGameShell, SWT.NULL);
				size.setText("Size: ");
				size.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 1, 1));
				
				String[] items = new String[36];
				for(int i = 0; i < 36; i++)
					items[i] = String.valueOf(i+15);
				c1 =  new Combo(newGameShell, SWT.READ_ONLY);
				c2 =  new Combo(newGameShell, SWT.READ_ONLY);
				c1.setItems(items);
				c1.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 1,1));
				c1.select(17);
				c1.setEnabled(false);
				c2.setItems(retrieveItems());
				c2.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 1,1));
				c2.select(4);
				c2.setEnabled(false);
				
				Label hidden1 = new Label(newGameShell, SWT.NULL);
				hidden1.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 1, 1));
				Label name = new Label(newGameShell, SWT.NULL);
				name.setText("Name: ");
				name.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 1, 1));
				Text t = new Text(newGameShell, SWT.BORDER | SWT.SINGLE);
				t.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
				
				Button okButton = new Button(newGameShell, SWT.PUSH);
				GridData okGD = new GridData(GridData.FILL, GridData.CENTER, false, false, 4, 1);
				okButton.setLayoutData(okGD);
				okButton.setText("&OK");
				
				newGameShell.setText("New Game");
				newGameShell.open();
			
				b.addSelectionListener(new SelectionAdapter(){

					public void widgetSelected(SelectionEvent e) {
						if(!c1.isEnabled()){
							c1.setEnabled(true);
							c2.setEnabled(true);
						}
						else{
							c1.setEnabled(false);
							c2.setEnabled(false);
						}
					}
				});
				
				okButton.addSelectionListener(new SelectionAdapter(){
					
					public void widgetSelected(SelectionEvent e) {
						
						if(t.getCharCount() == 0)
							return;
						Point mazeSize = new Point(Integer.parseInt(c1.getItem(c1.getSelectionIndex())), 
								Integer.parseInt(c2.getItem(c2.getSelectionIndex())));
						try {
							mazeName = URLEncoder.encode(t.getText(), "UTF-8");
						} catch (UnsupportedEncodingException e1) {e1.printStackTrace();}
												
						newGameShell.dispose();
						notifyObservers("generate maze " + mazeName + " " + mazeSize.x + " " + mazeSize.y);
					}
				});
				
				c1.addSelectionListener(new SelectionAdapter(){
					public void widgetSelected(SelectionEvent e) {
						c2.setItems(retrieveItems());
						c2.select(4);
					}
				});
				
				shell.setEnabled(false);
				while(!newGameShell.isDisposed()){
					if(!newGameShell.getDisplay().readAndDispatch())
						newGameShell.getDisplay().sleep();
				}
				
				shell.setEnabled(true);
				shell.setFocus();
			}
			
			String[] retrieveItems(){
				
				String[] items = new String[9];
				
				for(int i = -4; i < 5; i++){
					items[i+4] = String.valueOf(i + Integer.valueOf(c1.getItem(c1.getSelectionIndex())));
				}
				
				return items;
			}
		});
		
		buttonLoadGame.addSelectionListener(new SelectionAdapter(){
			
			public void widgetSelected(SelectionEvent event){
				
				Shell loadGameShell = new Shell(shell);
				loadGameShell.setSize(250, 90);
				GridLayout gd = new GridLayout();
				gd.numColumns = 2;
				gd.makeColumnsEqualWidth = false;
				loadGameShell.setLayout(gd);
				
				Label name = new Label(loadGameShell, SWT.NULL);
				name.setText("Name: ");
				name.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 1, 1));
				Text t = new Text(loadGameShell, SWT.BORDER | SWT.SINGLE);
				t.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 1, 1));
				
				Button okButton = new Button(loadGameShell, SWT.PUSH);
				GridData okGD = new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1);
				okButton.setLayoutData(okGD);
				okButton.setText("&OK");
				
				loadGameShell.setText("Load Game");
				loadGameShell.open();
				
				
				okButton.addSelectionListener(new SelectionAdapter(){
					
					public void widgetSelected(SelectionEvent e){
						try {
							mazeName = URLEncoder.encode(t.getText(), "UTF-8");
						} catch (UnsupportedEncodingException e1) {e1.printStackTrace();}
						loadGameShell.dispose();
						notifyObservers("display maze " + mazeName);
					}
				});				
				
				shell.setEnabled(false);
				while(!loadGameShell.isDisposed()){
					if(!loadGameShell.getDisplay().readAndDispatch())
						loadGameShell.getDisplay().sleep();
				}
				
				shell.setEnabled(true);
				shell.setFocus();
			}
		});
		
		buttonStopGame.addSelectionListener(new SelectionAdapter(){

			public void widgetSelected(SelectionEvent event) {
				if(!md.isStopped()){
					md.stop();
					md.TitleScreen();
				}
				if(md.isWon())
					creditsItem.setEnabled(true);
				shell.setMenuBar(menuBar);
				buttonNewGame.setEnabled(true);
				buttonLoadGame.setEnabled(true);
				buttonStopGame.setVisible(false);
				buttonStopGame.setEnabled(false);
				buttonGetClue.setVisible(false);
				buttonGetClue.setEnabled(false);
			}
		});	
		
		buttonGetClue.addSelectionListener(new SelectionAdapter(){
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(md.isWon()){
					buttonGetClue.setEnabled(false);
					return;
				}
				clues++;
				//if(clues == 3)
					//buttonGetClue.setEnabled(false);
				notifyObservers("solve maze " + mazeName + " " + md.getCharPosistion() + " " + md.getGoalPosistion());				
			}
		});
		
		creditsItem.addSelectionListener(new SelectionAdapter(){

			public void widgetSelected(SelectionEvent e) {
				Shell creditsShell = new Shell(shell);
				creditsShell.setSize(300, 200);
				creditsShell.open();
				
				if(md.getClip()!=null)
					if(md.getClip().isActive())
						md.getClip().stop();
				
				shell.setEnabled(false);
				while(!creditsShell.isDisposed()){
					if(!creditsShell.getDisplay().readAndDispatch())
						creditsShell.getDisplay().sleep();
				}
				
				shell.setEnabled(true);
				
			}
			
		});
		
		exitItem.addSelectionListener(new SelectionAdapter(){
			
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
	}

	@Override
	public void start() {
		this.run();
		
	}

	@Override
	public void setCommands(String cmdName, Command cmd) {
		this.cmds.setCommands(cmdName, cmd);
	}

	@Override
	public Command getUserCommand(String cmd) {	
		return this.cmds.selectCommand(cmd);
	}

	@Override
	public String getUserArg(String arg) {
		return null;
	}

	@Override
	public void displayMaze(Maze m, String s) {	
		this.maze = m;
		if(m!= null){
			shell.setMenuBar(null);
			md.setPositions(s);
			md.setMaze(maze);
			md.start();
			buttonNewGame.setEnabled(false);
			buttonLoadGame.setEnabled(false);
			buttonStopGame.setVisible(true);
			buttonStopGame.setEnabled(true);
			buttonGetClue.setVisible(true);
			buttonGetClue.setEnabled(true);
			clues = 0;
		}
	}

	@Override
	public void displaySolution(Solution s) {		
		md.insertClue(s);
	}
	
	@Override
	public void displayError(String err) {
		Shell errShell = new Shell(shell);
		errShell.setText("ERROR");
		errShell.setSize(200, 100);
		errShell.setLayout(new GridLayout(1,false));
		errShell.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false , true, 1, 1));
		Label l = new Label(errShell, SWT.NULL);
		l.setText(err);
		l.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		errShell.open();
		
		shell.setEnabled(false);
		while(!errShell.isDisposed()){
			if(!errShell.getDisplay().readAndDispatch())
				errShell.getDisplay().sleep();
		}
		
		shell.setEnabled(true);
	}
	
	/**
	 * Add Observer.
	 * 
	 * @param o
	 *            adds an observer to this the observers list.
	 */
	@Override
	public void addObserver(Observer o) {
		this.Observers.add(o);
	}

	/**
	 * Delete Observer.
	 * 
	 * @param o
	 *            Removes an observer from the observers list.
	 */
	@Override
	public void deleteObserver(Observer o) {
		this.Observers.remove(o);
	}

	/**
	 * Notify Observers.
	 *
	 * notify the observers.
	 */
	@Override
	public void notifyObservers(Object obj) {
		for (Observer observer : Observers)
			observer.update(this, obj);
	}


}
