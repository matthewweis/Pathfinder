package com.mweis.pathfinder.engine.entity.ai.behavior;

public class ConditionalLeaf {
	private Object trueChild, falseChild;
	private Conditional condition;
	
	public ConditionalLeaf(Conditional condition, ConditionalLeaf trueChild, ConditionalLeaf falseChild) {
		this.trueChild = trueChild;
		this.falseChild = falseChild;
		this.condition = condition;
	}
	
	public ConditionalLeaf(Conditional condition, Behavior trueChild, Behavior falseChild) {
		this.trueChild = trueChild;
		this.falseChild = falseChild;
		this.condition = condition;
	}
	
	public ConditionalLeaf(Conditional condition, Behavior trueChild, ConditionalLeaf falseChild) {
		this.trueChild = trueChild;
		this.falseChild = falseChild;
		this.condition = condition;
	}
	
	public ConditionalLeaf(Conditional condition, ConditionalLeaf trueChild, Behavior falseChild) {
		this.trueChild = trueChild;
		this.falseChild = falseChild;
		this.condition = condition;
	}
		
	public Object getChild() {
		return condition.condition() ? trueChild : falseChild;
	}
	
}
