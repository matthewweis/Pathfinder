package com.mweis.pathfinder.engine.entity.components;

import com.badlogic.ashley.core.Component;
import com.mweis.pathfinder.engine.input.InputHandler;

public class InputComponent implements Component {
	public InputHandler input;
	
	public InputComponent(InputHandler input) {
		this.input = input;
	}
}
