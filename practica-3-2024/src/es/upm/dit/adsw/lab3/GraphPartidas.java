package es.upm.dit.adsw.lab3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import es.upm.dit.adsw.Partida;
import es.upm.dit.adsw.Tablero;
import es.upm.dit.adsw.Tupla;
import es.upm.dit.adsw.graph.Graph;
import es.upm.dit.adsw.graph.Link;
import es.upm.dit.adsw.graph.Node;

/**
 * Clase que representa un grafo dirigido de tableros.
 */
public class GraphPartidas extends Graph {

    private Map<String, Node> tableroToNodeMap = new HashMap<String, Node>();

    public GraphPartidas(List<Partida> partidas) {
        super();
        Map<Node, Map<Node, Integer>> contador = new HashMap<>();
        for(Partida partida: partidas){
        	Node anterior = null;
        	Node actual;
        	for(Tablero tablero: partida.turnos) {
        		actual = anadeNodo(tablero.toString());
        		if(anterior != null) {
        			var cuentas = contador.get(anterior);
        			if(cuentas == null) {
        				cuentas = new HashMap<>();
        				contador.put(anterior, cuentas);
        			}
        			cuentas.put(actual, cuentas.getOrDefault(contador, 0) + 1);
        		}
        		anterior = actual;
        	}
        }
        for(Entry<Node, Map<Node, Integer>> e1: contador.entrySet()) {
        	Node anterior = e1.getKey();
        	for(Entry<Node, Integer> e2: e1.getValue().entrySet()) {
        		Node actual = e2.getKey();
    			addLink(new Link(anterior, actual, e2.getValue()));

        	}
        }

    }

    public Node anadeNodo(String tablero) {
    	Node node = tableroToNodeMap.get(tablero);
        if (node == null) {
        	node = new Node(tablero);
            tableroToNodeMap.put(tablero, node);
            addNode(node);
        }
        return node;
    }

    public List<Link> getLinksOfNode(Node node) {
        return links.stream()
            .filter(link -> link.getFromNode().equals(node))
            .toList();
    }
    public int BFS2(String startRepresentation, String endRepresentation) {
        Map<Node, Integer> distancias = new HashMap<>();
        List<Node> toVisit = new ArrayList<>();

        while(! toVisit.isEmpty() ){
            Node actual=toVisit.remove(0);
            if(actual.getName().equals(endRepresentation)) {
            	return distancias.get(actual);
            }
            for(Link saliente : getLinksOfNode(actual)){
                Node hijo = saliente.getToNode();
                if(! distancias.containsKey(hijo)) {
                	distancias.put(hijo, distancias.get(actual)+1);
                	toVisit.add(hijo);
                }
            }
        }
        return -1;
    }

    public int BFS(String startRepresentation, String endRepresentation) {
        Map<Node, Integer> distances = new HashMap<Node, Integer>();
        Node startNode = tableroToNodeMap.get(startRepresentation);
        Node endNode = tableroToNodeMap.get(endRepresentation);
        List<Node> queue = new ArrayList<Node>();
        queue.add(startNode);
        distances.put(startNode, 0);
        while (!queue.isEmpty()) {
            Node currentNode = queue.remove(0);
            if (currentNode.equals(endNode)) {
                return distances.get(currentNode);
            }
            List<Link> links = getLinksOfNode(currentNode);
            for (Link link : links) {
                Node nextNode = link.getToNode();
                if (!distances.containsKey(nextNode)) {
                    distances.put(nextNode, distances.get(currentNode) + 1);
                    queue.add(nextNode);
                }
            }
        }
        return -1;
    }


    public Map<String, Node> getTableroToNodeMap() {
        return tableroToNodeMap;
    }
    
}
