
package com.callcenter.services;

import java.util.concurrent.CompletableFuture;

/**
 * @author Gustavo Clavijo
 * Interface de clase manejadora de llamadas
 */
public interface Dispatcher {
	/**
	 * Metodo principal encargado de realizar el despacho de llamadas de forma
	 * asincrona
	 * @param call , llamada entrante
	 * @return se retorna un mensaje indicando si la llamada es exitosa o no
	 */
	public CompletableFuture<String> dispatchCall (String llamada);
}
