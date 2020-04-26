package examples.platformer.gameobjects;

import java.util.ArrayList;
import java.util.List;

import com.nimbus.engine.ecs.component.lib.CAABB;
import com.nimbus.engine.ecs.component.lib.CBody;
import com.nimbus.engine.ecs.component.lib.CMass;
import com.nimbus.engine.ecs.component.lib.CPlayer;
import com.nimbus.engine.ecs.component.lib.CSprite;
import com.nimbus.engine.ecs.component.lib.CTransform;
import com.nimbus.engine.ecs.component.lib.CVelocity;
import com.nimbus.engine.ecs.entity.Entity;
import com.nimbus.engine.graphics.Vector2f;
import com.nimbus.engine.graphics.image.Image;
import com.nimbus.engine.graphics.image.ImageTile;
import com.nimbus.engine.graphics.light.Light;

public class GOPlayer {

	public static final String PLAYER_IMAGE_PATH = "/platformer/penguin_walk16x21.png";
	public static final String WALK_ANIMATION_SPRITE_MAP_PATH = "/platformer/penguin_walk16x21.png";
	public static final String FLY_ANIMATION_SPRITE_MAP_PATH = "/platformer/penguin_fly21x19.png";
	
	public static final ImageTile PLAYER_IMAGE_TILE_MAP = new ImageTile(PLAYER_IMAGE_PATH, 16, 21);
	public static final ImageTile PLAYER_WALK_ANIMATION_TILE_MAP = new ImageTile(WALK_ANIMATION_SPRITE_MAP_PATH, 16, 21);
	public static final ImageTile PLAYER_FLY_ANIMATION_TILE_MAP = new ImageTile(FLY_ANIMATION_SPRITE_MAP_PATH, 21, 19);
	
	public static final int MASS = 400;
	public static final int WIDTH = 16;
	public static final int HEIGHT = 21;
	public static final int WALK_SPEED = 100;
	public static final int FLIGHT_FORCE = 150;
	public static final Light light = new Light(140, 0xffffffff);
	
	public static Entity createPlayer(float startX, float startY) {
		Entity player = new Entity();
		player.addComponent(new CPlayer());
		player.addComponent(new CTransform(new Vector2f(startX, startY), new Vector2f(1, 1), 0));		
		player.addComponent(new CSprite(SPRITES.PLAYER_RIGHT.getSprite(), SPRITES.PLAYER_RIGHT.getName()));
		player.addComponent(new CVelocity(new Vector2f(0, 0)));
		player.addComponent(new CBody(WIDTH, HEIGHT));
		player.addComponent(new CMass(MASS));
		player.addComponent(new CAABB());
		return player;
	}
	
	public enum SPRITES {
		PLAYER_RIGHT {
			public String getName() {
				return "PLAYER_RIGHT_FACING_SPRITE";
			}
			public  Image getSprite() {
				return PLAYER_IMAGE_TILE_MAP.getTileImage(0, 0);
			}
		}, 
		PLAYER_LEFT {
			public String getName() {
				return "PLAYER_LEFT_FACING_SPRITE";
			}
			public  Image getSprite() {
				return PLAYER_IMAGE_TILE_MAP.getTileImage(0, 1);
			}
		};
		
		public abstract String getName();
		public abstract Image getSprite();
	}
	
	public enum ANIMATIONS {
		WALK_RIGHT {
			public String getName() {
				return "WALK_RIGHT_ANIMATION";
			}
			public List<int[]> getAnimationTuple() {
				List<int[]> animationTuple = new ArrayList<int[]>();
				animationTuple.add(new int[]{0, 1});
				animationTuple.add(new int[]{0, 0});
				return animationTuple;
			}
			public ImageTile getTileMap() {
				return PLAYER_WALK_ANIMATION_TILE_MAP;
			}
			@Override
			public int getAnimationSpeed() {
				return 10;
			}
		}, 
		WALK_LEFT {
			public String getName() {
				return "WALK_LEFT_ANIMATION";
			}			
			public List<int[]> getAnimationTuple() {
				List<int[]> animationTuple = new ArrayList<int[]>();
				animationTuple.add(new int[]{1, 1});
				animationTuple.add(new int[]{1, 0});
				return animationTuple;
			}
			public ImageTile getTileMap() {
				return PLAYER_WALK_ANIMATION_TILE_MAP;
			}
			@Override
			public int getAnimationSpeed() {
				return 10;
			}
		}, 
		FLY_LEFT {
			public String getName() {
				return "FLY_LEFT_ANIMATION";
			}			
			public List<int[]> getAnimationTuple() {
				List<int[]> animationTuple = new ArrayList<int[]>();
				animationTuple.add(new int[]{1, 1});
				animationTuple.add(new int[]{1, 0});
				return animationTuple;
			}
			public ImageTile getTileMap() {
				return PLAYER_FLY_ANIMATION_TILE_MAP;
			}
			@Override
			public int getAnimationSpeed() {
				return 30;
			}
		}, 
		FLY_RIGHT {
			public String getName() {
				return "FLY_RIGHT_ANIMATION";
			}
			public List<int[]> getAnimationTuple() {
				List<int[]> animationTuple = new ArrayList<int[]>();
				animationTuple.add(new int[]{0, 1});
				animationTuple.add(new int[]{0, 0});
				return animationTuple;
			}
			public ImageTile getTileMap() {
				return PLAYER_FLY_ANIMATION_TILE_MAP;
			}
			@Override
			public int getAnimationSpeed() {
				return 30;
			}
		};
		
		public abstract String getName();
		public abstract ImageTile getTileMap();
		public abstract List<int[]> getAnimationTuple();
		public abstract int getAnimationSpeed();
	}
	
	public enum ACTIONS {
		WALK_LEFT {
			public String getName() {
				return "WALK_LEFT_ACTION";
			}
		}, 
		WALK_RIGHT {
			public String getName() {
				return "WALK_RIGHT_ACTION";
			}
		}, 
		FLY {
			public String getName() {
				return "FLY_ACTION";
			}
		}, 
		THROW_SNOWBALL{
			public String getName() {
				return "THROW_SNOWBALL_ACTION";
			}
		};
		
		public abstract String getName();
	}
	
}
