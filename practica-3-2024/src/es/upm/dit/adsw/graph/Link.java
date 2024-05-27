package es.upm.dit.adsw.graph;

public class Link {
    private Node fromNode;
    private Node toNode;
    private int weight;

    public Link(Node fromNode, Node toNode, int weight) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.weight = weight;
    }

    public Node getFromNode() {
        return fromNode;
    }

    public Node getToNode() {
        return toNode;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return fromNode + " -> " + toNode + " [Weight: " + weight + "]";
    }
} 
