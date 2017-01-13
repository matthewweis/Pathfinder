package com.mweis.pathfinder.engine.util;

import com.badlogic.gdx.math.Vector2;

public class MouseAction {
	public float x, y;
	public boolean bool;
	
	public MouseAction(Vector2 coords, boolean bool) {
		if (coords != null) { 
			this.x = coords.x;
			this.y = coords.y;
		}
		this.bool = bool;
	}
	
}
