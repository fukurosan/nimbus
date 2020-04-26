package examples.platformer.system;

import java.util.List;

import com.nimbus.engine.Renderer;
import com.nimbus.engine.ecs.Datastore;
import com.nimbus.engine.ecs.Query;
import com.nimbus.engine.ecs.component.lib.CBody;
import com.nimbus.engine.ecs.component.lib.CPlayer;
import com.nimbus.engine.ecs.component.lib.CSprite;
import com.nimbus.engine.ecs.component.lib.CSpriteAnimation;
import com.nimbus.engine.ecs.component.lib.CTransform;
import com.nimbus.engine.ecs.component.lib.CVelocity;
import com.nimbus.engine.ecs.component.lib.GCInput;
import com.nimbus.engine.ecs.entity.Entity;
import com.nimbus.engine.ecs.system.ASystem;

import examples.platformer.gameobjects.GOPlayer;
import examples.platformer.gameobjects.GOSnowball;

public class SPlayerCommand extends ASystem {
	
	private Datastore datastore;
	private GCInput inputHandler;
	
	public SPlayerCommand() {
		super();
	}
	
	public SPlayerCommand(int priority, boolean isParallel) {
		super(priority, isParallel);
	}
	
	public List<Entity> getEntitiesToProcess() {
		Query query = new Query();
		query
		.hasComponent(CPlayer.class)
		.hasComponent(CTransform.class)
		.hasComponent(CVelocity.class);
		return datastore.executeQuery(query);
	}

	@Override
	public void init(Datastore datastore) {
		this.datastore = datastore;
		inputHandler = (GCInput) datastore.getGlobalComponent(GCInput.class);
	}

	@Override
	public void update(float deltaTime) {
		for(Entity entity : getEntitiesToProcess()) {
			CTransform transform = (CTransform) entity.getComponent(CTransform.class);
			CBody body = (CBody) entity.getComponent(CBody.class);
			CVelocity velocity = (CVelocity) entity.getComponent(CVelocity.class);
			CSprite sprite = (CSprite) entity.getComponent(CSprite.class);
			CSpriteAnimation animation = (CSpriteAnimation) entity.getComponent(CSpriteAnimation.class);
			
			//Handle Left
			if(inputHandler.getKeyDown(GOPlayer.ACTIONS.WALK_LEFT.getName())) {
				transform.getPosition().setX(transform.getPosition().getX() - GOPlayer.WALK_SPEED * deltaTime);
				
				if(sprite != null) {
					sprite.setSprite(GOPlayer.SPRITES.PLAYER_LEFT.getSprite());
					sprite.setName(GOPlayer.SPRITES.PLAYER_LEFT.getName());
				}
				
				if(animation == null){
					entity.addComponent(new CSpriteAnimation(GOPlayer.ANIMATIONS.WALK_LEFT.getTileMap(), GOPlayer.ANIMATIONS.WALK_LEFT.getAnimationTuple(), GOPlayer.ANIMATIONS.WALK_LEFT.getAnimationSpeed(), GOPlayer.ANIMATIONS.WALK_LEFT.getName()));					
				}
			}
			else if(inputHandler.getLastKeyDown(GOPlayer.ACTIONS.WALK_LEFT.getName())) {
				if(animation != null && animation.getName() == GOPlayer.ANIMATIONS.WALK_LEFT.getName()) {					
					entity.removeComponentClass(CSpriteAnimation.class);
				}
			}
			
			//Handle Right
			if(inputHandler.getKeyDown(GOPlayer.ACTIONS.WALK_RIGHT.getName())) {
				transform.getPosition().setX(transform.getPosition().getX() + GOPlayer.WALK_SPEED * deltaTime);
				
				if(sprite != null) {
					sprite.setSprite(GOPlayer.SPRITES.PLAYER_RIGHT.getSprite());
					sprite.setName(GOPlayer.SPRITES.PLAYER_RIGHT.getName());
				}
				
				if(animation == null){
					entity.addComponent(new CSpriteAnimation(GOPlayer.ANIMATIONS.WALK_RIGHT.getTileMap(), GOPlayer.ANIMATIONS.WALK_RIGHT.getAnimationTuple(), GOPlayer.ANIMATIONS.WALK_RIGHT.getAnimationSpeed(), GOPlayer.ANIMATIONS.WALK_RIGHT.getName()));					
				}
			}
			else if(inputHandler.getLastKeyDown(GOPlayer.ACTIONS.WALK_RIGHT.getName())) {
				if(animation != null && animation.getName() == GOPlayer.ANIMATIONS.WALK_RIGHT.getName()) {					
					entity.removeComponentClass(CSpriteAnimation.class);
				}
			}
			
			//Handle Flight
			if(inputHandler.getKeyDown(GOPlayer.ACTIONS.FLY.getName())) {
				velocity.getVelocity().setY(-GOPlayer.FLIGHT_FORCE);
				if(sprite.getName() == GOPlayer.SPRITES.PLAYER_LEFT.getName()) {
					//We are facing left
					if(animation == null || animation.getName() != GOPlayer.ANIMATIONS.FLY_LEFT.getName()) {
						entity.addComponent(new CSpriteAnimation(GOPlayer.ANIMATIONS.FLY_LEFT.getTileMap(), GOPlayer.ANIMATIONS.FLY_LEFT.getAnimationTuple(), GOPlayer.ANIMATIONS.FLY_LEFT.getAnimationSpeed(), GOPlayer.ANIMATIONS.FLY_LEFT.getName()));					
					}
				}
				else if(sprite.getName() == GOPlayer.SPRITES.PLAYER_RIGHT.getName()) {
					//We are facing right
					if(animation == null || animation.getName() != GOPlayer.ANIMATIONS.FLY_RIGHT.getName()) {
						entity.addComponent(new CSpriteAnimation(GOPlayer.ANIMATIONS.FLY_RIGHT.getTileMap(), GOPlayer.ANIMATIONS.FLY_RIGHT.getAnimationTuple(), GOPlayer.ANIMATIONS.FLY_RIGHT.getAnimationSpeed(), GOPlayer.ANIMATIONS.FLY_RIGHT.getName()));											
					}
				}
			}
			else if(inputHandler.getLastKeyDown(GOPlayer.ACTIONS.FLY.getName())) {
				if(animation != null && (animation.getName() == GOPlayer.ANIMATIONS.FLY_LEFT.getName() || animation.getName() == GOPlayer.ANIMATIONS.FLY_RIGHT.getName())) {
					entity.removeComponentClass(CSpriteAnimation.class);
				}
			}
			
			//Handle Snowball
			if(!inputHandler.getKeyDown(GOPlayer.ACTIONS.THROW_SNOWBALL.getName()) && inputHandler.getLastKeyDown(GOPlayer.ACTIONS.THROW_SNOWBALL.getName())) {
				float startX = transform.getPosition().getX() + body.getWidth() / 2;
				float startY = transform.getPosition().getY() + body.getHeight() / 2;
				boolean headedLeft = sprite.getName() == GOPlayer.SPRITES.PLAYER_LEFT.getName();
				datastore.addEntity(GOSnowball.createSnowball(startX, startY, headedLeft), false);
			}
		}
	}
	


	@Override
	public void render(Renderer renderer) {}
	
	
	
}
