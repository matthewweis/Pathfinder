package com.mweis.pathfinder.game.views.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.Input.Buttons;
import com.mweis.pathfinder.engine.camera.VirtualViewport;
import com.mweis.pathfinder.engine.entity.components.commands.MovementCommand;
import com.mweis.pathfinder.engine.entity.systems.MovementSystem;
import com.mweis.pathfinder.engine.entity.systems.RenderingSystem;
import com.mweis.pathfinder.engine.util.Debug;
import com.mweis.pathfinder.engine.views.ResourceManager;
import com.mweis.pathfinder.game.entity.EntityFactory;

public class GameScreen implements Screen {
	Engine engine = new Engine();
	SpriteBatch batch = new SpriteBatch();
	OrthographicCamera cam = new OrthographicCamera(1920.0f, 1080.0f);//(100.0f, 100.0f);
	
	@Override
	public void show() {
		System.out.println("show");
		Debug.isDebugMode = true;
		System.out.println("INIT");
		
		// ADD SPRITES
		ResourceManager.loadSprite("sprite1", "badlogic.jpg");
		Sprite sprite = ResourceManager.getSprite("sprite1");
		sprite.setSize(20, 20);
		//entity = EntityFactory.spawnTestEntity(0.0f, 0.0f, 30.0f, sprite);
		
		engine.addSystem(new MovementSystem());
		engine.addSystem(new RenderingSystem(batch));
		//engine.addEntity(entity);
		
		for (int i=0; i < 10; i++) {
			for (int j=0; j < 10; j++) {
				Entity entity = EntityFactory.spawnTestEntity(i*100, j*100, 30.0f, sprite);
				entity.flags = i*10 + j;
				engine.addEntity(entity);
			}
		}
	}
	
	private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            cam.zoom += 0.02;
            //If the A Key is pressed, add 0.02 to the Camera's Zoom
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            cam.zoom -= 0.02;
            //If the Q Key is pressed, subtract 0.02 from the Camera's Zoom
        }
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
        

//        cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, 100/cam.viewportWidth);

//        float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
//        float effectiveViewportHeight = cam.viewportHeight * cam.zoom;

//        cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, 100 - effectiveViewportWidth / 2f);
//        cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, 100 - effectiveViewportHeight / 2f);
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
//		cam.viewportWidth = width;                 // Viewport of 30 units!
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
	}
	
}
