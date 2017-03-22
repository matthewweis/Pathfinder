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
import com.badlogic.gdx.math.Vector2;
import com.mweis.pathfinder.engine.world.graph.Edge;
import com.mweis.pathfinder.engine.world.graph.Graph;
import com.mweis.pathfinder.engine.world.graph.Node;

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
	
	private Room startRoom, endRoom;
	private List<Room> noncriticalRooms, criticalRooms, halls, dungeon;
	private Map<Room, RoomType> roomTypeMap;
	private Map<Room, List<Room>> criticalRoomGraph;
	private Graph dungeonGraph;
//	private Map<Room, List<Room>> testMap;
	
	public final int UNITS_PER_PARTITION = 20; // units per square in the spatial partition for vectors -> rooms
	public final int PARTITION_WIDTH;
	private final Map<Integer, List<Room>> spatialPartition; // where Integer is x+y*unitsPerPartition coord
		
	public Dungeon(Room start, Room end, List<Room> noncriticalRooms, List<Room> criticalRooms, List<Room> halls, Map<Room, List<Room>> criticalRoomGraph,
			int minSideLength, int maxSideLength, int hallWidth, float minRatio, float maxRatio) {
		this.startRoom = start;
		this.endRoom = end;
		this.noncriticalRooms = noncriticalRooms;
		this.criticalRooms = criticalRooms;
		this.halls =  halls;
		this.criticalRoomGraph = criticalRoomGraph;
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
		
		this.PARTITION_WIDTH = (int) Math.ceil((double)this.WIDTH / this.UNITS_PER_PARTITION);
		this.spatialPartition = this.createSpatialParition();
		this.dungeonGraph = this.createDungeonGraph();
//		this.testMap = this.createTestGraph();
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
	
	public Map<Room, List<Room>> getCriticalRoomGraph() {
		return criticalRoomGraph;
	}
	
	public Graph getDungeonGraph() {
		return dungeonGraph;
	}
	
	public Room getStartRoom() {
		return startRoom;
	}
	
	public Room getEndRoom() {
		return endRoom;
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
		sr.rect(startRoom.getLeft(), startRoom.getBottom(), startRoom.getWidth(), startRoom.getHeight());
		sr.setColor(Color.GOLD);
		sr.rect(endRoom.getLeft(), endRoom.getBottom(), endRoom.getWidth(), endRoom.getHeight());
		
		sr.setColor(Color.RED);
		for (Room halls : getHalls()) {
			sr.rect(halls.getLeft(), halls.getBottom(), halls.getWidth(), halls.getHeight());
		}
		
		sr.end();
		
		// DRAW SPATIAL PARTITION MAP
		sr.begin(ShapeRenderer.ShapeType.Line);
		sr.setColor(Color.BLACK);
		for (Integer i : spatialPartition.keySet()) {
			int x = (i % PARTITION_WIDTH) * UNITS_PER_PARTITION, y = (i / PARTITION_WIDTH) * UNITS_PER_PARTITION;
			sr.rect(x, y, UNITS_PER_PARTITION, UNITS_PER_PARTITION);
		}
		
		
		// DRAW CRITICAL GRAPH
//		sr.setColor(Color.CYAN);
//		for (Room start : criticalRoomGraph.keySet()) {
//			for (Room end : criticalRoomGraph.get(start)) {
//				sr.line(start.getCenterX(), start.getCenterY(), end.getCenterX(), end.getCenterY());
//			}
//		}
		
		// DRAW DUNGEON GRAPH
		sr.setColor(Color.BLACK);
		for (Room room : dungeonGraph.nodes.keySet()) {
			for (Edge edge : dungeonGraph.nodes.get(room).edges) {
				Room a = edge.a.room;
				Room b = edge.b.room;
				sr.line(a.getCenterX(), a.getCenterY(), b.getCenterX(), b.getCenterY());
			}
		}
		
		// DRAW TEST GRAPH
//		sr.setColor(Color.WHITE);
//		for (Room a : testMap.keySet()) {
//			for (Room b : testMap.get(a)) {
//				sr.line(a.getCenterX(), a.getCenterY(), b.getCenterX(), b.getCenterY());
//			}
//		}
		
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
	
	private Graph createDungeonGraph() {
		/*
		 * Rooms connect iff a hall passes through them
		 * a hall is a room
		 */
		Graph graph = new Graph();//new HashMap<Room, List<Room>>();
		
		// because a hallway will always connect two rooms, we use this as a reference for graph building
		for (Room hall : this.getHalls()) {
			for (Room room : this.getDungeon()) {
				if (this.roomTypeMap.get(room) != RoomType.HALLWAY) { // this also implicitly checks that hall != room
					if (hall.touches(room)) {
						float dist = new Vector2(hall.getCenterX(), hall.getCenterY()).dst(new Vector2(room.getCenterX(), room.getCenterY()));
						Node n1, n2;
						
						if (graph.nodes.containsKey(hall)) {
							n1 = graph.nodes.get(hall);
						} else {
							n1 = new Node(hall);
							graph.nodes.put(hall, n1);
						}
						
						if (graph.nodes.containsKey(room)) {
							n2 = graph.nodes.get(room);
						} else {
							n2 = new Node(room);
							graph.nodes.put(room, n2);
						}
						
						Edge edge = new Edge(n1, n2, dist);
						
						n1.edges.add(edge);
						n2.edges.add(edge);
					}
				}
			}
		}
		
		for (int i=0; i < getHalls().size(); i++) {
			for (int j=i+1; j < getHalls().size(); j++) {
				Room h1 = getHalls().get(i);
				Room h2 = getHalls().get(j);
				if (h1.touches(h2)) {
					System.out.println("connected 2 halls");
					float dist = new Vector2(h1.getCenterX(), h1.getCenterY()).dst(new Vector2(h2.getCenterX(), h2.getCenterY()));
					Node n1 = graph.nodes.get(h1);
					Node n2 = graph.nodes.get(h2);
					Edge edge = new Edge(n1, n2, dist);
					n1.edges.add(edge);
					n2.edges.add(edge);
				}
			}
		}
		
		return graph;
	}
	
	private Map<Room, List<Room>> createTestGraph() {
		/*
		 * Rooms connect iff a hall passes through them
		 * a hall is a room
		 */
		HashMap<Room, List<Room>> graph = new HashMap<Room, List<Room>>();
		
		// because a hallway will always connect two rooms, we use this as a reference for graph building
		for (Room hall : this.getHalls()) {
			for (Room room : this.getDungeon()) {
				if (this.roomTypeMap.get(room) != RoomType.HALLWAY) { // this also implicitly checks that hall != room
					if (hall.touches(room)) {
						if (graph.containsKey(hall)) {
							graph.get(hall).add(room);
						} else {
							ArrayList<Room> l = new ArrayList<Room>(1);
							l.add(room);
							graph.put(hall, l);
						}
						if (graph.containsKey(room)) {
							graph.get(room).add(hall);
						} else {
							ArrayList<Room> l = new ArrayList<Room>(1);
							l.add(hall);
							graph.put(room, l);
						}
					}
				}
			}
		}
		
		return graph;
	}
	
	/*
	 * Creates a spatial partition, where world cords / unitsPerPartition map to the rooms.
	 * Make sure dungeon is in world space before calling this method.
	 */
	private Map<Integer, List<Room>> createSpatialParition() {
		Map<Integer, Set<Room>> map = new HashMap<Integer, Set<Room>>(); // no repeats
		
		/*
		 * HORRIBLE RUNTIME. But only needs to be run once per dungeon generation.
		 * It feels like x and y could increment by unitsPerPartition each time, but this produces weird results
		 */
		for (Room room : this.getDungeon()) {
			for (int y=room.getBottom(); y <= room.getTop(); y++) {
				for (int x=room.getLeft(); x <= room.getRight(); x++) {
					int key = calculatePartitionKey(x, y);
					if (map.containsKey(key)) {
						map.get(key).add(room);
					} else {
						map.put(key, new HashSet<Room>());
					}
				}
			}
		}
//		for (int y=0; y < HEIGHT; y += 1) {
//			for (int x=0; x < WIDTH; x += 1) {
//				int key = calculatePartitionKey(x, y);
//				for (Room room : this.getDungeon()) {
//					if (room.getBounds().contains(x, y)) {
//						if (map.containsKey(key)) {
//							map.get(key).add(room);
//						} else {
//							map.put(key, new HashSet<Room>());
//						}
//					}
//				}
//			}
//		}
		
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
	
	public Integer calculatePartitionKey(float x, float y) {
		int px = (int)x / UNITS_PER_PARTITION, py = (int)y / UNITS_PER_PARTITION;
		return px + py * PARTITION_WIDTH;
	}
	
	public Integer calculatePartitionKey(int x, int y) {
		int px = x / UNITS_PER_PARTITION, py = y / UNITS_PER_PARTITION;
		return px + py * PARTITION_WIDTH;
	}
}
