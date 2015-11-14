package com.spymaze.levelbuilder.sprite;

import java.io.File;

public class TileSprite extends Sprite {
	
	public static TileSprite TILE;
	public static TileSprite UNREACHABLE;
	public static TileSprite START_TILE;
	public static TileSprite END_TILE;

	public static TileSprite DEV_TILE = new TileSprite(new File("./sprites/development/developmentTile.png"));

	public TileSprite(File f) {
		super(f);
	}
	
	public static void loadSprites(int missionNumber) {
		TILE = new TileSprite(new File("./sprites/mission " + missionNumber + " sprites/ground/tile.png"));
		UNREACHABLE = new TileSprite(new File("./sprites/mission " + missionNumber + " sprites/ground/unreachabletile.png"));
		START_TILE = new TileSprite(new File("./sprites/mission " + missionNumber + " sprites/ground/starttile.png"));
		END_TILE = new TileSprite(new File("./sprites/mission " + missionNumber + " sprites/ground/endtile.png"));
	}

}
