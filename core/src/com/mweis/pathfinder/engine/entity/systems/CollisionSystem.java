package com.mweis.pathfinder.engine.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.mweis.pathfinder.engine.entity.components.CollisionComponent;
import com.mweis.pathfinder.engine.entity.components.PositionComponent;
import com.mweis.pathfinder.engine.util.Debug;
import com.mweis.pathfinder.engine.util.Mappers;
import com.mweis.pathfinder.engine.util.SystemPriorities;

public class CollisionSystem extends IteratingSystem {
	
	private Engine engine;
	private Family family;
	private Matrix4 combined; // TEMP FOR COLL DEBUG
	
	public CollisionSystem(Engine engine) {
		super(Family.all(CollisionComponent.class, PositionComponent.class).get(), SystemPriorities.DEFAULT.get());
		this.engine = engine;
		this.family = Family.all(CollisionComponent.class, PositionComponent.class).get();
	}
	
	public void update(Matrix4 combined) { // TEMP FOR COLL DEBUG
		this.combined = combined;
	}
	
	@Override
	public void processEntity(Entity e1, float deltaTime) {
		// horrible n^2 entity coll check. Temporary implementation
		CollisionComponent c1 = Mappers.collisionMapper.get(e1);
		
		if (c1.isCollider) {
			for (Entity e2 : engine.getEntitiesFor(family)) {
				if (e1 != e2) {
					CollisionComponent c2 = Mappers.collisionMapper.get(e2);
					if (c2.isCollider) {
						// pull each bounding box and transform it by position.
						Vector2 p1 = Mappers.positionMapper.get(e1).position;
						BoundingBox b1 = c1.getBoundingBox().mul(new Matrix4().trn(new Vector3(p1.x, p1.y, 0.0f)));
						
						Vector2 p2 = Mappers.positionMapper.get(e2).position;
						BoundingBox b2 = c2.getBoundingBox().mul(new Matrix4().trn(new Vector3(p2.x, p2.y, 0.0f)));
						
//						System.out.println(b1.toString() + ", " + b2.toString());
						
						// TEMP FOR COLL DEBUG
						Debug.DrawDebugLine(new Vector2(b1.min.x, b1.min.y), new Vector2(b1.min.x, b1.max.y), combined);
						Debug.DrawDebugLine(new Vector2(b1.min.x, b1.min.y), new Vector2(b1.max.x, b1.min.y), combined);
						Debug.DrawDebugLine(new Vector2(b1.max.x, b1.max.y), new Vector2(b1.max.x, b1.min.y), combined);
						Debug.DrawDebugLine(new Vector2(b1.max.x, b1.max.y), new Vector2(b1.min.x, b1.max.y), combined);
						
						boolean alreadyOverlapping = c1.collisions.contains(e2);
						boolean overlapping = b1.intersects(b2);
						
						/*
						 * WARNING!!!!!
						 * IF AN ENTITY'S isCollider BOOL CHANGES THEN IT MIGHT REMAIN ON THE COLLS LIST LONGER THAN IT SHOULD
						 * THIS MEANS ENTITIYS COULD ACCIDENTLY OVERLAP. THIS WILL NEED TO CHANGE THEN SPATIAL PARTITIONING IS INTRODUCED
						 */
						
						
						if (overlapping && !alreadyOverlapping) {
							// only handle 1-way collision because this algorithm will check twice
							// can be improved with for i=1..n, j=i+1..n
							c1.selfBehavior(e1);
							if (c2.isSubjectToInfluence) {
								c1.perscribeBehavior(e2);
							}
							c1.collisions.add(e2);
						} else if (!overlapping && alreadyOverlapping) {
							c1.collisions.remove(e2);
						}
					}
				}
			}
		}
	}
}


//public class CollisionSystem extends EntitySystem implements EntityListener {
//	
//	private Engine engine;
//	private Family family;
//	private ImmutableArray<Entity> entities;
//	
//	public CollisionSystem(Engine engine) {
//		super(SystemPriorities.DEFAULT.get());
//		this.engine = engine;
//		this.family = Family.all(CollisionComponent.class, PositionComponent.class).get();
//	}
//	
//	@Override
//	public void update(float deltaTime) {
//		// horrible n^2 entity coll check. Temporary implementation
//		for (Entity e1 : entities) {
//			CollisionComponent c1 = Mappers.collisionMapper.get(e1);
//			if (c1.isCollider) {
//				for (Entity e2 : entities) {
//					if (e1 != e2) {
//						CollisionComponent c2 = Mappers.collisionMapper.get(e2);
//						if (c2.isCollider) {
//							// pull each bounding box and transform it by position.
//							Vector2 p1 = Mappers.positionMapper.get(e1).position;
//							BoundingBox b1 = c1.getBoundingBox().mul(new Matrix4().trn(new Vector3(p1.x, p1.y, 0.0f)));
//							
//							Vector2 p2 = Mappers.positionMapper.get(e2).position;
//							BoundingBox b2 = c2.getBoundingBox().mul(new Matrix4().trn(new Vector3(p2.x, p2.y, 0.0f)));
//							
//							System.out.println(b1.toString() + ", " + b2.toString());
//							
//							if (b1.intersects(b2)) {
//								// only handle 1-way collision because this algorithm will check twice
//								// can be improved with for i=1..n, j=i+1..n
//								c1.selfBehavior();
//								c1.perscribeBehavior(e2);
//							}
//						}
//					}
//				}
//			}
//		}
//	}
//	
//	/*
//	 * Called when this system is added to engine.
//	 */
//	@Override
//	public void addedToEngine(Engine engine) {
//		System.out.println("added to engine");
//        entities = engine.getEntitiesFor(family);
//    }
//	
//	/*
//	 * Called when an entity is added, so we can update the entity array accordingly.
//	 */
//	@Override
//	public void entityAdded(Entity entity) {
//		if (family.matches(entity)) {
//			entities = engine.getEntitiesFor(family);
//		}
//	}
//	
//	/*
//	 * Called when an entity is removed, so we can update the entity array accordingly.
//	 */
//	@Override
//	public void entityRemoved(Entity entity) {
//		if (family.matches(entity)) {
//			entities = engine.getEntitiesFor(family);
//		}
//	}
//}
