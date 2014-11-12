/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import automenta.vivisect.dimensionalize.AbegoTreeLayout;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedPseudograph;

/**
 *
 * @author me
 */
public class SimpleTree {

    public static void main(String[] args) {
        
        Graph g = new DirectedPseudograph(DefaultEdge.class);
        g.addVertex("root");        
        g.addVertex("a");
        g.addVertex("b");
        g.addEdge("root", "a");
        g.addEdge("root", "b");
        
        g.addVertex("aa");
        g.addVertex("ab");
        g.addEdge("a", "aa");
        g.addEdge("a", "ab");
        
        g.addVertex("ba");
        g.addEdge("b", "ba");
        
        g.addVertex("root2");
        g.addVertex("c");
        g.addEdge("root2", "c");
        
        g.addEdge("c", "ba");
        
        //g.addEdge("ab", "c");
        
        new SimpleDirectedGraph(g, new AbegoTreeLayout());
    }
}
