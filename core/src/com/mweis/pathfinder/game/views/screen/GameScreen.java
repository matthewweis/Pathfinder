package com.mweis.pathfinder.game.views.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input.Keys;
import com.mweis.pathfinder.engine.entity.components.commands.MovementCommand;
import com.mweis.pathfinder.engine.entity.systems.MovementSystem;
import com.mweis.pathfinder.engine.entity.systems.RenderingSystem;
import com.mweis.pathfinder.engine.util.Debug;
import com.mweis.pathfinder.engine.views.ResourceManager;
import com.mweis.pathfinder.game.entity.EntityFactory;

public class GameScreen implements Screen {
	Engine engine = new Engine();
	Entity entity = EntityFactory.spawnTestEntity(0.0f, 0.0f, 0.0f, ResourceManager.getSprite("sprite1"));
	SpriteBatch batch = new SpriteBatch();
	
	@Override
	public void show() {
		System.out.println("show");
		Debug.isDebugMode = true;
		System.out.println("INIT");
		
		// ADD SPRITES
		ResourceManager.loadSprite("sprite1", "badlogic.jpg");
		
		engine.addSystem(new MovementSystem());
		engine.addSystem(new RenderingSystem(batch));
		engine.addEntity(entity);
		//batch.begin();
	}

	@Override
	public void render(float delta) {
		batch.begin();
		engine.update(delta);
		batch.end();
		if (Gdx.input.isKeyPressed(Keys.A)) {
			entity.add(new MovementCommand(0.0f, 0.0f));
		} else if (Gdx.input.isKeyPressed(Keys.S)) {
			entity.remove(MovementCommand.class);
		}
	}

	@Override
	public void resize(int width, int height) {
		System.out.println("resize");
	}

	@Override
	public void pause() {
		System.out.println("pause");
	}

	@Override
	public void resume() {
		System.out.println("resume");
	}

	@Override
	public void hide() {
		System.out.println("hide");
	}

	@Override
	public void dispose() {
		System.out.println("dispose");
	}
	
}
