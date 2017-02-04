package com.mweis.pathfinder.engine.views;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class ResourceManager {
	
	private static final HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
	private static final HashMap<String, Texture> textures = new HashMap<String, Texture>();
	
	public static Sprite loadSprite(String id, String file) {
		Sprite sprite = new Sprite(new Texture(Gdx.files.internal(file)));
		sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		sprites.put(id, sprite);
		return sprite;
	}
	
	public static Sprite loadSprite(String id, String file, int srcX, int srcY, int srcWidth, int srcHeight) {
		Sprite sprite = new Sprite(new Texture(Gdx.files.internal(file)), srcX, srcY, srcWidth, srcHeight);
		sprite.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		sprites.put(id, sprite);
		return sprite;
	}
	
	public static void loadTexture(String id, String file) {
		Texture t = new Texture(Gdx.files.internal(file));
		t.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
		textures.put(id, t);
	}
	
	// direct return can cause problems if sprites are scaled
	public static Sprite getSprite(String id) {
		return sprites.get(id);
	}
	
	public static Texture getTexture(String id) {
		return textures.get(id);
	}
	
	public static void dispose() {
		Iterator it = textures.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        ((Texture)pair.getValue()).dispose();
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    /*// THIS IS ONLY NEEDED WHEN FONTS ARE ADDED
	    it = fonts.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        ((BitmapFont)pair.getValue()).dispose();
	        it.remove(); // avoids a ConcurrentModificationException
	        */
	}
	
	private ResourceManager() {};
}
