/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import automenta.vivisect.Video;
import automenta.vivisect.graph.AnimatedProcessingGraphCanvas;
import automenta.vivisect.graph.GraphDisplay;
import automenta.vivisect.graph.ProcessingGraphCanvas;
import automenta.vivisect.swing.NWindow;
import java.awt.Color;
import org.jgrapht.Graph;

import org.jgrapht.graph.DirectedMultigraph;

/**
 *
 * @author me
 */
public class ViewDirectedGraph {
    
    
    public static class DefaultDisplay<V,E> implements GraphDisplay<V,E> {

        long startTime = System.currentTimeMillis();

        public float getTime() {
            return (System.currentTimeMillis() - startTime)/1000.0f;
        }
        
        @Override
        public Shape getVertexShape(V v) {
            return Shape.Ellipse;
        }

        @Override
        public String getVertexLabel(V v) {
            return v.toString();
        }

        @Override
        public float getVertexSize(V v) {
            return 16.0f + 7f * (float)Math.sin( getTime()*10f);
        }

        @Override
        public int getVertexColor(V o) {
            return Video.getColor(o.hashCode(), 0.75f, 0.95f, 0.75f).getRGB();
        }

        @Override
        public float getEdgeThickness(E edge, ProcessingGraphCanvas.VertexDisplay source, ProcessingGraphCanvas.VertexDisplay target) {
            return 23.0f;
        }

        @Override
        public int getEdgeColor(E e) {
            return Color.GRAY.getRGB();
        }

        @Override
        public int getTextColor(V v) {
            return Color.WHITE.getRGB();
        }

        @Override
        public boolean updateNext() {            
            //enables continuous animation
            return true;
        }

        @Override
        public int getVertexStrokeColor(V v) {
            return 0;
        }

        @Override
        public float getVertexStroke(V v) {
            return 0;
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
