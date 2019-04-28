package com.callcenter.util;
/**
 * Enumeracion de roles de empleados
 * @author gustavo clavijo
 */
public enum TypeEmployee {

	OPERATOR("Operador",1),
	DIRECTOR("Director",3),
	SUPERVISOR("Supervisor",2);

	private String type;
    private Integer order;
	
	TypeEmployee(String type , Integer order) {
        this.type = type;
        this.order = order;
    }

	public String getType() {
		return type;
	}
	
	public Integer getOrder() {
		return order;
	}
}
