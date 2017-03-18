package com.mweis.pathfinder.engine.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.mweis.pathfinder.engine.views.AnimationMap;

public class AnimationComponent implements Component {
	public AnimationMap map;
	public float stateTime;
	public Vector2 offset;
	public float width, height;
	
	/*
	 * Assumes offsets are same for all frames, eventually may need to make method which allows a list of offsets
	 */
	public AnimationComponent(AnimationMap map, Vector2 offset, float width, float height) {
		this.map = map;
		this.offset = offset;
		this.width = width;
		this.height = height;
	}
}
