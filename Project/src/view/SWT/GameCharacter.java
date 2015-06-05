package view.SWT;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import algorithms.mazeGenerators.Directions;

class GameCharacter {
	int x, y;
	Image spelunkyDown, spelunkyUp, spelunkyLeft, spelunkyRight;
	Directions dir;
	
	public GameCharacter(int x, int y) {
		this.x = x;
		this.y = y;
		try {
			spelunkyDown = new Image(null, new FileInputStream("resources/sprites/spelunkyDown.png"));
			spelunkyUp = new Image(null, new FileInputStream("resources/sprites/spelunkyUp.png"));
			spelunkyLeft = new Image(null, new FileInputStream("resources/sprites/spelunkyLeft.png"));
			spelunkyRight = new Image(null, new FileInputStream("resources/sprites/spelunkyRight.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void paint(GC gc, int w, int h) {

		switch (dir) {
		case DOWN:
			gc.drawImage(spelunkyDown, 0, 0, spelunkyDown.getBounds().width, spelunkyDown.getBounds().height,
					x - 3, y , w,h);
			break;
		case UP:
			gc.drawImage(spelunkyUp, 0, 0, spelunkyUp.getBounds().width, spelunkyUp.getBounds().height,
					x - 3, y , w,h);
			break;
		case LEFT:
			gc.drawImage(spelunkyLeft, 0, 0, spelunkyLeft.getBounds().width, spelunkyLeft.getBounds().height,
					x - 3, y , w,h);
		break;
		case RIGHT:
			gc.drawImage(spelunkyRight, 0, 0, spelunkyRight.getBounds().width, spelunkyRight.getBounds().height,
					x - 3, y , w,h);
		break;
		
		default:
		break;
		}
	}
}
