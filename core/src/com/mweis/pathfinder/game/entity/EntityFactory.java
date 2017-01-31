package com.mweis.pathfinder.game.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mweis.pathfinder.engine.entity.components.DirectionComponent;
import com.mweis.pathfinder.engine.entity.components.InputComponent;
import com.mweis.pathfinder.engine.entity.components.PositionComponent;
import com.mweis.pathfinder.engine.entity.components.SpeedComponent;
import com.mweis.pathfinder.engine.entity.components.SpriteComponent;
import com.mweis.pathfinder.engine.entity.components.commands.MovementCommand;
import com.mweis.pathfinder.engine.input.InputHandler;

public class EntityFactory {
	
	public static Entity spawnTestEntity(float x, float y, float speed, Sprite sprite, Engine engine) {
		Entity entity = new Entity();
		entity.add(new DirectionComponent());
		entity.add(new PositionComponent(x, y));
		entity.add(new SpriteComponent(sprite));
		entity.add(new SpeedComponent(speed));
		engine.addEntity(entity);
		return entity;
	}
	
	public static Entity spawnTestPlayer(float x, float y, float speed, Sprite sprite, Engine engine) {
		Entity entity = new Entity();
		entity.add(new DirectionComponent());
		entity.add(new PositionComponent(x, y));
		//entity.add(new InputComponent(input));
		entity.add(new SpeedComponent(speed));
		entity.add(new SpriteComponent(sprite));
		engine.addEntity(entity);
		return entity;
	}
	
	private EntityFactory() {}; // Factories need no instantiation
	
}
