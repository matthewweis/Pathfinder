package com.mweis.pathfinder.engine.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.math.Vector2;
import com.mweis.pathfinder.engine.entity.components.CollisionComponent;
import com.mweis.pathfinder.engine.entity.components.DirectionComponent;
import com.mweis.pathfinder.engine.entity.components.PositionComponent;
import com.mweis.pathfinder.engine.entity.components.SpeedComponent;
import com.mweis.pathfinder.engine.entity.components.commands.PathMovementCommand;
import com.mweis.pathfinder.engine.util.Mappers;
import com.mweis.pathfinder.engine.util.SystemPriorities;
import com.mweis.pathfinder.engine.world.Dungeon;
import com.mweis.pathfinder.engine.world.Room;

public class PathingSystem extends IteratingSystem {
	
	Dungeon dungeon;
	
	public PathingSystem(Dungeon dungeon) {
		super(Family.all(PathMovementCommand.class, PositionComponent.class, SpeedComponent.class).get(), SystemPriorities.INPUT_PROCESSER.get());
		this.dungeon = dungeon;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		PathMovementCommand mc = Mappers.pathMovementMapper.get(entity);
		PositionComponent pc = Mappers.positionMapper.get(entity);
		SpeedComponent sc = Mappers.speedMapper.get(entity);
		
		
		if (mc.reachedNode) {
			if (mc.path.getCount() == 1) {
				entity.remove(PathMovementCommand.class);
				return;
			} else if (mc.path.getCount() > mc.nextNode) { // begin moving towards next node if we reached our last one
				mc.alpha = 0.0f;
				mc.dist = mc.path.nodes.get(mc.nextNode).getCenter().dst(mc.path.nodes.get(mc.nextNode+1).getCenter());
				mc.reachedNode = false;
				mc.nextNode++;
			} else { // done with our journey
				entity.remove(PathMovementCommand.class);
				return;
			}
		}
		
		pc.hasMoved = true; // flag that this entity has moved, meaning the partitionSystem must recalculate its position
		
		Vector2 lastPos = new Vector2(pc.position);
		mc.alpha += (sc.speed / mc.dist) * deltaTime;
		
		if (mc.path.getCount() == 1) {
			pc.position.set(mc.start).lerp(mc.end, mc.alpha);
		} else if (mc.nextNode == 0) {
			pc.position.set(mc.start).lerp(mc.path.get(mc.nextNode).getCenter(), mc.alpha);
		} else if (mc.nextNode == mc.path.getCount()) {
			pc.position.set(mc.path.get(mc.nextNode-1).getCenter()).lerp(mc.end, mc.alpha);
		} else {
			pc.position.set(mc.path.get(mc.nextNode-1).getCenter()).lerp(mc.path.get(mc.nextNode).getCenter(), mc.alpha);
		}

		
		DirectionComponent dir = Mappers.directionMapper.get(entity);
		if (mc.alpha >= 1.0f) { // done with lerp, must rm move command
			mc.reachedNode = true;
			// if we don't ensure the position is right then every lerp gives *slightly* different results
			pc.position.set(mc.path.get(mc.nextNode).getCenter());
		} else if (dir != null) {
			dir.setAngleRadians((float) Math.atan2(pc.position.y - mc.path.get(mc.nextNode).getCenterY(), pc.position.x - mc.path.get(mc.nextNode).getCenterX()));
		}
		
		CollisionComponent cc = Mappers.collisionMapper.get(entity);
		if (cc != null) {						
			if (dungeon.getRoomsContainingArea(cc.getHitBox(pc.position)).size == 0) {
				entity.remove(PathMovementCommand.class);
				pc.position.set(lastPos);
			}
		}
		
	}

}

/*
 		Room startNode = dungeon.getRoomAtPoint(pc.position);
		Room endNode = dungeon.getRoomAtPoint(mc.end);
		
		if (endNode == null) {
			entity.remove(MovementCommand.class);
			return;
		}
		
		Heuristic<Room> heuristic = new Heuristic<Room>() {
			@Override
			public float estimate(Room node, Room endNode) {
				return Math.abs(new Vector2(node.getCenterX(), node.getCenterY()).dst(new Vector2(endNode.getCenterX(), endNode.getCenterY())));
			}
		};
		DefaultGraphPath<Room> outPath = new DefaultGraphPath<Room>();
		pathFinder.searchNodePath(startNode, endNode, heuristic, outPath);
		System.out.println(outPath.nodes.toString());
*/
