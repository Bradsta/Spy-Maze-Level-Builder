package com.spymaze.levelbuilder.sprite;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {
	
	public static Sprite ENEMY;
	public static Sprite ENEMYBACK1;
	public static Sprite ENEMYBACK2;
	public static Sprite ENEMYLEFT1;
	public static Sprite ENEMYLEFT2;
	public static Sprite ENEMYRIGHT1;
	public static Sprite ENEMYRIGHT2;
	public static Sprite ENEMYSTRAIGHT1;
	public static Sprite ENEMYSTRAIGHT2;
	
	public static Sprite GUY;
	public static Sprite GUYBACK1;
	public static Sprite GUYBACK2;
	public static Sprite GUYLEFT1;
	public static Sprite GUYLEFT2;
	public static Sprite GUYRIGHT1;
	public static Sprite GUYRIGHT2;
	public static Sprite GUYSTRAIGHT1;
	public static Sprite GUYSTRAIGHT2;
	
	public final File f;
	public AffineTransform af;
	public Image i;
	
	public Sprite(File f) {
		this.f = f;
		
		try {
			this.i = ImageIO.read(f);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		this.af = new AffineTransform();
	}
	
	public static void loadSprites(int missionNumber) {
		ENEMY = new Sprite(new File("./sprites/mission " + missionNumber + " sprites/enemy/enemy.png"));
		ENEMYBACK1 = new Sprite(new File("./sprites/mission " + missionNumber + " sprites/enemy/enemyback1.png"));
		ENEMYBACK2 = new Sprite(new File("./sprites/mission " + missionNumber + " sprites/enemy/enemyback2.png"));
		ENEMYLEFT1 = new Sprite(new File("./sprites/mission " + missionNumber + " sprites/enemy/enemyleft1.png"));
		ENEMYLEFT2 = new Sprite(new File("./sprites/mission " + missionNumber + " sprites/enemy/enemyleft2.png"));
		ENEMYRIGHT1 = new Sprite(new File("./sprites/mission " + missionNumber + " sprites/enemy/enemyright1.png"));
		ENEMYRIGHT2 = new Sprite(new File("./sprites/mission " + missionNumber + " sprites/enemy/enemyright2.png"));
		ENEMYSTRAIGHT1 = new Sprite(new File("./sprites/mission " + missionNumber + " sprites/enemy/enemystraight1.png"));
		ENEMYSTRAIGHT2 = new Sprite(new File("./sprites/mission " + missionNumber + " sprites/enemy/enemystraight2.png"));
		
		GUY = new Sprite(new File("./sprites/mission " + missionNumber + " sprites/guy/guy.png"));
		GUYBACK1 = new Sprite(new File("./sprites/mission " + missionNumber + " sprites/guy/guyback1.png"));
		GUYBACK2 = new Sprite(new File("./sprites/mission " + missionNumber + " sprites/guy/guyback2.png"));
		GUYLEFT1 = new Sprite(new File("./sprites/mission " + missionNumber + " sprites/guy/guyleft1.png"));
		GUYLEFT2 = new Sprite(new File("./sprites/mission " + missionNumber + " sprites/guy/guyleft2.png"));
		GUYRIGHT1 = new Sprite(new File("./sprites/mission " + missionNumber + " sprites/guy/guyright1.png"));
		GUYRIGHT2 = new Sprite(new File("./sprites/mission " + missionNumber + " sprites/guy/guyright2.png"));
		GUYSTRAIGHT1 = new Sprite(new File("./sprites/mission " + missionNumber + " sprites/guy/guystraight1.png"));
		GUYSTRAIGHT2 = new Sprite(new File("./sprites/mission " + missionNumber + " sprites/guy/guystraight2.png"));
	}
	
	/**
	 * Returns image of sprite (2D)
	 * @return Sprite image
	 */
	public Image getImage() {
		return i;
	}
	
	/**
	 * @return current location of transform
	 */
	public Point getLocation() {
		return new Point((int) af.getTranslateX(), (int) af.getTranslateY());
	}
	
	/**
	 * @param anchor where sprite is rotating on
	 * @return angle created between vector of anchor and current sprite location
	 */
	public double getTheta(Point anchor) {
		return -1 * Math.atan2((int) af.getTranslateX()-anchor.x, (int) af.getTranslateY()-anchor.y);
	}
	
	/**
	 * @param x - X value to move sprite
	 * @param y - Y value to move sprite
	 */
	public void move(int x, int y) {
		af = new AffineTransform();
		af.translate(x, y);
	}
	
	/**
	 * @param x - x value translated
	 * @param y - y value translated
	 */
	public void translate(int x, int y) {
		af.translate(x, y);
	}
	
	/**
	 * @param theta - Angle to rotate
	 * @param anchor - Point to rotate around
	 */
	public void rotate(double theta, Point anchor, int startX, int startY) {
		af.rotate(theta, anchor.x-startX, anchor.y-startY);
	}
	
	/**
	 * Renders sprite
	 * @param g2d - Graphics
	 * @param io - ImageObserver
	 */
	public void render(Graphics2D g2d, ImageObserver io) {
		g2d.drawImage(i, af, io);
	}

}
