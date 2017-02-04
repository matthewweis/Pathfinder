package com.mweis.pathfinder.game.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mweis.pathfinder.engine.entity.components.AnimationComponent;
import com.mweis.pathfinder.engine.entity.components.DirectionComponent;
import com.mweis.pathfinder.engine.entity.components.PositionComponent;
import com.mweis.pathfinder.engine.entity.components.SpeedComponent;
import com.mweis.pathfinder.engine.entity.components.SpriteComponent;
import com.mweis.pathfinder.engine.views.AnimationMap;
import com.mweis.pathfinder.engine.views.ResourceManager;

public class EntityFactory {
	
	public static Entity spawnTestEntity(float x, float y, float speed, Sprite sprite, Engine engine) {
		Entity entity = new Entity();
		entity.add(new DirectionComponent());
		entity.add(new PositionComponent(x, y));
		entity.add(new SpriteComponent(sprite));
		entity.add(new SpeedComponent(speed));
		engine.addEntity(entity);
		return entity;
	}
	
	public static Entity spawnTestEntity(float x, float y, float speed, AnimationMap map, Engine engine) {
		Entity entity = new Entity();
		entity.add(new DirectionComponent());
		entity.add(new PositionComponent(x, y));
		entity.add(new AnimationComponent(map));
		entity.add(new SpeedComponent(speed));
		engine.addEntity(entity);
		return entity;
	}
	
	public static Entity spawnTestEntity(float x, float y, float speed, AnimationMap map, float originX, float originY, Engine engine) {
		Entity entity = new Entity();
		entity.add(new DirectionComponent());
		entity.add(new PositionComponent(x, y));
		entity.add(new AnimationComponent(map, originX, originY));
		entity.add(new SpeedComponent(speed));
		engine.addEntity(entity);
		return entity;
	}
	
	public static Entity spawnMage(float x, float y, Engine engine) {
		Texture walkSheet = ResourceManager.getTexture("mage");
		final int FRAME_COLS = 5;
		final int FRAME_ROWS = 5;
		TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);
		
		AnimationMap map = new AnimationMap() {
			
			@Override
			public Animation<TextureRegion> walkUp() {
				TextureRegion[] frames = {tmp[1][2], tmp[1][3], tmp[1][4]};
				return new Animation<TextureRegion>(0.25f, frames);
			}
			@Override
			public Animation<TextureRegion> walkDown() {
				TextureRegion[] frames = {tmp[0][0], tmp[0][1], tmp[0][1]};
				return new Animation<TextureRegion>(0.25f, frames);
			}
			@Override
			public Animation<TextureRegion> walkLeft() {
				TextureRegion[] frames = {tmp[0][0], tmp[0][1], tmp[0][1]};
				return new Animation<TextureRegion>(0.25f, frames);
			}
			@Override
			public Animation<TextureRegion> walkRight() {
				TextureRegion[] frames = {tmp[0][0], tmp[0][1], tmp[0][1]};
				return new Animation<TextureRegion>(0.25f, frames);
			}
			@Override
			public Animation<TextureRegion> crawlLeft() {
				TextureRegion[] frames = {tmp[1][0], tmp[1][1]};
				return new Animation<TextureRegion>(0.25f, frames);
			}
			@Override
			public Animation<TextureRegion> crawlRight() {
				TextureRegion[] frames = {tmp[1][0], tmp[1][1]};
				return new Animation<TextureRegion>(0.25f, frames);
			}
			@Override
			public Animation<TextureRegion> attack() {
				TextureRegion[] frames = {tmp[0][3], tmp[0][4]};
				return new Animation<TextureRegion>(0.25f, frames);
			}
			@Override
			public Animation<TextureRegion> _default() {
				TextureRegion[] frames = {tmp[0][0]};
				return new Animation<TextureRegion>(0.25f, frames);
			}
		};
		
		return EntityFactory.spawnTestEntity(0.0f, 0.0f, 40.0f, map,
				walkSheet.getWidth() / (FRAME_COLS * 2), walkSheet.getHeight() / (FRAME_ROWS * 2), engine);
	}
	
//	public static Entity spawnTestPlayer(float x, float y, float speed, Sprite sprite, Engine engine) {
//		Entity entity = new Entity();
//		entity.add(new DirectionComponent());
//		entity.add(new PositionComponent(x, y));
//		//entity.add(new InputComponent(input));
//		entity.add(new SpeedComponent(speed));
//		entity.add(new SpriteComponent(sprite));
//		engine.addEntity(entity);
//		return entity;
//	}
	
	private EntityFactory() {}; // Factories need no instantiation
	
}
