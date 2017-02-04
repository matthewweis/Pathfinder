package com.mweis.pathfinder.engine.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.mweis.pathfinder.engine.entity.components.DirectionComponent;
import com.mweis.pathfinder.engine.entity.components.PositionComponent;
import com.mweis.pathfinder.engine.entity.components.SpeedComponent;
import com.mweis.pathfinder.engine.entity.components.commands.MovementCommand;
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
	
	/*
	float lastTime = 0.0f;
	float currTime = 0.0f;
	Vector2 lastPos;
	*/
	
	public void update (float deltaTime) {
		for (Entity entity : engine.getEntitiesFor(family)) {
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
			
			
			/*
			if (lastPos == null) {
				lastPos = new Vector2(position.position);
			}
			*/
			
//			Vector2 dir = (move.end.sub(move.start)).nor();
//			position.position = move.start;
			
//			position.position.add(move.dir.scl(speed.speed).scl(deltaTime));
//			if (move.start.dst(position.position) >= move.dist) {
//				Debug.printCommaSeperated("REMOVE", move.alpha);
//				entity.remove(MovementCommand.class);
//			}
			
//			Debug.printCommaSeperated(position.position.x, "to ", position.position.x, " | " ,position.position.y, "to ", position.position.y);
//			Debug.printCommaSeperated(move.alpha);

			/*
			currTime += deltaTime;
			if (currTime > lastTime + 0.05) {
				float calcSpeed = lastPos.dst(position.position) / (currTime - lastTime);
				System.out.println(calcSpeed);
				lastTime = currTime;
				lastPos = new Vector2(position.position);
			}
			*/
		}
	}
}
