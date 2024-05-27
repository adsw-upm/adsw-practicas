package es.upm.dit.adsw.p3;

import es.upm.dit.adsw.graph.Node;
import es.upm.dit.adsw.lab3.GraphPartidas;

/**
 * Hebra que calcula las distancias entre tableros, hasta que no existe ningún tablero sin
 * calcular en el monitor.
 */
public class HebraDistancias extends Thread {

	private GraphPartidas g;
	private MonitorDistancias monitor;

	/**
	 * Crea una hebra de cálculo.
	 * @param g Grafo dirigido de tableros
	 * @param monitor Monitor de distancias donde se almacenan tableros pendientes y distancias calculadas.
	 */
	public HebraDistancias(GraphPartidas g, MonitorDistancias monitor) {
		this.g=g;
		this.monitor=monitor;
	}

	@Override
	public void run() {
		//TODO: Desarrollar este método
	}
}
