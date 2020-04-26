package examples.platformer;

import com.nimbus.engine.Nimbus;
import com.nimbus.engine.NimbusSettings;
import com.nimbus.engine.extension.AGame;

import examples.platformer.scenes.PlayingScene;

public class GameManager extends AGame {

	public GameManager() {
		pushScene(new PlayingScene(this));
	}

	@Override
	public void init(Nimbus nimbus) {
		nimbus.getRenderer().setAmbientColour(0xff656565);
	}

	@Override
	public void dispose() {

	}

	public static void main(String[] args) {
		NimbusSettings.INSTANCE.WIDTH = 320;
		NimbusSettings.INSTANCE.HEIGHT = 240;
		NimbusSettings.INSTANCE.SCALE = 3f;
		NimbusSettings.INSTANCE.CAP_FPS = false;
		NimbusSettings.INSTANCE.SHOW_FPS = true;

		Nimbus nimbus = new Nimbus(new GameManager());
		nimbus.start();
	}
}
