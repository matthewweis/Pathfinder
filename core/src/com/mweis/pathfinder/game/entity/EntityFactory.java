package com.mweis.pathfinder.game.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mweis.pathfinder.engine.entity.components.SpeedComponent;
import com.mweis.pathfinder.engine.entity.components.SpriteComponent;

public class EntityFactory {
	
	public static Entity spawnTestEntity(float x, float y, float speed, Sprite sprite) {
		Entity entity = new Entity();
		//entity.add(new DirectionComponent());
		//entity.add(new PositionComponent(x, y));
		entity.add(new SpriteComponent(sprite));
		entity.add(new SpeedComponent(speed));
		return entity;
	}
	
	private EntityFactory() {}; // Factories need no instantiation
	
}
