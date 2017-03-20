package com.mweis.pathfinder.engine.entity.components;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.mweis.pathfinder.engine.entity.Behavior;

public class CollisionComponent implements Component {
	private float width, height;
	private Behavior ISelf, IPerscription; // leave behaviors null if nothing should occur
	public ArrayList<Entity> collisions = new ArrayList<Entity>(); // keep tracks of entities currently overlapping with this
	
	public Vector2 offset;
	
	/*
	 * Creates a CollisionComponent with no special behaviors.
	 * Width and Height are the dims of the bounding box.
	 */
	public CollisionComponent(Vector2 offset, float width, float height) {
		this.offset = offset;
		this.width = width;
		this.height = height;
		updateBoundingBox();
	}
	
	/*
	 * If only one of the behaviors selfBehavior or perscribeBehavior is desired, leave the other one null.
	 * Width and Height are the dims of the bounding box.
	 */
	public CollisionComponent(Behavior selfBehavior, Behavior perscribeBehavior, Vector2 offset, float width, float height) {
		this(offset, width, height);
		ISelf = selfBehavior;
		IPerscription = perscribeBehavior;
	}
	
	public void setWidth(float width) {
		this.width = width;
		updateBoundingBox();
	}
	
	public void setHeight(float height) {
		this.height = height;
		updateBoundingBox();
	}
	
	public void setOffset(Vector2 offset) {
		this.offset = offset;
		updateBoundingBox();
	}
	
	/*
	 * This boolean tells if objects and other entities may pass through this one.
	 */
	public boolean isCollider = true;
	
	/*
	 * This boolean tells whether or not this entity is currently able to be prescribed behavior by another entity.
	 */
	public boolean isSubjectToInfluence = true;
	
	/*
	 * Any behavior that an entity will exhibit on itself after a collision is described here
	 * An example of this might be a bullet with a universal impact behavior of disposing itself.
	 * 
	 * (this method should always be called (in the system) after both interactionBehaviors have been handled)
	 */
	public void selfBehavior(Entity self) {
		if (ISelf != null) {
			ISelf.perscribeBehavior(self);
		}
	}
	
	/*
	 * Behavior an entity prescribes onto the entity it collided with.
	 * An example of this might be a bullet dealing damage, or a spell causing a debuff.
	 * 
	 * (this method should always be called (in the system) before selfBehavior.
	 */
	public void perscribeBehavior(Entity impactEntity) {
		if (IPerscription != null) {
			IPerscription.perscribeBehavior(impactEntity);
		}
	}
	
	/*
	 * Returns the NORMALIZED bounding box of this entity.
	 * THIS SHOULD ALWAYS BE (0, 0, 0) to (width, height, 0) because the CollisionSystem will denormalize this box!
	 */
	public BoundingBox bbox;
	public BoundingBox getBoundingBox() {
		return bbox;
	}
	
	private void updateBoundingBox() {
		bbox = new BoundingBox(new Vector3(-offset.x, -offset.y, 0.0f), new Vector3(width-offset.x, height-offset.y, 0.0f));
	}
}
