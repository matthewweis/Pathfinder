package com.mweis.pathfinder.engine.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mweis.pathfinder.engine.entity.components.AnimationComponent;
import com.mweis.pathfinder.engine.entity.components.CollisionComponent;
import com.mweis.pathfinder.engine.entity.components.DirectionComponent;
import com.mweis.pathfinder.engine.entity.components.PositionComponent;
import com.mweis.pathfinder.engine.entity.components.SpriteComponent;
import com.mweis.pathfinder.engine.util.Mappers;
import com.mweis.pathfinder.engine.util.SystemPriorities;

public class RenderingSystem extends IteratingSystem {
	SpriteBatch batch; // later in the dev process we will want to calculate max entities and pass it to this batch for efficiency
	
	public RenderingSystem(SpriteBatch batch) {
		super(Family.all(PositionComponent.class).one(SpriteComponent.class, AnimationComponent.class).get(), SystemPriorities.RENDERING.get());
		this.batch = batch;
	}
	
	public void setSpriteBatch(SpriteBatch batch) {
		this.batch = batch;
	}
		
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		batch.begin();
		PositionComponent position = Mappers.positionMapper.get(entity);
		
		if (Mappers.spriteMapper.has(entity)) {
			SpriteComponent sprite = Mappers.spriteMapper.get(entity);
			
			if (sprite.sprite != null) {
				sprite.sprite.setCenter(position.position.x, position.position.y); // set sprite pos to entity pos
			}
			
			if (sprite.sprite != null) {
				if (sprite.sprite.getTexture() != null) {
					sprite.sprite.draw(batch);
				}
			}
		} else if (Mappers.animationMapper.has(entity)) {
			AnimationComponent anim = Mappers.animationMapper.get(entity);
			
			// moving check
			if (Mappers.movementMapper.has(entity)) {
				anim.stateTime += deltaTime; // only update movement when walking
			} else {
				anim.stateTime = 0.0f; // otherwise "reset" to standing frame
			}
			
			TextureRegion currFrame = anim.map._default().getKeyFrame(anim.stateTime, true);
			
			if (Mappers.directionMapper.has(entity)) {
				DirectionComponent dir = Mappers.directionMapper.get(entity);
				if (dir.getDegrees() > -135 && dir.getDegrees() < -45) { // up
					currFrame = anim.map.walkUp().getKeyFrame(anim.stateTime, true);
				} else if (dir.getDegrees() > -45 && dir.getDegrees() < 45) { // left
					currFrame = anim.map.walkLeft().getKeyFrame(anim.stateTime, true);
					currFrame.flip(!currFrame.isFlipX(), false);
				} else if (dir.getDegrees() > 45 && dir.getDegrees() < 135) { // down
					currFrame = anim.map.walkDown().getKeyFrame(anim.stateTime, true);
					currFrame.flip(currFrame.isFlipX(), false);
				} else { // right
					currFrame = anim.map.walkRight().getKeyFrame(anim.stateTime, true);
					currFrame.flip(currFrame.isFlipX(), false);
				}
			}
			
			// draw sprite s.t. position is its midpoint
			batch.draw(currFrame, position.position.x - anim.offset.x, position.position.y - anim.offset.y, anim.width, anim.height);
			batch.end();
		}
	}
}
