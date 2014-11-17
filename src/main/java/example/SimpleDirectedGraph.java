/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import automenta.vivisect.graph.display.DefaultDisplay;
import automenta.vivisect.Video;
import automenta.vivisect.dimensionalize.AbegoTreeLayout;
import automenta.vivisect.dimensionalize.FastOrganicLayout;
import automenta.vivisect.graph.AnimatingGraphVis;
import automenta.vivisect.graph.GraphDisplay;
import automenta.vivisect.graph.AbstractGraphVis;
import automenta.vivisect.graph.EdgeVis;
import automenta.vivisect.graph.VertexVis;
import automenta.vivisect.swing.NWindow;
import automenta.vivisect.swing.PCanvas;
import java.awt.Color;
import org.jgrapht.Graph;

import org.jgrapht.graph.DirectedMultigraph;

/**
 *
 * @author me
 */
public class SimpleDirectedGraph {
    
    

    public SimpleDirectedGraph(Graph g, GraphDisplay layout) {
        
        new NWindow("Directed Graph", 
                new PCanvas( 
                        new AnimatingGraphVis(g, 
                                new DefaultDisplay(),
                                layout                                
                        )
                )
        ).show(800,800,true);
        
    }

    
    public static void main(String[] args) {
        DirectedMultigraph g = new DirectedMultigraph(Integer.class);
        g.addVertex("A");
        g.addVertex("B");
        g.addEdge("A","B",1);
        
        new SimpleDirectedGraph(g, new FastOrganicLayout());
        
                                        
    }
    
}
