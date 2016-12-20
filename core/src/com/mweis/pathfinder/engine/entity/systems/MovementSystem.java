package com.mweis.pathfinder.engine.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.mweis.pathfinder.engine.camera.VirtualViewport;
import com.mweis.pathfinder.engine.entity.components.PositionComponent;
import com.mweis.pathfinder.engine.entity.components.SpeedComponent;
import com.mweis.pathfinder.engine.entity.components.SpriteComponent;
import com.mweis.pathfinder.engine.entity.components.commands.MovementCommand;
import com.mweis.pathfinder.engine.util.Debug;
import com.mweis.pathfinder.engine.util.Mappers;

public class MovementSystem extends IteratingSystem {
		
	public MovementSystem() {
		super(Family.all(MovementCommand.class, PositionComponent.class, SpeedComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) { // this happens for all entities
		//System.out.println("MovementSystem using dt of: " + deltaTime + " this should be constant.");
		//DirectionComponent direction = Mappers.directionMapper.get(entity);
		//PositionComponent position = Mappers.positionMapper.get(entity);
		MovementCommand move = Mappers.movementMapper.get(entity);
		PositionComponent position = Mappers.positionMapper.get(entity);
		SpeedComponent speed = Mappers.speedMapper.get(entity);
		SpriteComponent sprite = Mappers.spriteMapper.get(entity);
		
		if (position.position.x < move.position.x) {
			position.position.x += speed.speed * deltaTime;
		} else if (position.position.x > move.position.x) {
			position.position.x -= speed.speed * deltaTime;
		}
		
		
		if (position.position.y < move.position.y) {
			position.position.y += speed.speed * deltaTime;
		} else if (position.position.y > move.position.y) {
			position.position.y -= speed.speed * deltaTime;
		}
		
		Debug.printCommaSeperated(position.position.x, "to ", move.position.x, " | " ,position.position.y, "to ", move.position.y);
		
		// TEMP, WILL NEED A CLASS TO CONFORM GFX TO LOGIC
		if (sprite != null) {
			sprite.sprite.setCenter(position.position.x, position.position.y);
		}
	}	
}
