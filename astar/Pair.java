package astar;

import java.util.Comparator;

public class Pair{
    private Double cost;
    private Node node;

    public Pair(double cost, Node station){
        this.cost = cost;
        this.node = station;

    }

    public Node getNode(){
        return this.node;
    }

    public Double getCost(){
        return this.cost;
    }

}

class PairComparator implements Comparator<Pair>{

    @Override
    public int compare(Pair o1, Pair o2) {
        return o1.getCost().compareTo(o2.getCost());
    }

}
