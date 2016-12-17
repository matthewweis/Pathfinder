package com.mweis.pathfinder;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.mweis.pathfinder.engine.views.ScreenManager;
import com.mweis.pathfinder.game.views.screen.GameScreen;

public class PathfinderGame implements ApplicationListener {

	@Override
	public void create() {
		ScreenManager.setScreen(new GameScreen());
	}

	@Override
	public void resize(int width, int height) {
		ScreenManager.getCurrentScreen().resize(width, height);
	}

	@Override
	public void render() {
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
