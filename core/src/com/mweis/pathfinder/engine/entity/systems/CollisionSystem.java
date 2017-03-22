package com.mweis.pathfinder.engine.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.mweis.pathfinder.engine.entity.components.CollisionComponent;
import com.mweis.pathfinder.engine.entity.components.PositionComponent;
import com.mweis.pathfinder.engine.util.Debug;
import com.mweis.pathfinder.engine.util.Mappers;
import com.mweis.pathfinder.engine.util.SystemPriorities;
import com.mweis.pathfinder.engine.world.Room;

/*
 * A collision system to handle ENTITY ON ENTITY collisions.
 * For all concerns with collisions of entities with the world, see the Movement system.
 */
public class CollisionSystem extends IteratingSystem {
	
	private PartitionSystem partitionSystem;
	private Matrix4 combined; // TEMP FOR COLL DEBUG
	
	public CollisionSystem(PartitionSystem partitionSystem) {
		super(Family.all(CollisionComponent.class, PositionComponent.class).get(), SystemPriorities.DEFAULT.get());
		this.partitionSystem = partitionSystem;
	}
	
	public void update(Matrix4 combined) { // TEMP FOR COLL DEBUG
		this.combined = combined;
	}
	
	@Override
	public void processEntity(Entity e1, float deltaTime) {
	
		CollisionComponent c1 = Mappers.collisionMapper.get(e1);
		Vector2 p1 = Mappers.positionMapper.get(e1).position;
		
		Debug.DrawDebugRect(c1.getHitBox(p1), combined);
		
		for (Room room : partitionSystem.roomsContainingEntity(e1)) {
			for (Entity e2 : partitionSystem.entitiesInRoom(room)) {
				if (e1 != e2) {
//					CollisionComponent c1 = Mappers.collisionMapper.get(e1);
//					Vector2 p1 = Mappers.positionMapper.get(e1).position;
					CollisionComponent c2 = Mappers.collisionMapper.get(e2);
					Vector2 p2 = Mappers.positionMapper.get(e2).position;
					
					boolean alreadyOverlapping = c1.collisions.contains(e2, true);
					boolean overlapping = c1.getHitBox(p1).overlaps(c2.getHitBox(p2));
					
					if (overlapping && !alreadyOverlapping) {
						// only handle 1-way collision because this algorithm will check twice
						// can be improved with for i=1..n, j=i+1..n
						c1.selfBehavior(e1);
						if (c2.isSubjectToInfluence) {
							c1.perscribeBehavior(e2);
						}
						c1.collisions.add(e2);
					} else if (!overlapping && alreadyOverlapping) {
						c1.collisions.removeValue(e2, true);
					}
				}
			}
		}
	}
}
