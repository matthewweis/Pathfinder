package com.mweis.pathfinder.engine.entity.components.commands;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class MovementCommand implements Component {
	public Vector2 end; // desired position
	public Vector2 start;
	public float alpha = 0; // alpha [0, 1] for lerp
	public float dist;
	
	
	public MovementCommand(Vector2 start, float x, float y) {
		end = new Vector2(x, y);
		this.start = start;
		dist = start.dst(end);
	}
	
	public MovementCommand(Vector2 start, Vector2 end) {
		this.start = start;
		this.end = end;
		dist = start.dst(end);
	}
		
}
