package com.spymaze.levelbuilder.level;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import com.spymaze.levelbuilder.sprite.CharacterSprite;
import com.spymaze.levelbuilder.sprite.Direction;
import com.spymaze.levelbuilder.sprite.LoadedTileSprite;
import com.spymaze.levelbuilder.sprite.Sprite;
import com.spymaze.levelbuilder.sprite.TileSprite;
import com.spymaze.levelbuilder.utility.Utility;


public class Level {
	
	public TileSprite[][] tileSprites;
	public ArrayList<CharacterSprite> charSprites = new ArrayList<CharacterSprite>();
	
	public int xMax;
	public int yMax;
	
	private int tilesWidth;
	private int tilesHeight;

	public Level(TileSprite[][] tileSprites, ArrayList<CharacterSprite> charSprites, int xMax, int yMax) {
		this.tileSprites = tileSprites;
		this.charSprites = charSprites;
		this.xMax = xMax;
		this.yMax = yMax;
		
		tilesWidth = Utility.nextEven((float) xMax / 64.0F);
		tilesHeight = Utility.nextEven((float) yMax / 64.0F);
	}
	
	/**
	 * Returns an instance of a Level that is encoded into a file.
	 * 
	 * @param loc
	 * @return
	 */
	public static Level loadLevel(File loc, int xMax, int yMax) {
		try {
			TileSprite[][] ts = null;
			ArrayList<CharacterSprite> cs = new ArrayList<CharacterSprite>();
			FileReader fStream = new FileReader(loc);
			BufferedReader br = new BufferedReader(fStream);
			String nextLine = null;
			
			while ((nextLine = br.readLine()) != null) {
				String dec = Utility.encryptString(nextLine, '1');
				
				if (dec.contains("Dimensions")) {
					ts = new TileSprite[Integer.parseInt(dec.replace("Dimensions:", "").split(" ")[0])]
		                                [Integer.parseInt(dec.replace("Dimensions:", "").split(" ")[1])];
				} else if (dec.contains("tile")) {
					TileSprite used = null;
					
					if (dec.contains("\\tile.png")) used = TileSprite.TILE;
					else if (dec.contains("\\unreachabletile.png")) used = TileSprite.UNREACHABLE;
					else if (dec.contains("\\developmentTile.png")) used = TileSprite.DEV_TILE;
					else if (dec.contains("\\starttile.png")) {
						used = TileSprite.START_TILE;
					} else {
						used = TileSprite.END_TILE;
					}
					
					ts[Integer.parseInt(Utility.parse("x:", " ", dec))][Integer.parseInt(Utility.parse("y:", " ", dec))] = used;
				} else if (dec.contains("char")) {
					File spriteLoc = Sprite.ENEMY.f;
					Direction dir = null;
					String directionParsed = Utility.parse("dir:", " ", dec);
					
					for (Direction d : Direction.values()) {
						if (d.toString().equals(directionParsed)) {
							dir = d;
							break;
						}
					}
					
					cs.add(new CharacterSprite(spriteLoc, dir, Integer.parseInt(Utility.parse("x:", " ", dec)),
							Integer.parseInt(Utility.parse("y:", " ", dec)), 0, 0));
				}
			}
			
			br.close();
			
			return new Level(ts, cs, xMax, yMax);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Returns the tiles that should be drawn on the screen currently to avoid any need to draw the entire map.
	 * <p>
	 * Does not loop through all tiles on the map, but rather the tiles that should be loaded for more efficiency.
	 * <p>
	 * 
	 * @param guy        The main guy.
	 * @param lastLoaded The last loaded tile sprites.
	 * @param deltaX     The change of the guy since we last loaded the tiles on the x-axis. (-1 <= deltaX <= 1)
	 * @param deltaY     The change of the guy since we last loaded the tiles on the y-axis. (-1 <= deltaY <= 1)
	 * @param xMax       The width of the view.
	 * @param yMax       The height of the view.
	 * @return           The loaded two dimensional tile sprite array.
	 */
	public ArrayList<LoadedTileSprite> getLoadedTiles(CharacterSprite guy, ArrayList<LoadedTileSprite> lastLoaded, int deltaX, int deltaY) {
		//Offsets +-1 to draw 1 tile off screen
		int loadedTilesStartX = ((guy.xLoc-(tilesWidth/2)-1) < 0) ? 0 : (guy.xLoc-(tilesWidth/2)-1);
		int loadedTilesEndX = (this.tileSprites.length < (guy.xLoc+(tilesWidth/2)+1)) ? this.tileSprites.length : (guy.xLoc+(tilesWidth/2)+1);
		int loadedTilesStartY = ((guy.yLoc-(tilesHeight/2)-1) < 0) ? 0 : (guy.yLoc-(tilesHeight/2)-1);
		int loadedTilesEndY = (this.tileSprites[0].length < (guy.yLoc+(tilesHeight/2)+1)) ? this.tileSprites[0].length : (guy.yLoc+(tilesHeight/2)+1);
		
		ArrayList<LoadedTileSprite> lts = (lastLoaded == null) ? new ArrayList<LoadedTileSprite>() : lastLoaded;
		
		int add = 0;
		int remove = 0;
		
		if (lts.size() == 0) {
			for (int x=loadedTilesStartX; x < loadedTilesEndX; x++) {
				for (int y=loadedTilesStartY; y < loadedTilesEndY; y++) {
					lts.add(new LoadedTileSprite(this.tileSprites[x][y].f, x - (guy.xLoc-(tilesWidth/2)), y - (guy.yLoc-(tilesHeight/2))));
				}
			}
		} else if (deltaX != 0) {
			add = (deltaX == 1) ? guy.xLoc+(tilesWidth/2) : guy.xLoc-(tilesWidth/2)-1;
			remove = (deltaX == 1) ? -1 : tilesWidth;
			
			for (int index=0; index < lts.size(); index++) {
				if (lts.get(index).xLoc == remove) {
					lts.remove(index);
					
					index--;
					
					continue;
				}
				
				lts.get(index).xLoc -= deltaX;
			}
			
			if (add >= 0 && add < this.tileSprites.length) {
				for (int y=loadedTilesStartY; y < loadedTilesEndY; y++) {
					lts.add(new LoadedTileSprite(this.tileSprites[add][y].f, add - (guy.xLoc-(tilesWidth/2)), y - (guy.yLoc-(tilesHeight/2))));
				}
			}
		} else if (deltaY != 0) {
			add = (deltaY == 1) ? guy.yLoc+(tilesHeight/2) : guy.yLoc-(tilesHeight/2)-1;
			remove = (deltaY == 1) ? -1 : tilesHeight;
			
			for (int index=0; index < lts.size(); index++) {
				if (lts.get(index).yLoc == remove) {
					lts.remove(index);
					
					index--;
					
					continue;
				}
				
				lts.get(index).yLoc -= deltaY;
			}
			
			if (add >= 0 && add < this.tileSprites[0].length) {
				for (int x=loadedTilesStartX; x < loadedTilesEndX; x++) {
					lts.add(new LoadedTileSprite(this.tileSprites[x][add].f, x - (guy.xLoc-(tilesWidth/2)), add - (guy.yLoc-(tilesHeight/2))));
				}
			}
		}

		return lts;
	}
	
	/**
	 * Returns the enemies that should be drawn on the screen currently to avoid any need of drawing all of them.
	 * <p>
	 * 
	 * @param guy
	 * @param character 
	 * @return
	 */
	public CharacterSprite getLoadedChar(CharacterSprite guy, CharacterSprite character) {
		if ((character.xLoc >= (guy.xLoc-(tilesWidth/2)-1) && character.xLoc <= (guy.xLoc+(tilesWidth/2)+1))
				&& (character.yLoc >= (guy.yLoc-(tilesHeight/2)-1) && character.yLoc <= (guy.yLoc+(tilesHeight/2)+1))) {
			if (character.loadedCharSprite == null) {
				character.loadedCharSprite = character.clone();
			} else {
				//Not creating a new instance, but just updating current values.
				character.loadedCharSprite.xOffset = character.xOffset;
				character.loadedCharSprite.yOffset = character.yOffset;
				
				character.loadedCharSprite.i = character.i;
			}
			
			character.loadedCharSprite.xLoc = character.xLoc - (guy.xLoc-(tilesWidth/2));
			character.loadedCharSprite.yLoc = character.yLoc - (guy.yLoc-(tilesHeight/2));
		} else {
			character.loadedCharSprite = null;
		}
		
		return character.loadedCharSprite;
	}
	
	/**
	 * Returns where the guy is supposed to be painted.
	 * <p>
	 * 
	 * @param guy  The main guy.
	 * @param xMax The width of the view.
	 * @param yMax The height of the view
	 * @return     Where the guy is supposed to be painted at in the loaded tile sprite 2D array.
	 */
	public CharacterSprite getLoadedGuy(CharacterSprite guy) {
		CharacterSprite loadedGuy = new CharacterSprite(guy.f, guy.direction, tilesWidth/2, tilesHeight/2, guy.xOffset, guy.yOffset);
		loadedGuy.i = guy.i;
		
		return loadedGuy;
	}

}
