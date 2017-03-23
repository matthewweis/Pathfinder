package com.mweis.pathfinder.game.entity.behavior.states;

import java.util.Random;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.mweis.pathfinder.engine.entity.components.PositionComponent;
import com.mweis.pathfinder.engine.entity.components.commands.MovementCommand;
import com.mweis.pathfinder.engine.util.Mappers;

public enum ZombieStates implements State<Entity> /* telegram is redundant */ {
	
	IDLE() {
		Random random = new Random();
		
		@Override
		public void enter(Entity entity) {
			entity.remove(MovementCommand.class);
			System.out.println("enter");
		}

		@Override
		public void update(Entity entity) {
			PositionComponent pc = Mappers.positionMapper.get(entity);
			final float minWalk = 50.0f, maxWalk = 60.0f;
			
			if (pc != null) {
				float endX = pc.position.x + minWalk + random.nextFloat() * (maxWalk - minWalk);
				float endY = pc.position.y + minWalk + random.nextFloat() * (maxWalk - minWalk);
				
				if (random.nextBoolean()) {
					endX = -endX;
				}
				if (random.nextBoolean()) {
					endY = -endY;
				}
				 
				entity.add(new MovementCommand(pc.position, endX, endY));
			}
		}

		@Override
		public void exit(Entity entity) {
			float x = 2;
		}

		@Override
		public boolean onMessage(Entity entity, Telegram telegram) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
}
