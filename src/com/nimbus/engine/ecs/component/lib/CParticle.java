package com.nimbus.engine.ecs.component.lib;

import com.nimbus.engine.ecs.component.IComponent;

public class CParticle implements IComponent {

	public static final float CENTER_TILT = -50;
	
	// Direction
	private float directionX;
	private float directionY;
	// Particle life span
	private float cooldown;
	// Particle speed
	private float force;
	// Particle colour
	private int colour;
	// Light colour
	private int lightColour;
	// Light intensity
	private int lightSize;

	public CParticle(float tiltX, float tiltY, float cooldown, float force, int colour, int lightSize, int lightColour) {
		this.cooldown = cooldown;
		this.force = force;
		this.colour = colour;
		this.lightSize = lightSize;
		this.lightColour = lightColour;
		//The tilt will tilt the particle in a certain direction. The force will set a minimum and maximum speed
		directionX = (float) ((Math.random() * 100) + tiltX) * force;
		directionY = (float) ((Math.random() * 100) + tiltY) * force;
	}

	public float getDirectionX() {
		return directionX;
	}

	public void setDirectionX(float directionX) {
		this.directionX = directionX;
	}

	public float getDirectionY() {
		return directionY;
	}

	public void setDirectionY(float directionY) {
		this.directionY = directionY;
	}

	public float getCooldown() {
		return cooldown;
	}

	public void setCooldown(float cooldown) {
		this.cooldown = cooldown;
	}

	public float getForce() {
		return force;
	}

	public void setForce(float force) {
		this.force = force;
	}

	public int getColour() {
		return colour;
	}

	public void setColour(int colour) {
		this.colour = colour;
	}

	public int getLightSize() {
		return lightSize;
	}

	public void setLightSize(int lightSize) {
		this.lightSize = lightSize;
	}

	public int getLightColour() {
		return lightColour;
	}

	public void setLightColour(int lightColour) {
		this.lightColour = lightColour;
	}

}
