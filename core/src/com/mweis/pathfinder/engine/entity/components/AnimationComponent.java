package com.mweis.pathfinder.engine.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mweis.pathfinder.engine.views.AnimationMap;

public class AnimationComponent implements Component {
	public AnimationMap map;
	public float stateTime;
	public final boolean hasOrigins;
	public float originX, originY;
	
	public AnimationComponent(AnimationMap map) {
		this.map = map;
		this.hasOrigins = false;
	}
	
	public AnimationComponent(AnimationMap map, float originX, float originY) {
		this.map = map;
		this.originX = originX;
		this.originY = originY;
		this.hasOrigins = true;
	}
}
