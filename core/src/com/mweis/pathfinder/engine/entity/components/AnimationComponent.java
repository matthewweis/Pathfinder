package com.mweis.pathfinder.engine.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationComponent implements Component {
	public Animation<TextureRegion> animation;
	public float stateTime;
	public final boolean hasOrigins;
	public float originX, originY;
	
	public AnimationComponent(Animation<TextureRegion> animation) {
		this.animation = animation;
		this.hasOrigins = false;
	}
	
	public AnimationComponent(Animation<TextureRegion> animation, float originX, float originY) {
		this.animation = animation;
		this.originX = originX;
		this.originY = originY;
		this.hasOrigins = true;
	}
}
