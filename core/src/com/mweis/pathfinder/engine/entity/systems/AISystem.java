package com.mweis.pathfinder.engine.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mweis.pathfinder.engine.entity.ai.behavior.Behavior;
import com.mweis.pathfinder.engine.entity.components.AIComponent;
import com.mweis.pathfinder.engine.util.Mappers;
import com.mweis.pathfinder.engine.util.SystemPriorities;

/*
 * A system to handle all entities, and their decision making.
 */
public class AISystem extends IteratingSystem {	
	public AISystem() {
		super(Family.all(AIComponent.class).get(), SystemPriorities.PRE_PROCESSING.get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Behavior currentBehavior = Mappers.aiMapper.get(entity).behaviorTree.getBehavior();
		currentBehavior.perscribeBehavior(entity);
	}
}
