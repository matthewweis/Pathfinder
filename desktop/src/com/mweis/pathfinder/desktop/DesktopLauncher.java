package com.mweis.pathfinder.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mweis.pathfinder.PathfinderGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = 1920/2;
		config.height = 1080/2;
		
		new LwjglApplication(new PathfinderGame(), config);
	}
}
