package view.SWT;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

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
	Button buttonNewGame, buttonStopGame, buttonGetClue;
	int clues;
	private MyCommands cmds;
	Integer hash;

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
		buttonNewGame.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false, 1, 1));
		
		md = new MazeBoard(shell, SWT.BORDER | SWT.DOUBLE_BUFFERED, null);
		md.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
		
		buttonStopGame = new Button(this.shell, SWT.PUSH);
		buttonStopGame.setText("Stop Game");
		buttonStopGame.setLayoutData(new GridData(SWT.BEGINNING, SWT.TOP, false, false, 1, 1));
		buttonStopGame.setVisible(false);
		buttonStopGame.setEnabled(false);
		

		buttonGetClue = new Button(this.shell, SWT.PUSH);
		buttonGetClue.setText("Clue");
		buttonGetClue.setLayoutData(new GridData(SWT.FILL , SWT.TOP, false, false, 1,1));
		buttonGetClue.setVisible(false);
		buttonGetClue.setEnabled(false);
		
		buttonNewGame.addSelectionListener(new SelectionAdapter(){

			public void widgetSelected(SelectionEvent event) {				
				hash = new Random().nextInt();
				hash = hash.hashCode();
				
				notifyObservers("generate maze " + hash + " 32 32");
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
				if(clues == 3)
					buttonGetClue.setEnabled(false);
				
				notifyObservers("solve maze " + hash + " " + md.getCharPosistion() + " " + md.getGoalPosistion());				
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
	public void displayMaze(Maze m) {	
		this.maze = m;
		if(m!= null){
			shell.setMenuBar(null);
			md.setMaze(maze);
			md.start();
			buttonNewGame.setEnabled(false);
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
