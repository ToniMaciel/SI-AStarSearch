package astar;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Astar{

    static double h[][] = new double[15][15];
    public static void main(String[] args) throws IOException{
        //Building the graph
        //Adding nodes
        Node e1 = new Node(1, "Blue", Double.POSITIVE_INFINITY);
        Node e2_blue = new Node(2, "Blue", Double.POSITIVE_INFINITY);
        Node e2_yellow = new Node(2, "Yellow", Double.POSITIVE_INFINITY);
        Node e3_blue = new Node(3, "Blue", Double.POSITIVE_INFINITY);
        Node e3_red = new Node(3, "Red", Double.POSITIVE_INFINITY);
        Node e4_blue = new Node(4, "Blue", Double.POSITIVE_INFINITY);
        Node e4_green = new Node(4, "Green", Double.POSITIVE_INFINITY);
        Node e5_blue = new Node(5, "Blue", Double.POSITIVE_INFINITY);
        Node e5_yellow = new Node(5, "Yellow", Double.POSITIVE_INFINITY);
        Node e6 = new Node(6, "Blue", Double.POSITIVE_INFINITY);
        Node e7 = new Node(7, "Yellow", Double.POSITIVE_INFINITY);
        Node e8_yellow = new Node(8, "Yellow", Double.POSITIVE_INFINITY);
        Node e8_green = new Node(8, "Green", Double.POSITIVE_INFINITY);
        Node e9_yellow = new Node(9, "Yellow", Double.POSITIVE_INFINITY);
        Node e9_red = new Node(9, "Red", Double.POSITIVE_INFINITY);
        Node e10 = new Node(10, "Yellow", Double.POSITIVE_INFINITY);
        Node e11 = new Node(11, "Red", Double.POSITIVE_INFINITY);
        Node e12 = new Node(12, "Green", Double.POSITIVE_INFINITY);
        Node e13_green = new Node(13, "Green", Double.POSITIVE_INFINITY);
        Node e13_red = new Node(13, "Red", Double.POSITIVE_INFINITY);
        Node e14 = new Node(14, "Green", Double.POSITIVE_INFINITY);
        //Adding edges
        add_connection(e1, e2_blue, 10*2);
        add_connection(e2_blue, e2_yellow, 4);
        add_connection(e2_blue, e3_blue, 8.5*2);
        add_connection(e3_blue, e3_red, 4);
        add_connection(e3_blue, e4_blue, 6.3*2);
        add_connection(e4_blue, e4_green, 4);
        add_connection(e4_blue, e5_blue, 13*2);
        add_connection(e5_blue, e5_yellow, 4);
        add_connection(e5_blue, e6, 3*2);
        add_connection(e2_yellow, e9_yellow, 10*2);
        add_connection(e2_yellow, e10, 3.5*2);
        add_connection(e5_yellow, e7, 2.4*2);
        add_connection(e5_yellow, e8_yellow, 30*2);
        add_connection(e8_yellow, e8_green, 4);
        add_connection(e8_yellow, e9_yellow, 9.6*2);
        add_connection(e9_yellow, e9_red, 4);
        add_connection(e4_green, e8_green, 15.3*2);
        add_connection(e4_green, e13_green, 12.8*2);
        add_connection(e8_green, e12, 6.4*2);
        add_connection(e13_green, e14, 5.1*2);
        add_connection(e13_green, e13_red, 4);
        add_connection(e3_red, e9_red, 9.4*2);
        add_connection(e3_red, e13_red, 18.7*2);
        add_connection(e9_red, e11, 12.2*2);
        
        // Building table 01
        heurist_values();

        Node begin = e6;
        Node end = e13_red;
        PriorityQueue<Pair> frontier = new PriorityQueue<>(new PairComparator());
        begin.totalCost = 0.0;
        frontier.add(new Pair((begin.totalCost + h[begin.station][end.station]), begin));
        
        a_star(end, frontier);
        
    }

    private static void a_star(Node destination, PriorityQueue<Pair> frontier) {
        int iteration = 1;
        
        while(!destination.equals(frontier.peek().getNode())){
            printFrontier(frontier, iteration, destination);
            Node parentNode = frontier.poll().getNode();
            iteration++;
            
            for (Pair edge : parentNode.connections) {
                Node childNode = edge.getNode();
                if(!childNode.isVisited){
                    if(parentNode.totalCost + edge.getCost() < childNode.totalCost){
                        childNode.parentNode = parentNode;
                        childNode.totalCost = parentNode.totalCost + edge.getCost();
                        Double f = childNode.totalCost + h[childNode.station][destination.station];
                        frontier.add(new Pair(f, childNode));
                    }
                }
            }
            parentNode.isVisited = true;
        }

        printFrontier(frontier, iteration, destination);
        System.out.println("Done!");
        printPath(frontier.peek().getNode());
    }

    private static void printPath(Node node) {      
        if(node.parentNode == null)
            System.out.print("(" + node.station + "," + node.line + ")" + " --> ");
        else{
            printPath(node.parentNode);
            
            if(node.isVisited)
                System.out.print("(" + node.station + "," + node.line + ")" + " --> ");
            else
                System.out.print("(" + node.station + "," + node.line + ")");
        }

    }

    private static void printFrontier(PriorityQueue<Pair> frontier, int iteration, Node destination) {
        System.out.print(iteration + "a iteração Fronteira Ordenada = ");

        PriorityQueue<Pair> frontierClone = new PriorityQueue<>(frontier);
        Pair aux;
        
        while( (aux = frontierClone.poll()) != null){
            Node node = aux.getNode();
            System.out.print("(" + node.station + ", " + node.line + ", " + new BigDecimal(aux.getCost()).setScale(2, RoundingMode.HALF_EVEN).doubleValue() + ", " + new BigDecimal(aux.getCost() - h[node.station][destination.station]).setScale(2, RoundingMode.HALF_EVEN).doubleValue() + ", " + h[node.station][destination.station] + ")");
            if (frontierClone.peek() != null) 
                System.out.print(", "); 
        }

        System.out.println();
    }

    private static void add_connection(Node station0, Node station1, double minutes) {
        station0.connections.add(new Pair(minutes, station1));
        station1.connections.add(new Pair(minutes, station0));
    }

    private static void heurist_values() throws IOException{
        FileReader fr = new FileReader("table01.txt");
        Scanner in = new Scanner(fr);
        String value;
        for (int i = 1; i < h.length; i++) {
            for (int j = i; j < h[i].length; j++) {
                value = in.next();
                h[i][j] = Double.parseDouble(value);
                h[j][i] = Double.parseDouble(value);
            }
        }
        fr.close();
        in.close();
    }
}