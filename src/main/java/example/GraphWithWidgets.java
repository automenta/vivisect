/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import automenta.vivisect.dimensionalize.FastOrganicLayout;
import automenta.vivisect.graph.AbstractGraphVis;
import automenta.vivisect.graph.display.DefaultDisplay;
import automenta.vivisect.graph.AnimatingGraphVis;
import automenta.vivisect.graph.EdgeVis;
import automenta.vivisect.graph.GraphDisplay;
import automenta.vivisect.graph.VertexVis;
import automenta.vivisect.gui.GControl;
import automenta.vivisect.gui.GPanel;
import automenta.vivisect.gui.GSlider;
import automenta.vivisect.gui.GSlider2D;
import automenta.vivisect.swing.NWindow;
import automenta.vivisect.swing.PCanvas;
import static example.ScaleFreeGraph.DemoVertexFactory;
import org.jgrapht.Graph;
import org.jgrapht.generate.ScaleFreeGraphGenerator;
import org.jgrapht.graph.DefaultEdge;

import org.jgrapht.graph.DirectedPseudograph;

/**
 *
 * @author me
 */
public class GraphWithWidgets {
    
    
    public static class VertexMenu implements GraphDisplay {

        
        @Override
        public void vertex(AbstractGraphVis g, VertexVis v) {
            
            GControl control = (GControl) v.the(GControl.class);
            if (control == null) {         
                
                GPanel panel;
                v.the(GControl.class, control = panel = new GPanel(g.getCanvas(), -30, -30, 150, 80));
                
                panel.setText(v.label);
                panel.setCollapsible(false);
                panel.setAlpha(237);                
                panel.add(new GSlider2D(g.getCanvas(), 0, 25, 75, 35));
                panel.add(new GSlider(g.getCanvas(), 0, 55, 85, 35, 10f));
            }
            
            control.moveTo(v.x * v.scale, v.y * v.scale);
            
        }     
        
        @Override
        public void edge(AbstractGraphVis g, EdgeVis e) {
        }
        
    }
    
    public static class DefaultDisplay2 extends DefaultDisplay {

        @Override
        public float getTime() {
            return 0f;
        }

        @Override
        public void vertex(AbstractGraphVis g, VertexVis v) {
            super.vertex(g, v);
            v.speed = 0.01f;
        }
        
    }
    
    public GraphWithWidgets(Graph g, GraphDisplay layout) {
        
        new NWindow("Directed Graph with Widgets on Nodes", 
                new PCanvas( 
                        new AnimatingGraphVis(g, 
                                new DefaultDisplay2(),                                
                                layout,                      
                                new VertexMenu()
                        )
                ).setZoom(0.5f)
        ).show(800,800,true);
        
    }

    
    public static void main(String[] args) {
        /*DirectedMultigraph g = new DirectedMultigraph(Integer.class);
        g.addVertex("A");
        g.addVertex("B");
        g.addEdge("A","B",1);*/
        
        Graph g = new DirectedPseudograph(DefaultEdge.class);
        new ScaleFreeGraphGenerator<>(16).generateGraph(g, DemoVertexFactory, null);
        
        new GraphWithWidgets(g, 
                //new AbegoTreeLayout());
                new FastOrganicLayout());
        
                                        
    }
    
}
