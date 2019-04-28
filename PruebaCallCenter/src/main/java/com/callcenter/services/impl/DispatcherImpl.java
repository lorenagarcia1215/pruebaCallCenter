/**
 * 
 */
package com.callcenter.services.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.callcenter.entities.Employee;
import com.callcenter.services.Dispatcher;
import com.callcenter.util.Constants;
import com.callcenter.util.FactoryEmployee;

/**
 * @author Gustavo Clavijo Clase manejadora de llamadas
 */
@Service
public class DispatcherImpl implements Dispatcher {

	private static final Logger logger = LoggerFactory.getLogger(DispatcherImpl.class);

	@Async("threadPoolTaskExecutor")
	public CompletableFuture<String> dispatchCall(String call) {
		long start = 0;
		logger.info("Comenzo dispatchCall  con la llamada {}.", call);
		Optional<Employee> employeeAttendant = Optional.empty();
		for (int attemp = 1; attemp <= Constants.MAX_ATTEMPS_WAIT; attemp++) {
			// Ordernar por tipo personalizadamente y recorrer preguntando disponibles
			employeeAttendant = getEmployeeAtentand();
			if (employeeAttendant.isPresent()) {
				logger.info("Llamada {} atendida por el empleado con id {} y role {}", call,
						employeeAttendant.get().getId(), employeeAttendant.get().getRole());
				start = System.currentTimeMillis();
				break;
			} else {
				// Si todos los empleados estan indisponibles se espera en linea
				logger.info("No se encuentran empleados disponibles para la llamada {} intento {}", call, attemp);
				waitAvailabilityTime(attemp);
			}
		}
		//Se valida si despues de todos los intentos se obtuvo un empleado disponible
		if (employeeAttendant.isPresent()) {
			processCall();
			employeeAttendant.get().setAvailable(Boolean.TRUE);
			changeAvailabilityEmployee(employeeAttendant.get());
		} else {
			return CompletableFuture.completedFuture(Constants.MESSAGE_MAX_WAIT);
		}

		logger.info("Termino dispatchCall con la llamada {} y la duracion de {} seg.", call,
				(System.currentTimeMillis() - start) / 1000);
		return CompletableFuture.completedFuture(Constants.MESSAGE_SUCCES);

	}

	/**
	 * Metodo que obtiene el tiempo de espera en linea en caso de no tener empleados
	 * disponibles
	 * 
	 * @param attemp
	 *            , numero de intento
	 */
	private void waitAvailabilityTime(Integer attemp) {
		try {
			Thread.sleep(Constants.TIME_WAIT_MILIS * attemp);
		} catch (InterruptedException e) {
			logger.error("Proceso interrunpido llamada en espera", e);
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Metodo utilizado para obtener de forma sincronizada el empleado disponible
	 * que debe atender la llamda segun su rol y actualizar que ese empleado ahora
	 * no va estar disponible
	 * 
	 * @return Empleado disponible en caso de existir
	 */
	private synchronized Optional<Employee> getEmployeeAtentand() {
		return queryEmployees().parallelStream().sorted(Comparator.comparingInt(Employee::getOrderAttention))
				.filter(p -> p.getAvailable() == Boolean.TRUE).findFirst().map(p -> {
					Employee employee = new Employee(p.getId(), p.getRole(), Boolean.FALSE, p.getOrderAttention());
					changeAvailabilityEmployee(employee);
					return employee;
				});
	}

	/**
	 * Metodo que realiza la consulta de todos los empleados que pueden atender las
	 * llamadas segun las reglas de negocio que se definan
	 * 
	 * @return Listado de Empleados
	 */
	private synchronized List<Employee> queryEmployees() {
		// Para efectos de practicos de la prueba se crean empleados en memoria
		return FactoryEmployee.getListEmployee();
	}

	/**
	 * Metodo que cambia el estado de disponibilidad de un Empleado
	 * 
	 * @param empleado
	 *            a ser actualizado
	 */
	private synchronized void changeAvailabilityEmployee(Employee employee) {
		// Para efectos de practicos de la prueba se cambia el estado del empleado en
		// memoria
		FactoryEmployee.updateEmployee(employee);
	}

	/**
	 * Metodo utilizado para sinmular la duracion de la llamada
	 */
	private void processCall() {
		try {
			Thread.sleep(getDurationtCall());
		} catch (InterruptedException e) {
			logger.error("Proceso interrunpido", e);
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Metodo para cualcular aleatoriamente un tiempo en milisegundos entre 5 a 10
	 * segundos
	 * 
	 * @return numero de milisegundos generado
	 */
	private int getDurationtCall() {
		return (new Random()).ints(5000, (10000 + 1)).findFirst().getAsInt();
	}
}
