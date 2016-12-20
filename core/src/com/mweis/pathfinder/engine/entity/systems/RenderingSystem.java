package com.mweis.pathfinder.engine.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mweis.pathfinder.engine.entity.components.PositionComponent;
import com.mweis.pathfinder.engine.entity.components.SpriteComponent;
import com.mweis.pathfinder.engine.util.Debug;
import com.mweis.pathfinder.engine.util.Mappers;
import com.mweis.pathfinder.engine.util.SystemPriorities;

public class RenderingSystem extends IteratingSystem {
	SpriteBatch batch;
	
	public RenderingSystem(SpriteBatch batch) {
		super(Family.all(SpriteComponent.class, PositionComponent.class).get(), SystemPriorities.RENDERING.get());
		this.batch = batch;
	}
	
	public void setSpriteBatch(SpriteBatch batch) {
		this.batch = batch;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		PositionComponent position = Mappers.positionMapper.get(entity);
		SpriteComponent sprite = Mappers.spriteMapper.get(entity);
		
		sprite.sprite.setPosition(position.position.x, position.position.y); // set sprite pos to entity pos
//		sprite.sprite.setOrigin(position.position.x, position.position.y);
		
		if (sprite.sprite != null) {
			if (sprite.sprite.getTexture() != null) {
				sprite.sprite.draw(batch);
			}
		}
	}

}
