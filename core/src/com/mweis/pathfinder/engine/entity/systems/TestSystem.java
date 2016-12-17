package com.mweis.pathfinder.engine.entity.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mweis.pathfinder.engine.entity.components.PositionComponent;

public class TestSystem extends EntitySystem {
	
	private ImmutableArray<Entity> entities;
	
	public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class).get());
    }

    public void update(float deltaTime) {
        System.out.println("update!");
    }
}
