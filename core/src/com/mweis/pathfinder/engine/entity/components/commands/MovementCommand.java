package com.mweis.pathfinder.engine.entity.components.commands;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class MovementCommand implements Component {
	public Vector2 position; // desired position
}
