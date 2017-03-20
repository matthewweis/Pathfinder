package com.mweis.pathfinder.engine.world;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.mweis.pathfinder.engine.entity.components.CollisionComponent;
import com.mweis.pathfinder.engine.entity.components.PositionComponent;

/*
 * Holds the entire dungeon.
 */
public class Dungeon {
	
	public final int WIDTH, HEIGHT, MIN_SIDE_LENGTH, MAX_SIDE_LENGTH, HALL_WIDTH, CORRIDOR_COUNT, ROOM_COUNT, HALL_COUNT;
	public final float MIN_RATIO, MAX_RATIO;
	
	private Room start, end;
	private List<Room> rooms, corridors, halls, dungeon;
	private Map<Room, List<Room>> graph;
	
//	private final int collisionPartitionsX = 10, collisionPartitionsY = 10;
//	private int collisionPartitionWidth, collisionPartitionHeight;
	private BitSet collisionMap;
	
	public Dungeon(Room start, Room end, List<Room> rooms, List<Room> corridors, List<Room> halls, Map<Room, List<Room>> graph,
			int minSideLength, int maxSideLength, int hallWidth, float minRatio, float maxRatio) {
		this.start = start;
		this.end = end;
		this.rooms = rooms;
		this.corridors = corridors;
		this.halls = halls;
		this.graph = graph;
		this.MIN_SIDE_LENGTH = minSideLength;
		this.MAX_SIDE_LENGTH = maxSideLength;
		this.HALL_WIDTH = hallWidth;
		this.CORRIDOR_COUNT = corridors.size();
		this.ROOM_COUNT = rooms.size();
		this.HALL_COUNT = halls.size();
		this.MIN_RATIO = minRatio;
		this.MAX_RATIO = maxRatio;
		
		this.dungeon = new ArrayList<Room>();
		this.dungeon.addAll(rooms);
		this.dungeon.addAll(corridors);
		this.dungeon.addAll(halls);
		this.putDungeonInWorldSpace();
		
		this.WIDTH = this.calculateWidth();
		this.HEIGHT = this.calculateHeight();
		
//		this.collisionPartitionWidth = this.WIDTH / this.collisionPartitionsX;
//		this.collisionPartitionHeight = this.HEIGHT / this.collisionPartitionsY;
		
		this.collisionMap = createCollisionMap();
	}
	
	public List<Room> getDungeon() {
		return dungeon;
	}
	
	public List<Room> getRooms() {
		return rooms;
	}
	
	public List<Room> getHalls() {
		return halls;
	}
	
	public List<Room> getCorridors() {
		return corridors;
	}
	
	public Map<Room, List<Room>> getGraph() {
		return graph;
	}
	
	public Room getStartRoom() {
		return start;
	}
	
	public Room getEndRoom() {
		return end;
	}
	
	ShapeRenderer sr = new ShapeRenderer();
	public void render(Matrix4 combined) {
		sr.setProjectionMatrix(combined);
	    sr.begin(ShapeRenderer.ShapeType.Filled);
	    
	    sr.setColor(Color.BROWN);
	    for (Room corridor : getCorridors()) {
	    	sr.rect(corridor.getLeft(), corridor.getBottom(), corridor.getWidth(), corridor.getHeight());
	    }
	    sr.setColor(Color.GREEN);
		for (Room rooms : getRooms()) {
			sr.rect(rooms.getLeft(), rooms.getBottom(), rooms.getWidth(), rooms.getHeight());
		}
		// will draw start and end rooms twice, but it's ok to overlap
		sr.setColor(Color.LIGHT_GRAY);
		sr.rect(start.getLeft(), start.getBottom(), start.getWidth(), start.getHeight());
		sr.setColor(Color.GOLD);
		sr.rect(end.getLeft(), end.getBottom(), end.getWidth(), end.getHeight());
		sr.setColor(Color.RED);
		for (Room halls : getHalls()) {
			sr.rect(halls.getLeft(), halls.getBottom(), halls.getWidth(), halls.getHeight());
		}
	    
	    sr.end();
	}
	
	private int calculateWidth() {
		int leftmostWall = Integer.MAX_VALUE;
		int rightmostWall = Integer.MIN_VALUE;
		for (Room room : this.getDungeon()) {
			if (room.getLeft() < leftmostWall) {
				leftmostWall = room.getLeft();
			}
			if (room.getRight() > rightmostWall) {
				rightmostWall = room.getRight();
			}
		}
		return rightmostWall - leftmostWall;
	}
	
	private int calculateHeight() {
		int bottomWall = Integer.MAX_VALUE;
		int topWall = Integer.MIN_VALUE;
		for (Room room : this.getDungeon()) {
			if (room.getBottom() < bottomWall) {
				bottomWall = room.getBottom();
			}
			if (room.getTop() > topWall) {
				topWall = room.getTop();
			}
		}
		return topWall - bottomWall;
	}
	
//	/*
//	 * Where entity room is unspecified.
//	 * O(n) time
//	 */
//	public boolean isColliding(CollisionComponent cc) {
//		for (Room room : this.getDungeon()) {
//			if (isColliding(cc, room)) {
//				return true;
//			}
//		}
//		return false;
//	}
//	
	/*
	 * Where entity room is specified.
	 * O(1) time
	 */
	public boolean isColliding(BoundingBox bb) {
		float smallest = Math.min(MIN_SIDE_LENGTH, HALL_WIDTH);
		if (bb.getWidth() < smallest && bb.getHeight() < smallest) { // ensure coll box small enough for corner algorithm to work
			return collisionMap.get((int)bb.min.x+(int)bb.min.y*this.WIDTH) |
					collisionMap.get((int)bb.max.x+(int)bb.max.y*this.WIDTH) |
					collisionMap.get((int)bb.min.x+(int)bb.max.y*this.WIDTH) |
					collisionMap.get((int)bb.max.x+(int)bb.min.y*this.WIDTH);
		} else {
			try {
				throw new Exception();
			} catch (Exception e) {
				System.out.println("no collision algorithm for entities of that size");
				e.printStackTrace();
			}
		}
		return false;
	}
	
	private void putDungeonInWorldSpace() {
		int leftmostWall = Integer.MAX_VALUE;
		int bottomWall = Integer.MAX_VALUE;
		for (Room room : this.getDungeon()) {
			if (room.getLeft() < leftmostWall) {
				leftmostWall = room.getLeft();
			}
			if (room.getBottom() < bottomWall) {
				bottomWall = room.getBottom();
			}
		}
		
		// usually this will shift up and right, but if the leftmost happens to be positive (which is extremely unlikely) this still orients it to 0,0
		int dx = -leftmostWall;
		int dy = -bottomWall;
		
		for (Room room : this.getDungeon()) {
			room.shift(dx, dy);
		}
		
	}
	
	private BitSet createCollisionMap() {
		BitSet collisionMap = new BitSet(this.WIDTH * this.HEIGHT);
		for (Room room : this.getDungeon()) {
			for (int y = room.getBottom(); y < room.getTop(); y++) {
				for (int x = room.getLeft(); x < room.getRight(); x++) {
					collisionMap.set(x+y*this.WIDTH);
				}
			}
		}
		return collisionMap;
	}
}
