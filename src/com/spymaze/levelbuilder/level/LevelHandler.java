package com.spymaze.levelbuilder.level;

import com.spymaze.levelbuilder.sprite.CharacterSprite;
import com.spymaze.levelbuilder.sprite.Direction;
import com.spymaze.levelbuilder.sprite.Sprite;
import com.spymaze.levelbuilder.sprite.TileSprite;

//I really need to recode this to make it more generic and without switch cases :S
public class LevelHandler extends Thread {

	public final Level level;
	
	private int time = 700;
	private int dist = 64;
	
	public int currentKey = -1;
	public int lastKey = -1;
	public int nextKey = -1;
	
	public CharacterSprite guy;
	
	public boolean doLevel = true;
	public boolean lost = false;
	
	public LevelHandler(Level level) {
		this.level = level;

		GUY: 
		for (int x=0; x<level.tileSprites.length; x++) {
			for (int y=0; y<level.tileSprites[x].length; y++) {
				if (level.tileSprites[x][y].equals(TileSprite.START_TILE)) {
					this.guy = new CharacterSprite(Sprite.GUY.f, null, x, y, 0, 0);
					break GUY;
				}
			}
		}
		
		//Guy can be null due to main menu
		
		GuyThread.start();
	}
	
	/* Keys
	 * 
	 * DOWN = 40
	 * RIGHT = 39
	 * UP = 38
	 * LEFT = 37
	 */
	
	public void run() {
		try {
			while (doLevel) {
				
				for (int i=0; i<level.charSprites.size(); i++) {
					
					CharacterSprite cs = level.charSprites.get(i);
					
					if (cs.direction == Direction.NORTH 
							&& (cs.yLoc == 0 || !level.tileSprites[cs.xLoc][cs.yLoc-1].equals(TileSprite.TILE))) {
						cs.i = Sprite.ENEMYSTRAIGHT1.i;
						cs.direction = Direction.SOUTH;
					} else if (cs.direction == Direction.SOUTH 
							&& (cs.yLoc == (level.tileSprites[0].length-1) || !level.tileSprites[cs.xLoc][cs.yLoc+1].equals(TileSprite.TILE))) {
						cs.i = Sprite.ENEMYBACK1.i;
						cs.direction = Direction.NORTH;
					} else if (cs.direction == Direction.WEST 
							&& (cs.xLoc == 0 || !level.tileSprites[cs.xLoc-1][cs.yLoc].equals(TileSprite.TILE))) {
						cs.i = Sprite.ENEMYRIGHT1.i;
						cs.direction = Direction.EAST;
					} else if (cs.direction == Direction.EAST 
							&& (cs.xLoc == (level.tileSprites.length-1) || !level.tileSprites[cs.xLoc+1][cs.yLoc].equals(TileSprite.TILE))) {
						cs.i = Sprite.ENEMYLEFT1.i;
						cs.direction = Direction.WEST;
					}
					
					switch (cs.direction) {
					case NORTH:
						cs.yOffset -= dist/32;
						
						if (cs.yOffset % 16 == 0) {
							if (!cs.i.equals(Sprite.ENEMYBACK1.i)) {
								cs.i = Sprite.ENEMYBACK1.i;
							} else {
								cs.i = Sprite.ENEMYBACK2.i;
							}
						}
						
						if (cs.yOffset % 64 == 0) {
							cs.yLoc--;
							cs.yOffset = 0;
						}
						
						LOST: if (guy != null
								&& guy.xLoc == cs.xLoc
								&& cs.yLoc >= guy.yLoc
								&& cs.yLoc - guy.yLoc <= 2) {
							for (int y=guy.yLoc; y<=cs.yLoc; y++) {
								if (level.tileSprites[guy.xLoc][y] == TileSprite.UNREACHABLE) {
									break LOST;
								}
							}
							
							doLevel = false;
						}
						
						break;
					case SOUTH:
						cs.yOffset += dist/32;
						
						if (cs.yOffset % 16 == 0) {
							if (!cs.i.equals(Sprite.ENEMYSTRAIGHT1.i)) {
								cs.i = Sprite.ENEMYSTRAIGHT1.i;
							} else {
								cs.i = Sprite.ENEMYSTRAIGHT2.i;
							}
						}
						
						if (cs.yOffset % 64 == 0) {
							cs.yLoc++;
							cs.yOffset = 0;
						}
						
						LOST: if (guy != null
								&& guy.xLoc == cs.xLoc
								&& guy.yLoc >= cs.yLoc
								&& guy.yLoc - cs.yLoc <= 2) {
							for (int y=guy.yLoc; y>=cs.yLoc; y--) {
								if (level.tileSprites[guy.xLoc][y] == TileSprite.UNREACHABLE) {
									break LOST;
								}
							}
							
							doLevel = false;
						}
						
						break;
					case WEST:
						cs.xOffset -= dist/32;
						
						if (cs.xOffset % 16 == 0) {
							if (!cs.i.equals(Sprite.ENEMYLEFT1.i)) {
								cs.i = Sprite.ENEMYLEFT1.i;
							} else {
								cs.i = Sprite.ENEMYLEFT2.i;
							}
						}
						
						if (cs.xOffset % 64 == 0) {
							cs.xLoc--;
							cs.xOffset = 0;
						}
						
						LOST: if (guy != null
								&& cs.yLoc == guy.yLoc
								&& cs.xLoc >= guy.xLoc
								&& cs.xLoc - guy.xLoc <= 2) {
							for (int x=guy.xLoc; x<=cs.xLoc; x++) {
								if (level.tileSprites[x][guy.yLoc] == TileSprite.UNREACHABLE) {
									break LOST;
								}
							}
							
							doLevel = false;
						}

						break;
					case EAST:
						cs.xOffset += dist/32;
						
						if (cs.xOffset % 16 == 0) {
							if (!cs.i.equals(Sprite.ENEMYRIGHT1.i)) {
								cs.i = Sprite.ENEMYRIGHT1.i;
							} else {
								cs.i = Sprite.ENEMYRIGHT2.i;
							}
						}
						
						if (cs.xOffset % 64 == 0) {
							cs.xLoc++;
							cs.xOffset = 0;
						}
						
						LOST: if (guy != null
								&& guy.yLoc == cs.yLoc
								&& guy.xLoc >= cs.xLoc
								&& guy.xLoc - cs.xLoc <= 2) {
							for (int x=guy.xLoc; x>=cs.xLoc; x--) {
								if (level.tileSprites[x][guy.yLoc] == TileSprite.UNREACHABLE) {
									break LOST;
								}
							}
							
							doLevel = false;
						}

						break;
					}
					
				}
				
				Thread.sleep(time/32);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Thread GuyThread = new Thread() {
		public void run() {
			try {
				while (doLevel) {
					if ((currentKey != -1 || (lastKey != -1 && (guy.yOffset != 0 || guy.xOffset != 0)))
						|| (guy.xOffset == 0 && guy.yOffset == 0 && currentKey == -1 && nextKey != -1)) {
						
						if (guy.xOffset == 0 && guy.yOffset == 0 && currentKey == -1) {
							currentKey = lastKey = nextKey;
						}
						
						int key = (currentKey == -1) ? lastKey : currentKey;
						
						switch (key) {
						case 40: // SOUTH
							if (guy.yLoc == (level.tileSprites[0].length-1) 
								|| (guy.yOffset == 0 && level.tileSprites[guy.xLoc][guy.yLoc+1].equals(TileSprite.UNREACHABLE))) break;
							
							guy.yOffset += dist/32;
							
							if (guy.yOffset % 16 == 0) {
								if (!guy.i.equals(Sprite.GUYSTRAIGHT1.i)) {
									guy.i = Sprite.GUYSTRAIGHT1.i;
								} else {
									guy.i = Sprite.GUYSTRAIGHT2.i;
								}
							}
							
							if (guy.yOffset % 64 == 0) {
								guy.yLoc++;
								guy.yOffset = 0;
							}
							
							break;
						case 39: // EAST
							if (guy.xLoc == (level.tileSprites.length-1)
								|| (guy.xOffset == 0 && level.tileSprites[guy.xLoc+1][guy.yLoc].equals(TileSprite.UNREACHABLE))) break;
							
							guy.xOffset += dist/32;
							
							if (guy.xOffset % 16 == 0) {
								if (!guy.i.equals(Sprite.GUYRIGHT1.i)) {
									guy.i = Sprite.GUYRIGHT1.i;
								} else {
									guy.i = Sprite.GUYRIGHT2.i;
								}
							}
							
							if (guy.xOffset % 64 == 0) {
								guy.xLoc++;
								guy.xOffset = 0;
							}
							
							break;
						case 38: // NORTH
							if (guy.yLoc == 0
								|| (guy.yOffset == 0 && level.tileSprites[guy.xLoc][guy.yLoc-1].equals(TileSprite.UNREACHABLE))) break;
							
							guy.yOffset -= dist/32;
							
							if (guy.yOffset % 16 == 0) {
								if (!guy.i.equals(Sprite.GUYBACK1.i)) {
									guy.i = Sprite.GUYBACK1.i;
								} else {
									guy.i = Sprite.GUYBACK2.i;
								}
							}
							
							if (guy.yOffset % 64 == 0) {
								guy.yLoc--;
								guy.yOffset = 0;
							}
							
							break;
						case 37: // WEST
							if (guy.xLoc == 0
								|| (guy.xOffset == 0 && level.tileSprites[guy.xLoc-1][guy.yLoc].equals(TileSprite.UNREACHABLE))) break;
							
							guy.xOffset -= dist/32;
							
							if (guy.xOffset % 16 == 0) {
								if (!guy.i.equals(Sprite.GUYLEFT1.i)) {
									guy.i = Sprite.GUYLEFT1.i;
								} else {
									guy.i = Sprite.GUYLEFT2.i;
								}
							}
							
							if (guy.xOffset % 64 == 0) {
								guy.xLoc--;
								guy.xOffset = 0;
							}
							
							break;
						}
					}
					
					if (level.tileSprites[guy.xLoc][guy.yLoc] == TileSprite.END_TILE) {
						doLevel = false;
					}
					
					Thread.sleep(time/38); // Just a tad quicker than the enemies
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
}
