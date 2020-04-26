package com.nimbus.engine.ecs.component;

public interface IGlobalComponent {
	//There can only be one global component of the same type in a scene at any given time
	//Global components are managed by the component manager and not bound to an entity
	//Global components are made available to all entity systems in a given scene
}