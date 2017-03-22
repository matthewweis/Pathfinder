package com.mweis.pathfinder.game.views.screen;


import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mweis.pathfinder.engine.entity.components.commands.MovementCommand;
import com.mweis.pathfinder.engine.entity.systems.CollisionSystem;
import com.mweis.pathfinder.engine.entity.systems.AISystem;
import com.mweis.pathfinder.engine.entity.systems.MovementSystem;
import com.mweis.pathfinder.engine.entity.systems.PartitionSystem;
import com.mweis.pathfinder.engine.entity.systems.PlayerInputSystem;
import com.mweis.pathfinder.engine.entity.systems.RenderingSystem;
import com.mweis.pathfinder.engine.util.Mappers;
import com.mweis.pathfinder.engine.views.ResourceManager;
import com.mweis.pathfinder.engine.world.Dungeon;
import com.mweis.pathfinder.game.entity.EntityFactory;
import com.mweis.pathfinder.game.world.DungeonFactory;

public class GameScreen implements Screen {
	Engine engine = new Engine();
	SpriteBatch batch = new SpriteBatch();
	OrthographicCamera cam = new OrthographicCamera(1280.0f, 720.0f);
	Entity player, test = null;
	CollisionSystem cs = null; // TEMP FOR COLL DEBUG
	
	Dungeon dungeon;
	boolean isCameraLocked = false;
	
	@Override
	public void show() {
		System.out.println("show");
		
		// load textures
		ResourceManager.loadTexture("mage", "mage.png");
		
		long before = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

		// create world
		dungeon = DungeonFactory.generateDungeon();
		
		long after = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		System.out.format("memory consumption was %d before dungeon gen, and is now %d.%n", before, after);
		
		// add systems 
		engine.addSystem(new PartitionSystem(dungeon, engine));
		cs = new CollisionSystem(engine.getSystem(PartitionSystem.class)); // TEMP FOR COLL DEBUG
		engine.addSystem(new MovementSystem(dungeon, engine));
		engine.addSystem(cs); // TEMP FOR COLL DEBUG
		engine.addSystem(new RenderingSystem(batch));
		engine.addSystem(new PlayerInputSystem());
		engine.addSystem(new AISystem());
		cs.update(cam.combined); // TEMP FOR COLL DEBUG
		
		// attach entity listeners
		
		// add entities
		Vector2 spawn = new Vector2(dungeon.getStartRoom().getCenterX(), dungeon.getStartRoom().getCenterY());
		player = EntityFactory.spawnMage(spawn.x, spawn.y, 12.8f, 6.0f, engine);
		test = EntityFactory.spawnAiTest(player, spawn.x + 15, spawn.y, 10.0f, 6.0f, engine);
		
		// setup input (this class is the listener)
		setupInput();
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
        	if (Gdx.graphics.isFullscreen()) {
        		Gdx.graphics.setWindowedMode(1280, 720);
        	} else {
        		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        	}
        }
        
        // make space lock camera on player, otherwise allow free movement
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
        	isCameraLocked = !isCameraLocked;
        } 
        if (isCameraLocked) {
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
	    
	    dungeon.render(cam.combined);
		engine.update(delta);
	}

	@Override
	public void resize(int width, int height) {
		System.out.format("resized screen to (%d, %d)%n", width, height);
//		cam.viewportWidth = width
//        cam.viewportHeight = width * height/width; // Lets keep things in proportion.
//        cam.update();
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
