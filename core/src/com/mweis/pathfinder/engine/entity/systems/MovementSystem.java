package com.mweis.pathfinder.engine.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.Family.Builder;
import com.badlogic.ashley.signals.Listener;
import com.badlogic.ashley.signals.Signal;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.mweis.pathfinder.engine.camera.VirtualViewport;
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
		}
	}
	
//	Family.all(MovementCommand.class, PositionComponent.class, SpeedComponent.class).get()
//	@Override
//	protected void processEntity(Entity entity, float deltaTime) { // this happens for all entities
//		//System.out.println("MovementSystem using dt of: " + deltaTime + " this should be constant.");
//		//DirectionComponent direction = Mappers.directionMapper.get(entity);
//		//PositionComponent position = Mappers.positionMapper.get(entity);
//		System.out.println("movement sys tick");
//		MovementCommand move = Mappers.movementMapper.get(entity);
//		PositionComponent position = Mappers.positionMapper.get(entity);
//		SpeedComponent speed = Mappers.speedMapper.get(entity);
//		SpriteComponent sprite = Mappers.spriteMapper.get(entity);
//		
//		if (position.position.x < move.position.x) {
//			position.position.x += speed.speed * deltaTime;
//		} else if (position.position.x > move.position.x) {
//			position.position.x -= speed.speed * deltaTime;
//		}
//		
//		if (position.position.y < move.position.y) {
//			position.position.y += speed.speed * deltaTime;
//		} else if (position.position.y > move.position.y) {
//			position.position.y -= speed.speed * deltaTime;
//		}
//		
//		Debug.printCommaSeperated(position.position.x, "to ", move.position.x, " | " ,position.position.y, "to ", move.position.y);
//	}	
}
