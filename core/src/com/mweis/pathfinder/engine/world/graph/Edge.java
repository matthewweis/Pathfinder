package com.mweis.pathfinder.engine.world.graph;

public class Edge {
	public Node a, b;
	public float weight;
	
	public Edge(Node a, Node b, float weight) {
		this.a = a;
		this.b = b;
		this.weight = weight;
	}
}
