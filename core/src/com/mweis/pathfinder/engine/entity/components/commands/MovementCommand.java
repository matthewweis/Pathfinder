package com.mweis.pathfinder.engine.entity.components.commands;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class MovementCommand implements Component {
	public Vector2 position; // desired position
	public float alpha = 0; // alpha [0, 1] for lerp
	
	public MovementCommand(float x, float y) {
		position = new Vector2(x, y);
	}
		
}
