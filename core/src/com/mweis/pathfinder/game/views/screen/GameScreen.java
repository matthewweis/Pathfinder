package com.mweis.pathfinder.game.views.screen;

import java.util.Arrays;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mweis.pathfinder.engine.entity.components.PositionComponent;
import com.mweis.pathfinder.engine.entity.components.commands.MovementCommand;
import com.mweis.pathfinder.engine.entity.systems.MovementSystem;
import com.mweis.pathfinder.engine.entity.systems.PlayerInputSystem;
import com.mweis.pathfinder.engine.entity.systems.RenderingSystem;
import com.mweis.pathfinder.engine.util.Debug;
import com.mweis.pathfinder.engine.views.AnimationMap;
import com.mweis.pathfinder.engine.views.ResourceManager;
import com.mweis.pathfinder.game.entity.EntityFactory;

public class GameScreen implements Screen {
	Engine engine = new Engine();
	SpriteBatch batch = new SpriteBatch();
	OrthographicCamera cam = new OrthographicCamera(1920.0f, 1080.0f);//(100.0f, 100.0f);
//	InputHandler input = new InputHandler();
	Entity player = null;
	
	@Override
	public void show() {
		System.out.println("show");
		Debug.isDebugMode = true;
		
		// ADD SPRITES
//		ResourceManager.loadSprite("sprite1", "badlogic.jpg");
		ResourceManager.loadTexture("mage", "mage.png");
		
		engine.addSystem(new MovementSystem(engine));
		engine.addSystem(new RenderingSystem(batch));
		engine.addSystem(new PlayerInputSystem());
		
		player = EntityFactory.spawnMage(0.0f, 0.0f, engine);
		
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
	
	private void setupInput() {
		
		Vector3 vec = new Vector3(0.0f, 0.0f, 0.0f); // use this vec for mouse coords to prevent making vec3s all the time
		Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                if (button == Input.Buttons.RIGHT) {
                	vec.x = x;
                	vec.y = y;
                	Vector3 mouse = cam.unproject(vec);
                	Vector2 pv = player.getComponent(PositionComponent.class).position;
        			player.add(new MovementCommand(pv.x, pv.y, mouse.x, mouse.y));
                    return true;
                }
                if (button == Input.Buttons.LEFT) {
                	vec.x = x;
                	vec.y = y;
                	Vector3 mouse = cam.unproject(vec);
                	System.out.println(mouse.x + ", " + mouse.y);
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
				cam.zoom += amount * .03;
				return true;
			}
        });
		
    }
	
	@Override
	public void render(float delta) {
		handleInput(); // will make changes to camera
	    cam.update();
	    batch.setProjectionMatrix(cam.combined);
		
		batch.begin(); // eventually rendering system can handle all this
		engine.update(delta);
		batch.end();
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
