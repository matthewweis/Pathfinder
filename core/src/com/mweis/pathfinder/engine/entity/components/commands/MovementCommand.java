package com.mweis.pathfinder.engine.entity.components.commands;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class MovementCommand implements Component {
	public Vector2 end; // desired position
	public Vector2 start;
	public float alpha = 0; // alpha [0, 1] for lerp
	public float dist;
	
	
	public MovementCommand(float startx, float starty, float x, float y) {
		end = new Vector2(x, y);
		start = new Vector2(startx, starty);
		dist = start.dst(end);
	}
		
}
