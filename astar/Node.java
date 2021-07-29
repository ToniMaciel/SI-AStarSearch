package astar;

import java.util.ArrayList;

public class Node{
    public int station;
    public String line;
    public ArrayList<Pair> connections;
    public boolean isVisited = false;
    public Double totalCost;
    public Node parentNode = null;

    public Node(int station, String line, double cost){
        this.station = station;
        this.line = line;
        this.totalCost = cost;
        this.connections = new ArrayList<>();
    }
}