package com.mweis.pathfinder.engine.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mweis.pathfinder.engine.entity.components.DirectionComponent;
import com.mweis.pathfinder.engine.entity.components.PositionComponent;
import com.mweis.pathfinder.engine.entity.components.SpeedComponent;
import com.mweis.pathfinder.engine.entity.components.commands.MovementCommand;
import com.mweis.pathfinder.engine.util.Debug;
import com.mweis.pathfinder.engine.util.Mappers;

public class MovementSystem extends IteratingSystem {
	
	public MovementSystem() {
		super(Family.all(DirectionComponent.class, MovementCommand.class, PositionComponent.class, SpeedComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) { // this happens for all entities
		System.out.println("MovementSystem using dt of: " + deltaTime + " this should be constant.");
		DirectionComponent direction = Mappers.directionMapper.get(entity);
		PositionComponent position = Mappers.positionMapper.get(entity);
		SpeedComponent speed = Mappers.speedMapper.get(entity);
		
		if (direction == null) {
			Debug.printCommaSeperated("direction null");
		} else if (position == null){
			Debug.printCommaSeperated("position null");
		} else if (speed  == null){
			Debug.printCommaSeperated("speed null");
		}
		
		// 1st make sure direction is set
		//System.out.println(direction.getRadians() + ", " + position.position.x + ", " + position.position.y + ", " + speed.speed);
	}	
}
