package com.mweis.pathfinder.engine.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Interpolation;
import com.mweis.pathfinder.engine.entity.components.PositionComponent;
import com.mweis.pathfinder.engine.entity.components.SpeedComponent;
import com.mweis.pathfinder.engine.entity.components.SpriteComponent;
import com.mweis.pathfinder.engine.entity.components.commands.MovementCommand;
import com.mweis.pathfinder.engine.util.Debug;
import com.mweis.pathfinder.engine.util.Mappers;

/*
 * The MovementSystem takes all entities which (AT ANY GIVEN TIME) have Movement, Position, and Speed and handles
 * moving them from place A to B using some Math.lerp.
 * 
 * Because this MovementSystem handles a MovementCommand (note Command, not Component) it must inherit
 * EntitySystem rather than IteratingSystem in order to update the list each tick.
 * 
 * TODO: Change update entities per tick into update entities per MovementSystem added if possible
 */

public class MovementSystem extends EntitySystem {
	
	Engine engine;
	Family family = Family.all(MovementCommand.class, PositionComponent.class, SpeedComponent.class).get();
	
	public MovementSystem(Engine engine) {
		this.engine = engine;
	}
	
	public void update (float deltaTime) {
		for (Entity entity : engine.getEntitiesFor(family)) {
			System.out.println("movement sys tick");
			MovementCommand move = Mappers.movementMapper.get(entity);
			PositionComponent position = Mappers.positionMapper.get(entity);
			SpeedComponent speed = Mappers.speedMapper.get(entity);
			SpriteComponent sprite = Mappers.spriteMapper.get(entity);
			
			
			// MAKE THIS A LERP
			move.alpha += deltaTime;
			position.position = position.position.lerp(move.position, move.alpha);
			
			Debug.printCommaSeperated(position.position.x, "to ", move.position.x, " | " ,position.position.y, "to ", move.position.y);
			Debug.printCommaSeperated(move.alpha);
			
			if (move.alpha >= 1.0f) { // done with lerp, must rm move command
				Debug.printCommaSeperated("REMOVE", move.alpha);
				entity.remove(MovementCommand.class);
			}
		}
	}
}
