package com.mweis.pathfinder.engine.entity.ai.behavior;

/*
 *          Condition
 *       /             \
 *     Cond.          behavior
 *    /
 *  beh. beh.
 */
public class BehaviorTree {
	
	private Object root;
	
	/*
	 * Root is either a behavior or a conditional.
	 * Every path down the tree must lead to a behavior.
	 */
	public BehaviorTree(Behavior root) {
		this.root = root;
	}
	
	public BehaviorTree(ConditionalLeaf root) {
		this.root = root;
	}
	
	/*
	 * goes down the tree (based on conditionals) and returns the behavior
	 * This assumes the tree is been properly formed, if it's malformed a casting exception will be thrown
	 */
	public Behavior getBehavior() {
		Object node = root;
		while (node instanceof ConditionalLeaf) {
			node = ((ConditionalLeaf)root).getChild();
		}
		return (Behavior)node;
	}
	
}
