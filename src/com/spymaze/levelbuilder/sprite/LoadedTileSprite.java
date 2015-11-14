package com.spymaze.levelbuilder.sprite;

import java.io.File;

public class LoadedTileSprite extends TileSprite {
	
	public int xLoc;
	public int yLoc;
	
	public LoadedTileSprite(File f, int xLoc, int yLoc) {
		super(f);
		
		this.xLoc = xLoc;
		this.yLoc = yLoc;
	}

}
