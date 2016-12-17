package com.mweis.pathfinder.game.views.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mweis.pathfinder.engine.entity.components.DirectionComponent;
import com.mweis.pathfinder.engine.entity.components.PositionComponent;
import com.mweis.pathfinder.engine.entity.components.SpeedComponent;
import com.mweis.pathfinder.engine.entity.components.commands.MovementCommand;
import com.mweis.pathfinder.engine.entity.systems.MovementSystem;
import com.mweis.pathfinder.engine.entity.systems.TestSystem;
import com.mweis.pathfinder.engine.util.Debug;

public class GameScreen implements Screen {
	Engine engine = new Engine();
	Entity entity = new Entity();
	Entity entity2 = new Entity();
	
	@Override
	public void show() {
		System.out.println("show");
		Debug.isDebugMode = true;
		System.out.println("INIT");
		
		engine.addSystem(new MovementSystem());
		engine.addSystem(new TestSystem());
		
		entity.add(new MovementCommand());
		entity.add(new DirectionComponent());
		entity.add(new PositionComponent());
		entity.add(new SpeedComponent());
		engine.addEntity(entity);
		entity2.add(new MovementCommand());
		engine.addEntity(entity2);
	}

	@Override
	public void render(float delta) {
		//System.out.println("render");
		//Debug.printCommaSeperated(Gdx.input.getX(), Gdx.input.getY());
		engine.update(delta);
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
