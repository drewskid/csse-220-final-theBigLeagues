package ui;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Zombie extends Sprite{
	
	private Image zombieImage;
	private int enemydx = 5;
	private int enemydy = 5;
	
	public Zombie(int xPosition, int yPosition) {
		super(xPosition, yPosition);
		setZombieImage(new ImageIcon(
				getClass().getResource("/enemy.png")).getImage());

	}
	public void updateEnemy() {
		xPosition += enemydx;
		if(xPosition < 50 || xPosition > 700) {
			enemydx = -enemydx;
		}
	}
	public int getEnemyX() {
		return xPosition;
	}
	public int getEnemyY() {
		return yPosition;
	}
	public Image getZombieImage() {
		return zombieImage;
	}
	public void setZombieImage(Image image) {
		this.zombieImage = image;
	}
	
}

//package model;
//
//public class GameModel {
//	private int enemyX;
//	private int enemyY;
//	private int enemyDX;
//	public GameModel() {
//		enemyX = 100;
//		enemyY = 200;
//		enemyDX = 2;
//	}
//	public void updateEnemy() {
//		enemyX += enemyDX;
//		if(enemyX < 50 || enemyX > 700) {
//			enemyDX = -enemyDX;
//		}
//	}
//	public int getEnemyX() {
//		return enemyX;
//	}
//	public int getEnemyY() {
//		return enemyY;
//	}
//}
