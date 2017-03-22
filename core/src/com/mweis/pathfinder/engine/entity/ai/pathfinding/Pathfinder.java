package com.mweis.pathfinder.engine.entity.ai.pathfinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mweis.pathfinder.engine.world.Room;

public class Pathfinder {
	
	/*
	 * A* pathfinding algorithm.
	 */
	public static List<Room> findPath(Room start, Room end, Map<Room, List<Room>> graph) {
		List<Room> open = new ArrayList<Room>(), closed = new ArrayList<Room>();
		boolean foundEnd = false;
		while (!open.isEmpty() && !foundEnd) {
			
		}
		
		return null;
	}
}
