package com.mweis.pathfinder.engine.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

/*
 * This class is ONLY VISIBILE TO THE INPUTHANDLER PACKAGE!
 * This is used to handle the raw input needed for InputHandler to work,
 * but hides implementation details from the rest of the code.
 */
class InputListener implements InputProcessor {
	
	@Override
	public boolean keyDown(int keycode) {
		InputHandler.keysDown.add(keycode);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		InputHandler.keysUp.add(keycode);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		InputHandler.lastMouseButtonsPressed.put(button, new Vector2(screenX, screenY));
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		InputHandler.lastMouseButtonsReleased.put(button, new Vector2(screenX, screenY));
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
