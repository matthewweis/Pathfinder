package com.mweis.pathfinder.engine.util;

/*
 * While the order of processing might not matter for some systems, it does matter in some special cases.
 * All systems should be given an ordering from this enum, but most will be default (especially if normal game logic).
 */
public enum SystemPriorities {
	
	INPUT(0), DEFAULT(1), RENDERING(2);
	
	private final int priority;
	
	private SystemPriorities(int priority) {
		this.priority = priority;
	}
	
	public int get() {
		return priority;
	}
}
