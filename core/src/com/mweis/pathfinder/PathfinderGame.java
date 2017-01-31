package com.mweis.pathfinder;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.mweis.pathfinder.engine.input.InputHandler;
import com.mweis.pathfinder.engine.views.ScreenManager;
import com.mweis.pathfinder.game.views.screen.GameScreen;

public class PathfinderGame implements ApplicationListener {

	@Override
	public void create() {
		ScreenManager.setScreen(new GameScreen());
		
		// interpolate test
//		Vector2 start = new Vector2(0.0f, 0.0f);
//		Vector2 end = new Vector2(1.0f, 1.0f);
//		Vector2 position = new Vector2(0.0f,0.0f);
//		
//		for (float alpha = 0.0f; alpha <= 1.0f; alpha += 0.01) {
//			System.out.println(alpha + ": " + position.toString());
//			position.set(start).lerp(end, alpha);
//		}
		
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
//		InputHandler.update();
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
