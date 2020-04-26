package examples.platformer.system;

import com.nimbus.engine.Renderer;
import com.nimbus.engine.ecs.Datastore;
import com.nimbus.engine.ecs.component.lib.GCInput;
import com.nimbus.engine.ecs.system.ASystem;
import com.nimbus.engine.graphics.light.Light;

public class SMouseLantern extends ASystem {

	GCInput inputHandler;
	Light light = new Light(200, 0xffffffff);
	
	public SMouseLantern() {
		super();
	}
	
	public SMouseLantern(int priority, boolean isParallel) {
		super(priority, isParallel);
	}

	@Override
	public void init(Datastore datastore) {
		inputHandler = (GCInput) datastore.getGlobalComponent(GCInput.class);
	}

	@Override
	public void update(float deltaTime) {}

	@Override
	public void render(Renderer renderer) {
		renderer.drawLight(light, inputHandler.getMouseX() + renderer.getCameraX(), inputHandler.getMouseY() + renderer.getCameraY());
	}
	
}
