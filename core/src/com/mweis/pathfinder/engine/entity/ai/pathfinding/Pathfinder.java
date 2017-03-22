package com.mweis.pathfinder.engine.entity.ai.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.mweis.pathfinder.engine.world.Room;
import com.mweis.pathfinder.engine.world.graph.Edge;
import com.mweis.pathfinder.engine.world.graph.Graph;
import com.mweis.pathfinder.engine.world.graph.Node;

public class Pathfinder {
	
	/*
	 * A* pathfinding algorithm.
	 */
	public static List<Node> findPath(Node start, Node end, Graph graph) {
		
		if (start == end) {
			List<Node> ret = new ArrayList<Node>(1);
			ret.add(start);
			return ret;
		}
		
		
		Stack<Node> open = new Stack<Node>(), closed = new Stack<Node>();
		open.push(graph.nodes.get(start));
		
		
//		Map<Room, Float> gScore = new HashMap<Room, Float>();
//		for (Room room : graph.keySet()) {
//			gScore.put(room, Float.POSITIVE_INFINITY);
//		}
//		gScore.put(start, 0.0f);
		
		
		
		return null;
	}
}
