package com.mweis.pathfinder.engine.views;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface AnimationMap {
	public Animation<TextureRegion> walkUp();
	public Animation<TextureRegion> walkDown();
	public Animation<TextureRegion> walkLeft();
	public Animation<TextureRegion> walkRight();
	public Animation<TextureRegion> crawlLeft();
	public Animation<TextureRegion> crawlRight();
	public Animation<TextureRegion> attack();
	public Animation<TextureRegion> _default();
}
