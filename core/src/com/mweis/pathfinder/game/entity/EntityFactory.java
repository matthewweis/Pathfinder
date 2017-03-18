package com.mweis.pathfinder.game.entity;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.mweis.pathfinder.engine.entity.Behavior;
import com.mweis.pathfinder.engine.entity.components.AnimationComponent;
import com.mweis.pathfinder.engine.entity.components.CollisionComponent;
import com.mweis.pathfinder.engine.entity.components.DirectionComponent;
import com.mweis.pathfinder.engine.entity.components.PositionComponent;
import com.mweis.pathfinder.engine.entity.components.SpeedComponent;
import com.mweis.pathfinder.engine.entity.components.SpriteComponent;
import com.mweis.pathfinder.engine.entity.systems.CollisionSystem;
import com.mweis.pathfinder.engine.views.AnimationMap;
import com.mweis.pathfinder.engine.views.ResourceManager;

public class EntityFactory {
	
	public static Entity spawnMage(float x, float y, float speed, float width, float height, Engine engine) {
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
				TextureRegion[] frames = {tmp[0][0], tmp[0][1], tmp[0][2]};
				return new Animation<TextureRegion>(0.25f, frames);
			}
			@Override
			public Animation<TextureRegion> walkRight() {
				TextureRegion[] frames = {tmp[0][0], tmp[0][1], tmp[0][2]};
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
		
		
		Entity entity = new Entity();
		entity.add(new DirectionComponent());
		entity.add(new PositionComponent(x, y));
		float oy = walkSheet.getWidth() / (FRAME_COLS * 20); // just a little above feet
		float ox = walkSheet.getHeight() / (FRAME_ROWS * 4); // x is midpoint
		
		entity.add(new AnimationComponent(map, new Vector2(ox, oy), width, height));
		entity.add(new SpeedComponent(speed));
		entity.add(new CollisionComponent(null, new Behavior() {
			@Override
			public void perscribeBehavior(Entity entity) {
				System.out.println("mage pokes entity " + entity.toString().substring(31) + ", that'll show him!");
			} }, new Vector2(ox, oy), width, height));
		engine.addEntity(entity);
		
		return entity;
	}
	
	private EntityFactory() {}; // Factories need no instantiation
	
}
