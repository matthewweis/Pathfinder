package com.mweis.pathfinder.engine.entity.ai.behavior;

import com.badlogic.ashley.core.Entity;

/*
 * Holds a method so that any behavior (retreat, knockback, debuff, damage, etc) can be perscribed onto an entity.
 * This interface exists to circumvent Ashley's inability to handle abstract components.
 * This allows variance in components without needing type reflection.
 */
public interface Behavior {
	void perscribeBehavior(Entity entity);
}
