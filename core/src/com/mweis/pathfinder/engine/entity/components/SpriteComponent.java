package com.mweis.pathfinder.engine.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteComponent implements Component {
	public Sprite sprite;
	
	public SpriteComponent(Sprite sprite) {
		this.sprite = sprite;
	}
}
