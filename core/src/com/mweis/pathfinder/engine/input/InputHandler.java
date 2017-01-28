package com.mweis.pathfinder.engine.input;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mweis.pathfinder.engine.util.Debug;

/*
 * A front-end method set for the back-end class InputListener.
 * Although this should completely be completely static, forcing an instance gives more control over who has access to
 * input. This can be changed if input is desired to be more global.
 */
public class InputHandler {
	
	static HashSet<Integer> keysDown = new HashSet<Integer>(); // only visible to InputListener and this
	static HashSet<Integer> keysUp = new HashSet<Integer>(); // only visible to InputListener and this
	static HashMap<Integer, Vector2> lastMouseButtonsPressed = new HashMap<Integer, Vector2>(); // records the 
	static HashMap<Integer, Vector2> lastMouseButtonsReleased = new HashMap<Integer, Vector2>();
	private static InputListener LISTENER; // never use this, just keep reference to protect from GC
	
	public InputHandler() {
		if (LISTENER == null) {
			LISTENER = new InputListener();
			Gdx.input.setInputProcessor(LISTENER);
		}
	}
	
	public static void update() {
		
		// this needs to not clear down keys not yet released..
		Iterator<Integer> it = keysDown.iterator();
		while (it.hasNext()) {
			Integer i = it.next();
			if (keysDown.contains(i) && keysUp.contains(i)) {
				it.remove();
//				keysDown.remove(i);
				keysUp.remove(i);
			}
		}
		Iterator<Vector2> it2 = lastMouseButtonsPressed.values().iterator();
		while (it.hasNext()) {
			Vector2 i = it2.next();
			Debug.printCommaSeperated("mouse button pressed", i.toString());
			if (lastMouseButtonsPressed.containsKey(i.x) && lastMouseButtonsReleased.containsKey(i.x)) {
				it2.remove();
//				lastMouseButtonsPressed.remove(i.x);
				lastMouseButtonsReleased.remove(i.x);
			}
		}
		
	}
	
	public boolean wasKeyDown(int keycode) {
		return keysDown.contains(keycode);
	} 
	public boolean wasKeyUp(int keycode) {
		return keysUp.contains(keycode);
	}
	
	public MouseAction wasMousePressed(int button) {
		if (lastMouseButtonsPressed.containsKey(button)) {
			return new MouseAction(lastMouseButtonsPressed.get(button), true);
		} else {
			return new MouseAction(null, false);
		}
	}
	
	public MouseAction wasMouseReleased(int button) {
		if (lastMouseButtonsReleased.containsKey(button)) {
			return new MouseAction(lastMouseButtonsReleased.get(button), true);
		} else {
			return new MouseAction(null, false);
		}
	}
	
}
