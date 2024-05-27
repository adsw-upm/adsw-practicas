package es.upm.dit.adsw.graph;

import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class Graph {
    protected List<Node> nodes = new ArrayList<>();
    protected List<Link> links = new ArrayList<>();

    public void addNode(Node node) {
        // Flag para verificar si el nodo ya existe
        boolean exists = false;

        // Buscar en la lista de nodos existentes
        for (Node existingNode : nodes) {
            if (existingNode.getName().equals(node.getName())) {
                exists = true;
                break; // Salir del bucle si se encuentra un nodo con el mismo nombre
            }
        }

        // Añadir el nodo solo si no existe uno con el mismo nombre
        if (!exists) {
            nodes.add(node);
        } else {
            System.out.println("Ya existe un nodo con el nombre: '" + node.getName() + "'.");
        }
    }

    public void addLink(Link link) {
        links.add(link);
    }

    public void exportarCSV(String nombreArchivo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            // Escribir el encabezado en la primera línea
            pw.println("from,to,weight");

            // Iterar sobre cada enlace y escribir sus datos en el archivo
            for (Link link : links) {
                int fromNodeHash = link.getFromNode().hashCode();
                int toNodeHash = link.getToNode().hashCode();
                int weight = link.getWeight();
                pw.println(fromNodeHash + "," + toNodeHash + "," + weight);
            }
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo: " + e.getMessage());
        }
    }

    public List<Node> getNodes() {
        return nodes;
    }
    public List<Link> getLinks() {
        return links;
    }


}
