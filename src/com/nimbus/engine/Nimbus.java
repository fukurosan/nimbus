package com.nimbus.engine;

import com.nimbus.engine.extension.AGame;

public class Nimbus implements Runnable {

	private Thread thread;
	private Window window;
	private Renderer renderer;
	private InputHandler inputHandler;
	private NimbusSettings settings = NimbusSettings.INSTANCE;
	private AGame game;

	private boolean running = false;

	public Nimbus(AGame game) {
		this.game = game;
	}

	public void start() {
		window = new Window();
		renderer = new Renderer(this);
		inputHandler = InputHandler.INSTANCE;
		inputHandler.attachToNimbus(this);
		this.thread = new Thread(this);
		thread.run();
	}

	public void stop() {

	}

	public void run() {
		this.running = true;
		boolean render = false;

		double firstTime = 0;
		double lastTime = System.nanoTime() / 1e9d;
		double passedTime = 0;
		double unprocessedTime = 0;

		double frameTime = 0;
		int frames = 0;
		int fps = 0;

		game.init(this);

		while (running) {
			if (settings.CAP_FPS) {
				render = false;
			}
			firstTime = System.nanoTime() / 1e9d;
			passedTime = firstTime - lastTime;
			lastTime = firstTime;

			unprocessedTime += passedTime;
			frameTime += passedTime;

			while (unprocessedTime >= settings.UPDATE_CAP) {
				unprocessedTime -= settings.UPDATE_CAP;

				game.peekScene().update(this, (float) settings.UPDATE_CAP);
				inputHandler.update();
				
				render = true;

				if (frameTime >= 1.0) {
					frameTime = 0;
					fps = frames;
					frames = 0;
				}
			}

			if (render) {
				renderer.clear();
				game.peekScene().render(this, renderer);
				renderer.process();
				game.peekScene().postProcessingFinished(this, renderer);
				if (settings.SHOW_FPS) {
					int cameraX = renderer.getCameraX();
					int cameraY = renderer.getCameraY();
					renderer.setCameraX(0);
					renderer.setCameraY(0);
					renderer.setAlphaMod(0.5f);
					renderer.drawText("FPS:" + fps, 0, 0, 0xff00ff00, Integer.MAX_VALUE);
					renderer.setAlphaMod(1f);
					renderer.setCameraX(cameraX);
					renderer.setCameraY(cameraY);
				}
				window.update();
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		this.dispose();
	}

	private void dispose() {
		game.dispose();
	}

	public Window getWindow() {
		return window;
	}

	public InputHandler getInputHandler() {
		return inputHandler;
	}

	public Renderer getRenderer() {
		return renderer;
	}

}
