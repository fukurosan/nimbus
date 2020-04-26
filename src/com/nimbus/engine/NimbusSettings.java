package com.nimbus.engine;

public enum NimbusSettings {
	
	INSTANCE;
	
	public String TITLE = "Nimbus Engine";
	
	public boolean DEBUG_MODE = false;
	public boolean SHOW_FPS = false;
	public boolean CAP_FPS = false;
	public double UPDATE_CAP = 1.0 / 60.0;
	
	public int WIDTH = 320;
	public int HEIGHT = 240;
	public float SCALE = 3f;
	
}
