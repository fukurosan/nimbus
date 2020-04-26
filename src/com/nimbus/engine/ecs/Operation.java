package com.nimbus.engine.ecs;

public class Operation {

	private OPERATION_TYPES operationType;
	private Object data;
	
	public Operation(OPERATION_TYPES operationType, Object data) {
		this.operationType = operationType;
		this.data = data;
	}
	
	public enum OPERATION_TYPES {
		HAS, NOT_HAS, INNER_JOIN, OUTER_JOIN
	}

	public OPERATION_TYPES getOperationType() {
		return operationType;
	}

	public Object getData() {
		return data;
	}
}
