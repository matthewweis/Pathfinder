package com.mweis.pathfinder.engine.world;

import com.badlogic.gdx.math.Rectangle;

/*
 * https://github.com/fisherevans/ProceduralGeneration/blob/master/dungeons/src/main/java/com/fisherevans/procedural_generation/dungeons/Room.java
 * This is a variant of the /fisherevans/'s Room class, which was used alongside the DungeonFactory for random generation.
 * This class has more build on top of it.
 */
public class Room implements Comparable<Room> {
	private int _left, _right, _top, _bottom;
	private Rectangle bounds = new Rectangle();
	
	public Room(int x1, int y1, int width, int height) {		
        _left = x1;
        _right = x1;
        if(width < 0)
            _left +=  width;
        else
            _right +=  width;

		_bottom = y1;
		_top = y1;
        if(height < 0)
            _bottom +=  height;
        else
            _top +=  height;
        updateBounds();
	}
	
	public boolean touches(Room b) {
		return touches(b, 0);
	}
	
	public boolean touches(Room b, int padding) {
		return !(b.getLeft()-padding >= this.getRight() ||
				b.getRight() <= this.getLeft()-padding ||
				b.getTop() <= this.getBottom()-padding ||
				b.getBottom()-padding >= this.getTop());
	}
	
	public boolean overlaps(Rectangle area) {
		return bounds.overlaps(area);
//		return !(area.getX() >= this.getRight() ||
//				area.getX() + area.getWidth() <= this.getLeft() ||
//				area.getY() + area.getHeight() <= this.getBottom() ||
//				area.getY() >= this.getTop());				
	}
	
	/*
	 * True if they intersect, but the area is not contained within th
	 */
	public boolean borderTouches(Rectangle area) {
		return area.overlaps(bounds) && !area.contains(bounds) && !bounds.contains(area);
	}
	
	public void expand(int by) {
		_left -= by;
		_right += by;
		_top += by;
		_bottom -= by;
		updateBounds();
	}

    public int getArea() {
        return getWidth()*getHeight();
    }
	
	public int getWidth() {
		return _right - _left;
	}
	
	public int getHeight() {
		return _top - _bottom;
	}
	
	public int getLeft() {
		return _left;
	}
	
	public int getRight() {
		return _right;
	}
	
	public int getTop() {
		return _top;
	}
	
	public int getBottom() {
		return _bottom;
	}
	
	public float getCenterX() {
		return (_left+_right)/2.0f;
	}
	
	public float getCenterY() {
		return (_top+_bottom)/2.0f;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public void shift(int x, int y) {
		_left += x;
		_right += x;
		
		_top += y;
		_bottom += y;
		updateBounds();
	}

    public double getRatio() {
        return getRatio(getWidth(), getHeight());
    }

    public static double getRatio(int width, int height) {
        if(width > height)
            return width/((double)height);
        else
            return height/((double)width);
    }
	
	public String toString() {
		return String.format("[L:%d,  R:%d, T:%d, B:%d]", _left, _right, _top, _bottom);
	}

    public int compareTo(Room room) {
        int d = room.getArea() - getArea();
        if(d == 0) {
            return (int) Math.signum((room.getCenterX()*room.getCenterX()+room.getCenterY()*room.getCenterY())
                    -(getCenterX()*getCenterX()+getCenterY()*getCenterY()));
        } else
            return d;
    }
    
    private void updateBounds() {
    	bounds.set(_left, _bottom, _right - _left, _top - _bottom);
    }
}
