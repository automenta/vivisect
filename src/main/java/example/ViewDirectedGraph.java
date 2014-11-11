/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import automenta.vivisect.graph.AnimatedProcessingGraphCanvas;
import automenta.vivisect.graph.GraphDisplay;
import automenta.vivisect.graph.ProcessingGraphCanvas;
import automenta.vivisect.swing.NWindow;
import automenta.vivisect.swing.Swing;
import java.awt.Color;
import org.jgrapht.Graph;

import org.jgrapht.graph.DirectedMultigraph;

/**
 *
 * @author me
 */
public class ViewDirectedGraph {
    
    
    public static class DefaultDisplay implements GraphDisplay {

        long startTime = System.currentTimeMillis();

        public float getTime() {
            return (System.currentTimeMillis() - startTime)/1000.0f;
        }
        
        @Override
        public Shape getVertexShape(Object v) {
            return Shape.Ellipse;
        }

        @Override
        public String getVertexLabel(Object v) {
            return v.toString();
        }

        @Override
        public float getVertexSize(Object v) {
            return 16.0f + 7f * (float)Math.sin( getTime()*10f);
        }

        @Override
        public int getVertexColor(Object o) {
            return Swing.getColor(o.hashCode(), 0.75f, 0.95f, 0.75f).getRGB();
        }

        @Override
        public float getEdgeThickness(Object edge, ProcessingGraphCanvas.VertexDisplay source, ProcessingGraphCanvas.VertexDisplay target) {
            return 23.0f;
        }

        @Override
        public int getEdgeColor(Object e) {
            return Color.GRAY.getRGB();
        }

        @Override
        public int getTextColor(Object v) {
            return Color.WHITE.getRGB();
        }

        @Override
        public boolean updateNext() {            
            //enables continuous animation
            return true;
        }
        
        
    }

    public ViewDirectedGraph(Graph g) {
        
        new NWindow("Directed Graph", 
                new AnimatedProcessingGraphCanvas(g, new DefaultDisplay())        
        ).show(800,800,true);
        
    }

    
    public static void main(String[] args) {
        DirectedMultigraph g = new DirectedMultigraph(Integer.class);
        g.addVertex("A");
        g.addVertex("B");
        g.addEdge("A","B",1);
        
        new ViewDirectedGraph(g);        
    }
    
}
