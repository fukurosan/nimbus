package com.nimbus.engine.ecs;

import java.util.ArrayList;
import java.util.List;

import com.nimbus.engine.ecs.component.IComponent;

public class Query {

	private List<Operation> operations;

	public Query() {
		operations = new ArrayList<Operation>();
	}

	public Query hasComponent(Class<? extends IComponent> component) {
		operations.add(new Operation(Operation.OPERATION_TYPES.HAS, component));
		return this;
	}

	public Query notHasComponent(Class<? extends IComponent> component) {
		operations.add(new Operation(Operation.OPERATION_TYPES.NOT_HAS, component));
		return this;
	}

	public Query innerJoin(Query query) {
		operations.add(new Operation(Operation.OPERATION_TYPES.INNER_JOIN, query));
		return this;
	}

	public Query outerJoin(Query query) {
		operations.add(new Operation(Operation.OPERATION_TYPES.OUTER_JOIN, query));
		return this;
	}

	public List<Operation> getOperations() {
		return operations;
	}

}
