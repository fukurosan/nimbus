package com.nimbus.engine.ecs.event;

import com.nimbus.engine.ecs.component.IGlobalComponent;

public interface IComponentListener {
	void globalComponentAdded(IGlobalComponent component);
	void globalComponentRemoved(IGlobalComponent component);
}
