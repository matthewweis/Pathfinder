package com.mweis.pathfinder.engine.entity.components.commands;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.math.Vector2;
import com.mweis.pathfinder.engine.world.Room;

public class PathMovementCommand implements Component {
	public DefaultGraphPath<Room> path;
	public int nextNode;
	public float alpha;
	public float dist;
	public boolean reachedNode;
	public Vector2 start, end; // start is only used for pathing to the 1st node on the path
	
	public PathMovementCommand(Vector2 start, Vector2 end, DefaultGraphPath<Room> path) {
		this.path = path;
		if (path.getCount() == 1) {
			this.dist = start.dst(end);
		} else {
			this.dist = start.dst(path.nodes.get(0).getCenter());
		}
		this.start = start;
		this.end = end;
	}
}
