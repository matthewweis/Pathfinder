package com.mweis.pathfinder.engine.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mweis.pathfinder.engine.entity.components.InputComponent;
import com.mweis.pathfinder.engine.entity.components.commands.MovementCommand;
import com.mweis.pathfinder.engine.util.Mappers;
import com.mweis.pathfinder.engine.util.MouseAction;
import com.mweis.pathfinder.engine.util.MouseButtons;
import com.mweis.pathfinder.engine.util.SystemPriorities;

public class PlayerInputSystem extends IteratingSystem {
	
	public PlayerInputSystem() {
		super(Family.all(InputComponent.class).get(), 0);
//		super(Family.all(InputComponent.class).get(), SystemPriorities.INPUT.get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		System.out.println("input tick");
		// if blocks for all *raw* input (which will be replaced by a command pattern)
		InputComponent input = Mappers.inputMapper.get(entity);
		MouseAction action = input.input.wasMousePressed(MouseButtons.RIGHT);
		
		if (action.bool) {
			entity.add(new MovementCommand(action.x, action.y));
		}
	}
	
}
