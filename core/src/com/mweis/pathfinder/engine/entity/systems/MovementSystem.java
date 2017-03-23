package com.mweis.pathfinder.engine.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.math.Vector2;
import com.mweis.pathfinder.engine.entity.components.CollisionComponent;
import com.mweis.pathfinder.engine.entity.components.DirectionComponent;
import com.mweis.pathfinder.engine.entity.components.PositionComponent;
import com.mweis.pathfinder.engine.entity.components.SpeedComponent;
import com.mweis.pathfinder.engine.entity.components.commands.MovementCommand;
import com.mweis.pathfinder.engine.util.Mappers;
import com.mweis.pathfinder.engine.util.SystemPriorities;
import com.mweis.pathfinder.engine.world.Dungeon;
import com.mweis.pathfinder.engine.world.Room;

/*
 * The MovementSystem takes all entities which (AT ANY GIVEN TIME) have Movement, Position, and Speed and handles
 * moving them from place A to B using some Math.lerp.
 * This System will handle all collisions with the world.
 */

public class MovementSystem extends IteratingSystem {
	Dungeon dungeon;
	IndexedAStarPathFinder<Room> pathFinder;
	
	public MovementSystem(Dungeon dungeon) {
		super(Family.all(MovementCommand.class, PositionComponent.class, SpeedComponent.class).get(), SystemPriorities.INPUT_PROCESSER.get());
		this.dungeon = dungeon;
		this.pathFinder = new IndexedAStarPathFinder<Room>(dungeon.getDungeonGraph());
	}
	
	@Override
	public void processEntity(Entity entity, float deltaTime) {
		MovementCommand mc = Mappers.movementMapper.get(entity);
		PositionComponent pc = Mappers.positionMapper.get(entity);
		SpeedComponent sc = Mappers.speedMapper.get(entity);
		pc.hasMoved = true; // flag that this entity has moved, meaning the partitionSystem must recalculate its position
		
		Vector2 lastPos = new Vector2(pc.position);
		
		mc.alpha += (sc.speed / mc.dist) * deltaTime;
		pc.position.set(mc.start).lerp(mc.end, mc.alpha);
		
		DirectionComponent dir = Mappers.directionMapper.get(entity);
		if (mc.alpha >= 1.0f) { // done with lerp, must rm move command
			entity.remove(MovementCommand.class);
			// if we don't ensure the position is right then every lerp gives *slightly* different results
			pc.position.set(mc.end);
		} else if (dir != null) {
			dir.setAngleRadians((float) Math.atan2(pc.position.y - mc.end.y, pc.position.x - mc.end.x));
		}
		
		// if new position is invalid in the world, snap them back to where they were prior and stop moving
		CollisionComponent cc = Mappers.collisionMapper.get(entity);
		if (cc != null) {						
			if (dungeon.getRoomsContainingArea(cc.getHitBox(pc.position)).size == 0) {
				entity.remove(MovementCommand.class);
				pc.position.set(lastPos);
			}
		}
	}
}
