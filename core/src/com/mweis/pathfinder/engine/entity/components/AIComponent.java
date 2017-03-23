package com.mweis.pathfinder.engine.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.mweis.pathfinder.engine.entity.ai.behavior.BehaviorTree;

public class AIComponent implements Component {
	public BehaviorTree behaviorTree;
	public State<Entity> state; // must be updated in system and made in Factory
	
	public AIComponent(BehaviorTree behaviorTree) {
		this.behaviorTree = behaviorTree;
		this.state = null;
	}
	
	public AIComponent(State<Entity> state) {
		this.state = state;
		this.behaviorTree = null;
	}
}
 