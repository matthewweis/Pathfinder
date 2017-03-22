package com.mweis.pathfinder.engine.world.graph;

import java.util.ArrayList;
import java.util.List;

import com.mweis.pathfinder.engine.world.Room;

public class Node {
	public Room room;
	public List<Edge> edges;
	
	public Node(Room room, Edge ... edges) {
		this.room = room;
		this.edges = new ArrayList<Edge>(edges.length);
		for (Edge edge : edges) {
			this.edges.add(edge);
		}
	}
	
	public Node(Room room, List<Edge> edges) {
		this.room = room;
		this.edges = edges;
	}
	
	public Node(Room room) {
		this.room = room;
		this.edges = new ArrayList<Edge>();
	}
}
