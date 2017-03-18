package com.mweis.pathfinder.engine.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

/*
 * may or may not need, an idea
 */
public abstract class DynamicEntitySystem extends EntitySystem implements EntityListener {
	
	private Engine engine;
	private Family family;
	private ImmutableArray<Entity> entities;
	
	public DynamicEntitySystem(Family family, Engine engine) {
		this.family = family;
		this.engine = engine;
	}
	
	
	@Override
	public void entityAdded(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void entityRemoved(Entity entity) {
		// TODO Auto-generated method stub
		
	}

}
