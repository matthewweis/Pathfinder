package com.mweis.pathfinder.engine.entity.components;

import com.badlogic.ashley.core.Component;
import com.mweis.pathfinder.engine.entity.ai.behavior.BehaviorTree;

public class AIComponent implements Component {
	public BehaviorTree behaviorTree;
	
	public AIComponent(BehaviorTree behaviorTree) {
		this.behaviorTree = behaviorTree;
	}
}
