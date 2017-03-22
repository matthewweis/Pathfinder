package com.mweis.pathfinder.engine.entity.components;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.mweis.pathfinder.engine.entity.Behavior;

public class CollisionComponent implements Component {
	private Rectangle hitbox; // eventually may want multiple
	private Behavior ISelf, IPerscription; // leave behaviors null if nothing should occur
	public ArrayList<Entity> collisions = new ArrayList<Entity>(); // keep tracks of entities currently overlapping with this
	
	/*
	 * Creates a CollisionComponent with no special behaviors.
	 * Width and Height are the dims of the bounding box.
	 * This is the MODEL SPACE box, and is converted to world space in the getter function.
	 */
	public CollisionComponent(float x, float y, float width, float height) {
		hitbox = new Rectangle(x, y, width, height);
	}
	
	/*
	 * If only one of the behaviors selfBehavior or perscribeBehavior is desired, leave the other one null.
	 * Width and Height are the dims of the bounding box.
	 */
	public CollisionComponent(Behavior selfBehavior, Behavior perscribeBehavior, float x, float y, float width, float height) {
		this(x, y, width, height);
		ISelf = selfBehavior;
		IPerscription = perscribeBehavior;
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
	 * Returns a copy of the hit box which is converted to world space.
	 */
	public Rectangle getHitBox(Vector2 position) {
		return new Rectangle(hitbox.x + position.x, hitbox.y + position.y, hitbox.width, hitbox.height);
	}
}
