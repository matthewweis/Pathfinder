package com.mweis.pathfinder.engine.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class PositionComponent implements Component {
	public Vector2 position;
	
	public PositionComponent(float x, float y){
		position = new Vector2(x, y);
	}
}