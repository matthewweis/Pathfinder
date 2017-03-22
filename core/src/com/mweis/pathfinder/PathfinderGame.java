package com.mweis.pathfinder;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mweis.pathfinder.engine.views.ScreenManager;
import com.mweis.pathfinder.game.views.screen.GameScreen;

public class PathfinderGame implements ApplicationListener {

	@Override
	public void create() {
		Gdx.graphics.setTitle("Pathfinder");
		ScreenManager.setScreen(new GameScreen());		
	}

	@Override
	public void resize(int width, int height) {
		ScreenManager.getCurrentScreen().resize(width, height);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		ScreenManager.getCurrentScreen().render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void pause() {
		ScreenManager.getCurrentScreen().pause();
	} 

	@Override
	public void resume() {
		ScreenManager.getCurrentScreen().resume();
	}

	@Override
	public void dispose() {
		// NOTE: ScreenManager handles disposal of screens on change. 
		// This only exists for the final screen to close (Graphic resource dump on close)
		ScreenManager.getCurrentScreen().dispose();
	}
}
