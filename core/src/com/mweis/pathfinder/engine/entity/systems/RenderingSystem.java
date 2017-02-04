package com.mweis.pathfinder.engine.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mweis.pathfinder.engine.entity.components.AnimationComponent;
import com.mweis.pathfinder.engine.entity.components.DirectionComponent;
import com.mweis.pathfinder.engine.entity.components.PositionComponent;
import com.mweis.pathfinder.engine.entity.components.SpriteComponent;
import com.mweis.pathfinder.engine.util.Mappers;
import com.mweis.pathfinder.engine.util.SystemPriorities;

public class RenderingSystem extends IteratingSystem {
	SpriteBatch batch;
	
	public RenderingSystem(SpriteBatch batch) {
		super(Family.all(PositionComponent.class).one(SpriteComponent.class, AnimationComponent.class).get(), SystemPriorities.RENDERING.get());
		this.batch = batch;
	}
	
	public void setSpriteBatch(SpriteBatch batch) {
		this.batch = batch;
	}
		
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		PositionComponent position = Mappers.positionMapper.get(entity);
		
		if (Mappers.spriteMapper.has(entity)) {
			SpriteComponent sprite = Mappers.spriteMapper.get(entity);
			
			if (sprite.sprite != null) {
				sprite.sprite.setCenter(position.position.x, position.position.y); // set sprite pos to entity pos
			}
//			sprite.sprite.setOrigin(position.position.x, position.position.y);
			
//			if (sprite.sprite != null) {
				if (sprite.sprite.getTexture() != null) {
					sprite.sprite.draw(batch);
//					batch.draw(sprite, position.position.x, position.position.y);
				}
//			}
		} else if (Mappers.animationMapper.has(entity)) {
			AnimationComponent anim = Mappers.animationMapper.get(entity);
			
			// moving check
			if (Mappers.movementMapper.has(entity)) {
				anim.stateTime += deltaTime; // only update movement when walking
			} else {
				anim.stateTime = 0.0f; // otherwise "reset" to standing pose
			}
			
			TextureRegion currFrame = anim.map._default().getKeyFrame(anim.stateTime, true);
			
			
			if (Mappers.directionMapper.has(entity)) {
				DirectionComponent dir = Mappers.directionMapper.get(entity);
				if (dir.getDegrees() > -135 && dir.getDegrees() < -45) {
					currFrame = anim.map.walkUp().getKeyFrame(anim.stateTime, true);
				} else if (dir.getDegrees() > -45 && dir.getDegrees() < 45) {
					currFrame = anim.map.walkLeft().getKeyFrame(anim.stateTime, true);
					currFrame.flip(!currFrame.isFlipX(), false);
				} else if (dir.getDegrees() > 45 && dir.getDegrees() < 135) {
					currFrame = anim.map.walkDown().getKeyFrame(anim.stateTime, true);
					currFrame.flip(currFrame.isFlipX(), false);
				} else {
					currFrame = anim.map.walkRight().getKeyFrame(anim.stateTime, true);
					currFrame.flip(currFrame.isFlipX(), false);
				}
			}
			
//			batch.draw(currFrame, position.position.x, position.position.y, 0.0f, 0.0f, currFrame.getRegionWidth(), 
//					currFrame.getRegionHeight(), 1.0f, 1.0f, rotation);
			if (anim.hasOrigins) {
				batch.draw(currFrame, position.position.x - (anim.originX / 2), position.position.y - (anim.originY / 2), anim.originX, anim.originY);
			} else {
				batch.draw(currFrame, position.position.x, position.position.y);
			}
		}
	}
}
