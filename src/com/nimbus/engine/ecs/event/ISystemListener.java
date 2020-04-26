package com.nimbus.engine.ecs.event;

import com.nimbus.engine.ecs.system.ASystem;

public interface ISystemListener {
	public abstract void systemAdded(ASystem adminSystem);
	public abstract void systemRemoved(ASystem adminSystem);
}
