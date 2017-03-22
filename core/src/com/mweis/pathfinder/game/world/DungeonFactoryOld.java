package com.mweis.pathfinder.game.world;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/*
 * http://www.gamasutra.com/blogs/AAdonaac/20150903/252889/Procedural_Dungeon_Generation_Algorithm.php
 * https://www.reddit.com/r/gamedev/comments/1dlwc4/procedural_dungeon_generation_algorithm_explained/
 * http://tinykeep.com/dungen/
 */
class DungeonFactoryOld {
	
	
	public static void generateDungeon(int cells, float radius) {
		for (Vector2 v : getRandomPointsInCircle(cells, radius)) {
			System.out.println(v.toString());
		}
	}
	
	
	public static Vector2[] getRandomPointsInCircle(int numPoints, float radius) {
		Random random = new Random();
		Vector2[] points = new Vector2[numPoints];
		for (int i=0; i < numPoints; i++) {
			float t = (float) (2 * Math.PI * random.nextFloat());
			float u = (float) (random.nextFloat() + random.nextFloat());
			float r = u > 1.0f ? 2.0f - u : u;
			points[i] = new Vector2(radius*r*(float)Math.cos(t), radius*r*(float)Math.sin(t));
		}
		return points;
	}
	
	/*
	 * min is min width/height a rectangle can be
	 * max is max width/height a rectangle can be
	 */
	public static Rectangle[] makeRectanglesAroundPoints(Vector2[] points, float min, float max) {
		Random random = new Random();
		Rectangle[] rectangles = new Rectangle[points.length];
		for (int i=0; i < points.length; i++) {
			
			float r1 = random.nextFloat();//(float) Math.sqrt(1 - random.nextFloat());
			float r2 = random.nextFloat();//(float) Math.sqrt(random.nextFloat());
			
			float width = min + r1 * (max - min);
			float height = min + r2 * (max - min);
			
			// apply no required room ratio at this point in time
			rectangles[i] = new Rectangle(points[i].x - width/2, points[i].y - height/2, width, height);
		}
		return rectangles;
	}
	
	/*
	 * runtime will be horrible, but this only needs to be run once per level gen
	 */
	public static boolean steerAwayOverlappingRectangles(Rectangle[] r) {
		Arrays.sort(r, new Comparator<Rectangle>() {
			/*
			 * Sort rectangles w.r.t their Y (low to high) so they never need to go down.
			 */
			@Override
			public int compare(Rectangle r1, Rectangle r2) {
				if (r1.getY() < r2.getY()) {
					return 1;
				} else if (r1.getY() > r2.getY()) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		
		boolean overlap = false;
//		while (overlap) {
//			overlap = false;
			for (int i=0; i < r.length; i++) {
				for (int j=0; j < r.length; j++) { // could reduce range of j
					if (i == j) continue;
					if (r[i].overlaps(r[j])) {
						overlap = true;
//						float left = r[i].x + r[i].width - r[j].x;
						float right = r[j].x + r[j].width - r[i].x;
						float up = r[i].y + r[i].height - r[j].y;
//						float down = r[j].y + r[j].height - r[i].y;
						
						// move my minimum amount (which also maximized overlap!)
//						float min = Math.min(left, Math.min(right, Math.min(up, down)));
//						float min = Math.min(left, Math.min(right, up));
						float min = Math.min(right, up);
//						if (left == min) {
//							r[j].setX(r[i].x - r[j].width);
						/*} else */if (right == min) {
							r[j].setX(r[i].x + r[i].width);
						} else if (up == min) {
							r[j].setY(r[i].y + r[i].height);
//						} else if (down == min) {
//							r[j].setY(r[i].y - r[j].height);
						} else { // FOR TESTING ONLY
							try {
								throw new Exception();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
//		}
			return overlap;
	}
	
	/*
	 * http://fisherevans.com/blog/post/dungeon-generation
	 */
	public static boolean seperateRooms(Rectangle[] rooms) {
		float padding = 0.0f;
		float dx, dxa, dxb, dy, dya, dyb; // delta values of overlap
		boolean overlap = true;
//		while (overlap) {
			overlap = false;
			for (int i=0; i < rooms.length; i++) {
				Rectangle a = rooms[i];
				for (int j=i+1; j < rooms.length; j++) { // ! if problem check iter vals of j
					Rectangle b = rooms[j];
//					Rectangle b_padded = new Rectangle(b);
//					b_padded.set(b.x+padding, b.y+padding, b.width-(2*padding), b.height-(2*padding));
					if (a.overlaps(b)) {
						System.out.println(a + " overlaps " + b);
//						overlap = true;
						
						// find 2 smallest deltas required to stop the overlap
						dx = Math.min((a.x+a.width)-b.x+padding, a.x-(b.x+b.width)-padding);
						dy = Math.min(a.y-(b.y+b.height)+padding, (a.y+a.height)-b.y-padding);
						
						
						// only keep the smallest delta
						if (Math.abs(dx) < Math.abs(dy)) {
							dy = 0;
						} else {
							dx = 0;
						}
						
						// create a delta for each rect as half of the whole delta
						dxa = -dx/2;
						dxb = dx+dxa;
						dya = -dy/2;
						dyb = dy+dya;
						
						// shift both rectangles
						a.setPosition(a.x+dxa, a.y+dya);
						b.setPosition(b.x+dxb, b.y+dyb);
						if (Math.max(Math.abs(dx), Math.abs(dy)) > 1E-3){
							overlap = true;
						}
//						overlap = true;
//						System.out.println(Math.max(Math.abs(dx), Math.abs(dy)));
//						System.out.println(b.toString());
					}
				}
			}
//		}
		return overlap;
	}
	
	
	private DungeonFactoryOld() { }; // factories need no instantiation
}
