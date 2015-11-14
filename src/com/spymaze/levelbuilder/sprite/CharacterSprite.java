package com.spymaze.levelbuilder.sprite;

import java.io.File;


public class CharacterSprite extends Sprite implements Comparable<CharacterSprite>, Cloneable {
	
	public Direction direction;
	
	//Note: xLoc/yLoc are numbers from the [][] of tileSprites to determine location
	public int xLoc;
	public int yLoc;
	public int xOffset;
	public int yOffset;
	
	public int cachedXLoc = -1; //Cached locs are used for the "guy" when loading tiles
	public int cachedYLoc = -1;
	
	public CharacterSprite loadedCharSprite; //Used for the guy and for loading the enemy sprites
	
	public CharacterSprite(File f, Direction direction, int xLoc, int yLoc, int xOffset, int yOffset) {
		super(f);
		
		this.direction = direction;
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	@Override
	public CharacterSprite clone() {
		CharacterSprite cloned = new CharacterSprite(this.f, this.direction, this.xLoc, this.yLoc, this.xOffset, this.yOffset);
		
		cloned.cachedXLoc = this.cachedXLoc;
		cloned.cachedYLoc = this.cachedYLoc;
		
		cloned.i = this.i;
		
		return cloned;
	}

	@Override
	public int compareTo(CharacterSprite another) {
		if (this.direction != null
				&& another.direction != null) {
			return (this.direction.val - another.direction.val);
		}
		
		return 0;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof CharacterSprite) {
			return (((CharacterSprite)o).xLoc == this.xLoc && ((CharacterSprite)o).yLoc == this.yLoc);
		}
		
		return false;
	}

}
