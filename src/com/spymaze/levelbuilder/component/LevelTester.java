package com.spymaze.levelbuilder.component;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.spymaze.levelbuilder.level.Level;
import com.spymaze.levelbuilder.level.LevelHandler;
import com.spymaze.levelbuilder.sprite.CharacterSprite;
import com.spymaze.levelbuilder.sprite.LoadedTileSprite;
import com.spymaze.levelbuilder.sprite.TileSprite;


public class LevelTester extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	//Used for "double-buffering"
	private Image doubleImg;
	private Graphics doubleGfx;
	
	private final LevelHandler levelHandler;

	private ArrayList<CharacterSprite> loadedChars = new ArrayList<CharacterSprite>();
	private ArrayList<LoadedTileSprite> loadedTiles;
	private CharacterSprite cachedGuy;
	
	private final long startTime;
	
	public LevelTester(final TileSprite[][] tileSprites, final ArrayList<CharacterSprite> charSprites) {
		ArrayList<CharacterSprite> cs = new ArrayList<CharacterSprite>();
		
		for (int i=0; i<charSprites.size(); i++) {
			cs.add(charSprites.get(i).clone());
		}
		
		setBackground(Color.BLACK);
		
		startTime = System.currentTimeMillis();
		
		this.levelHandler = new LevelHandler(new Level(tileSprites, cs, 800, 480));
		
		this.levelHandler.start();
		
		repaint.start();
	}

	@Override
	public void run() {		
		JFrame jframe = new JFrame("Test Window");
		jframe.setSize(800 + 6, 480 + 28);
		
		jframe.add(this);
		
		jframe.setVisible(true);
		jframe.setResizable(false);
		
		jframe.addKeyListener(kl);
		
		this.addKeyListener(kl);
		
		jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //System.exit(0);
		
		try {
			while (levelHandler.doLevel && jframe.isVisible()) {
				Thread.sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (!levelHandler.doLevel) {
			if (levelHandler.level.tileSprites[levelHandler.guy.xLoc][levelHandler.guy.yLoc] != TileSprite.END_TILE) {
				JOptionPane.showMessageDialog(null, "Sorry, you've lost!");
			} else {
				JOptionPane.showMessageDialog(null, "Congrats, you've won!");
			}
		}
		
		levelHandler.doLevel = false;
		
		jframe.removeKeyListener(kl);
		
		this.removeKeyListener(kl);
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if (levelHandler != null
				&& levelHandler.level != null
				&& levelHandler.guy != null) {			
			cachedGuy = levelHandler.guy.clone(); //Need to cache guy, otherwise there will be inconsistencies due to the multiple threading of the game.
			
			//So we don't have to update loaded tiles every time paint gets called
			if (cachedGuy.xLoc != cachedGuy.cachedXLoc || cachedGuy.yLoc != cachedGuy.cachedYLoc) {
				loadedTiles = levelHandler.level.getLoadedTiles(cachedGuy,
						loadedTiles,
						cachedGuy.xLoc - cachedGuy.cachedXLoc, cachedGuy.yLoc - cachedGuy.cachedYLoc);
				
				levelHandler.guy.cachedXLoc = cachedGuy.xLoc;
				levelHandler.guy.cachedYLoc = cachedGuy.yLoc;
			}

			if (loadedTiles != null) {
				for (LoadedTileSprite lts : loadedTiles) {
					g.drawImage(lts.i, (lts.xLoc * 64) - cachedGuy.xOffset, (lts.yLoc * 64) - cachedGuy.yOffset, this);
				}
			}
			
			loadedChars.clear();
			
			for (CharacterSprite cs : levelHandler.level.charSprites) {
				CharacterSprite loadedCharSprite = levelHandler.level.getLoadedChar(cachedGuy, cs);
				
				if (loadedCharSprite != null) loadedChars.add(loadedCharSprite);
			}
			
			Collections.sort(loadedChars);
	
			for (CharacterSprite cs : loadedChars) {
				g.drawImage(cs.i, ((cs.xLoc * 64) + cs.xOffset) - cachedGuy.xOffset, ((cs.yLoc * 64) + cs.yOffset) - cachedGuy.yOffset, this);
			}
			
			cachedGuy.loadedCharSprite = levelHandler.level.getLoadedGuy(cachedGuy);
			
			g.drawImage(cachedGuy.loadedCharSprite.i, (cachedGuy.loadedCharSprite.xLoc * 64), (cachedGuy.loadedCharSprite.yLoc * 64), this);
			
			g.setColor(Color.RED);
			g.drawString(timeToString(System.currentTimeMillis()-startTime), 10, 20);
			g.drawString((System.currentTimeMillis()-startTime) + "ms", 10, 35);
		}
	}
	
	@Override
	public void update(Graphics g) {
		if (doubleImg == null)  {
			doubleImg = createImage(getSize().width, getSize().height); 
			doubleGfx = doubleImg.getGraphics(); 
		} 

		doubleGfx.setColor(getBackground()); 
		doubleGfx.fillRect(0, 0, getSize().width, this.getSize().height); 

		doubleGfx.setColor(getForeground()); 
		paint(doubleGfx);
		
		g.drawImage(doubleImg, 0, 0, this); 
	}
	
	private String timeToString(long timePassed) {
		int hours = (int) (timePassed / 3600000);
		int minutes = (int) ((timePassed % 3600000) / 60000);
		int seconds = (int) ((((timePassed % 3600000) % 60000)) / 1000);
		
		String time = "";
		
		if (hours < 10) time += "0" + hours + ":";
		else time += hours + ":"; 
			
		if (minutes < 10) time += "0" + minutes + ":";
		else time += minutes + ":";
		
		if (seconds < 10) time += "0" + seconds;
		else time += seconds;
		
		return time;
	}
	
	/**
	 * Repaint thread, calling <code>repaint()</code> calls paint(g) and update(g)
	 */
	private Thread repaint = new Thread() {
		public void run() {
			try {
				while (levelHandler.doLevel) {
					repaint();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	private final KeyListener kl = new KeyListener() {
		
		@Override
		public void keyReleased(KeyEvent e) {
			levelHandler.currentKey = -1;
			levelHandler.nextKey = -1;
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (levelHandler.currentKey == -1
					&& levelHandler.guy != null
					&& levelHandler.guy.xOffset == 0
					&& levelHandler.guy.yOffset == 0) {
				levelHandler.currentKey = e.getKeyCode();
				
				if (levelHandler.currentKey >= 37 && levelHandler.currentKey <= 40) {
					levelHandler.lastKey = levelHandler.currentKey;
				}
			}
			
			if (e.getKeyCode() >= 37 && e.getKeyCode() <= 40) {
				levelHandler.nextKey = e.getKeyCode();
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// Not used.
		}
		
	};

}
