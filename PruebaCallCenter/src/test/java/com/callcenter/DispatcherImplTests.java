package com.callcenter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.callcenter.services.Dispatcher;
import com.callcenter.util.Constants;
import com.callcenter.util.FactoryEmployee;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DispatcherImplTests {

	@Autowired
	Dispatcher dispatcher;

	/**
	 * Test para probar el caso de prueba cuando se lanzan 10 llamadas al mismo
	 * momento de forma concurrente Con el test se demuestra que el sistema esta
	 * preparado para lanzar las diez llamadas de forma concurrente y todas terminan
	 * exitosamente.
	 * 
	 * Con la linea executor.setCorePoolSize(10); del la clase de configuracion
	 * AsyncConf se puede definir cuantos hilos (llamadas) puede recibir los metodos
	 * con las notaciones @Async , en caso de superar el numero de hilos previstos
	 * estos se encolaran y quedaran a la espera de la finalizacion de los otros
	 * hilos
	 * 
	 */
	@Test
	public void concurrent10Calls() {
		List<String> myList = Arrays.asList("llamada1", "llamada2", "llamada3", "llamada4", "llamada5", "llamada6",
				"llamada7", "llamada8", "llamada9", "llamada10");
		// Resultado obtenido en la prueba
		List<String> resultsTest = new ArrayList<>();
		// Resultado Esperado
		List<String> resultsExpected = Arrays.asList(Constants.MESSAGE_SUCCES, Constants.MESSAGE_SUCCES,
				Constants.MESSAGE_SUCCES, Constants.MESSAGE_SUCCES, Constants.MESSAGE_SUCCES, Constants.MESSAGE_SUCCES,
				Constants.MESSAGE_SUCCES, Constants.MESSAGE_SUCCES, Constants.MESSAGE_SUCCES, Constants.MESSAGE_SUCCES);
		// Listado de llamadas concurrentes
		List<CompletableFuture<String>> resultsCalls = new ArrayList<>();

		myList.stream().forEach(call -> {
			CompletableFuture<String> result = dispatcher.dispatchCall(call);
			resultsCalls.add(result);
		});

		// Se sincroniza el resultado de las llamadas (hilos) realizadas
		CompletableFuture.allOf(resultsCalls.toArray(new CompletableFuture[resultsCalls.size()])).join();

		for (CompletableFuture<String> completableFuture : resultsCalls) {
			try {
				if (completableFuture.get() != null) {
					// Se obtienen los resultados de los diferentes hilos
					resultsTest.add(completableFuture.get());
				} else {
					FactoryEmployee.setListEmployee(null);
					fail();
				}
			} catch (InterruptedException | ExecutionException e) {
				FactoryEmployee.setListEmployee(null);
				fail();
				Thread.currentThread().interrupt();
			}
		}
		// Se reinicia el listado de empleados
		FactoryEmployee.setListEmployee(null);
		// se comprueba que el resultado obtenido sea igual al esperado
		assertThat(resultsTest, is(resultsExpected));
	}

	/**
	 * Test que prueba el funcionamiento del sistema cuando se realizan llamadas que
	 * superan el numero de empleados disponibles , para efectos de pruebas se crean
	 * 12 empleados disponibles de los tres roles descritos en los requerimientos,
	 * en este caso se lanzan 20 llamadas esperando una respuesta exitosa para todas
	 * las llamadas . Ademas esta prueba cubre el caso de prueba de mas de 10
	 * llamadas concurrentes donde esto se soluciona encolando dichas llamadas hasta
	 * el maximo parametrizado
	 * 
	 * Cuando el sistema no encuentra empleados disponible se implemento un proceso
	 * de espera con un numero de reintentos para consultar de nuevo si ya hay
	 * empleados disponbiles , para cada reintento se aumenta exponencialmente el
	 * numero de espera para volver a preguntar si hay empleados disponibles , en
	 * caso de que se supere el numero de reintentos y no se logre obtener un
	 * empleado disponible el sistema para esa llamada resonde que se comuniquen
	 * luego .
	 * 
	 * Otra posible definicion que se pudo tener encuenta es que se permaneciera en
	 * linea hasta que se obtuviera un empleado o se colgara la llamada.
	 */
	@Test
	public void callsEmployeeNotAvailable() {
		List<String> myList = Arrays.asList("llamada1", "llamada2", "llamada3", "llamada4", "llamada5", "llamada6",
				"llamada7", "llamada8", "llamada9", "llamada10", "llamada11", "llamada12", "llamada13", "llamada14",
				"llamada15", "llamada16", "llamada17", "llamada18", "llamada19", "llamada20");
		// Resultado obtenido en la prueba
		List<String> resultsTest = new ArrayList<>();
		// Resultado Esperado
		List<String> resultsExpected = Arrays.asList(Constants.MESSAGE_SUCCES, Constants.MESSAGE_SUCCES,
				Constants.MESSAGE_SUCCES, Constants.MESSAGE_SUCCES, Constants.MESSAGE_SUCCES, Constants.MESSAGE_SUCCES,
				Constants.MESSAGE_SUCCES, Constants.MESSAGE_SUCCES, Constants.MESSAGE_SUCCES, Constants.MESSAGE_SUCCES,
				Constants.MESSAGE_SUCCES, Constants.MESSAGE_SUCCES, Constants.MESSAGE_SUCCES, Constants.MESSAGE_SUCCES,
				Constants.MESSAGE_SUCCES, Constants.MESSAGE_SUCCES, Constants.MESSAGE_SUCCES, Constants.MESSAGE_SUCCES,
				Constants.MESSAGE_SUCCES, Constants.MESSAGE_SUCCES);
		// Listado de llamadas concurrentes
		List<CompletableFuture<String>> resultsCalls = new ArrayList<>();

		myList.stream().forEach(call -> {
			CompletableFuture<String> result = dispatcher.dispatchCall(call);
			resultsCalls.add(result);
		});

		// Se sincroniza el resultado de las llamadas (hilos) realizadas
		CompletableFuture.allOf(resultsCalls.toArray(new CompletableFuture[resultsCalls.size()])).join();

		for (CompletableFuture<String> completableFuture : resultsCalls) {
			try {
				if (completableFuture.get() != null) {
					// Se obtienen los resultados de los diferentes hilos
					resultsTest.add(completableFuture.get());
				} else {
					FactoryEmployee.setListEmployee(null);
					fail();
				}
			} catch (InterruptedException | ExecutionException e) {
				FactoryEmployee.setListEmployee(null);
				Thread.currentThread().interrupt();
				fail();
			}
		}
		FactoryEmployee.setListEmployee(null);
		// se comprueba que el resultado obtenido sea igual al esperado
		assertThat(resultsTest, is(resultsExpected));
	}
}
