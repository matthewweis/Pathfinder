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
import com.mweis.pathfinder.engine.entity.ai.behavior.Behavior;
import com.mweis.pathfinder.engine.entity.ai.behavior.BehaviorTree;
import com.mweis.pathfinder.engine.entity.ai.behavior.Conditional;
import com.mweis.pathfinder.engine.entity.ai.behavior.ConditionalLeaf;
import com.mweis.pathfinder.engine.entity.components.AIComponent;
import com.mweis.pathfinder.engine.entity.components.AnimationComponent;
import com.mweis.pathfinder.engine.entity.components.CollisionComponent;
import com.mweis.pathfinder.engine.entity.components.DirectionComponent;
import com.mweis.pathfinder.engine.entity.components.PositionComponent;
import com.mweis.pathfinder.engine.entity.components.SpeedComponent;
import com.mweis.pathfinder.engine.entity.components.SpriteComponent;
import com.mweis.pathfinder.engine.entity.components.commands.MovementCommand;
import com.mweis.pathfinder.engine.entity.systems.CollisionSystem;
import com.mweis.pathfinder.engine.util.Mappers;
import com.mweis.pathfinder.engine.views.AnimationMap;
import com.mweis.pathfinder.engine.views.ResourceManager;

public class EntityFactory {
	
	public static Entity spawnMage(float x, float y, float speed, float scale, Engine engine) {
		Texture walkSheet = ResourceManager.getTexture("mage");
		final int FRAME_COLS = 5;
		final int FRAME_ROWS = 5;
		
		TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);
		
		float ratioY = scale / (walkSheet.getHeight() / FRAME_ROWS);
		float ratioX = scale / (walkSheet.getWidth() / FRAME_COLS);
		
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
		float anim_ox = ratioX * walkSheet.getHeight() / (FRAME_ROWS * 2); // x is midpoint
		float anim_oy = ratioY * walkSheet.getWidth() / (FRAME_COLS * 10); // just a little above feet
		
		entity.add(new AnimationComponent(map, new Vector2(anim_ox, anim_oy), scale, scale));
		entity.add(new SpeedComponent(speed));
		
		// each sprite is in a 60x60 box
		
		float coll_ox = scale * .1f;
		float coll_oy = scale * 0.10f;
		
		entity.add(new CollisionComponent(new Behavior() {
			@Override
			public void perscribeBehavior(Entity entity) {

			}
		}, new Behavior() {
			@Override
			public void perscribeBehavior(Entity entity) {
				System.out.println("mage pokes entity " + entity.toString().substring(31) + ", that'll show him!");
			} }, -coll_ox, -coll_oy, scale * 0.25f, scale * 0.75f));
		engine.addEntity(entity);
		
		return entity;
	}
	
	public static Entity spawnAiTest(Entity player, float x, float y, float speed, float scale, Engine engine) {
		Texture walkSheet = ResourceManager.getTexture("mage");
		final int FRAME_COLS = 5;
		final int FRAME_ROWS = 5;
		
		TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);
		
		float ratioY = scale / (walkSheet.getHeight() / FRAME_ROWS);
		float ratioX = scale / (walkSheet.getWidth() / FRAME_COLS);
		
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
		float anim_ox = ratioX * walkSheet.getHeight() / (FRAME_ROWS * 2); // x is midpoint
		float anim_oy = ratioY * walkSheet.getWidth() / (FRAME_COLS * 10); // just a little above feet
		
		entity.add(new AnimationComponent(map, new Vector2(anim_ox, anim_oy), scale, scale));
		entity.add(new SpeedComponent(speed));
		
		// each sprite is in a 60x60 box
		
		float coll_ox = scale * .1f;
		float coll_oy = scale * 0.10f;
		
		entity.add(new CollisionComponent(new Behavior() {
			@Override
			public void perscribeBehavior(Entity entity) {

			}
		}, new Behavior() {
			@Override
			public void perscribeBehavior(Entity entity) {
				System.out.println("test pokes entity " + entity.toString().substring(31) + ", that'll show him!");
			} }, -coll_ox, -coll_oy, scale * 0.25f, scale * 0.75f));
		
		ConditionalLeaf root = new ConditionalLeaf(
				// CONDITION
				new Conditional() {
					@Override
					public boolean condition() {
						return Mappers.movementMapper.has(player);
					}
				},
				// TRUE BEHAVIOR
				new Behavior(){
					@Override
					public void perscribeBehavior(Entity entity) {
						if (Mappers.positionMapper.has(entity) && Mappers.positionMapper.has(player)) { // run to where they are
							Vector2 myPos = Mappers.positionMapper.get(entity).position;
							Vector2 playerPos = Mappers.positionMapper.get(player).position;
							entity.add(new MovementCommand(myPos.x, myPos.y, playerPos.x, playerPos.y));
						}
					}	
				},
				// FALSE BEHAVIOR
				new Behavior() {
					@Override
					public void perscribeBehavior(Entity entity) {
						if (Mappers.movementMapper.has(entity)) {
							entity.remove(MovementCommand.class);
						}
					}
				});
		BehaviorTree bt = new BehaviorTree(root);
		entity.add(new AIComponent(bt));
		
		engine.addEntity(entity);
		
		return entity;
	}
	
	private EntityFactory() {}; // Factories need no instantiation
	
}
