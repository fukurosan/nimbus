package examples.platformer.gameobjects;

import com.nimbus.engine.ecs.component.lib.CAABB;
import com.nimbus.engine.ecs.component.lib.CBody;
import com.nimbus.engine.ecs.component.lib.CMass;
import com.nimbus.engine.ecs.component.lib.CRectangleShape;
import com.nimbus.engine.ecs.component.lib.CTransform;
import com.nimbus.engine.ecs.component.lib.CVelocity;
import com.nimbus.engine.ecs.entity.Entity;
import com.nimbus.engine.graphics.Vector2f;

import examples.platformer.component.CSnowball;

public class GOSnowball {
	public static final int MASS = 600;
	public static final int WIDTH = 8;
	public static final int HEIGHT = 8;
	public static final int SPEED = 400;
	public static final int UPWARDS_FORCE = 200;
	public static final int COLOUR = 0xff00ffff;
	public static final int LIGHT_SIZE = 12;
	public static final int LIGHT_COLOUR = 0xff00ffff;

	public static final float EXPLOSION_COOLDOWN = 0.3f;
	public static final float EXPLOSION_FORCE = 2.0f;
	public static final int EXPLOSION_COLOUR = 0xffffffff;
	public static final int EXPLOSION_LIGHT_COLOUR = 0xffffffff;
	public static final int EXPLOSION_NUMBER_OF_PARTICLES = 15;
	public static final int EXPLOSION_PARTICLE_LIGHT_SIZE = 12;
	
	
	public static Entity createSnowball(float startX, float startY, boolean headedLeft) {
		Entity snowball = new Entity();
		snowball.addComponent(new CSnowball());
		snowball.addComponent(new CTransform(new Vector2f(startX, startY), new Vector2f(1, 1), 0));
		snowball.addComponent(new CVelocity(new Vector2f(headedLeft ? -GOSnowball.SPEED : GOSnowball.SPEED, -GOSnowball.UPWARDS_FORCE)));
		snowball.addComponent(new CMass(GOSnowball.MASS));
		snowball.addComponent(new CBody(GOSnowball.WIDTH, GOSnowball.HEIGHT));
		snowball.addComponent(new CRectangleShape(true, GOSnowball.COLOUR));
		snowball.addComponent(new CAABB());
		return snowball;
	}
}
