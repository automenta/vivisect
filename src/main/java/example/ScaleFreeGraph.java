/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import automenta.vivisect.dimensionalize.FastOrganicLayout;
import org.jgrapht.Graph;
import org.jgrapht.VertexFactory;
import org.jgrapht.generate.ScaleFreeGraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedPseudograph;

/**
 *
 * @author me
 */
public class ScaleFreeGraph {

    static final VertexFactory<Object> DemoVertexFactory = new VertexFactory<Object>() {
        private int i;

        public Object createVertex() {
            return new Integer(++i);
        }
    };
    
    public static void main(String[] args) {
        
        Graph g = new DirectedPseudograph(DefaultEdge.class);
        new ScaleFreeGraphGenerator<>(16).generateGraph(g, DemoVertexFactory, null);
        new SimpleDirectedGraph(g, new FastOrganicLayout());
    }
}
