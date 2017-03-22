package com.mweis.pathfinder.engine.world.graph;

import java.util.HashMap;
import java.util.Map;

import com.mweis.pathfinder.engine.world.Room;

public class Graph {
	public Map<Room, Node> nodes;
	
	public Graph(Map<Room, Node> nodes) {
		this.nodes = nodes;
	}
	
	public Graph() {
		this.nodes = new HashMap<Room, Node>();
	}
}
