package com.mweis.pathfinder.engine.entity.components;

import com.badlogic.ashley.core.Component;

public class SpeedComponent implements Component {
	public float speed;
	
	public SpeedComponent() {
		speed = 0.0f;
	}
	
	public SpeedComponent(float speed) {
		this.speed = speed;
	}
}
