package com.mweis.pathfinder.engine.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;

/*
 * Holds the entire dungeon.
 * 
 * More optimizations include:
 * reducing size of hallways to only fit between rooms
 * fixing small cracks between seemingly connected rooms
 */
public class Dungeon {
	
	public final int WIDTH, HEIGHT, MIN_SIDE_LENGTH, MAX_SIDE_LENGTH, HALL_WIDTH, CORRIDOR_COUNT, ROOM_COUNT, HALL_COUNT;
	public final float MIN_RATIO, MAX_RATIO;
	
	private Room start, end;
	private List<Room> noncriticalRooms, criticalRooms, halls, dungeon;
	private Map<Room, RoomType> roomTypeMap;
	private Map<Room, List<Room>> graph;
	
	private final int unitsPerPartition = 20; // units per square in the spatial partition for vectors -> rooms
	private final int partitionWidth;
	private final Map<Integer, List<Room>> spatialPartition; // where Integer is x+y*unitsPerPartition coord
		
	public Dungeon(Room start, Room end, List<Room> noncriticalRooms, List<Room> criticalRooms, List<Room> halls, Map<Room, List<Room>> graph,
			int minSideLength, int maxSideLength, int hallWidth, float minRatio, float maxRatio) {
		this.start = start;
		this.end = end;
		this.noncriticalRooms = noncriticalRooms;
		this.criticalRooms = criticalRooms;
		this.halls =  halls;
		this.graph = graph;
		this.MIN_SIDE_LENGTH = minSideLength;
		this.MAX_SIDE_LENGTH = maxSideLength;
		this.HALL_WIDTH = hallWidth;
		this.CORRIDOR_COUNT = criticalRooms.size();
		this.ROOM_COUNT = noncriticalRooms.size();
		this.HALL_COUNT = halls.size();
		this.MIN_RATIO = minRatio;
		this.MAX_RATIO = maxRatio;
		
		this.dungeon = new ArrayList<Room>();
		
		this.roomTypeMap = new HashMap<Room, RoomType>();
		for (Room room : noncriticalRooms) {
			this.dungeon.add(room);
			this.roomTypeMap.put(room, RoomType.NONCRITICAL);
		}
		for (Room corridor : criticalRooms) {
			this.dungeon.add(corridor);
			this.roomTypeMap.put(corridor, RoomType.CRITICAL);
		}
		for (Room hall : halls) {
			this.dungeon.add(hall);
			this.roomTypeMap.put(hall, RoomType.HALLWAY);
		}
		
		this.putDungeonInWorldSpace();
		
		this.WIDTH = this.calculateWidth();
		this.HEIGHT = this.calculateHeight();
		
		this.partitionWidth = (int) Math.ceil((double)this.WIDTH / this.unitsPerPartition);
		this.spatialPartition = createSpatialParition();
	}
	
	public List<Room> getDungeon() {
		return dungeon;
	}
	
	public List<Room> getRooms() {
		return noncriticalRooms;
	}
	
	public List<Room> getHalls() {
		return halls;
	}
	
	public List<Room> getCorridors() {
		return criticalRooms;
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
		
		// DRAW SPATIAL PARTITION MAP
		sr.begin(ShapeRenderer.ShapeType.Line);
		sr.setColor(Color.BLACK);
		for (Integer i : spatialPartition.keySet()) {
			int x = (i % partitionWidth) * unitsPerPartition, y = (i / partitionWidth) * unitsPerPartition;
			sr.rect(x, y, unitsPerPartition, unitsPerPartition);
		}
		
		
		// DRAW GRAPH
		sr.setColor(Color.PINK);
		for (Room start : graph.keySet()) {
			for (Room end : graph.get(start)) {
				sr.line(start.getCenterX(), start.getCenterY(), end.getCenterX(), end.getCenterY());
			}
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
	
	/*
	 * Because dungeons are created in the realm of -RADIUS to RADIUS, and are never guarenteed to hit the borders
	 * this function will normalize the rightmost and bottommost rooms to (0,y) and (x, 0) respectively
	 * this should only be called once in the constructor
	 */
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
	
	/*
	 * Creates a spatial partition, where world cords / unitsPerPartition map to the rooms.
	 * Make sure dungeon is in world space before calling this method.
	 */
	private Map<Integer, List<Room>> createSpatialParition() {
		Map<Integer, Set<Room>> map = new HashMap<Integer, Set<Room>>();
		
		/*
		 * HORRIBLE RUNTIME. But only needs to be run once per dungeon generation.
		 * It feels like x and y could increment by unitsPerPartition each time, but this produces weird results
		 */
		for (int y=0; y < HEIGHT; y += 1) {
			for (int x=0; x < WIDTH; x += 1) {
				int key = calculatePartitionKey(x, y);
				for (Room room : this.getDungeon()) {
					if (room.getBounds().contains(x, y)) {
						if (map.containsKey(key)) {
							map.get(key).add(room);
						} else {
							map.put(key, new HashSet<Room>());
						}
					}
				}
			}
		}
		
		Map<Integer, List<Room>> ret = new HashMap<Integer, List<Room>>();
		for (Integer key : map.keySet()) {
			ret.put(key, new ArrayList<Room>(map.get(key)));
		}
		return ret;
	}
	
	private Set<Room> getPotentialRoomsInArea(Rectangle area) {
		// create a list of rooms that area could potentially have from spatial partition
		Set<Room> potentialRooms = new HashSet<Room>();
		
		Integer aa = calculatePartitionKey(area.x, area.y);
		Integer bb = calculatePartitionKey(area.x + area.width, area.y);
		Integer cc = calculatePartitionKey(area.x, area.y + area.height);
		Integer dd = calculatePartitionKey(area.x + area.width, area.y + area.height);
		
		// no repeat rooms thanks to hashset
		if (spatialPartition.containsKey(aa)) {
			potentialRooms.addAll(spatialPartition.get(aa));
		} else if (aa != bb) {
			if (spatialPartition.containsKey(bb)) {
				potentialRooms.addAll(spatialPartition.get(bb));
			} else if (bb != cc) {
				if (spatialPartition.containsKey(cc)) {
					potentialRooms.addAll(spatialPartition.get(cc));
				} else if (cc != dd) {
					if (spatialPartition.containsKey(dd)) {
						potentialRooms.addAll(spatialPartition.get(dd));
					}
				}
			}
		} else {
			if (bb != cc) {
				if (spatialPartition.containsKey(cc)) {
					potentialRooms.addAll(spatialPartition.get(cc));
				} else if (cc != dd) {
					if (spatialPartition.containsKey(dd)) {
						potentialRooms.addAll(spatialPartition.get(dd));
					}
				}
			} else {
				if (cc != dd) {
					if (spatialPartition.containsKey(dd)) {
						potentialRooms.addAll(spatialPartition.get(dd));
					}
				}
			}
		}
		return potentialRooms;
	}
	
	public List<Room> getRoomsInArea(Rectangle area) {
		int biggest = HALL_WIDTH > MIN_SIDE_LENGTH ? HALL_WIDTH : MIN_SIDE_LENGTH;
		if (area.width > biggest || area.height > biggest) {
			try {
				throw new Exception();
			} catch (Exception e) {
				System.out.println("WARNING: Area -> Room algorithm has no case for entities larger than rooms");
				e.printStackTrace();
			}
		}
		
		// create a list of rooms that area could potentially have from spatial partition
		Set<Room> potentialRooms = this.getPotentialRoomsInArea(area);
		
		// perform a bounds check on the potential rooms
		List<Room> rooms = new ArrayList<Room>(potentialRooms.size());
		for (Room room : potentialRooms) {			
			if (room.getBounds().overlaps(area)) {
				rooms.add(room);
			}
		}
		
		return rooms;
	}
	
	public List<Room> getRoomsContainingArea(Rectangle area) {
		// create a list of rooms that area could potentially have from spatial partition
		Set<Room> potentialRooms = this.getPotentialRoomsInArea(area);
				
		// perform a bounds check on the potential rooms
		List<Room> rooms = new ArrayList<Room>(potentialRooms.size());
		for (Room room : potentialRooms) {			
			if (room.getBounds().contains(area)) {
				rooms.add(room);
			}
		}
		
		return rooms;
	}
	
	private Integer calculatePartitionKey(float x, float y) {
		int px = (int)x / unitsPerPartition, py = (int)y / unitsPerPartition;
		return px + py * partitionWidth;
	}
	
	private Integer calculatePartitionKey(int x, int y) {
		int px = x / unitsPerPartition, py = y / unitsPerPartition;
		return px + py * partitionWidth;
	}
}
