package com.mweis.pathfinder.engine.util;

public enum SystemPriorities {
	
	INPUT(-1), DEFAULT(0), RENDERING(1);
	
	private final int priority;
	
	private SystemPriorities(int priority) {
		this.priority = priority;
	}
	
	public int get() {
		return priority;
	}
}
