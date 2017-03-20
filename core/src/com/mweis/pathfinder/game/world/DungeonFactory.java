package com.mweis.pathfinder.game.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.mweis.pathfinder.engine.world.Room;

/*
 * An implementation of
 * https://github.com/fisherevans/ProceduralGeneration/blob/master/dungeons/src/main/java/com/fisherevans/procedural_generation/dungeons/DungeonGenerator.java
 * 
 * Corridors are the amount of "critical" rooms which will be passed through.
 * All intermediate "non-critical" dungeon rooms are included iff the halls pass through them.
 */
public class DungeonFactory {
	
	private static Random random = new Random();
	
	private static final int padding = -1;

	private static final int mapSize = 35*5;
	private static final int minSideLength = 5*5;
	private static final int maxSideLength = 15*5;
	private static final int corridorCount = 35*3;
	private static final int roomCount = 80*5;
	private static final double minRatio = 1.0;
	private static final double maxRatio = 1.5;
	private static final double touchedRoomChance = 1;
	
	
	public static List<List<Room>> generateDungeon() {
		List<Room> rooms = createRooms();
		seperateRooms(rooms);
		List<Room> corridors = findCorridors(rooms);
		centerCorridors(rooms, corridors);
		Map<Room, List<Room>> graph = connectRooms(corridors);
		List<Room> halls = createHalls(graph);
		for (Room hall : halls) {
			hall.expand(1);
		}
		List<Room> untouched = removeUntouched(rooms, halls);
		
		Room start = null, end = null;
		findStartAndEnd(corridors, start, end);
		
		List<List<Room>> ret = new ArrayList<List<Room>>();
		ret.add(rooms);
		ret.add(corridors);
		ret.add(halls);
//		ret.add(untouched);
		return ret;
	}
	
	private static List<Room> createRooms() {
		int width, height, x, y;
		double ratio;
		Room room;
		ArrayList<Room> rooms = new ArrayList<Room>(roomCount);
		for (int i=0; i < roomCount; i++) {
			do {
				width = getRandomSide();
				height = getRandomSide();
				ratio = Room.getRatio(width, height);
			} while (ratio < minRatio || ratio > maxRatio);
			x = getRandomGausInt(mapSize * 2) - mapSize - width/2;
			y = getRandomGausInt(mapSize * 2) - mapSize - height/2;
			room = new Room(x, y, width, height);
			rooms.add(room);
		}
		return rooms;
	}
	
	private static void seperateRooms(List<Room> rooms) {
		Room a, b;
		int dx, dxa, dxb, dy, dya, dyb;
		boolean touching;
		do {
			touching = false;
			for(int i = 0;i < rooms.size();i++) {
				a = rooms.get(i);
				for(int j = i+1;j < rooms.size();j++) {
					b = rooms.get(j);
					if(a.touches(b, padding)) {
						touching = true;
						dx = Math.min(a.getRight()-b.getLeft()+padding, a.getLeft()-b.getRight()-padding);
						dy = Math.min(a.getBottom()-b.getTop()+padding, a.getTop()-b.getBottom()-padding);
						if(Math.abs(dx) < Math.abs(dy)) dy = 0;
						else dx = 0;
						
						dxa = -dx/2;
						dxb = dx+dxa;
						
						dya = -dy/2;
						dyb = dy+dya;

						a.shift(dxa,  dya);
						b.shift(dxb,  dyb);
					}
				}
			}
		} while(touching);
	}
	
    private static List<Room> findCorridors(List<Room> rooms) {
    	Collections.sort(rooms);
    	List<Room> corridors = new ArrayList<Room>();
        for(int i = 0; i < corridorCount; i++) {
        	corridors.add(rooms.remove(0));
        }
        return corridors;
    }
	
	private static void centerCorridors(List<Room> rooms, List<Room> corridors) {
        int left = Integer.MAX_VALUE, right = Integer.MIN_VALUE;
        int top = Integer.MIN_VALUE, bottom = Integer.MAX_VALUE;
        for(Room corridor : corridors) {
            left = Math.min(left, corridor.getLeft());
            right = Math.max(right, corridor.getRight());
            top = Math.max(top, corridor.getTop());
            bottom = Math.min(bottom, corridor.getBottom());
        }
        int shiftX = (right+left)/2;
        int shiftY = (top+bottom)/2;
        for(Room corridor:corridors)
            corridor.shift(-shiftX, -shiftY);
        for(Room room : rooms)
            room.shift(-shiftX, -shiftY);
    }

    private static Map<Room, List<Room>> connectRooms(List<Room> corridors) {
        Room a, b, c;
        Map<Room, List<Room>> graph = new HashMap<Room, List<Room>>();
        double abDist, acDist, bcDist;
        boolean skip;
        for(int i = 0; i < corridors.size(); i++) {
            a = corridors.get(i);
            for(int j = i+1; j < corridors.size(); j++) {
                skip = false;
                b = corridors.get(j);
                abDist = Math.pow(a.getCenterX()-b.getCenterX(), 2) + Math.pow(a.getCenterY()-b.getCenterY(), 2);
                for(int k = 0;k < corridors.size();k++) {
                    if(k == i || k == j)
                        continue;
                    c = corridors.get(k);
                    acDist = Math.pow(a.getCenterX()-c.getCenterX(), 2) + Math.pow(a.getCenterY()-c.getCenterY(), 2);
                    bcDist = Math.pow(b.getCenterX()-c.getCenterX(), 2) + Math.pow(b.getCenterY()-c.getCenterY(), 2);
                    if(acDist < abDist && bcDist < abDist)
                        skip = true;
                    if(skip)
                        break;
                }
                if(!skip) {
                    if(graph.get(a) == null)
                        graph.put(a, new LinkedList<Room>());
                    graph.get(a).add(b);
                }
            }
        }
        return graph;
    }

    private static List<Room> createHalls(Map<Room, List<Room>> graph) {
        int dx, dy, x, y;
        Room a, b;
        List<Room> keys = new ArrayList<Room>();
        List<Room> halls = new ArrayList<Room>();
        keys.addAll(graph.keySet());
        Collections.sort(keys);
        for(Room outer : keys) {
            for(Room inner : graph.get(outer)) {
            	// make sure starting point is to the left
            	if(outer.getCenterX() < inner.getCenterX()) {
                    a = outer;
                    b = inner;
                } else {
                    a = inner;
                    b = outer;
                }
                x = (int) a.getCenterX();
                y = (int) a.getCenterY();
                dx = (int) b.getCenterX()-x;
                dy = (int) b.getCenterY()-y;
                
                if(random.nextInt(1) == 1) {
                    halls.add(new Room(x, y, dx+1, 1));
                    halls.add(new Room(x+dx, y, 1, dy));
                } else {
                    halls.add(new Room(x, y+dy, dx+1, 1));
                    halls.add(new Room(x, y, 1, dy));
                }
            }
        }
        return halls;
    }
    
    private static List<Room> removeUntouched(List<Room> rooms, List<Room> halls) {
    	Room room;
    	List<Room> untouched = new ArrayList<Room>();
    	boolean touched;
    	int i = 0;
    	while (i < rooms.size()) {
    		room = rooms.get(i);
    		touched = false;
    		for(Room hall : halls) {
    			if(room.touches(hall) && random.nextDouble() <= touchedRoomChance) {
    				touched = true;
    				break;
    			}
    		}
    		if(!touched) {
    			untouched.add(rooms.remove(i));
    		} else {
    			i++;
    		}
    	}
    	return untouched;
    }
    
    /*
     * Send list of corridors, and this will return the starting and ending rooms.
     */
    private static void findStartAndEnd(List<Room> corridors, Room start, Room end) {
        Room a, b;
        double maxDist = Double.MIN_VALUE, dist;
        for(int i = 0;i < corridors.size();i++) {
            a = corridors.get(i);
            for(int j = i+1;j < corridors.size();j++) {
                b = corridors.get(j);
                dist = Math.pow(a.getCenterX()-b.getCenterX(), 2) + Math.pow(a.getCenterY()-b.getCenterY(), 2);
                if(dist > maxDist) {
                    maxDist = dist;
                    if(random.nextBoolean()) {
                        start = a;
                        end = b;
                    } else {
                        start = b;
                        end = a;
                    }
                }
            }
        }
}
	
	
	
	private static int getRandomGausInt(int size) {
        double r = random.nextGaussian();
        r *= size/5;
        r += size/2;
        if(r < 0 || r > size)
            return getRandomGausInt(size);
        else
            return (int)r;
    }

    private static int getRandomGausSmallInt(int size) {
        double r = random.nextGaussian();
        r *= size/1.5;
        if(r < 0)
            r *= -1;
        if(r > size)
            return getRandomGausSmallInt(size);
        else
            return (int)r;
    }
	
	private static int getRandomSide() {
		return getRandomGausSmallInt(maxSideLength - minSideLength) + minSideLength;
	}
	
	private DungeonFactory() { } // factories need no instantiation
}
