package es.upm.dit.adsw.p3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.upm.dit.adsw.graph.Link;
import es.upm.dit.adsw.graph.Node;
import es.upm.dit.adsw.lab3.GraphPartidas;

public class BuscadorTableros implements InterfazBuscador {
	public Map<String, Map<String, Integer>> distancias;

	/**
	 * Construye el buscador a partir del grafo de partidas. Pre-calcula todas las distancias necesarias.
	 * 
	 * @param g Grafo dirigido de partidas
	 */
	public BuscadorTableros(GraphPartidas g) {
		this.distancias = new HashMap<String, Map<String, Integer>>();
		for (Node n1 : g.getNodes()) {
			String nodeRepresentation = n1.getName();
			Map<String, Integer> m = calcularDesdeOrigen(g, n1);
			distancias.put(nodeRepresentation, m);
		}
	}

	/**
	 * 
	 * @param g Grafo con todos los tableros
	 * @param origen Nodo a partir del cual se van a calcular las distancias
	 * @return Diccionario cuyas claves son tableros de destino y los valores son la distancia calculada. 
	 */
	public static Map<String, Integer> calcularDesdeOrigen(GraphPartidas g, Node origen) {
		Map<String, Integer> distancias = new HashMap<>();
		ArrayList<Link> pendientes = new ArrayList<>();

		distancias.put(origen.getName(), 0);
		for (Link arco : g.getLinksOfNode(origen)) {
			pendientes.add(arco);
		}

		while (!pendientes.isEmpty()) {
			Link arco = pendientes.remove(0);
			if (distancias.containsKey(arco.getToNode().getName())) {
				continue;
			}
			distancias.put(arco.getToNode().getName(), distancias.get(arco.getFromNode().getName()) + 1);
			for (Link saliente : g.getLinksOfNode(arco.getToNode())) {
				pendientes.add(saliente);
			}
		}
		return distancias;
	}

	/**
	 * Devuelve la distancia calculada desde el tablero origen al tablero destino
	 * @param origen Representación del tablero de origen
	 * @param destino Representación del tablero de destino
	 * @return Distancia entre los dos tableros
	 */
	public Integer getDistancia(String origen, String destino) throws Exception {
		if (!distancias.containsKey(origen)) {
			return null;
		}
		return distancias.get(origen).get(destino);
	}

}
