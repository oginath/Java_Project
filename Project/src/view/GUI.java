package view;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Observer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
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

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import commands.Command;

/**
 * The Class GUI. Inherits Basic Window, 
 * This class contains the game board and offers 
 * a graphical user interface. 
 */
public class GUI extends BasicWindow implements View {

	/**  The Observers of this class. */
	private ArrayList<Observer> Observers;
	
	/** The Maze board, . */
	MazeBoard md;
	
	/** The toolbar menus. */
	Menu menuBar, fileMenu, helpMenu;
	
	/** The toolbar menus headers. */
	MenuItem fileHeader, helpHeader;
	
	/** The credits menus items. */
	MenuItem exitItem, helpItem, creditsItem;
	
	/** The buttons. */
	Button buttonNewGame, buttonLoadGame, buttonStopGame, buttonGetClue;
	
	/** The number of clues. Sets the number of clues the user can use in one game */
	int clues;
	
	/** The Commands. */
	private MyCommands cmds;
	
	/** The name of the current maze. */
	String mazeName;
	
	/**
	 * Instantiates the class.
	 */
	public GUI() {
		super();
		this.Observers = new ArrayList<Observer>();
		this.cmds = new MyCommands();
	}
	
	/**
	 * Instantiates the window, lays out the widgets and the game board.
	 */
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
		
		md = new MazeBoard(shell, SWT.BORDER | SWT.DOUBLE_BUFFERED);
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
				if(clues == 3)
					buttonGetClue.setEnabled(false);
				notifyObservers("solve maze " + mazeName + " " + md.getCharPosition() + " " + md.getGoalPosition());				
			}
		});
		
		creditsItem.addSelectionListener(new SelectionAdapter(){

			public void widgetSelected(SelectionEvent e) {
				Shell creditsShell = new Shell(shell);
				creditsShell.setSize(300, 270);
				creditsShell.open();
				
				if(md.getClip()!=null)
					if(md.getClip().isActive())
						md.getClip().stop();

				Clip clip = null;
				AudioInputStream ais = null;
				try {
					clip = AudioSystem.getClip();
					ais = AudioSystem.getAudioInputStream(new File(
							"resources/music/Congrats!.wav"));
					clip.open(ais);
				} catch (LineUnavailableException | UnsupportedAudioFileException| IOException error) {error.printStackTrace();}
				clip.start();
				
				creditsShell.setBackground(display.getSystemColor(SWT.COLOR_DARK_GRAY));

				Label label = new Label(creditsShell, SWT.CENTER);
				label.setSize(210, 35);
				label.setBackground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
				label.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
				label.setLocation(40, 20);
				FontData font = new FontData("Verdana", 12, SWT.ITALIC);
				label.setFont(new Font(null, font));
				label.setText("Credits");
				
				Label credits = new Label(creditsShell, SWT.NULL);
				credits.setSize(300, 100);
				credits.setBackground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
				credits.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
				credits.setLocation(14, 160);
				font = new FontData("Verdana", 8, SWT.BOLD);
				credits.setFont(new Font(null, font));
				credits.setText("Developer: Or Ginath\n"
						+ "Sprites: Spelunky, spriters-resource.com\n"
						+ "Music: Spelunky\n"
						+ "Special thanks: Eliahu Khalastchi\n"
						+ "Date: 16/06/2015");

				try {
				final Image image = new Image(null, new FileInputStream("resources/sprites/creditsSpelunky.png"));
				creditsShell.addPaintListener(new PaintListener(){

					@Override
					public void paintControl(PaintEvent e) {

						e.gc.drawImage(image, 0, 0, image.getBounds().width,image.getBounds().height, 
								70, 60,(int)(image.getBounds().width*0.7), (int)(image.getBounds().height*0.7));
					}
					
				});
				Label s1 = new Label(creditsShell, SWT.SEPARATOR | SWT.SHADOW_OUT
						| SWT.HORIZONTAL);
				s1.setBounds(68, 110, 100, 2);
				} catch (FileNotFoundException e1) {e1.printStackTrace();}

				
				shell.setEnabled(false);
				while(!creditsShell.isDisposed()){
					if(!creditsShell.getDisplay().readAndDispatch())
						creditsShell.getDisplay().sleep();
				}
			
				clip.stop();
				clip.close();
				shell.setEnabled(true);
			}
			
		});
		
		exitItem.addSelectionListener(new SelectionAdapter(){
			
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		
		helpItem.addSelectionListener(new SelectionAdapter(){

			public void widgetSelected(SelectionEvent e) {
				Shell helpShell = new Shell(shell);
				helpShell.setText("Instructions");
				helpShell.setSize(400, 200);
				helpShell.setLayout(new GridLayout(1, false));
				helpShell.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false , true, 1, 1));
				
				Label l = new Label(helpShell, SWT.CENTER);
				l.setText("Your goal is to get from the starting position to the Chest,\n"
						+ "centered at the smaller circle.\n"
						+ "The maze, and your starting and goal positions are \nrandomly generated at the start of every game.\n"
						+ "Throughout the game, you will collect Totems, \nwhich will expand your line of sight.\n"
						+ "You can also ask for clues, but be smart about it,\n as you only get 3 per game!");
				
				l.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true, 1, 1));
				helpShell.open();
				
				shell.setEnabled(false);
				while(!helpShell.isDisposed()){
					if(!helpShell.getDisplay().readAndDispatch())
						helpShell.getDisplay().sleep();
				}
				
				shell.setEnabled(true);
				shell.setFocus();
			}
		});
	}

	/**
	 * Opens the window, uses inherited method Run.
	 * @see @class BasicWindow
	 */
	@Override
	public void start() {
		this.run();
	}

	/**
	 * Sets a new Command.
	 * 
	 * @param cmdName The name of the command.
	 * @param cmd The Command.
	 */
	@Override
	public void setCommands(String cmdName, Command cmd) {
		this.cmds.setCommands(cmdName, cmd);
	}

	/**
	 * Returns the latest command issued by the user.
	 * 
	 * @return The command
	 */
	@Override
	public Command getUserCommand(String cmd) {	
		return this.cmds.selectCommand(cmd);
	}
	
	/**
	 * No use for this method in this Class.
	 */
	@Override
	public String getUserArg(String arg) {return null;}

	/**
	 * Starts the game with the maze that was recieved from the server.
	 */
	@Override
	public void displayMaze(Maze m, String s) {	
		if(m!= null){
			shell.setMenuBar(null);
			md.setPositions(s);
			md.setMaze(m);
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

	/**
	 * Inserts the solution that was recieved from the server to the game board.
	 */
	@Override
	public void displaySolution(Solution s) {		
		md.insertClue(s);
	}
	
	/**
	 * Displays a string describing an error.
	 */
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
	 *            adds the observer to this the observers list.
	 */
	@Override
	public void addObserver(Observer o) {
		this.Observers.add(o);
	}

	/**
	 * Delete Observer.
	 * 
	 * @param observer
	 *            Removes the observer from the observers list.
	 */
	@Override
	public void deleteObserver(Observer observer) {
		this.Observers.remove(observer);
	}

	/**
	 * Notify Observers.
	 * 
	 * notify the observers.
	 *
	 * @param obj An object to pass to the observers.
	 */
	@Override
	public void notifyObservers(Object obj) {
		for (Observer observer : Observers)
			observer.update(this, obj);
	}
}
