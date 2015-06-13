package view.SWT;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import algorithms.mazeGenerators.Cell;
import algorithms.mazeGenerators.Directions;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

public class MazeBoard extends Canvas {

	private GameCharacter gameChar;
	private int w, h;
	private boolean flag = false;
	private boolean stopped, won;
	private int sX, sY;
	private int gX, gY;
	private Region region;
	private int radius;
	private Shell shell;
	private Image mazeTile, chest, totem;
	private Maze mazeData;
	private KeyAdapter kAdapter;
	private PaintListener paintListener;
	private Listener moveListener, resizeListener, mouseListener, titleListener;
	private ShellAdapter shellAdapter;
	private Label label, s1, s2;
	private Clip clip, cl;
	private long startTime, endTime;
	private ArrayList<Point> totems, clue;
	int totcount, music;
	HashMap<Point, Point> map;
	HashMap<Point, String> clueMap;

	public MazeBoard(Composite parent, int style, Maze mazeData) {

		super(parent, style);
		this.mazeData = mazeData;

		chest = null;
		mazeTile = null;
		try {
			chest = new Image(null, new FileInputStream(
					"resources/sprites/chest.png"));
			totem = new Image(null, new FileInputStream(
					"resources/sprites/totem.png"));
			mazeTile = new Image(null, new FileInputStream(
					"resources/patterns/mazeTile.png"));
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}

		TitleScreen();
	}

	void start() {

		won = false;
		stopped = false;
		

		startTime = System.currentTimeMillis();

		removeListener(SWT.Resize, titleListener);

		if (clip.isActive())
			clip.stop();

		clip = null;
		AudioInputStream ais = null;
		try {
			clip = AudioSystem.getClip();
			ais = AudioSystem.getAudioInputStream(new File(
					"resources/music/the Maze.wav"));
			clip.open(ais);
		} catch (LineUnavailableException | UnsupportedAudioFileException
				| IOException e) {
			e.printStackTrace();
		}
		clip.start();

		label.dispose();
		s1.dispose();
		s2.dispose();

		try {
			setBackgroundImage(new Image(null, new FileInputStream(
					"resources/patterns/mazePattern.png")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		map = new HashMap<Point, Point>();
//		Searchable s = new SearchableMaze(mazeData, true);
//		String[] start = s.getStartState().getState().split(" ");
//		sX = Integer.parseInt(start[0]);
//		sY = Integer.parseInt(start[1]);
//		
//		String[] goal = s.getGoalState().getState().split(" ");
//		gX = Integer.parseInt(goal[0]);
//		gY = Integer.parseInt(goal[1]);

		int counter = 0;
		if (mazeData.getCols() * mazeData.getRows() > 250) {
			counter = (int) Math.ceil(mazeData.getCols() * mazeData.getRows()
					/ 250);
		} else
			counter = 1;

		Random rand = new Random();
		totems = new ArrayList<Point>();
		for (int i = 0; i < counter; i++) {
			Point newpos;
			do {
				newpos = new Point(rand.nextInt(mazeData.getCols()),
						rand.nextInt(mazeData.getRows()));
			} while ((newpos.x != sX && newpos.y != sY)
					&& (newpos.x != gX && newpos.y != gY));
			totems.add(newpos);
		}

		clue = null;
		music = 0;
		totcount = 0;

		paintListener = new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				if (!clip.isActive()) {
					File file = null;
					switch (music) {
					case 0:
						file = new File("resources/music/Mines.wav");
						break;
					case 1:
						file = new File("resources/music/Jungle.wav");
						break;
					case 2:
						file = new File("resources/music/Temple.wav");
						break;
					case 3:
						file = new File("resources/music/Castle.wav");
						break;
					case 4:
						file = new File("resources/music/the Maze.wav");
						break;
					}
					music++;
					if (music == 5)
						music = 0;

					clip = null;
					AudioInputStream ais = null;
					try {
						clip = AudioSystem.getClip();
						ais = AudioSystem.getAudioInputStream(file);
						clip.open(ais);
					} catch (LineUnavailableException
							| UnsupportedAudioFileException | IOException error) {
						error.printStackTrace();
					}
					clip.start();
				}

				e.gc.setAntialias(1);
				e.gc.setAdvanced(true);

				int width = getSize().x;
				int height = getSize().y;

				w = width / mazeData.getMatrix()[0].length;
				h = height / mazeData.getMatrix().length;

				if (!flag) {
					gameChar = new GameCharacter(sX * w, sY * h);
					gameChar.dir = Directions.DOWN;
					flag = true;
				}

				gameChar.x = sX * w;
				gameChar.y = sY * h;
		
				int r = (int) ((getBounds().width + getBounds().height) / 35 - ((getBounds().width + getBounds().height) / 35 * 0.2));

				for (int j = 0; j < mazeData.getCols(); j++) {
					// e.gc.drawLine(j * w, 0, (j + 1) * w, 0);
					if (Math.abs(j - sX)*w < radius * 2
							&& Math.abs(0 - sY)*h < radius * 2
							|| Math.abs(j - gX)*w < r * 2
							&& Math.abs(0 - gY)*h < r * 2 || won)
						e.gc.drawImage(mazeTile, 0, 0,
								mazeTile.getBounds().width,
								mazeTile.getBounds().height, j * w, 0, w, h / 3);
				}

				for (int i = 0; i < mazeData.getRows(); i++) {
					// e.gc.drawLine(0, i * h, 0, (i + 1) * h);
					if (Math.abs(0 - sX)*w < radius * 2
							&& Math.abs(i - sY)*h < radius * 2
							|| Math.abs(0 - gX)*w < r * 2
							&& Math.abs(i - gY)*h < r * 2 || won)
						e.gc.drawImage(mazeTile, 0, 0,
								mazeTile.getBounds().width,
								mazeTile.getBounds().height, 0, i * h, w / 5, h);
					for (int j = 0; j < mazeData.getCols(); j++) {
						int x = j * w;
						int y = i * h;

						Point xy = new Point(x, y);
						Point ij = new Point(i, j);

						map.put(xy, ij);

						Cell cell = mazeData.getMatrix()[i][j];
						if ((Math.abs(j - sX)*w < radius * 2 && Math.abs(i
								- sY)*h < radius * 2)
								|| Math.abs(j - gX)*w < r * 2
								&& Math.abs(i - gY)*h < r * 2 || won) {
							if (cell.isbBorder()) {
								// e.gc.drawLine(x, y + h, (j + 1) * w, y + h);

								e.gc.drawImage(mazeTile, 0, 0,
										mazeTile.getBounds().width,
										mazeTile.getBounds().height, x, y + h,
										w, h / 3);
							}
							if (cell.isrBorder()) {
								// e.gc.drawLine(x + w, y, x + w, (i + 1) * h);

								e.gc.drawImage(mazeTile, 0, 0,
										mazeTile.getBounds().width,
										mazeTile.getBounds().height, (x + w)
												- w / 5, y + (h / 3), w / 5, h);
							}
						}
					}
				}

				// gameChar.paint(e, (int) (w - (w * 0.1)), h);
				gameChar.paint(e.gc, w, h);

				e.gc.drawImage(chest, 0, 0, chest.getBounds().width,
						chest.getBounds().height, gX * w, gY * h + h / 4,
						(int) (w - (w * 0.2)), (int) (h - (h * 0.2)));

				for (int i = 0; i < totems.size(); i++) {
					Point cur = totems.get(i);
					if ((Math.abs(cur.x - sX)*w < radius * 2 && Math
							.abs(cur.y - sY)*h < radius * 2)
							|| Math.abs(cur.x - gX)*w < r * 2
							&& Math.abs(cur.y - gY)*h < r * 2 || won)
						e.gc.drawImage(totem, 0, 0, totem.getBounds().width,
								totem.getBounds().height, cur.x * w, cur.y * h
										+ h / 4, (int) (w - (w * 0.2)), h);
				}

				if (clue != null) {
					e.gc.setLineWidth(2);
					e.gc.setForeground(getDisplay().getSystemColor(
							SWT.COLOR_WHITE));
					for (int i = 0; i < clue.size() - 1; i++) {
						Point cur = clue.get(i);

						int x = cur.x * w;
						int y = cur.y * h;

						if (!clueMap.containsKey(cur))
							continue;

						switch (clueMap.get(cur)) {
						case "v":
							e.gc.drawLine(x + (w / 2), y + (int) (h * 0.7), x
									+ (w / 2), (clue.get(i + 1).y) * h
									+ (int) (h * 0.7));
							break;

						case "h":
							e.gc.drawLine(x + (w / 2), y + (int) (h * 0.7),
									(clue.get(i + 1).x) * w + (w / 2), y
											+ (int) (h * 0.7));
							break;
						}
					}
				}

				changeRegion(gameChar.x, gameChar.y);
			}
		};

		kAdapter = new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				
				if (ke.keyCode == SWT.ARROW_UP) 
					moveChar(Directions.UP);

				else if (ke.keyCode == SWT.ARROW_DOWN) 
					moveChar(Directions.DOWN);

				else if (ke.keyCode == SWT.ARROW_LEFT) 
					moveChar(Directions.LEFT);

				else if (ke.keyCode == SWT.ARROW_RIGHT) 
					moveChar(Directions.RIGHT);
					
			}
		};

		mouseListener = new Listener(){
			Point p;
			boolean marker;
			@Override
			public void handleEvent(Event event) {
				switch (event.type){
				case SWT.MouseDown:
					if (Math.abs(event.x - (sX * w)-(w/2.5)) < 5 && Math.abs(event.y - (sY * h)-5) < 15){
						p = new Point(event.x, event.y);
						marker = true;
					}
					break;
			
				case SWT.MouseMove:
					if(marker){
						if(event.x < p.x - w){
							p.x = event.x;
							p.y = event.y;
							moveChar(Directions.LEFT);
						}
						
						else if(event.x > p.x + w){
							p.x = event.x;
							p.y = event.y;
							moveChar(Directions.RIGHT);
						}
						
						else if(event.y < p.y - h){
							p.x = event.x;
							p.y = event.y;
							moveChar(Directions.UP);
						}
						else if(event.y > p.y + h){
							p.x = event.x;
							p.y = event.y;
							moveChar(Directions.DOWN);
						}
					}
					break;
					
				case SWT.MouseUp:
					marker = false;
					break;
					
				}
			}
		};
		
		moveListener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (shell.isDisposed())
					return;
				Point absoluteLocation = toDisplay(getShell().getClientArea().x
						- getClientArea().x, getShell().getClientArea().y
						- getClientArea().y);
				shell.setLocation(absoluteLocation);
				if (shell.getMaximized()) {
					getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							handleEvent(null);
						}
					});
				}
			}
		};

		resizeListener = new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (shell.isDisposed())
					return;
				int width = getBounds().width - 3;
				int height = getBounds().height - 3;

				Point p = new Point(width, height);
				shell.setSize(p);
				if (width >= 0 && height >= 0) {
					if (gameChar == null)
						changeRegion(0, 0);
					else
						changeRegion(gameChar.x, gameChar.y);
				}
			}

		};

		shellAdapter = new ShellAdapter() {

			@Override
			public void shellActivated(ShellEvent arg0) {
				if (shell.isDisposed())
					return;
				shell.setVisible(true);

			}

			@Override
			public void shellDeactivated(ShellEvent arg0) {
				if (shell.isDisposed())
					return;
				shell.setVisible(false);

			}
		};

		shell = new Shell(getShell(), SWT.NO_TRIM | SWT.ON_TOP);
		shell.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLACK));

		shell.addShellListener(new ShellAdapter() {

			@Override
			public void shellActivated(ShellEvent arg0) {
				if (shell.isDisposed())
					return;
				shell.setVisible(true);

			}
		});
		shell.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				getShell().setActive();
				getShell().setFocus();
			}

		});
		addPaintListener(paintListener);
		addKeyListener(kAdapter);
		addListener(SWT.MouseDown, mouseListener);
		addListener(SWT.MouseMove, mouseListener);
		addListener(SWT.MouseUp, mouseListener);
		getShell().addShellListener(shellAdapter);
		getShell().addListener(SWT.Resize, moveListener);
		getShell().addListener(SWT.Move, moveListener);
		getShell().addListener(SWT.Paint, resizeListener);
		shell.addListener(SWT.Move, moveListener);

		shell.open();
		getShell().setFocus();
	}

	@SuppressWarnings("incomplete-switch")
	private void moveChar(Directions dir){
		Point xy;
		Point ij = null;
		boolean mark = false;
		
		switch(dir){
		case UP:
			gameChar.dir = Directions.UP;
			xy = new Point(gameChar.x, gameChar.y);

			ij = map.get(xy);

			if (ij != null) {
				if (mazeData.isCellInReach(ij.y, ij.x, Directions.UP)) {
					gameChar.y -= h;
					sX = ij.y;
					sY = ij.x - 1;
					mark = true;
				}
			}
			break;
			
		case DOWN:
			gameChar.dir = Directions.DOWN;
			xy = new Point(gameChar.x, gameChar.y);

			ij = map.get(xy);
			if (ij != null) {
				if (mazeData.isCellInReach(ij.y, ij.x, Directions.DOWN)) {
					gameChar.y += h;
					sX = ij.y;
					sY = ij.x + 1;
					mark = true;
				}
			}
			break;
			
		case LEFT:
			gameChar.dir = Directions.LEFT;
			xy = new Point(gameChar.x, gameChar.y);

			ij = map.get(xy);
			if (ij != null) {
				if (mazeData.isCellInReach(ij.y, ij.x, Directions.LEFT)) {
					gameChar.x -= w;
					sX = ij.y - 1;
					sY = ij.x;
					mark = true;
				}
			}
			break;
		case RIGHT:
			gameChar.dir = Directions.RIGHT;
			xy = new Point(gameChar.x, gameChar.y);

			ij = map.get(xy);
			if (ij != null) {
				if (mazeData
						.isCellInReach(ij.y, ij.x, Directions.RIGHT)) {
					gameChar.x += w;
					sX = ij.y + 1;
					sY = ij.x;
					mark = true;
				}
			}
			break;
		}
		if (sX == gX && sY == gY) {
			congrats();
			TitleScreen();
			return;
		}
		
		Point cur;
		for (int i = 0; i < totems.size(); i++) {
			cur = totems.get(i);
			if (sX == cur.x && sY == cur.y) {
				cl = null;
				AudioInputStream ais = null;
				try {
					cl = AudioSystem.getClip();
					ais = AudioSystem.getAudioInputStream(new File(
							"resources/sfx/Totem.wav"));
					cl.open(ais);
				} catch (LineUnavailableException
						| UnsupportedAudioFileException | IOException e) {
					e.printStackTrace();
				}
				cl.start();
				totems.remove(i);
				totcount++;
				break;
			}
		}

		if (clueMap != null) {
			cur = new Point(sX, sY);
			if (clueMap.containsKey(cur))
				clueMap.remove(cur);
		}
		
		if (!mark) {
			if (cl != null)
				if (cl.isActive())
					cl.stop();
			cl = null;
			AudioInputStream ais = null;
			try {
				cl = AudioSystem.getClip();
				ais = AudioSystem.getAudioInputStream(new File(
						"resources/sfx/collision.wav"));
				cl.open(ais);
			} catch (LineUnavailableException
					| UnsupportedAudioFileException | IOException e) {
				e.printStackTrace();
			}
			cl.start();
		}
		redraw();
	}
	
	private void changeRegion(int x, int y) {
		if (!shell.isDisposed()) {

			radius = (getBounds().width + getBounds().height) / 35;
			for (int i = 0; i < totcount; i++)
				radius += radius * 0.40;

			region = new Region();
			region.add(new Rectangle(0, 0, getBounds().width - 5,
					getBounds().height - 5));

			region.subtract(circle(radius, x + w / 3, y + h / 3));

			int r = (int) ((getBounds().width + getBounds().height) / 35 - ((getBounds().width + getBounds().height) / 35 * 0.2));
			region.subtract(circle(r, gX * w + w / 3, gY * h + h / 3));

			shell.setRegion(region);
		}

	}

	private void congrats() {
		won = true;
		redraw();
		endTime = System.currentTimeMillis();
		Shell congratsShell = new Shell(getShell());
		int width = getShell().getBounds().width;
		int height = getShell().getBounds().height;
		congratsShell.setSize(200, 100);
		congratsShell.setLocation(toDisplay(width - width / 2, height - height
				/ 2));

		long time = endTime - startTime;
		SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date date = new Date(time);

		Label label = new Label(congratsShell, SWT.CENTER);

		label.setAlignment(SWT.CENTER);
		label.setText("\nCongratulations!\n" + "You won!\n"
				+ "Elpased Time:     " + dateFormat.format(date));

		label.setBounds(congratsShell.getClientArea());

		if (clip.isActive())
			clip.stop();

		clip = null;
		AudioInputStream ais = null;
		try {
			clip = AudioSystem.getClip();
			ais = AudioSystem.getAudioInputStream(new File(
					"resources/sfx/dm_winner.wav"));
			clip.open(ais);
		} catch (LineUnavailableException | UnsupportedAudioFileException
				| IOException e) {
			e.printStackTrace();
		}
		clip.start();
		congratsShell.open();
		getShell().setEnabled(false);
		while (!congratsShell.isDisposed()) {
			if (!congratsShell.getDisplay().readAndDispatch())
				congratsShell.getDisplay().sleep();
		}

		getShell().setEnabled(true);
		stop();
	}

	void stop() {
		if (!shell.isDisposed())
			shell.dispose();
		removePaintListener(paintListener);
		removeKeyListener(kAdapter);
		removeListener(SWT.MouseDown, mouseListener);
		removeListener(SWT.MouseMove, mouseListener);
		removeListener(SWT.MouseUp, mouseListener);
		getShell().removeShellListener(shellAdapter);
		getShell().removeListener(SWT.Resize, moveListener);
		getShell().removeListener(SWT.Move, moveListener);
		getShell().removeListener(SWT.Paint, resizeListener);

		setBackground(new Color(null, 240, 240, 240));
		drawBackground(new GC(this), 0, 0, getBounds().width,
				getBounds().height);
		stopped = true;
	}

	void TitleScreen() {

		try {
			final Image title = new Image(null, new FileInputStream(
					"resources/sprites/titleSpelunky.png"));
			final Image chest = new Image(null, new FileInputStream(
					"resources/sprites/openChest.png"));
			final Image backg = new Image(null, new FileInputStream(
					"resources/images/titleBackground.png"));

			setBackgroundImage(backg);
			setBackgroundMode(SWT.INHERIT_FORCE);

			label = new Label(this, SWT.CENTER);
			label.setSize(410, 20);
			label.setForeground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
			label.setLocation(0, 100);
			FontData font = new FontData("Verdana", 18, SWT.ITALIC);
			label.setFont(new Font(null, font));
			label.setText("the Maze Spelunker");

			s1 = new Label(this, SWT.SEPARATOR | SWT.SHADOW_OUT
					| SWT.HORIZONTAL);
			s2 = new Label(this, SWT.SEPARATOR | SWT.SHADOW_OUT
					| SWT.HORIZONTAL);

			titleListener = new Listener() {

				@Override
				public void handleEvent(Event arg0) {
					if (!(getSize().x / 3 < 122)) {
						if (getSize().x >= 705)
							label.setSize(getSize().x / 3, 20);
						else
							label.setSize(getSize().x / 3, 55);
						label.setLocation(getSize().x / 3, 100);
						int x = label.getLocation().x;
						s1.setBounds(x, 400, label.getSize().x, 5);
						s2.setBounds((int) (label.getLocation().x + label
								.getLocation().x * 0.7) + 1, 183 + chest
								.getBounds().height / 2,
								chest.getBounds().width / 2, 5);
						;

					} else {
						label.setSize(122, 55);
						label.setLocation(122, 100);
					}
				}

			};
			addListener(SWT.Resize, titleListener);

			paintListener = new PaintListener() {
				@Override
				public void paintControl(PaintEvent e) {
					if (label.isDisposed())
						return;

					e.gc.drawImage(backg, 0, 0, backg.getBounds().width,
							backg.getBounds().height, 0, 0, getSize().x,
							getSize().y);

					if (!(getSize().x / 3 < 122)) {
						if (getSize().x >= 705)
							label.setSize(getSize().x / 3, 28);
						else
							label.setSize(getSize().x / 3, 55);
						label.setLocation(getSize().x / 3, 100);
						int x = label.getLocation().x;
						s1.setBounds(x, 400, label.getSize().x, 5);

						s2.setBounds((int) (label.getLocation().x + label
								.getLocation().x * 0.7) + 1, 183 + chest
								.getBounds().height / 2,
								chest.getBounds().width / 2, 5);
						;

					} else {
						label.setSize(122, 55);
						label.setLocation(122, 100);
					}

					e.gc.drawImage(title, 0, 0, title.getBounds().width,
							title.getBounds().height, label.getLocation().x
									+ label.getLocation().x / 8, 330,
							title.getBounds().width, title.getBounds().height);
					e.gc.drawImage(
							chest,
							0,
							0,
							chest.getBounds().width,
							chest.getBounds().height,
							(int) (label.getLocation().x + label.getLocation().x * 0.7),
							180, chest.getBounds().width / 2,
							chest.getBounds().height / 2);

				}
			};

			addPaintListener(paintListener);
			if (clip != null)
				if (clip.isActive())
					clip.stop();
			clip = null;
			AudioInputStream ais = null;
			try {
				clip = AudioSystem.getClip();
				ais = AudioSystem.getAudioInputStream(new File(
						"resources/music/Title.wav"));
				clip.open(ais);
			} catch (LineUnavailableException | UnsupportedAudioFileException
					| IOException e) {
				e.printStackTrace();
			}
			clip.start();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	void insertClue(Solution sol) {
		if (won)
			return;
		List<String> c = sol.getSoList();
		clue = new ArrayList<Point>();
		clueMap = new HashMap<Point, String>();

		for(int i = 0; i < c.size(); i++){
			if(c.get(i).equals(sX + " " + sY)){
				for(int j = 0; j < i + 1; i--, c.remove(j));
				break;
			}
		}
		
		for (int i = c.size(); i > 12; i--, c.remove(i));
		for (int i = 0; i < c.size(); i++) {
			String[] sp = c.get(i).split(" ");
			Point xy = new Point(Integer.parseInt(sp[0]),
					Integer.parseInt(sp[1]));
			clue.add(xy);
		}

		for (int i = 0; i < clue.size() - 1; i++) {
			Point p = clue.get(i);

			if (p.x == clue.get(i + 1).x && p.y != clue.get(i + 1).y)
				clueMap.put(p, "v");

			else if (p.x != clue.get(i + 1).x && p.y == clue.get(i + 1).y)
				clueMap.put(p, "h");
		}
		clue.remove(clue.size() - 1);
		getShell().setFocus();
		redraw();
	}

	void setMaze(Maze m) {
		this.mazeData = m;
	}

	public boolean isStopped() {
		return stopped;
	}

	public boolean isWon() {
		return won;
	}

	public Clip getClip() {
		return clip;
	}

	public void setPositions(String s){
		
		String[] sp = s.split(" ");
		sX = Integer.parseInt(sp[0]);
		sY = Integer.parseInt(sp[1]);
		
		gX = Integer.parseInt(sp[2]);
		gY = Integer.parseInt(sp[3]);		
	}
	
	public String getCharPosistion() {
		return sX + " " + sY;
	}

	public String getGoalPosistion() {
		return gX + " " + gY;
	}

	private static int[] circle(int r, int offsetX, int offsetY) {
		int[] polygon = new int[8 * r + 4];
		// x^2 + y^2 = r^2
		for (int i = 0; i < 2 * r + 1; i++) {
			int x = i - r;
			int y = (int) Math.sqrt(r * r - x * x);
			polygon[2 * i] = offsetX + x;
			polygon[2 * i + 1] = offsetY + y;
			polygon[8 * r - 2 * i - 2] = offsetX + x;
			polygon[8 * r - 2 * i - 1] = offsetY - y;
		}
		return polygon;
	}
}
