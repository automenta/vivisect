/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import automenta.vivisect.Video;
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
    
    
    public static class DefaultDisplay<V,E> implements GraphDisplay<V,E> {

        final int gray = Color.GRAY.getRGB();
        final int white = Color.WHITE.getRGB();
        
        long startTime = System.currentTimeMillis();

        public float getTime() {
            return (System.currentTimeMillis() - startTime)/1000.0f;
        }

        @Override
        public void edge(AbstractGraphVis<V,E> g, EdgeVis<V,E> e) {
            e.thickness = 23f;
            e.color = gray;
        }

        @Override
        public void vertex(final AbstractGraphVis<V,E> g, final VertexVis<V,E> v) {
            final Object o = v.vertex;
            
            v.shape = Shape.Ellipse;
            if (v.label == null)
                v.label = v.vertex.toString();
            
            v.radius = 16.0f + 7f * (float)Math.sin( getTime()*4f );
            v.color = Video.getColor(o.hashCode(), 0.75f, 0.95f, 0.75f).getRGB();
            v.textColor = white;
            v.stroke = 0;
            v.strokeColor = 0;
            v.scale = 10f;
            v.speed = 0.5f;
            
        }
        

        @Override
        public boolean preUpdate(AbstractGraphVis<V,E> g) {            
            //enables continuous animation
            return true;
        }       
        
        @Override
        public boolean postUpdate(AbstractGraphVis<V,E> g) {                        
            return true;
        }       
        
    }

    public SimpleDirectedGraph(Graph g) {
        FastOrganicLayout layout;
        
        new NWindow("Directed Graph", 
                new PCanvas( 
                        new AnimatingGraphVis(g, 
                                new DefaultDisplay(),
                                layout = new FastOrganicLayout()
                        )
                )
        ).show(800,800,true);
        
        /*this.layout = new FastOrganicLayout();*/
        layout.setInitialTemp(3f);
        layout.setMinDistanceLimit(75f);
        layout.setMaxDistanceLimit(200f);
        
        layout.setMaxIterations(1);
        
    }

    
    public static void main(String[] args) {
        DirectedMultigraph g = new DirectedMultigraph(Integer.class);
        g.addVertex("A");
        g.addVertex("B");
        g.addEdge("A","B",1);
        
        new SimpleDirectedGraph(g);        
    }
    
}