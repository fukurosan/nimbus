package com.nimbus.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public enum InputHandler implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	INSTANCE;
	
	private NimbusSettings settings = NimbusSettings.INSTANCE;

	private final int NUM_KEYS = 256;
	private boolean[] keys = new boolean[NUM_KEYS];
	private boolean[] keysLastFrame = new boolean[NUM_KEYS];

	private final int NUM_MOUSE_BUTTONS = 5;
	private boolean[] mouseButtons = new boolean[NUM_MOUSE_BUTTONS];
	private boolean[] mouseButtonsLastFrame = new boolean[NUM_MOUSE_BUTTONS];

	private int mouseX = 0;
	private int mouseY = 0;
	private int scroll = 0; // 1 = scrolling down, -1 = scrolling up, 0 = no scroll

	public void attachToNimbus(Nimbus nimbus) {
		nimbus.getWindow().getCanvas().addKeyListener(this);
		nimbus.getWindow().getCanvas().addMouseListener(this);
		nimbus.getWindow().getCanvas().addMouseMotionListener(this);
		nimbus.getWindow().getCanvas().addMouseWheelListener(this);
	}
	
	public void update() {
		scroll = 0;
		for (int i = 0; i < NUM_KEYS; i++) {
			keysLastFrame[i] = keys[i];
		}
		for (int i = 0; i < NUM_MOUSE_BUTTONS; i++) {
			mouseButtonsLastFrame[i] = mouseButtons[i];
		}
	}
	
	//Key was pressed on this frame
	public boolean isKeyDown(int keyCode) {
		return keys[keyCode] && !keysLastFrame[keyCode];
	}
	
	//Key was held down on this frame
	public boolean isKey(int keyCode) {
		return keys[keyCode];
	}
	
	//Key was released on this frame
	public boolean isKeyUp(int keyCode) {
		return !keys[keyCode] && keysLastFrame[keyCode];
	}
	
	//Mouse button was pressed on this frame
	public boolean isMouseButtonDown(int buttonCode) {
		return mouseButtons[buttonCode] && !mouseButtonsLastFrame[buttonCode];
	}
	
	//Mouse button was held down on this frame
	public boolean isMouseButton(int buttonCode) {
		return mouseButtons[buttonCode];
	}
	
	//Mouse button was released on this frame
	public boolean isMouseButtonUp(int buttonCode) {
		return !mouseButtons[buttonCode] && mouseButtonsLastFrame[buttonCode];
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		scroll = e.getWheelRotation();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = (int) (e.getX() / settings.SCALE);
		mouseY = (int) (e.getY() / settings.SCALE);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = (int) (e.getX() / settings.SCALE);
		mouseY = (int) (e.getY() / settings.SCALE);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseButtons[e.getButton()] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseButtons[e.getButton()] = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
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
