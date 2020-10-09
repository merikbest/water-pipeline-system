package merikbest.waterpipelinesystem.service;

public class EdgeWeighted implements Comparable<EdgeWeighted> {

    NodeWeighted source;
    NodeWeighted destination;
    double weight;

    public EdgeWeighted(NodeWeighted s, NodeWeighted d, double w) {
        source = s;
        destination = d;
        weight = w;
    }

    public String toString() {
        return String.format("(%s -> %s, %f)", source.name, destination.name, weight);
    }

    @Override
    public int compareTo(EdgeWeighted otherEdge) {
        if (this.weight > otherEdge.weight) {
            return 1;
        }
        else return -1;
    }
}
