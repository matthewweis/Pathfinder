package com.mweis.pathfinder.engine.graph;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.Graph;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Keys;

public class DGraph<N> implements Graph<N> {
	private ObjectMap<N, Array<Connection<N>>> nodes;
	
	public DGraph() {
		this.nodes = new ObjectMap<N, Array<Connection<N>>>();
	}

	@Override
	public Array<Connection<N>> getConnections(N fromNode) {
		return nodes.get(fromNode);
	}
	
	public void addKey(N key) {
		nodes.put(key, new Array<Connection<N>>());
	}
	
	public void addConnection(N key, Connection<N> connection) {
		nodes.get(key).add(connection);
	}
	
	public Keys<N> getKeys() {
		return nodes.keys();
	}
	
	public boolean hasKey(N key) {
		return nodes.containsKey(key);
	}
}
