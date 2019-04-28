package com.callcenter.util;

import java.util.Arrays;
import java.util.List;

import com.callcenter.entities.Employee;

/**
 * Clase creada para manejar en memoria los empleados pensada para facilitar los
 * difetentes casos de pruebas
 * 
 * @author gustavo clavijo
 */
public class FactoryEmployee {

	private FactoryEmployee() {
		throw new IllegalStateException("Factory Employee class");
	}

	private static List<Employee> listEmployess = null;

	/**
	 * Obtiene el listado de empleados
	 * 
	 * @return lista de empleados parametrizados
	 */
	public static synchronized List<Employee> getListEmployee() {
		if (listEmployess == null)
			createListEmployee();
		return listEmployess;
	}

	/**
	 * Actualziacion de estado de disponiblidad de los empleados en memoria
	 * 
	 * @param emmployee
	 *            , empleado a actualizar
	 */
	public static synchronized void updateEmployee(Employee emmployee) {
		getListEmployee().stream().filter(p -> p.getId() == emmployee.getId())
				.forEach(p -> p.setAvailable(emmployee.getAvailable()));
	}

	private static synchronized void createListEmployee() {
		if (listEmployess == null) {
			listEmployess = Arrays.asList(
					new Employee(1, TypeEmployee.OPERATOR.getType(), Boolean.TRUE, TypeEmployee.OPERATOR.getOrder()),
					new Employee(2, TypeEmployee.DIRECTOR.getType(), Boolean.TRUE, TypeEmployee.DIRECTOR.getOrder()),
					new Employee(3, TypeEmployee.OPERATOR.getType(), Boolean.TRUE, TypeEmployee.OPERATOR.getOrder()),
					new Employee(4, TypeEmployee.OPERATOR.getType(), Boolean.TRUE, TypeEmployee.OPERATOR.getOrder()),
					new Employee(5, TypeEmployee.SUPERVISOR.getType(), Boolean.TRUE,
							TypeEmployee.SUPERVISOR.getOrder()),
					new Employee(6, TypeEmployee.OPERATOR.getType(), Boolean.TRUE, TypeEmployee.OPERATOR.getOrder()),
					new Employee(7, TypeEmployee.OPERATOR.getType(), Boolean.TRUE, TypeEmployee.OPERATOR.getOrder()),
					new Employee(8, TypeEmployee.SUPERVISOR.getType(), Boolean.TRUE,
							TypeEmployee.SUPERVISOR.getOrder()),
					new Employee(9, TypeEmployee.OPERATOR.getType(), Boolean.TRUE, TypeEmployee.OPERATOR.getOrder()),
					new Employee(10, TypeEmployee.OPERATOR.getType(), Boolean.TRUE, TypeEmployee.OPERATOR.getOrder()),
					new Employee(11, TypeEmployee.SUPERVISOR.getType(), Boolean.TRUE,
							TypeEmployee.SUPERVISOR.getOrder()),
					new Employee(12, TypeEmployee.DIRECTOR.getType(), Boolean.TRUE, TypeEmployee.DIRECTOR.getOrder()));
		}
	}

	public static synchronized void setListEmployee(List<Employee> employee) {
		listEmployess = employee;
	}
}
