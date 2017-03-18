package com.mweis.pathfinder.engine.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mweis.pathfinder.engine.entity.components.DirectionComponent;
import com.mweis.pathfinder.engine.entity.components.PositionComponent;
import com.mweis.pathfinder.engine.entity.components.SpeedComponent;
import com.mweis.pathfinder.engine.entity.components.commands.MovementCommand;
import com.mweis.pathfinder.engine.util.Mappers;
import com.mweis.pathfinder.engine.util.SystemPriorities;

/*
 * The MovementSystem takes all entities which (AT ANY GIVEN TIME) have Movement, Position, and Speed and handles
 * moving them from place A to B using some Math.lerp.
 */

public class MovementSystem extends IteratingSystem {
	Engine engine;
	
	public MovementSystem(Engine engine) {
		super(Family.all(MovementCommand.class, PositionComponent.class, SpeedComponent.class).get(), SystemPriorities.DEFAULT.get());
		this.engine = engine;
	}
	
	@Override
	public void processEntity(Entity entity, float deltaTime) {
		MovementCommand move = Mappers.movementMapper.get(entity);
		PositionComponent position = Mappers.positionMapper.get(entity);
		SpeedComponent speed = Mappers.speedMapper.get(entity);
		
		move.alpha += (speed.speed / move.dist) * deltaTime;
		position.position.set(move.start).lerp(move.end, move.alpha);
		
		// done moving check and dir check
		if (move.alpha >= 1.0f) { // done with lerp, must rm move command
			entity.remove(MovementCommand.class);
		} else if (Mappers.directionMapper.has(entity)) {
			DirectionComponent dir = Mappers.directionMapper.get(entity);
			dir.setAngleRadians((float) Math.atan2(position.position.y - move.end.y, position.position.x - move.end.x));
		}
	}
}
