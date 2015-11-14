package com.spymaze.levelbuilder.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JLabel;

import com.spymaze.levelbuilder.sprite.CharacterSprite;
import com.spymaze.levelbuilder.sprite.Sprite;
import com.spymaze.levelbuilder.sprite.TileSprite;



@SuppressWarnings("serial")
public class LevelLabel extends JLabel {
	
	public boolean paintMap;
	public boolean startUsed;
	public boolean endUsed;
	
	public TileSprite[][] tileSprites;
	public ArrayList<CharacterSprite> charSprites = new ArrayList<CharacterSprite>();
	public Sprite currentSprite;
	public CharacterSprite currentCharSprite;
	
	private Point pressed = null;
	
	public LevelLabel() {
		Painter.start();
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				pressed = arg0.getPoint();
				
				addObject(pressed);
			}
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				pressed = null;
			}
		});
		
		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent arg0) {
				Point current = arg0.getPoint();
				
				if (pressed != null) {
					addObject(current);
				}
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				//UNUSED
			}
			
		});
	}
	
	private void addObject(Point current) {
		startUsed = false;
		endUsed = false;
		
		for (int x=0; x<tileSprites.length; x++) {
			for (int y=0; y<tileSprites[x].length; y++) {
				if (tileSprites[x][y] == TileSprite.START_TILE) startUsed = true;
				
				if (tileSprites[x][y] == TileSprite.END_TILE) endUsed = true;
			}
		}
		
		if (tileSprites != null
				&& current.x < (tileSprites.length * 64)
				&& current.y < (tileSprites[0].length * 64)) {
			if (currentSprite != null && currentSprite instanceof TileSprite) {
				if (!startUsed && currentSprite.equals(TileSprite.START_TILE)) {
					startUsed = true;
					tileSprites[(current.x/64)][(current.y/64)] = (TileSprite) currentSprite;
				} else if (!endUsed && currentSprite.equals(TileSprite.END_TILE)) {
					endUsed = true;
					tileSprites[(current.x/64)][(current.y/64)] = (TileSprite) currentSprite;
				} else if (!currentSprite.equals(TileSprite.START_TILE) && !currentSprite.equals(TileSprite.END_TILE)) {
					tileSprites[(current.x/64)][(current.y/64)] = (TileSprite) currentSprite;
				}
			} else if (currentCharSprite != null) {
				currentCharSprite = new CharacterSprite(currentCharSprite.f, currentCharSprite.direction, (current.x/64), (current.y/64), 0, 0);
				
				if (!charSprites.contains(currentCharSprite)
						&& tileSprites[currentCharSprite.xLoc][currentCharSprite.yLoc].equals(TileSprite.TILE)) {
					charSprites.add(currentCharSprite);
				}
				
				if (currentCharSprite.f.equals(Sprite.ENEMY.f)) {
					currentCharSprite = new CharacterSprite(Sprite.ENEMY.f, currentCharSprite.direction, -1, -1, 0, 0);
				}
			} else {
				int xLoc = (current.x/64);
				int yLoc = (current.y/64);
				
				tileSprites[xLoc][yLoc] = TileSprite.DEV_TILE;
				
				for (int i=0; i<charSprites.size(); i++) {
					if (charSprites.get(i).xLoc == xLoc && charSprites.get(i).yLoc == yLoc) {
						charSprites.remove(i);
					}
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if (paintMap) {
			for (int x=0; x<tileSprites.length; x++) {
				for (int y=0; y<tileSprites[x].length; y++) {
					if (tileSprites[x][y] != null)
					g2d.drawImage(tileSprites[x][y].getImage(), (x * 64), (y * 64), this);
				}
			}

			for (CharacterSprite cs : charSprites) {
				g2d.drawImage(cs.getImage(), (cs.xLoc * 64), (cs.yLoc * 64), this);
				//g2d.setColor(new Color(100, 50, 200));
				g2d.setColor(Color.WHITE);
				g2d.setFont(new Font("Impact", 0, 17));
				g2d.drawString("" + cs.direction, ((cs.xLoc * 64) + 10), ((cs.yLoc * 64) + 60));
			}
		}
	}
	
	private Thread Painter = new Thread() {
		public void run() {
			while (true) {
				try {
					repaint();
					sleep(30);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};
	
}
