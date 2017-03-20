package com.mweis.pathfinder.game.views.screen;


import java.util.List;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mweis.pathfinder.engine.entity.components.commands.MovementCommand;
import com.mweis.pathfinder.engine.entity.systems.CollisionSystem;
import com.mweis.pathfinder.engine.entity.systems.MovementSystem;
import com.mweis.pathfinder.engine.entity.systems.PlayerInputSystem;
import com.mweis.pathfinder.engine.entity.systems.RenderingSystem;
import com.mweis.pathfinder.engine.util.Debug;
import com.mweis.pathfinder.engine.util.Mappers;
import com.mweis.pathfinder.engine.views.ResourceManager;
import com.mweis.pathfinder.engine.world.Room;
import com.mweis.pathfinder.game.entity.EntityFactory;
import com.mweis.pathfinder.game.world.DungeonFactory;
import com.mweis.pathfinder.game.world.DungeonFactoryOld;

public class GameScreen implements Screen {
	Engine engine = new Engine();
	SpriteBatch batch = new SpriteBatch();
	OrthographicCamera cam = new OrthographicCamera(1920.0f, 1080.0f);//(100.0f, 100.0f);
	Entity player = null;
//	Entity testDummy = null;
	CollisionSystem cs = null; // TEMP FOR COLL DEBUG
	
	Vector2[] testDungeon;
	Rectangle[] testRooms;
	List<List<Room>> rooms;
	
	@Override
	public void show() {
		System.out.println("show");
		Debug.isDebugMode = true;
		
		// load textures
		ResourceManager.loadTexture("mage", "mage.png");
		
		// add systems
		cs = new CollisionSystem(engine); // TEMP FOR COLL DEBUG
		
		engine.addSystem(new MovementSystem(engine));
		engine.addSystem(cs); // TEMP FOR COLL DEBUG
		engine.addSystem(new RenderingSystem(batch));
		engine.addSystem(new PlayerInputSystem());
		
		cs.update(cam.combined);
		
		// attach entity listeners
		
		// add entities
		player = EntityFactory.spawnMage(0.0f, 0.0f, 12.8f, 6.0f, engine);
//		testDummy = EntityFactory.spawnMage(100.0f, 0.0f, 8.8f, 9.0f, engine);
		
		// setup input (this class is the listener)
		setupInput();
		
		testDungeon = DungeonFactoryOld.getRandomPointsInCircle(150, 90);
		testRooms = DungeonFactoryOld.makeRectanglesAroundPoints(testDungeon, 14.0f, 30.0f);
		rooms = DungeonFactory.generateDungeon();
	}
	
	private void handleInput() {
		
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam.translate(-3, 0, 0);
            //If the LEFT Key is pressed, translate the camera -3 units in the X-Axis
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam.translate(3, 0, 0);
            //If the RIGHT Key is pressed, translate the camera 3 units in the X-Axis
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cam.translate(0, -3, 0);
            //If the DOWN Key is pressed, translate the camera -3 units in the Y-Axis
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cam.translate(0, 3, 0);
            //If the UP Key is pressed, translate the camera 3 units in the Y-Axis
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            cam.rotate(-0.2f, 0, 0, 1);
            //If the W Key is pressed, rotate the camera by -rotationSpeed around the Z-Axis
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            cam.rotate(0.2f, 0, 0, 1);
            //If the E Key is pressed, rotate the camera by rotationSpeed around the Z-Axis
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
        	if (Mappers.movementMapper.has(player)) {
        		player.remove(MovementCommand.class);
        	}
        }
        
        // make space lock camera on player, otherwise allow free movement
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
        	cam.position.set(new Vector3(Mappers.positionMapper.get(player).position, 0));
        } else {
        	final float camEdge = 0.07f; // 7% of the screen can be used for cam movement
            final float xCameraBound = Gdx.graphics.getWidth() * camEdge;
            final float yCameraBound = Gdx.graphics.getHeight() * camEdge;
            final int camSpeed = 3;
            
            if (Gdx.input.getX() < xCameraBound) {
            	cam.translate(-camSpeed, 0, 0);
            } else if (Gdx.input.getX() > Gdx.graphics.getWidth() - xCameraBound) {
            	cam.translate(camSpeed, 0, 0);
            }
            if (Gdx.input.getY() < yCameraBound) {
            	cam.translate(0, camSpeed, 0);
            } else if (Gdx.input.getY() > Gdx.graphics.getHeight() - yCameraBound) {
            	cam.translate(0, -camSpeed, 0);
            }
        }
	}
	
	private void setupInput() {
		
		Vector3 vec = new Vector3(0.0f, 0.0f, 0.0f); // use this vec for mouse coords to prevent making vec3s all the time
		Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                if (button == Input.Buttons.RIGHT) {
                	vec.x = x;
                	vec.y = y;
                	Vector3 mouse = cam.unproject(vec);
                	Vector2 pv = Mappers.positionMapper.get(player).position;
        			player.add(new MovementCommand(pv.x, pv.y, mouse.x, mouse.y));
                    return true;
                }
                if (button == Input.Buttons.LEFT) {
                	
                }
                return false;
            }

			@Override
			public boolean keyDown(int keycode) {
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				System.out.println(cam.zoom);
				if (cam.zoom < 0.1f) {
					cam.zoom += amount * 0.005f;
				} else {
					cam.zoom += amount * cam.zoom * 0.3;
				}
				return true;
			}
        });
		
    }
	
	/*
	 * Called 60 times per second as per LibGdx specification.
	 */
	boolean overlap = true;
	@Override
	public void render(float delta) {
		handleInput(); // will make changes to camera
	    cam.update();
	    cs.update(cam.combined); // TEMP FOR COLL DEBUG
	    batch.setProjectionMatrix(cam.combined);
	    
	    ShapeRenderer sr = new ShapeRenderer();
	    sr.setProjectionMatrix(cam.combined);
	    sr.begin(ShapeRenderer.ShapeType.Filled);
	    
//	    for (Vector2 v : testDungeon) {
//			sr.circle(v.x, v.y, 1.0f);
//		}
//	    for (Rectangle r : testRooms) {
//	    	sr.rect(r.x, r.y, r.width, r.height);
//	    }
	    Color[] colors = {Color.BROWN, Color.GREEN, Color.RED, Color.TEAL};
	    
	    int i=0;
	    for (List<Room> l : rooms) {
	    	Color curr = sr.getColor();
	    	sr.setColor(colors[(i++)%4]);
	    	for (Room r : l) {
	    		sr.rect(r.getLeft(), r.getBottom(), r.getWidth(), r.getHeight());
	    	}
	    }
	    
//	    if (overlap) {
////	    	overlap = DungeonFactory.steerAwayOverlappingRectangles(testRooms);
//	    	overlap = DungeonFactoryOld.seperateRooms(testRooms);
////		    try {
////				Thread.sleep(0L);
////			} catch (InterruptedException e) {
////				e.printStackTrace();
////			}
////	    	System.out.println(testRooms[0].toString());
//	    }
	    
	    sr.end();
	    
	    
//		batch.begin(); // eventually rendering system can handle all this
		engine.update(delta);
//		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		System.out.println("resize");
//		cam.viewportWidth = width
//        cam.viewportHeight = width * height/width; // Lets keep things in proportion.
//        cam.update();
        System.out.println(Gdx.graphics.getWidth() + ", " + Gdx.graphics.getHeight());
	}

	@Override
	public void pause() {
		System.out.println("pause");
	}

	@Override
	public void resume() {
		System.out.println("resume");
	}

	@Override
	public void hide() {
		System.out.println("hide");
	}

	@Override
	public void dispose() {
		System.out.println("dispose");
		ResourceManager.dispose();
	}
	
}
