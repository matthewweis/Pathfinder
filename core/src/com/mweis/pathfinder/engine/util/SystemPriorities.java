package com.mweis.pathfinder.engine.util;

/*
 * While the order of processing might not matter for some systems, it does matter in some special cases.
 * All systems should be given an ordering from this enum, but most will be default (especially if normal game logic).
 */
public enum SystemPriorities {
	
	INPUT(0), INPUT_PROCESSER(1), PRE_PROCESSING(2), DEFAULT(3), POST_PROCESSING(4), RENDERING(5);
	
	/*
	 * + Input is for all input.
	 * + Pre Processing is for any system that default systems rely on. An example of this is the PartitionSystem,
	 * which partitions the entities into a spatial map before other systems (movement, collision, etc) handle them
	 * + Default means a system has normal game logic behavior
	 * + Post Processing is for systems which reply on the game state of default. Any analytics go here.
	 * + Rending is for graphics, which reply on a finalized game state (after update) to reflect the changes.
	 */
	
	private final int priority;
	
	private SystemPriorities(int priority) {
		this.priority = priority;
	}
	
	public int get() {
		return priority;
	}
}
