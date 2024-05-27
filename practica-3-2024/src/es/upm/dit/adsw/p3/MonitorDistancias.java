package es.upm.dit.adsw.p3;

import java.util.Map;
import java.util.Stack;

/**
 * Monitor que se encarga de controlar el acceso a tableros pendientes y a las distancias calculadas, para el
 * correcto funcionamiento de las hebras de cálculo de distancias.
 */
public class MonitorDistancias {
	private Map<String, Map<String, Integer>> distancias;
	private Stack<String> tablerosPendientes;

	public MonitorDistancias(Map<String, Map<String, Integer>> distancias) {
		//TODO: Desarrollar
	}
	
	synchronized void putTablero(String tablero) {
		//TODO: Desarrollar
	}
	
	synchronized String getTablero() {
		//TODO: Desarrollar
		return null;
	}

	synchronized public void putDistancias(String origen, Map<String, Integer> distancias) {
		//TODO: Desarrollar
	}
		

}
