package examples.platformer.level;

import com.nimbus.engine.ecs.Datastore;
import com.nimbus.engine.ecs.component.lib.CAABB;
import com.nimbus.engine.ecs.component.lib.CBody;
import com.nimbus.engine.ecs.component.lib.CCamera;
import com.nimbus.engine.ecs.component.lib.CSprite;
import com.nimbus.engine.ecs.component.lib.CTransform;
import com.nimbus.engine.ecs.entity.Entity;
import com.nimbus.engine.graphics.Vector2f;
import com.nimbus.engine.graphics.image.Image;
import com.nimbus.engine.graphics.image.ImageTile;
import com.nimbus.engine.graphics.light.Light;

import examples.platformer.component.CBlock;
import examples.platformer.gameobjects.GOPlayer;

public class Level1 {

	private static String imageTilePath = "/platformer/level/1/level1tileset16x16.png";
	private static String blocksFilePath = "/platformer/level/1/blocks.csv";
	private static String decorationsFilePath = "/platformer/level/1/decorations.csv";
	private static String backgroundImagePath = "/platformer/level/1/level1alternativebackground2640x480.png";
	
	private static final int LEVEL_WIDTH = 320 * 2;
	private static final int LEVEL_HEIGHT = 240 * 2;
	private static final int PLAYER_START_X = 0;
	private static final int PLAYER_START_Y = 0;
	private static final int TILE_WIDTH = 16;
	private static final int TILE_HEIGHT = 16;

	public static void buildLevel(Datastore datastore) {
		datastore.addEntity(Level1.getBackground(), true);

		ImageTile imageTile = new ImageTile(imageTilePath, 16, 16);
		int[][] blocks = Level.readMapTo2DArray(blocksFilePath);
		int[][] decorations = Level.readMapTo2DArray(decorationsFilePath);

		for (int y = 0; y < blocks.length; y++) {
			for (int x = 0; x < blocks[y].length; x++) {
				if (blocks[y][x] >= 0) {
					Image tile = imageTile.getTileImageAtIndex(blocks[y][x]);
					tile.setLightBlock(Light.ABSORBED);
					float tileX = (float) (x * Level1.TILE_WIDTH);
					float tileY = (float) (y * Level1.TILE_HEIGHT);
					datastore.addEntity(Level1.getBlock(tile, tileX, tileY), true);
				}
			}
		}

		for (int y = 0; y < decorations.length; y++) {
			for (int x = 0; x < decorations[y].length; x++) {
				if (decorations[y][x] >= 0) {
					Image tile = imageTile.getTileImageAtIndex(decorations[y][x]);
					float tileX = (float) (x * Level1.TILE_WIDTH);
					float tileY = (float) (y * Level1.TILE_HEIGHT);
					datastore.addEntity(Level1.getDecoration(tile, tileX, tileY), true);
				}
			}
		}

		Entity player = GOPlayer.createPlayer(PLAYER_START_X, PLAYER_START_Y);		
		CCamera camera = Level1.getCamera(PLAYER_START_X, PLAYER_START_Y);		
		player.addComponent(camera);
		datastore.addEntity(player, true);
	}
	
	private static CCamera getCamera(float startX, float startY) {
		CCamera camera = new CCamera(new Vector2f(startX, startY), 320, 240);
		camera.setBound(false);
		camera.setBoundOffsetX(LEVEL_WIDTH);
		camera.setBoundOffsetY(LEVEL_HEIGHT);
		camera.setCameraSpeed(4);		
		return camera;
	}
	
	public static Entity getBackground() {
		Image backgroundImage = new Image(backgroundImagePath);
		Entity background = new Entity();
		background.addComponent(new CSprite(backgroundImage));
		background.addComponent(new CTransform(new Vector2f(0, 0), new Vector2f(0, 0), 0));
		return background;
	}
	
	public static Entity getBlock(Image tile, float startX, float startY) {
		Entity block = new Entity();
		block.addComponent(new CSprite(tile));
		block.addComponent(new CTransform(new Vector2f(startX, startY), new Vector2f(1, 1), 1));
		block.addComponent(new CBody(Level1.TILE_WIDTH, Level1.TILE_HEIGHT));
		block.addComponent(new CAABB());
		block.addComponent(new CBlock());
		return block;
	}
	
	public static Entity getDecoration(Image tile, float startX, float startY) {
		Entity decoration = new Entity();
		decoration.addComponent(new CSprite(tile));
		decoration.addComponent(new CTransform(new Vector2f(startX, startY), new Vector2f(1, 1), 0));
		return decoration;
	}

}
