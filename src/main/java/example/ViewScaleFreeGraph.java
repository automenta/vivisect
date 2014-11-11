/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import org.jgrapht.Graph;
import org.jgrapht.VertexFactory;
import org.jgrapht.generate.ScaleFreeGraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedPseudograph;

/**
 *
 * @author me
 */
public class ViewScaleFreeGraph {
 
        
    public static void main(String[] args) {
    VertexFactory<Object> vertexFactory =
        new VertexFactory<Object>() {
            private int i;

            public Object createVertex()
            {
                return new Integer(++i);
            }
        };
        Graph g = new DirectedPseudograph(DefaultEdge.class);
        new ScaleFreeGraphGenerator<>(16).generateGraph(g, vertexFactory, null);
        new ViewDirectedGraph(g);        
    }
}
