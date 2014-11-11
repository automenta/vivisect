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

import org.jgrapht.graph.DirectedMultigraph;
import processing.core.PApplet;

/**
 *
 * @author me
 */
public class ViewDirectedGraph {
    
    
    public static class DefaultDisplay implements GraphDisplay {

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
            return 1.0f;
        }

        @Override
        public int getVertexColor(Object o) {
            return Swing.getColor(o.hashCode(), 0.75f, 0.95f).getRGB();
        }

        @Override
        public float getEdgeThickness(Object edge, ProcessingGraphCanvas.VertexDisplay source, ProcessingGraphCanvas.VertexDisplay target) {
            return 1.0f;
        }

        @Override
        public int getEdgeColor(Object e) {
            return Swing.getColor(e.hashCode(), 0.75f, 0.95f).getRGB();
        }

        @Override
        public int getTextColor(Object v) {
            return Color.WHITE.getRGB();
        }
        
    }
    
    public static void main(String[] args) {
        
        Class p = PApplet.class;
        
        DirectedMultigraph g = new DirectedMultigraph(Integer.class);
        
        new NWindow("Directed Graph", 
                new AnimatedProcessingGraphCanvas(g, new DefaultDisplay()))
                .show(800,800);
        
    }
    
}
