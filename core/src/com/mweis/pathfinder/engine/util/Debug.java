package com.mweis.pathfinder.engine.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class Debug {
	
	public static boolean isDebugMode = false;

	public static <T> void printCommaSeperated(T ... arr) {
		if (isDebugMode) {
			if (arr.length == 1) {
				System.out.println(arr[0].toString());
			} else {
				System.out.print(arr[0].toString());
				for (int i=1; i < arr.length; i++) {
					System.out.print(", ");
					System.out.print(arr[i].toString());
				}
				System.out.println();
			}
		}
	}
	
	// draw debug lines, from https://stackoverflow.com/questions/21835062/libgdx-draw-line:
	private static ShapeRenderer debugRenderer = new ShapeRenderer();

    public static void DrawDebugLine(Vector2 start, Vector2 end, int lineWidth, Color color, Matrix4 projectionMatrix)
    {
    	System.out.println("drawing debug line");
        Gdx.gl.glLineWidth(lineWidth);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(color);
        debugRenderer.line(start, end);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }

    public static void DrawDebugLine(Vector2 start, Vector2 end, Matrix4 projectionMatrix)
    {
    	System.out.println("drawing debug line");
        Gdx.gl.glLineWidth(2);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(Color.WHITE);
        debugRenderer.line(start, end);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }
	
	private Debug() { }; // THIS CLASS CAN NOT BE INSTANTIATED
	
}
