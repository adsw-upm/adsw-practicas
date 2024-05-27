package es.upm.dit.adsw.p3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import es.upm.dit.adsw.lab3.GraphPartidas;

public class BuscadorTablerosMonitor implements InterfazBuscador {

	public Map<String, Map<String, Integer>> distancias;

	public BuscadorTablerosMonitor(GraphPartidas g) {
		this.distancias = new HashMap<String, Map<String, Integer>>(); 

		//TODO: Desarrollar este constructor
	}
	public Integer getDistancia(String origen, String destino) throws Exception {
		if(!distancias.containsKey(origen)) return null;
		return distancias.get(origen).get(destino);
	}

}
