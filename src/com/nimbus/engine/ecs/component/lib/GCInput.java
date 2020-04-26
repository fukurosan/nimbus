package com.nimbus.engine.ecs.component.lib;

import java.util.HashMap;
import java.util.Map;

import com.nimbus.engine.ecs.component.IGlobalComponent;

public class GCInput implements IGlobalComponent {

	private Map<String, Integer> keyBindings;
	private Map<String, Boolean> keyDown;
	private Map<String, Boolean> keyDownLast;
	
	//For now, lets just store all mouse buttons, and collect their states separately
	private int mouseX;
	private int mouseY;
	private int scroll; // 1 = scrolling down, -1 = scrolling up, 0 = no scroll
	
	private final int NUM_MOUSE_BUTTONS = 5; //I guess this covers most mice
	private boolean[] mouseButtonDown = new boolean[NUM_MOUSE_BUTTONS];
	private boolean[] mouseButtonDownLast = new boolean[NUM_MOUSE_BUTTONS];

	public GCInput() {
		keyBindings = new HashMap<String, Integer>();
		keyDown = new HashMap<String, Boolean>();
		keyDownLast = new HashMap<String, Boolean>();
	}
	
	//Bind a command to a key code (e.g. "JUMP", KeyEvent.VK_SPACE)
	public void setKeyBinding(String command, int keyValue) {
		keyBindings.put(command, keyValue);
		if(!keyDown.containsKey(command)) {
			keyDown.put(command, false);
		}
		if(!keyDownLast.containsKey(command)) {
			keyDownLast.put(command, false);
		}
	}

	//Check if a command key is currently pressed
	public boolean getKeyDown(String command) {
		return keyDown.get(command);
	}
	
	//Check if command key was pressed on last tick
	public boolean getLastKeyDown(String command) {
		return keyDownLast.get(command);
	}
	
	//Check if mouse button is clicked
	public boolean getMouseButtonDown(int buttonIndex) {
		return mouseButtonDown[buttonIndex];
	}
	
	//Check if mouse button was clicked last frame
	public boolean getMouseButtonDownLast(int buttonIndex) {
		return mouseButtonDownLast[buttonIndex];
	}
	
	//Set key to pressed
	public void setKeyDown(String command) {
		keyDown.put(command, true);
	}
	
	//Update mouse position
	public void setMousePosition(int x, int y) {
		mouseX = x;
		mouseY = y;
	}
	
	//Update scroll position
	public void setScrollPosition(int position) {
		scroll = position;
	}
	
	//Update mouse buttons
	public void setMouseButtons(boolean[] mouseButtons) {
		mouseButtonDown = mouseButtons;
	}
	
	//Get all key codes that are being monitored
	public Map<String, Integer> getKeyMap() {
		return keyBindings;
	}
	
	//Prepare for next tick by moving current values to previous values, and clearing current
	public void nextTick() {
		keyDownLast = new HashMap<String, Boolean>(keyDown);
		keyBindings.forEach((k, v) -> {
			keyDown.put(k, false);
		});
		for (int i = 0; i < NUM_MOUSE_BUTTONS; i++) {
			mouseButtonDownLast[i] = mouseButtonDown[i];
		}
	}

	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	public int getScroll() {
		return scroll;
	}

}
