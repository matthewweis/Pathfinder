package com.mweis.pathfinder.engine.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class PositionComponent implements Component {
	public Vector2 position;
	public boolean hasMoved = false; // managed by partition system, perhaps this should be a bit on the entity flag instead?
	
	public PositionComponent(float x, float y){
		position = new Vector2(x, y);
	}
}