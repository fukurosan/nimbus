package com.nimbus.engine.extension;

import java.util.Stack;

import com.nimbus.engine.Nimbus;

public abstract class AGame {
	
	private Stack<AScene> scenes = new Stack<AScene>();
	
	public void popScene() {
		if(scenes.size() > 0) {
			scenes.peek().exiting();
			scenes.pop();
		}
		if(scenes.size() > 0) {
			scenes.peek().revealed();
		}
	}
	
	public void pushScene(AScene scene) {
		if(scenes.size() > 0) {
			scenes.peek().obscuring();
		}
		scenes.push(scene);
		scenes.peek().entered();
	}
	
	public AScene peekScene() {
		return scenes.peek();
	}

	
	//Life cycle method: After engine has started you can access different core components directly
	public abstract void init(Nimbus gc);
	//Life cycle method: This game is closing down, do any necessary cleanup
	public abstract void dispose();

}