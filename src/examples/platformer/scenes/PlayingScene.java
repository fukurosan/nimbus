package examples.platformer.scenes;

import java.awt.event.KeyEvent;

import com.nimbus.engine.Nimbus;
import com.nimbus.engine.Renderer;
import com.nimbus.engine.ecs.component.lib.GCInput;
import com.nimbus.engine.ecs.component.lib.GCQuadTree;
import com.nimbus.engine.ecs.system.lib.SAABBCollision;
import com.nimbus.engine.ecs.system.lib.SCamera;
import com.nimbus.engine.ecs.system.lib.SGravity;
import com.nimbus.engine.ecs.system.lib.SInput;
import com.nimbus.engine.ecs.system.lib.SMovement;
import com.nimbus.engine.ecs.system.lib.SParticle;
import com.nimbus.engine.ecs.system.lib.SShapeRenderer;
import com.nimbus.engine.ecs.system.lib.SSpriteRenderer;
import com.nimbus.engine.extension.AGame;
import com.nimbus.engine.extension.AScene;

import examples.platformer.gameobjects.GOPlayer;
import examples.platformer.level.Level1;
import examples.platformer.system.SPlayerCollision;
import examples.platformer.system.SPlayerCommand;
import examples.platformer.system.SPlayerLantern;
import examples.platformer.system.SSnowball;

public class PlayingScene extends AScene {

	int sceneWidth = 320 * 2;
	int sceneHeight = 240 * 2;

	public PlayingScene(AGame gameManager) {
		super();
		Level1.buildLevel(getDatastore());
		initGlobalComponents();
		initSystems();
	}

	private void initGlobalComponents() {
		GCInput inputHandler = new GCInput();
		inputHandler.setKeyBinding(GOPlayer.ACTIONS.WALK_LEFT.getName(), KeyEvent.VK_A);
		inputHandler.setKeyBinding(GOPlayer.ACTIONS.WALK_RIGHT.getName(), KeyEvent.VK_D);
		inputHandler.setKeyBinding(GOPlayer.ACTIONS.FLY.getName(), KeyEvent.VK_W);
		inputHandler.setKeyBinding(GOPlayer.ACTIONS.THROW_SNOWBALL.getName(), KeyEvent.VK_SPACE);
		getDatastore().addGlobalComponent(inputHandler);
		getDatastore().addGlobalComponent(new GCQuadTree(sceneWidth, sceneHeight));
	}

	private void initSystems() {
		this.getDatastore().addSystem(new SInput(1, false));
		this.getDatastore().addSystem(new SPlayerCommand(2, false));
		this.getDatastore().addSystem(new SGravity(3, false));
		this.getDatastore().addSystem(new SMovement(4, false));
		this.getDatastore().addSystem(new SAABBCollision(5, false));
		this.getDatastore().addSystem(new SPlayerCollision(6, false));
		this.getDatastore().addSystem(new SSnowball(6, false));
		this.getDatastore().addSystem(new SCamera(7, false));
		this.getDatastore().addSystem(new SSpriteRenderer(8, false));
		this.getDatastore().addSystem(new SShapeRenderer(8, false));
		this.getDatastore().addSystem(new SParticle(8, false));
		this.getDatastore().addSystem(new SPlayerLantern(9, false));
		//this.getDatastore().addSystem(new SMouseLantern(9, false)); //Activate this system for a light you can move around with your mouse
		//this.getDatastore().addSystem(new SHitboxDebugger(99999, true)); //Activate this system to debug the aabb hitboxes
	}

	public void entered() {
	}

	public void update(Nimbus nimbus, float deltaTime) {
		super.update(nimbus, deltaTime);
	}

	public void render(Nimbus nimbus, Renderer renderer) {
		super.render(nimbus, renderer);
	}

	public void postProcessingFinished(Nimbus nimbus, Renderer renderer) {
		int cameraX = renderer.getCameraX();
		int cameraY = renderer.getCameraY();
		renderer.setCameraX(0);
		renderer.setCameraY(0);
		renderer.setAlphaMod(0.5f);
		renderer.drawText("Move around: WAD", 0, renderer.getFont().getHeight(), 0xff00ff00, Integer.MAX_VALUE);
		renderer.drawText("Throw snowball: SPACE", 0, renderer.getFont().getHeight() * 2, 0xff00ff00, Integer.MAX_VALUE);
		renderer.setAlphaMod(1f);
		renderer.setCameraX(cameraX);
		renderer.setCameraY(cameraY);
	}
	
	public void exiting() {
	}

	public void obscuring() {
	}

	public void revealed() {
	}

}
