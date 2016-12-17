package com.mweis.pathfinder.engine.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mweis.pathfinder.engine.entity.components.SpeedComponent;
import com.mweis.pathfinder.engine.entity.components.SpriteComponent;
import com.mweis.pathfinder.engine.entity.components.commands.MovementCommand;
import com.mweis.pathfinder.engine.util.Debug;
import com.mweis.pathfinder.engine.util.Mappers;

public class MovementSystem extends IteratingSystem {
	
	public MovementSystem() {
		super(Family.all(MovementCommand.class, SpeedComponent.class, SpriteComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) { // this happens for all entities
		System.out.println("MovementSystem using dt of: " + deltaTime + " this should be constant.");
		//DirectionComponent direction = Mappers.directionMapper.get(entity);
		//PositionComponent position = Mappers.positionMapper.get(entity);
		SpeedComponent speed = Mappers.speedMapper.get(entity);
		SpriteComponent sprite = Mappers.spriteMapper.get(entity);
		
		// 1st make sure direction is set
		Debug.printCommaSeperated(sprite.sprite.getRotation(), sprite.sprite.getX(), sprite.sprite.getY(), speed.speed);
	}	
}
