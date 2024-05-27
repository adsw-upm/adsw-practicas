package es.upm.dit.adsw.p3;

public interface InterfazBuscador {
	/**
	 * Devuelve la distancia calculada entre un tablero de origen y otro de destino en el grafo con el que se ha creado el buscador.
	 * @param origen Representación del tablero de origen
	 * @param destino Representación del tablero de destino
	 * @return Distancia entre ambos tableros, o null si no existe un camino posible entre origen y destino.
	 * @throws Exception Si hay algún problema en calcular la distancia.
	 */
	public Integer getDistancia(String origen, String destino) throws Exception;
	
}
