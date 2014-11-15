/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gremlin.display;

import automenta.vivisect.Video;
import automenta.vivisect.graph.AbstractGraphVis;
import automenta.vivisect.graph.VertexVis;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.apache.commons.math3.linear.ArrayRealVector;

/**
 *
 * @author me
 */
public class SomDisplay<V extends Vertex, E extends Edge> extends NeuralDisplay<V,E>  {

    float baseSize = 16;
    final int strokeColor = Video.color(255, 255, 255, 70);
    final int defaultEdgeColor = Video.color(127,127,127,255);
    
    //TODO make this based on the SOM's upper and lower bounds
    final float vectorScale = 10f;
    

    
    @Override
    public void vertex(final AbstractGraphVis<V,E> g, final VertexVis<V,E> vv) {

        Vertex v = vv.getVertex();
        
        ArrayRealVector pos = property(v, "position", null);
                
        Double signal = property(v, "signal", 0.5d);
        Double activity = property(v, "activity", 0d);
        //String layer = property(v, "layer", v.toString());

        float total = (float)(signal + activity);
        
        vv.radius = baseSize/2f + (float)(baseSize * Math.abs(total))/2f;
        
        if (pos!=null) {
            double[] p = pos.getDataRef();            
            
            float vs = 255f / vectorScale;
            
            if (p.length == 1) {
                vv.color = Video.color((float)p[0]*vs, (float)p[0]*vs, (float)p[0]*vs, 255f);
                vv.setPosition((float)p[0] * baseSize*4f, 0);
            }
            else if (p.length == 2) {
                vv.color = Video.color((float)p[0]*vs, (float)p[1]*vs, 0f, 255f);
                vv.setPosition((float)p[0] * baseSize*4f, (float)p[1] * baseSize*4f);
            }
            else if (p.length >= 3) {
                vv.color = Video.color((float)p[0]*vs, (float)p[1]*vs, (float)p[2]*vs, 255f);
            }
        }
        else {
            vv.color = defaultEdgeColor;
        }
        
        vv.label = v.toString();
        vv.speed = 0.8f;
        vv.strokeColor = strokeColor;                
        if (signal != 0)
            vv.stroke = Math.abs( signal.floatValue() ) * 5f;
        else
            vv.stroke = 0;


    }
    
    
    
}
